import './globals.css';
import SessionProviderWrapper from './sessionProviderWrapper';

export const metadata = {
  title: 'Room Admin Helper',
  description: 'on dev',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <SessionProviderWrapper>
          {/* will take children as its children */}
          <div className="flex flex-col min-h-screen">
            {/* Main Content */}
            <main className="flex-grow p-4 bg-gray-50">{children}</main>

            {/* Footer */}
            <footer className="bg-gray-100 text-center p-4 text-sm">
              © 2024 Room Admin Helper
            </footer>
          </div>
        </SessionProviderWrapper>
      </body>
    </html>
  );
}
