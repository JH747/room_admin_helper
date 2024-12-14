import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Legend,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Legend);

const SalesChart = ({ data }) => {
  const productNames = Object.keys(data[Object.keys(data)[0]]);
  const dates = Object.keys(data);

  const datasets = productNames.map((product) => ({
    label: product,
    data: dates.map((date) =>
      Object.values(data[date][product] || {}).reduce((a, b) => a + b, 0)
    ),
    fill: false,
    borderColor: `#${Math.floor(Math.random() * 16777215).toString(16)}`, // 랜덤 색상
    tension: 0.3,
  }));

  const chartData = {
    labels: dates,
    datasets,
  };

  return (
    <div>
      <h2>판매 그래프</h2>
      <Line
        data={chartData}
        options={{
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
          },
        }}
      />
    </div>
  );
};

export default SalesChart;
