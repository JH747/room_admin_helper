'use client';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';
import { getSession } from 'next-auth/react';

export default function Page() {
  const router = useRouter();
  const [authInfo, setAuthInfo] = useState({
    yapenId: '',
    yapenPass: '',
    yogeiId: '',
    yogeiPass: '',
  });

  const addr = process.env.NEXT_PUBLIC_BE_ADDR;

  async function fetchData() {
    const session = await getSession();
    if (!session) {
      router.push('/errorpages/403');
    }
    const res = await fetch(`${addr}/settings/platformsAuthInfo`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${session.token}` },
      withCredentials: true,
    });
    const data = await res.json();
    setAuthInfo(data);
  }

  useEffect(() => {
    fetchData();
  }, []);

  async function setAuthInfoHandler() {
    if (
      authInfo.yapenId &&
      authInfo.yapenPass &&
      authInfo.yogeiId &&
      authInfo.yogeiPass
    ) {
      const session = await getSession();
      const setRes = await fetch(`${addr}/settings/platformsAuthInfo`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${session.token}`,
        },
        body: JSON.stringify({
          yapenId: authInfo.yapenId,
          yapenPass: authInfo.yapenPass,
          yogeiId: authInfo.yogeiId,
          yogeiPass: authInfo.yogeiPass,
        }),
      });
      if (!setRes.ok) {
        alert('Failed to set platform auth information.');
      }
    } else {
      alert('Please fill in all fields.');
    }
  }

  return (
    <div className="p-4 max-w-xl mx-auto">
      {/* <p>{JSON.stringify(authInfo)}</p> */}
      <h1 className="text-2xl font-bold mb-4">Auth Information Management</h1>
      <div className="flex flex-col space-y-4 mb-4">
        {' '}
        {/* 각 그룹 간 간격 추가 */}
        {/* Yapen Section */}
        <div className="flex flex-col space-y-2 w-full">
          <div className="flex flex-col">
            <label className="font-medium mb-1">Yapen ID</label>
            <input
              type="text"
              value={authInfo.yapenId}
              onChange={(e) =>
                setNewRoom({ ...authInfo, yapenId: e.target.value })
              }
              className="border p-2 rounded w-full"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium mb-1">Yapen Pass</label>
            <input
              type="password"
              value={authInfo.yapenPass}
              onChange={(e) =>
                setNewRoom({ ...authInfo, yapenPass: e.target.value })
              }
              className="border p-2 rounded w-full"
            />
          </div>
        </div>
        {/* Yogei Section */}
        <div className="flex flex-col space-y-2 w-full">
          <div className="flex flex-col">
            <label className="font-medium mb-1">Yogei ID</label>
            <input
              type="text"
              value={authInfo.yogeiId}
              onChange={(e) =>
                setNewRoom({ ...authInfo, yogeiId: e.target.value })
              }
              className="border p-2 rounded w-full"
            />
          </div>
          <div className="flex flex-col">
            <label className="font-medium mb-1">Yogei Pass</label>
            <input
              type="password"
              value={authInfo.yogeiPass}
              onChange={(e) =>
                setNewRoom({ ...authInfo, yogeiPass: e.target.value })
              }
              className="border p-2 rounded w-full"
            />
          </div>
        </div>
      </div>

      <button
        onClick={setAuthInfoHandler}
        className="bg-blue-500 text-white px-4 py-2 rounded"
      >
        Set Auth Info
      </button>
    </div>
  );
}
