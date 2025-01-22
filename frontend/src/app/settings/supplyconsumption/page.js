'use client';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function Page() {
  const router = useRouter();
  const [standardRooms, setStandardRooms] = useState([]);
  const [supplies, setSupplies] = useState([]);
  const [supplyConsumptions, setSupplyConsumptions] = useState([]);
  const [newSupplyConsumption, setNewSupplyConsumption] = useState({
    standardRoomName: '',
    supplyName: '',
    consumption: '',
  });
  const addr = process.env.NEXT_PUBLIC_BE_ADDR;

  async function fetchData() {
    const session = await getSession();
    if (!session) {
      router.push('/errorpages/403');
    }
    const res1 = await fetch(`${addr}/settings/supplyConsumption`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data = await res1.json();
    setSupplyConsumptions(data);
    // fetch standard rooms
    const res2 = await fetch(`${addr}/settings/standardRoomsInfo`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data2 = await res2.json();
    setStandardRooms(data2);
    // fetch supplies
    const res3 = await fetch(`${addr}/settings/supply`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data3 = await res3.json();
    setSupplies(data3);
  }

  useEffect(() => {
    fetchData();
  }, []);

  async function handleDelete(supplyConsumption) {
    const session = await getSession();
    console.log(supplyConsumption);
    console.log(session.token);
    await fetch(`${addr}/settings/supplyConsumption?delete=true`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${session.token}`,
      },
      body: JSON.stringify({
        standardRoomName: supplyConsumption.standardRoomName,
        supplyName: supplyConsumption.supplyName,
        consumption: supplyConsumption.consumption,
      }),
    });
    fetchData();
  }

  async function handleAddSupplyConsumption() {
    if (
      newSupplyConsumption.standardRoomName &&
      newSupplyConsumption.supplyName &&
      newSupplyConsumption.consumption
    ) {
      const session = await getSession();
      const addRes = await fetch(`${addr}/settings/supplyConsumption`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${session.token}`,
        },
        body: JSON.stringify({
          standardRoomName: newSupplyConsumption.standardRoomName,
          supplyName: newSupplyConsumption.supplyName,
          consumption: newSupplyConsumption.consumption,
        }),
      });
      if (!addRes.ok) {
        alert(
          'Failed to add supplyConsumption. Check if display order is unique.'
        );
      }
      setNewSupplyConsumption({
        standardRoomName: '',
        supplyName: '',
        consumption: '',
      }); // 입력 필드 초기화
    } else {
      alert('모든 필드를 채워주세요.');
    }
    fetchData();
  }

  return (
    <div className="p-4 max-w-xl mx-auto">
      {/* <p>{JSON.stringify(supplies)}</p> */}
      <h1 className="text-2xl font-bold mb-4">SupplyConsumption Management</h1>
      <div className="space-y-4">
        {supplyConsumptions.map((supplyConsumption, index) => (
          <div
            key={index}
            className="flex items-center justify-between border p-2 rounded"
          >
            <div>
              <p>Standard Room Name: {supplyConsumption.standardRoomName}</p>
              <p>Supply Name: {supplyConsumption.supplyName}</p>
              <p>Consumption: {supplyConsumption.consumption}</p>
            </div>
            <button
              onClick={() => handleDelete(supplyConsumption)}
              className="bg-red-500 text-white px-4 py-2 rounded"
            >
              Delete
            </button>
          </div>
        ))}
      </div>

      {/* 새로운 룸 생성 */}
      <div className="mt-6 border-t pt-4">
        <h2 className="text-lg font-bold mb-2">Add New SupplyConsumption</h2>
        <div className="flex items-center space-x-2 mb-4">
          <select
            value={newSupplyConsumption.standardRoomName} // 현재 선택된 값
            onChange={
              (e) =>
                setNewRoom({
                  ...newSupplyConsumption,
                  standardRoomName: e.target.value,
                }) // 값 변경 핸들러
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
          <select
            value={newSupplyConsumption.supplyName} // 현재 선택된 값
            onChange={
              (e) =>
                setNewRoom({
                  ...newSupplyConsumption,
                  supplyName: e.target.value,
                }) // 값 변경 핸들러
            }
            className="border p-2 rounded w-full"
          >
            <option value="" disabled>
              Select Supply Type
            </option>
            {supplies.map((supply, index) => (
              <option key={index} value={supply.name}>
                {supply.name}
              </option>
            ))}
          </select>

          <input
            type="number"
            placeholder="Consumption"
            value={newSupplyConsumption.consumption}
            onChange={(e) =>
              setNewSupplyConsumption({
                ...newSupplyConsumption,
                consumption: e.target.value,
              })
            }
            className="border p-2 rounded w-full"
          />
        </div>
        <button
          onClick={handleAddSupplyConsumption}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Add SupplyConsumption
        </button>
      </div>
    </div>
  );
}
