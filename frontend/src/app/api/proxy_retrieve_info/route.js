export async function GET(request) {
  const { searchParams } = new URL(request.url);
  const start_date = searchParams.get('start_date');
  const end_date = searchParams.get('end_date');
  const mode = searchParams.get('mode');

  try {
    const clientAuthHeader = request.headers.get('Authorization');
    const djangoResponse = await fetch(
      `http://127.0.0.1:8080/analysis/general?start_date=${start_date}&end_date=${end_date}&mode=${mode}`,
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
