'use client';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function RoomListPage() {
  const router = useRouter();
  const [rooms, setRooms] = useState([]);
  const [roomOptions, setRoomOptions] = useState([]);
  const [newRoom, setNewRoom] = useState({
    standard_room_name: '',
    yapen_room_name: '',
    yogei_room_name: '',
  });

  async function fetchData() {
    const session = await getSession();
    if (!session) {
      router.push('/errorpages/403');
    }
    const res1 = await fetch('http://127.0.0.1:8000/api/setroominfo/', {
      method: 'GET',
      headers: { Authorization: `Token ${session.token}` },
    });
    const data1 = await res1.json();
    setRooms(data1);
    const res2 = await fetch('http://127.0.0.1:8000/api/setstandardroominfo/', {
      method: 'GET',
      headers: { Authorization: `Token ${session.token}` },
      withCredentials: true,
    });
    const data2 = await res2.json();
    const roomNames = data2.map((room) => room.standard_room_name);
    setRoomOptions(roomNames);
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
      !newRoom.standard_room_name &&
      (!newRoom.yapen_room_name || !newRoom.yogei_room_name)
    )
      return;
    const newRoomWithId = { ...newRoom, id: Date.now() };
    setRooms([...rooms, newRoomWithId]);
    setNewRoom({
      standard_room_name: '',
      yapen_room_name: '',
      yogei_room_name: '',
    });
  };

  return (
    <div className="max-w-2xl mx-auto p-4">
      <h2 className="text-2xl font-bold text-center mb-4">Room List</h2>
      <table className="w-full border">
        <thead>
          <tr>
            <th className="border px-4 py-2">기준 객실 이름</th>
            <th className="border px-4 py-2">야놀자 객실 이름</th>
            <th className="border px-4 py-2">여기어때 객실 이름</th>
            <th className="border px-4 py-2">작업</th>
          </tr>
        </thead>
        <tbody>
          {rooms.map((room) => (
            <tr key={room.id}>
              <td className="border px-4 py-2">{room.standard_room_name}</td>
              <td className="border px-4 py-2">{room.yapen_room_name}</td>
              <td className="border px-4 py-2">{room.yogei_room_name}</td>
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
              <select
                className="w-full p-1 border rounded"
                value={newRoom.standard_room_name}
                onChange={(e) =>
                  setNewRoom({ ...newRoom, standard_room_name: e.target.value })
                }
              >
                <option value="">기준 객실 선택</option>
                {roomOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
            </td>
            <td className="border px-4 py-2">
              <input
                type="text"
                className="w-full p-1 border rounded"
                placeholder="야놀자 객실 이름"
                value={newRoom.yapen_room_name}
                onChange={(e) =>
                  setNewRoom({ ...newRoom, yapen_room_name: e.target.value })
                }
              />
            </td>
            <td className="border px-4 py-2">
              <input
                type="text"
                className="w-full p-1 border rounded"
                placeholder="여기어때 객실 이름"
                value={newRoom.yogei_room_name}
                onChange={(e) =>
                  setNewRoom({ ...newRoom, yogei_room_name: e.target.value })
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
