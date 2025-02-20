export async function GET(request) {
  const { searchParams } = new URL(request.url);
  const start_date = searchParams.get('start_date');
  const end_date = searchParams.get('end_date');
  const mode = searchParams.get('mode');

  const addr = process.env.NEXT_PUBLIC_BE_ADDR;

  try {
    const clientAuthHeader = request.headers.get('Authorization');
    const djangoResponse = await fetch(
      `${addr}/analysis/general?start_date=${start_date}&end_date=${end_date}&mode=${mode}`,
      {
        method: 'GET',
        headers: {
          Authorization: clientAuthHeader,
        },
      }
    );

    return new Response(djangoResponse.body, {
      headers: {
        'Content-Type': 'text/event-stream',
        'Cache-Control': 'no-cache',
        Connection: 'keep-alive',
      },
    });
  } catch (error) {
    console.error('Error proxying request to Django:', error);
    return new Response(JSON.stringify({ error: 'Error proxying request' }), {
      status: 500,
    });
  }
}
