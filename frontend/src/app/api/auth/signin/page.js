'use client';

import { useState } from 'react';
import { signIn } from 'next-auth/react';
import { useRouter } from 'next/navigation';

export default function SignInPage() {
  const router = useRouter();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async () => {
    setError(null);
    const result = await signIn('credentials', {
      redirect: false,
      username,
      password,
    });

    if (result.error) {
      setError('Login failed. Please check your username and password.');
    } else {
      router.push('/'); // 로그인 성공 시 홈으로 리다이렉트
    }
  };

  return (
    <div className="max-w-md mx-auto p-4">
      <h2 className="text-2xl font-bold text-center mb-4">Sign In</h2>
      {error && <p className="text-red-500 mb-2">{error}</p>}
      <div className="mb-4">
        <label className="block mb-1">Username</label>
        <input
          type="text"
          className="w-full p-2 border rounded"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
      </div>
      <div className="mb-4">
        <label className="block mb-1">Password</label>
        <input
          type="password"
          className="w-full p-2 border rounded"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <button
        className="w-full bg-blue-600 text-white p-2 rounded mt-4"
        onClick={handleSubmit}
      >
        Sign In
      </button>
    </div>
  );
}
