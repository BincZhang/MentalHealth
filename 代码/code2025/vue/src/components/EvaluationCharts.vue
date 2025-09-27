<template>
  <div>
    <h2>测评结果统计</h2>
    <div style="margin-bottom: 20px;">
      <canvas id="lineChart"></canvas>
    </div>
    <div style="margin-bottom: 20px;">
      <canvas id="pieChart"></canvas>
    </div>
  </div>
</template>

<script>
import { onMounted } from 'vue';
import Chart from 'chart.js/auto';

export default {
  props: ['evaluationData'],
  setup(props) {
    onMounted(() => {
      if (props.evaluationData && props.evaluationData.length > 0) {
        // 折线图
        const lineCtx = document.getElementById('lineChart').getContext('2d');
        new Chart(lineCtx, {
          type: 'line',
          data: {
            labels: props.evaluationData.map(data => data.date),
            datasets: [{
              label: '测评结果',
              data: props.evaluationData.map(data => data.score),
              borderColor: 'rgba(75, 192, 192, 1)',
              borderWidth: 2,
              fill: false
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
              y: {
                beginAtZero: true
              }
            }
          }
        });

        // 饼状图
        const pieCtx = document.getElementById('pieChart').getContext('2d');
        new Chart(pieCtx, {
          type: 'pie',
          data: {
            labels: ['正常', '轻度', '中度', '重度'],
            datasets: [{
              data: [
                props.evaluationData.filter(data => data.level === '正常').length,
                props.evaluationData.filter(data => data.level === '轻度').length,
                props.evaluationData.filter(data => data.level === '中度').length,
                props.evaluationData.filter(data => data.level === '重度').length
              ],
              backgroundColor: [
                'rgba(75, 192, 192, 0.6)',
                'rgba(255, 206, 86, 0.6)',
                'rgba(54, 162, 235, 0.6)',
                'rgba(255, 99, 132, 0.6)'
              ]
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false
          }
        });
      }
    });
  }
};
</script>

<style scoped>
canvas {
  width: 100%;
  height: 300px;
}
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
</style>