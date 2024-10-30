import NextAuth from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';

const authOptions = {
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        // next-auth 기본제공 로그인 UI에서만 필요, 커스텀 로그인 페이지 사용시 username, password 필드 내부 속성 불요
        username: { label: 'Username', type: 'text', placeholder: 'jsmith' },
        password: { label: 'Password', type: 'password' },
      },
      async authorize(credentials) {
        // Django 백엔드로 인증 요청 보내기
        try {
          const res = await fetch('http://127.0.0.1:8000/common/login/', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              id: credentials.username,
              pass: credentials.password,
            }),
          });
          const data = await res.json();
          if (res.ok && data.token) {
            return { token: data.token };
          }
          return null;
        } catch (error) {
          console.error('Authorization error:', error);
          return null;
        }
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }) {
      // 첫 로그인 시 유저 토큰 저장
      if (user) {
        token.token = user.token;
      }
      return token;
    },
    async session({ session, token }) {
      // 세션에 토큰 포함
      session.token = token.token;
      return session;
    },
  },
  pages: {
    signIn: '/auth/signin',
  },
  session: {
    strategy: 'jwt',
  },
  secret: process.env.NEXTAUTH_SECRET,
};

const handler = NextAuth(authOptions);
export { handler as GET, handler as POST };
