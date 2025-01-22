'use client';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function Page() {
  const router = useRouter();
  const [standardRooms, setStandardRooms] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [newRoom, setNewRoom] = useState({
    standardRoomName: '',
    yapenRoomName: '',
    yogeiRoomName: '',
    displayOrder: '',
  });
  const addr = process.env.NEXT_PUBLIC_BE_ADDR;

  async function fetchData() {
    const session = await getSession();
    if (!session) {
      router.push('/errorpages/403');
    }
    // fetch platforms rooms
    const res1 = await fetch(`${addr}/settings/platformsRoomsInfo`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data = await res1.json();
    setRooms(data);
    // fetch standard rooms
    const res2 = await fetch(`${addr}/settings/standardRoomsInfo`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data2 = await res2.json();
    setStandardRooms(data2);
  }

  useEffect(() => {
    fetchData();
  }, []);

  async function handleDelete(room) {
    const session = await getSession();
    console.log(room);
    console.log(session.token);
    await fetch(`${addr}/platformRoomsInfo?delete=true`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${session.token}`,
      },
      body: JSON.stringify({
        standardRoomName: room.standardRoomName,
        yapenRoomName: room.yapenRoomName,
        yogeiRoomName: room.yogeiRoomName,
        displayOrder: room.displayOrder,
      }),
    });
    fetchData();
  }

  async function handleAddRoom() {
    if (
      newRoom.standardRoomName &&
      newRoom.yapenRoomName &&
      newRoom.yogeiRoomName &&
      newRoom.displayOrder
    ) {
      const session = await getSession();
      const addRes = await fetch(`${addr}/settings/platformsRoomsInfo`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${session.token}`,
        },
        body: JSON.stringify({
          standardRoomName: newRoom.standardRoomName,
          yapenRoomName: newRoom.yapenRoomName,
          yogeiRoomName: newRoom.yogeiRoomName,
          displayOrder: newRoom.displayOrder,
        }),
      });
      if (!addRes.ok) {
        alert('Failed to add room. Check if display order is unique.');
      }
      setNewRoom({
        standardRoomName: '',
        yapenRoomName: '',
        yogeiRoomName: '',
        displayOrder: '',
      }); // 입력 필드 초기화
    } else {
      alert('모든 필드를 채워주세요.');
    }
    fetchData();
  }

  return (
    <div className="p-4 max-w-xl mx-auto">
      {/* <p>{JSON.stringify(rooms)}</p> */}
      <h1 className="text-2xl font-bold mb-4">Room Management</h1>
      <div className="space-y-4">
        {rooms.map((room, index) => (
          <div
            key={index}
            className="flex items-center justify-between border p-2 rounded"
          >
            <div>
              <p className="font-medium">
                Standard Room Name: {room.standardRoomName}
              </p>
              <p>Yapen Room Name: {room.yapenRoomName}</p>
              <p>Yogei Room Name: {room.yogeiRoomName}</p>
              <p>Display Order: {room.displayOrder}</p>
            </div>
            <button
              onClick={() => handleDelete(room)}
              className="bg-red-500 text-white px-4 py-2 rounded"
            >
              Delete
            </button>
          </div>
        ))}
      </div>

      {/* 새로운 룸 생성 */}
      <div className="mt-6 border-t pt-4">
        <h2 className="text-lg font-bold mb-2">Add New Room</h2>
        <div className="flex items-center space-x-2 mb-4">
          <select
            value={newRoom.standardRoomName} // 현재 선택된 값
            onChange={
              (e) =>
                setNewRoom({ ...newRoom, standardRoomName: e.target.value }) // 값 변경 핸들러
            }
            className="border p-2 rounded w-full"
          >
            <option value="" disabled>
              Select Room Type
            </option>
            {standardRooms.map((standardRoom, index) => (
              <option key={index} value={standardRoom.roomName}>
                {standardRoom.roomName}
              </option>
            ))}
          </select>

          <input
            type="text"
            placeholder="Yapen Room Name"
            value={newRoom.yapenRoomName}
            onChange={(e) =>
              setNewRoom({ ...newRoom, yapenRoomName: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
          <input
            type="text"
            placeholder="Yogei Room Name"
            value={newRoom.yogeiRoomName}
            onChange={(e) =>
              setNewRoom({ ...newRoom, yogeiRoomName: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
          <input
            type="number"
            placeholder="Display Order"
            value={newRoom.displayOrder}
            onChange={(e) =>
              setNewRoom({ ...newRoom, displayOrder: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
        </div>
        <button
          onClick={handleAddRoom}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Add Room
        </button>
      </div>
    </div>
  );
}
