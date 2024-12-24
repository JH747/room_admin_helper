import Link from 'next/link';

export default function Page() {
  const pages = [
    { id: 1, label: 'Overbooking 감지', href: '/analysis/detect' },
    { id: 2, label: '이전 예약 모아보기', href: '/analysis/retrieve' },
    { id: 3, label: '물품 소모 감지', href: '/analysis/supply' },
  ];

  return (
    <div className="flex flex-col items-center justify-center h-full">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Analysis</h2>
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
