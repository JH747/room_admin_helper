'use client';
import { getSession } from 'next-auth/react';
import { useEffect, useState } from 'react';
import { useSession, signOut } from 'next-auth/react';
import { SessionProvider } from 'next-auth/react';

export function AuthControl() {
  // const [isAuthed, setIsAuthed] = useState(false);

  // useEffect(() => {
  //   const checkAuth = async () => {
  //     const session = await getSession();
  //     if (session?.token) {
  //       setIsAuthed(true);
  //     }
  //   };

  //   checkAuth();
  // }, []);
  const { data: session } = useSession();
  const isAuthed = !!session?.token;

  return isAuthed ? (
    <>
      <li>
        <button onClick={() => signOut()} className="hover:underline">
          SignOut
        </button>
      </li>
      <li>
        <a href="/settings/standardroomlist" className="hover:underline">
          Set Standard Rooms
        </a>
      </li>
      <li>
        <a href="/settings/roomlist" className="hover:underline">
          Set Rooms
        </a>
      </li>
    </>
  ) : (
    <>
      <li>
        <a href="/api/auth/signin" className="hover:underline">
          SignIn
        </a>
      </li>
      <li>
        <a href="/api/auth/signup" className="hover:underline">
          SignUp
        </a>
      </li>
    </>
  );
}
