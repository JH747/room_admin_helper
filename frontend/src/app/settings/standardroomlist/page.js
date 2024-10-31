'use client';

import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function StandardRoomListPage() {
  const [rooms, setRooms] = useState([]);
  const [newRoom, setNewRoom] = useState({
    standard_room_name: '',
    room_quantity: '',
    display_order: '',
  });

  async function fetchData() {
    const session = await getSession();
    const res1 = await fetch('http://127.0.0.1:8000/api/setstandardroominfo/', {
      method: 'GET',
      headers: { Authorization: `Token ${session.token}` },
    });
    const data1 = await res1.json();
    setRooms(data1);
  }

  useEffect(() => {
    fetchData();
    console.log(rooms);
  }, []);

  const handleDelete = (id) => {
    setRooms(rooms.filter((room) => room.id !== id));
  };

  const handleAdd = () => {
    if (
      !newRoom.standard_room_name ||
      !newRoom.room_quantity ||
      !newRoom.display_order
    )
      return;
    const newRoomWithId = { ...newRoom, id: Date.now() };
    setRooms([...rooms, newRoomWithId]);
    setNewRoom({
      standard_room_name: '',
      room_quantity: '',
      display_order: '',
    });
  };

  return (
    <div className="max-w-2xl mx-auto p-4">
      <h2 className="text-2xl font-bold text-center mb-4">
        Standard Room List
      </h2>
      <table className="w-full border">
        <thead>
          <tr>
            <th className="border px-4 py-2">기준 객실 이름</th>
            <th className="border px-4 py-2">객실 수량</th>
            <th className="border px-4 py-2">정렬 순서</th>
            <th className="border px-4 py-2">작업</th>
          </tr>
        </thead>
        <tbody>
          {rooms.map((room) => (
            <tr key={room.id}>
              <td className="border px-4 py-2">{room.standard_room_name}</td>
              <td className="border px-4 py-2">{room.room_quantity}</td>
              <td className="border px-4 py-2">{room.display_order}</td>
              <td className="border px-4 py-2">
                <button
                  className="bg-red-500 text-white p-1 rounded"
                  onClick={() => handleDelete(room.id)}
                >
                  삭제
                </button>
              </td>
            </tr>
          ))}
          <tr>
            <td className="border px-4 py-2">
              <input
                type="text"
                className="w-full p-1 border rounded"
                placeholder="객실 이름"
                value={newRoom.standard_room_name}
                onChange={(e) =>
                  setNewRoom({ ...newRoom, standard_room_name: e.target.value })
                }
              />
            </td>
            <td className="border px-4 py-2">
              <input
                type="number"
                className="w-full p-1 border rounded"
                placeholder="객실 수량"
                value={newRoom.room_quantity}
                onChange={(e) =>
                  setNewRoom({ ...newRoom, room_quantity: e.target.value })
                }
              />
            </td>
            <td className="border px-4 py-2">
              <input
                type="number"
                className="w-full p-1 border rounded"
                placeholder="정렬 순서"
                value={newRoom.display_order}
                onChange={(e) =>
                  setNewRoom({ ...newRoom, display_order: e.target.value })
                }
              />
            </td>
            <td className="border px-4 py-2">
              <button
                className="bg-blue-600 text-white p-1 rounded"
                onClick={handleAdd}
              >
                등록
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
