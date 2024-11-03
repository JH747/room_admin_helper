'use client';

import { useState } from 'react';
import dayjs from 'dayjs';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

export default function CalendarPage() {
  const [currentDate, setCurrentDate] = useState(dayjs());
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  // Function to get the days of the current month, including padding for the start day
  const getDaysInMonth = () => {
    const startDay = currentDate.startOf('month').day(); // 첫 번째 날짜의 요일
    const daysInMonth = currentDate.daysInMonth();

    // 앞쪽에 시작 요일에 맞춰 빈 칸을 채워야 하므로, padding 배열 생성
    const daysArray = Array.from({ length: startDay }, () => null).concat(
      Array.from({ length: daysInMonth }, (_, i) => ({
        date: currentDate.date(i + 1),
        key: i,
      }))
    );
    console.log(daysArray);

    return daysArray;
  };

  // Navigation handlers
  const goToPreviousMonth = () => {
    setCurrentDate((prevDate) => prevDate.subtract(1, 'month'));
  };

  const goToNextMonth = () => {
    setCurrentDate((prevDate) => prevDate.add(1, 'month'));
  };

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
          // onClick={handleSubmit}
        >
          찾기
        </button>
      </div>

      <div className="text-center mb-4">
        <h2 className="text-2xl font-bold">
          {currentDate.format('MMMM YYYY')}
        </h2>
        <div className="flex justify-between mt-4">
          <button onClick={goToPreviousMonth} className="text-blue-600">
            Previous
          </button>
          <button onClick={goToNextMonth} className="text-blue-600">
            Next
          </button>
        </div>
      </div>

      <div className="grid grid-cols-7 gap-4 text-center">
        {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day) => (
          <div key={day} className="font-bold">
            {day}
          </div>
        ))}

        {getDaysInMonth().map((day, key) => (
          <div key={key} className="border p-2 rounded h-24">
            {day ? (
              <div>
                <div className="font-bold">{day.date.date()}</div>
                <div className="text-sm text-gray-500">
                  {/* Placeholder for backend data */}
                </div>
              </div>
            ) : (
              <div />
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
