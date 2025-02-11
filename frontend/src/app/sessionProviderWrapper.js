'use client';

import { SessionProvider } from 'next-auth/react';
import { AuthControl } from './authControl';

export default function SessionProviderWrapper({ children }) {
  return (
    <SessionProvider>
      <header className="bg-blue-600 text-white p-4 shadow-md">
        <nav className="flex justify-between items-center">
          <h1 className="text-lg font-semibold">Room Admin Helper</h1>
          <ul className="flex space-x-4 text-sm">
            <li>
              <a href="/" className="hover:underline">
                Home
              </a>
            </li>
            <AuthControl />
            <li>
              <a href="/about" className="hover:underline">
                About
              </a>
            </li>
          </ul>
        </nav>
      </header>
      {children}
    </SessionProvider>
  );
}
