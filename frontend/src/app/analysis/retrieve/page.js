'use client';

import { getSession } from 'next-auth/react';
import { useState } from 'react';
import { EventSourcePolyfill } from 'event-source-polyfill';
import 'react-datepicker/dist/react-datepicker.css';
import DatePicker from 'react-datepicker';
import dayjs from 'dayjs';

import SalesChart from './saleschart';

export default function Page() {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [data, setData] = useState(null);
  const [isChartVisible, setIsChartVisible] = useState(false); // 차트 표시 여부

  async function handleSubmit() {
    const sDate = dayjs(startDate).format('YYYY-MM-DD');
    const eDate = dayjs(endDate).format('YYYY-MM-DD');
    const session = await getSession();

    const EventSource = EventSourcePolyfill;

    // console.log(session.token);

    const eventSource = new EventSource(
      `/api/proxy_retrieve_info?start_date=${sDate}&end_date=${eDate}&mode=retrieve`,
      {
        headers: {
          Authorization: `Bearer ${session.token}`,
        },
      }
    );
    eventSource.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        if (data.status == 'Ping') {
          console.log('Ping received');
        } else if (data.status == 'Connection opened') {
          console.log('Connection opened');
        } else {
          setData(data);
          setIsChartVisible(true); // 데이터가 설정되면 차트를 표시
          eventSource.close();
        }
      } catch (error) {
        console.error('JSON parsing error:', error);
      }
    };
    eventSource.onerror = (error) => {
      console.error('SSE error:', error);
      eventSource.close();
    };
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
