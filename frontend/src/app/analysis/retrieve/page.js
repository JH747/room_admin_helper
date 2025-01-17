'use client';

import { getSession } from 'next-auth/react';
import { useState } from 'react';
import { EventSourcePolyfill } from 'event-source-polyfill';
import 'react-datepicker/dist/react-datepicker.css';
import DatePicker from 'react-datepicker';
import dayjs from 'dayjs';

import SalesChart from './saleschart';
import { Client } from '@stomp/stompjs';
import { useEffect } from 'react';

export default function Page() {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [data, setData] = useState({});
  const [isChartVisible, setIsChartVisible] = useState(false); // 차트 표시 여부
  const [client, setClient] = useState(null);

  useEffect(() => {
    const connectWebSocket = async () => {
      try {
        const session = await getSession(); // 세션 비동기 호출
        if (!session || !session.token) {
          console.error('Session or token is missing.');
          return;
        }

        const stompClient = new Client({
          brokerURL: 'ws://localhost:8080/ws',
          connectHeaders: {
            Authorization: `Bearer ${session.token}`, // 토큰 추가
          },
          debug: function (str) {
            console.log(str);
          },
          reconnectDelay: 5000,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000,
        });

        stompClient.onConnect = function (frame) {
          console.log('Connected: ' + frame);
          stompClient.subscribe('/queue/general', function (message) {
            console.log('From Server: ' + message.body);
            const data = JSON.parse(message.body);
            setData(data);
            setIsChartVisible(true); // 데이터가 설정되면 차트를 표시
          });
        };

        stompClient.activate();
        setClient(stompClient);
      } catch (error) {
        console.error('Error connecting to WebSocket:', error);
      }
    };

    connectWebSocket();

    return () => {
      if (client) {
        client.deactivate(); // 컴포넌트 언마운트 시 WebSocket 연결 해제
      }
    };
  }, []);

  async function handleSubmit() {
    const sDate = dayjs(startDate).format('YYYY-MM-DD');
    const eDate = dayjs(endDate).format('YYYY-MM-DD');

    if (client) {
      client.publish({
        destination: '/app/analysis',
        body: JSON.stringify({
          start_date: sDate,
          end_date: eDate,
          mode: 'retrieve',
        }),
      });
    }

    // const EventSource = EventSourcePolyfill;
    // const eventSource = new EventSource(
    //   `/api/proxy_retrieve_info?start_date=${sDate}&end_date=${eDate}&mode=retrieve`,
    //   {
    //     headers: {
    //       Authorization: `Bearer ${session.token}`,
    //     },
    //   }
    // );
    // eventSource.onmessage = (event) => {
    //   try {
    //     const data = JSON.parse(event.data);
    //     if (data.status == 'Ping') {
    //       console.log('Ping received');
    //     } else if (data.status == 'Connection opened') {
    //       console.log('Connection opened');
    //     } else {
    //       setData(data);
    //       setIsChartVisible(true); // 데이터가 설정되면 차트를 표시
    //       eventSource.close();
    //     }
    //   } catch (error) {
    //     console.error('JSON parsing error:', error);
    //   }
    // };
    // eventSource.onerror = (error) => {
    //   console.error('SSE error:', error);
    //   eventSource.close();
    // };
  }

  return (
    <>
      <div className="max-w-lg mx-auto p-4">
        <div className="flex justify-center mb-4">
          <div style={{ marginBottom: '10px' }}>
            <label>Start Date:</label>
            <DatePicker
              selected={startDate}
              onChange={(date) => setStartDate(date)}
              selectsStart
              startDate={startDate}
              endDate={endDate}
              placeholderText="MM/DD/YYYY"
            />
          </div>
          <div>
            <label>End Date:</label>
            <DatePicker
              selected={endDate}
              onChange={(date) => setEndDate(date)}
              selectsEnd
              startDate={startDate}
              endDate={endDate}
              minDate={startDate}
              placeholderText="MM/DD/YYYY"
            />
          </div>
          <button
            className="w-full bg-blue-600 text-white p-2 rounded mt-4"
            onClick={handleSubmit}
          >
            확인
          </button>
        </div>
      </div>
      {isChartVisible && <SalesChart data={data} />} {/* 조건부 렌더링 */}
    </>
  );
}
