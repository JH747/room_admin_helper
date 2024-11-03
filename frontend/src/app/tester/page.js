'use client';

import { useState } from 'react';
import dayjs from 'dayjs';

export default function Page() {
  const [currentDate, setCurrentDate] = useState(dayjs());

  // console.log('currentDate', currentDate);

  currentDate.add(1, 'day');

  console.log(currentDate.date(2));
  console.log(currentDate.daysInMonth());

  const items = ['Apple', 'Banana', 'Cherry'];

  return (
    <ul>
      {items.map((item, index) => (
        <li key={index}>{item}</li>
      ))}
    </ul>
  );
}
