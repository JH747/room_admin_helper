'use client';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function Page() {
  const router = useRouter();
  const [supplies, setSupplies] = useState([]);
  const [newSupply, setNewSupply] = useState({
    name: '',
    desiredQuantity: '',
    thresholdQuantity: '',
    currentQuantity: '',
  });

  async function fetchData() {
    const session = await getSession();
    if (!session) {
      router.push('/errorpages/403');
    }
    const res1 = await fetch('http://127.0.0.1:8080/settings/supply', {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data = await res1.json();
    setSupplies(data);
  }

  useEffect(() => {
    fetchData();
  }, []);

  async function handleDelete(supply) {
    const session = await getSession();
    console.log(supply);
    console.log(session.token);
    await fetch('http://127.0.0.1:8080/settings/supply?delete=true', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${session.token}`,
      },
      body: JSON.stringify({
        name: supply.name,
        desiredQuantity: supply.desiredQuantity,
        thresholdQuantity: supply.thresholdQuantity,
        currentQuantity: supply.currentQuantity,
      }),
    });
    fetchData();
  }

  async function handleAddSupply() {
    if (
      newSupply.name &&
      newSupply.desiredQuantity &&
      newSupply.thresholdQuantity &&
      newSupply.currentQuantity
    ) {
      const session = await getSession();
      const addRes = await fetch('http://127.0.0.1:8080/settings/supply', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${session.token}`,
        },
        body: JSON.stringify({
          name: newSupply.name,
          desiredQuantity: newSupply.desiredQuantity,
          thresholdQuantity: newSupply.thresholdQuantity,
          currentQuantity: newSupply.currentQuantity,
        }),
      });
      if (!addRes.ok) {
        alert('Failed to add supply. Check if display order is unique.');
      }
      setNewSupply({
        name: '',
        desiredQuantity: '',
        thresholdQuantity: '',
        currentQuantity: '',
      }); // 입력 필드 초기화
    } else {
      alert('모든 필드를 채워주세요.');
    }
    fetchData();
  }

  return (
    <div className="p-4 max-w-xl mx-auto">
      {/* <p>{JSON.stringify(supplies)}</p> */}
      <h1 className="text-2xl font-bold mb-4">Supply Management</h1>
      <div className="space-y-4">
        {supplies.map((supply, index) => (
          <div
            key={index}
            className="flex items-center justify-between border p-2 rounded"
          >
            <div>
              <p className="font-medium">Supply Name: {supply.name}</p>
              <p>Desired Quantity: {supply.desiredQuantity}</p>
              <p>Threshold Quantity: {supply.thresholdQuantity}</p>
              <p>Current Quantity: {supply.currentQuantity}</p>
            </div>
            <button
              onClick={() => handleDelete(supply)}
              className="bg-red-500 text-white px-4 py-2 rounded"
            >
              Delete
            </button>
          </div>
        ))}
      </div>

      {/* 새로운 룸 생성 */}
      <div className="mt-6 border-t pt-4">
        <h2 className="text-lg font-bold mb-2">Add New Supply</h2>
        <div className="flex items-center space-x-2 mb-4">
          <input
            type="text"
            placeholder="Supply Name"
            value={newSupply.name}
            onChange={(e) =>
              setNewSupply({ ...newSupply, name: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
          <input
            type="number"
            placeholder="Desired Quantity"
            value={newSupply.desiredQuantity}
            onChange={(e) =>
              setNewSupply({ ...newSupply, desiredQuantity: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
          <input
            type="number"
            placeholder="Threshold Quantity"
            value={newSupply.thresholdQuantity}
            onChange={(e) =>
              setNewSupply({ ...newSupply, thresholdQuantity: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
          <input
            type="number"
            placeholder="Current Quantity"
            value={newSupply.currentQuantity}
            onChange={(e) =>
              setNewSupply({ ...newSupply, currentQuantity: e.target.value })
            }
            className="border p-2 rounded w-full"
          />
        </div>
        <button
          onClick={handleAddSupply}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Add Supply
        </button>
      </div>
    </div>
  );
}
