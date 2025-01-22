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
        // 백엔드로 인증 요청 보내기
        try {
          const addr = process.env.NEXT_PUBLIC_BE_ADDR;
          const res = await fetch(`${addr}/auth/signin`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              id: credentials.username,
              pass: credentials.password,
            }),
          });
          const data = await res.text();
          if (res.ok && data) {
            return { token: data }; // 이 객체가 'user' 로 전달됨.
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
      // jwt callback: 첫 로그인 시 user의 token을 jwt에 저장
      if (user) {
        token.token = user.token;
      }
      // 첫 로그인이 아닐 경우 이미 저장된 token 반환
      return token;
    },
    async session({ session, token }) {
      // session callback: JWT에 저장된 token을 session에 저장
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
