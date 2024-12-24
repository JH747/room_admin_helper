'use client';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function Page() {
  const router = useRouter();
  const [rooms, setRooms] = useState([]);
  const [newRoom, setNewRoom] = useState({
    roomName: '',
    roomQuantity: '',
    displayOrder: '',
  });

  async function fetchData() {
    const session = await getSession();
    if (!session) {
      router.push('/errorpages/403');
    }
    const res1 = await fetch(
      'http://127.0.0.1:8080/settings/standardRoomsInfo',
      {
        method: 'GET',
        headers: { Authorization: `Bearer ${session.token}` },
        withCredentials: true,
      }
    );
    const data = await res1.json();
    setRooms(data);
  }

  useEffect(() => {
    fetchData();
  }, []);

  async function handleDelete(room) {
    const session = await getSession();
    console.log(room);
    console.log(session.token);
    await fetch(
      'http://127.0.0.1:8080/settings/standardRoomsInfo?delete=true',
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${session.token}`,
        },
        body: JSON.stringify({
          roomName: room.roomName,
          roomQuantity: room.roomQuantity,
          displayOrder: room.displayOrder,
        }),
      }
    );
    fetchData();
  }

  async function handleAddRoom() {
    if (newRoom.roomName && newRoom.roomQuantity && newRoom.displayOrder) {
      const session = await getSession();
      const addRes = await fetch(
        'http://127.0.0.1:8080/settings/standardRoomsInfo',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${session.token}`,
          },
          body: JSON.stringify({
            roomName: newRoom.roomName,
            roomQuantity: newRoom.roomQuantity,
            displayOrder: newRoom.displayOrder,
          }),
        }
      );
      console.log(addRes);
      if (!addRes.ok) {
        alert('Failed to add room. Check if display order is unique.');
      }
      setNewRoom({ roomName: '', roomQuantity: '', displayOrder: '' }); // 입력 필드 초기화
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
              <p className="font-medium">Room Name: {room.roomName}</p>
              <p>Quantity: {room.roomQuantity}</p>
              <p>Order: {room.displayOrder}</p>
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
          <input
            type="text"
            placeholder="Room Name"
            value={newRoom.roomName}
            onChange={(e) =>
              setNewRoom({ ...newRoom, roomName: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
          <input
            type="number"
            placeholder="Quantity"
            value={newRoom.roomQuantity}
            onChange={(e) =>
              setNewRoom({ ...newRoom, roomQuantity: e.target.value })
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
