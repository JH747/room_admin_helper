'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function SignupPage() {
  const router = useRouter();

  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [authCode, setAuthCode] = useState('');

  const [timer, setTimer] = useState(null);
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [error, setError] = useState(null);

  const startTimer = () => {
    let seconds = 300; // 5 minutes
    setTimer(seconds);
    const interval = setInterval(() => {
      seconds -= 1;
      setTimer(seconds);
      if (seconds <= 0) {
        clearInterval(interval);
      }
    }, 1000);
  };

  const validateEmail = (email) => {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
  };

  const handleSendCode = async () => {
    if (!validateEmail(email)) {
      setError('Invalid email format');
      return;
    }
    setError(null);
    setIsCodeSent(true);
    startTimer();

    // Send code to email API request
    await fetch('http://127.0.0.1:8080/auth/sendcode', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email }),
    });
  };

  const handleSubmit = async () => {
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    const response = await fetch('http://127.0.0.1:8080/auth/signup', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: username,
        pass: password,
        email: email,
        code: authCode,
      }),
    });

    const data = await response.json();
    if (data.message === 'signup success') {
      router.push('/');
    } else {
      setError('Signup failed. Please check the information and try again.');
    }
  };

  return (
    <div className="max-w-md mx-auto p-4">
      <h2 className="text-2xl font-bold text-center mb-4">Sign Up</h2>
      {error && <p className="text-red-500 mb-2">{error}</p>}
      <div className="mb-4">
        <label className="block mb-1">Email</label>
        <div className="flex">
          <input
            type="email"
            className="flex-grow p-2 border rounded-l"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button
            className="p-2 bg-blue-600 text-white rounded-r"
            onClick={handleSendCode}
          >
            {isCodeSent ? 'Resend Code' : 'Send Code'}
          </button>
        </div>
        {timer && timer > 0 && (
          <p className="text-gray-500 mt-2">
            Code expires in {Math.floor(timer / 60)}:
            {timer % 60 < 10 ? `0${timer % 60}` : timer % 60}
          </p>
        )}
      </div>
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
      <div className="mb-4">
        <label className="block mb-1">Confirm Password</label>
        <input
          type="password"
          className="w-full p-2 border rounded"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
      </div>
      <div className="mb-4">
        <label className="block mb-1">Authentication Code</label>
        <input
          type="text"
          className="w-full p-2 border rounded"
          value={authCode}
          onChange={(e) => setAuthCode(e.target.value)}
        />
      </div>
      <button
        className="w-full bg-blue-600 text-white p-2 rounded mt-4"
        onClick={handleSubmit}
      >
        Submit
      </button>
    </div>
  );
}
