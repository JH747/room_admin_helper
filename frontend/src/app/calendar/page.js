'use client';

import { useState } from 'react';
import { getSession } from 'next-auth/react';
import dayjs from 'dayjs';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { EventSourcePolyfill } from 'event-source-polyfill';

export default function CalendarPage() {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [calendarRange, setCalendarRange] = useState([]);

  const [result, setResult] = useState([]);

  // Function to get the days of a specific month, including padding for the start day
  const getDaysInMonth = (date, result) => {
    const startDay = date.startOf('month').day(); // 첫 번째 날짜의 요일
    const daysInMonth = date.daysInMonth();

    const daysArray = [];
    // 1. 시작 요일만큼 null로 채운 빈 칸 배열 추가
    for (let i = 0; i < startDay; i++) {
      daysArray.push(null);
    }

    // 2. 월의 실제 날짜로 구성된 배열 추가
    for (let i = 1; i <= daysInMonth; i++) {
      const tmpDate = date.date(i).format('YYYY-MM-DD');
      const problems = result[tmpDate] || [];
      daysArray.push({
        date: date.date(i),
        key: i - 1,
        problems: problems,
      });
    }
    return daysArray;

    // return Array.from({ length: startDay }, () => null).concat(
    //   Array.from({ length: daysInMonth }, (_, i) => ({
    //     date: date.date(i + 1),
    //     key: i,
    //   }))
    // );
  };

  // Calculate range of months between startDate and endDate
  const renderCalendarRange = () => {
    if (!startDate || !endDate) return [];

    const start = dayjs(startDate);
    const end = dayjs(endDate);
    const months = [];

    let current = start.startOf('month');

    while (current.isBefore(end) || current.isSame(end, 'month')) {
      months.push(current);
      current = current.add(1, 'month');
    }

    return months;
  };

  // Handle submit to render range of months
  async function handleSubmit() {
    const sDate = dayjs(startDate).format('YYYY-MM-DD');
    const eDate = dayjs(endDate).format('YYYY-MM-DD');
    const session = await getSession();
    // use polyfill to include headers
    const EventSource = EventSourcePolyfill;
    // pass it to proxy
    const eventSource = new EventSource(
      `/api/proxy_retrieve_info?start_date=${sDate}&end_date=${eDate}&detector_mode=yes`,
      {
        headers: {
          Authorization: `Token ${session.token}`,
        },
      }
    );
    eventSource.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data); // JSON 형식으로 파싱
        if (data.status === 'success') {
          setResult(data.result);
        }
        if (data.status === 'closed') {
          eventSource.close();
        }
        console.log(data); // 데이터 출력 또는 상태 업데이트
      } catch (error) {
        console.error('JSON parsing error:', error);
      }
    };
    // 에러 처리
    eventSource.onerror = (error) => {
      console.error('SSE error:', error);
      eventSource.close();
    };

    // const response = await fetch(
    //   `/api/proxy_retrieve_info?start_date=${sDate}&end_date=${eDate}&detector_mode=yes`,
    //   {
    //     headers: {
    //       Authorization: `Token ${session.token}`,
    //     },
    //   }
    // );
    // if (response.ok) {
    //   const reader = response.body.getReader();
    //   const decoder = new TextDecoder('utf-8');

    //   // SSE 데이터 스트리밍 읽기
    //   while (true) {
    //     const { value, done } = await reader.read();
    //     if (done) break;

    //     const message = decoder.decode(value, { stream: true });
    //     console.log(message);
    //   }
    // } else {
    //   console.error('Failed to fetch data:', response.statusText);
    // }

    // 여기서 result 데이터를 가공하여 상태 업데이트
    setCalendarRange(renderCalendarRange());
  }

  return (
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

      {calendarRange.map((month, index) => (
        <div key={index} className="mb-8">
          <h3 className="text-center font-bold text-lg mb-2">
            {month.format('YYYY년 MM월')}
          </h3>
          <div className="grid grid-cols-7 gap-4 text-center">
            {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day) => (
              <div key={day} className="font-bold">
                {day}
              </div>
            ))}
            {getDaysInMonth(month, result).map((day, key) => (
              <div key={key} className="border p-2 rounded h-24">
                {day ? (
                  <div>
                    <div className="font-bold">{day.date.date()}</div>
                    <div className="text-sm text-gray-500">
                      {/* Placeholder for backend data */}
                      {day.problems.map((problem, i) => (
                        <div key={i}>
                          {problem.problem} - {problem.room_type}
                        </div>
                      ))}
                    </div>
                  </div>
                ) : (
                  <div />
                )}
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}
