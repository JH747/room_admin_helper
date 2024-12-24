import Link from 'next/link';

export default function Page() {
  const pages = [
    { id: 1, label: '플랫폼 인증 정보 설정', href: '/settings/platformauth' },
    { id: 2, label: '기준 방 설정', href: '/settings/standardroomlist' },
    { id: 3, label: '방 설정', href: '/settings/roomlist' },
    { id: 4, label: '물품 설정', href: '/settings/supply' },
    { id: 5, label: '물품 소모 설정', href: '/settings/supplyconsumption' },
  ];

  return (
    <div className="flex flex-col items-center justify-center h-full">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Settings</h2>
      <div
        className="grid gap-4"
        style={{
          gridTemplateColumns: 'repeat(4, minmax(100px, 1fr))',
        }}
      >
        {pages.map((page) => (
          <Link key={page.id} href={`${page.href}`}>
            <button className="w-full bg-blue-600 text-white p-2 rounded">
              {page.label}
            </button>
          </Link>
        ))}
      </div>
    </div>
  );
}
