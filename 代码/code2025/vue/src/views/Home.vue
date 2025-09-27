<template>
  <div class="home-container page-container bg-home">
    <div class="card">你好同学！欢迎来到校园心理健康助手！</div>
    <div class="card" style="margin-top: 10px; width: 100%">
      <h2>测评结果趋势</h2>
      
      <div style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
        <div>
          <el-select v-model="selectedUserId" placeholder="选择用户" @change="loadUserTestResults">
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.name"
              :value="user.id">
            </el-option>
          </el-select>
          <el-button type="primary" style="margin-left: 10px" @click="loadAllData">刷新数据</el-button>
        </div>
      </div>
      
      <div v-loading="loading" style="width: 100%; height: 400px;">
        <div v-if="noData" style="display: flex; justify-content: center; align-items: center; height: 300px;">
          <span>暂无测评数据</span>
        </div>
        <div v-else>
          <canvas id="testResultsChart" style="width: 100%; height: 400px;"></canvas>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted, ref, computed} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";
import axios from 'axios';
import Chart from 'chart.js/auto';

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}"),
})

// 用户列表和选择
const userList = ref([]);
const selectedUserId = ref(null);

// 测评结果数据
const allTestResults = ref([]);
const filteredResults = ref([]);
const loading = ref(false);
const noData = computed(() => filteredResults.value.length === 0);

// 加载所有测评结果数据
const loadAllData = () => {
  loading.value = true;
  allTestResults.value = []; // 清空之前的数据
  
  // 从API获取数据，与TestResults.vue使用相同的API
  axios.get('http://localhost:9999/api/test/results', {
    headers: {
      'Authorization': `Bearer ${data.user.token}`
    }
  }).then(response => {
    loading.value = false;
    if (response.data && Array.isArray(response.data)) {
      // 处理数据，添加等级数值和测评类型名称
      allTestResults.value = response.data.map(item => {
        return {
          ...item,
          level: item.resultLevel || item.level,
          date: item.testDate || item.date,
          testTypeName: getTestTypeName(item.testType),
          userId: item.userId || 1
        };
      });
      
      console.log('成功获取到测评数据:', allTestResults.value);
      
      // 提取所有用户ID
      const userIds = [...new Set(allTestResults.value.map(item => item.userId))];
      
      // 如果发现新的用户ID，添加到用户列表
      userIds.forEach(id => {
        if (!userList.value.some(user => user.id === id)) {
          userList.value.push({ id, name: `${id}` });
        }
      });
      
      // 如果未选择用户但有数据，选择第一个用户
      if (selectedUserId.value === null && userList.value.length > 0) {
        selectedUserId.value = userList.value[0].id;
      }
      
      // 过滤选定用户的数据
      loadUserTestResults();
    } else {
      console.warn('返回的测评数据格式不符合预期');
      ElMessage.warning('未获取到测评数据');
    }
  }).catch(error => {
    loading.value = false;
    console.error('获取测评数据失败:', error);
    ElMessage.error('获取测评数据失败');
    
    // 如果API失败，使用模拟数据
    const mockData = generateMockTestData();
    allTestResults.value = mockData;
    
    // 如果未选择用户，选择第一个用户
    if (selectedUserId.value === null && userList.value.length > 0) {
      selectedUserId.value = userList.value[0].id;
    }
    
    // 过滤选定用户的数据
    loadUserTestResults();
  });
}

// 根据选定的用户ID加载测评结果
const loadUserTestResults = () => {
  if (selectedUserId.value === null) return;
  
  filteredResults.value = allTestResults.value.filter(item => item.userId === selectedUserId.value);
  console.log('当前用户测评数据:', filteredResults.value);
  
  renderChart();
}

// 根据测试类型获取名称
const getTestTypeName = (type) => {
  const typeMap = {
    'DEPRESSION': '抑郁自评',
    'ANXIETY': '焦虑自评',
    'STRESS': '压力测试',
    'PERSONALITY': '性格测试',
    'EMOTION': '情绪管理',
    'INTERPERSONAL': '人际关系'
  };
  return typeMap[type] || type;
}

// 生成模拟测评数据（API失败时使用）
const generateMockTestData = () => {
  const testTypes = ['DEPRESSION', 'ANXIETY', 'STRESS', 'PERSONALITY'];
  const mockData = [];
  
  // 为ID为1和2的用户生成模拟数据
  for (let userId = 1; userId <= 2; userId++) {
    // 每个用户2种测评类型
    const userTestTypes = userId === 1 ? 
      ['DEPRESSION', 'ANXIETY'] : 
      ['STRESS', 'PERSONALITY'];
    
    userTestTypes.forEach(testType => {
      // 每种测评类型4条数据，不同日期
      const recordCount = 4;
      
      for (let i = 0; i < recordCount; i++) {
        const month = (i + 1).toString().padStart(2, '0');
        const day = (5 + i * 5).toString().padStart(2, '0');
        const date = `2023-${month}-${day}`;
        
        // 模拟得分和等级
        let score, level;
        if (userId === 1) {
          // ID 1：从低分到高分，表示状态改善
          score = 50 + i * 10 + Math.floor(Math.random() * 10);
        } else {
          // ID 2：分数波动，表示情绪不稳定
          score = 65 + (i % 2 === 0 ? -15 : 15) + Math.floor(Math.random() * 10);
        }
        
        // 根据分数确定等级
        if (score < 60) {
          level = '重度';
        } else if (score < 70) {
          level = '中度';
        } else if (score < 80) {
          level = '轻度';
        } else {
          level = '正常';
        }
        
        mockData.push({
          id: mockData.length + 1,
          userId: userId,
          testType: testType,
          score: score,
          level: level,
          date: date,
          testTypeName: getTestTypeName(testType)
        });
      }
    });
  }
  
  return mockData;
}

// 渲染折线图
const renderChart = () => {
  if (filteredResults.value.length > 0) {
    // 确保DOM已经渲染并可用
    setTimeout(() => {
      const ctx = document.getElementById('testResultsChart');
      if (ctx) {
        // 检查是否已有图表实例，如果有则销毁
        if (window.testChart) {
          window.testChart.destroy();
        }
        
        // 获取所有日期并排序
        const sortedDates = [...new Set(filteredResults.value.map(item => item.date))].sort((a, b) => new Date(a) - new Date(b));
        
        // 根据测试类型分类数据
        const testTypes = [...new Set(filteredResults.value.map(item => item.testType))];
        const datasets = testTypes.map((testType, index) => {
          const typeData = filteredResults.value.filter(item => item.testType === testType);
          // 固定颜色映射
          const colors = [
            { border: 'rgba(75, 192, 192, 1)', background: 'rgba(75, 192, 192, 0.2)' },  // 抑郁 - 青色
            { border: 'rgba(255, 99, 132, 1)', background: 'rgba(255, 99, 132, 0.2)' },  // 焦虑 - 红色
            { border: 'rgba(255, 205, 86, 1)', background: 'rgba(255, 205, 86, 0.2)' },  // 压力 - 黄色
            { border: 'rgba(54, 162, 235, 1)', background: 'rgba(54, 162, 235, 0.2)' },  // 性格 - 蓝色
            { border: 'rgba(153, 102, 255, 1)', background: 'rgba(153, 102, 255, 0.2)' }, // 情绪 - 紫色
            { border: 'rgba(255, 159, 64, 1)', background: 'rgba(255, 159, 64, 0.2)' }   // 人际 - 橙色
          ];
          
          // 为每个日期准备数据点，如果某个日期没有数据，则设为null
          const dataPoints = sortedDates.map(date => {
            const point = typeData.find(item => item.date === date);
            return point ? point.score : null;
          });
          
          return {
            label: getTestTypeName(testType),
            data: dataPoints,
            borderColor: colors[index % colors.length].border,
            backgroundColor: colors[index % colors.length].background,
            borderWidth: 2,
            tension: 0.3,
            fill: false,
            spanGaps: true
          };
        });
        
        try {
          window.testChart = new Chart(ctx, {
            type: 'line',
            data: {
              labels: sortedDates,
              datasets: datasets
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              plugins: {
                legend: {
                  position: 'top',
                },
                title: {
                  display: true,
                  text: `用户 ${getUserName(selectedUserId.value)} 的测评得分趋势`,
                  font: {
                    size: 16,
                    weight: 'bold'
                  }
                },
                tooltip: {
                  callbacks: {
                    title: function(tooltipItems) {
                      return `日期: ${tooltipItems[0].label}`;
                    },
                    label: function(context) {
                      const testType = context.dataset.label;
                      const date = context.label;
                      const item = filteredResults.value.find(
                        item => item.date === date && getTestTypeName(item.testType) === testType
                      );
                      
                      if (!item) return [`测试类型: ${testType}`, '无数据'];
                      
                      return [
                        `测试类型: ${testType}`,
                        `得分: ${item.score || 0}`,
                        `等级: ${item.level || '未知'}`
                      ];
                    }
                  }
                }
              },
              scales: {
                y: {
                  beginAtZero: true,
                  min: 0,
                  max: 100,
                  title: {
                    display: true,
                    text: '得分',
                    font: {
                      weight: 'bold'
                    }
                  },
                  ticks: {
                    callback: function(value) {
                      return value + ' 分';
                    }
                  }
                },
                x: {
                  title: {
                    display: true,
                    text: '日期',
                    font: {
                      weight: 'bold'
                    }
                  }
                }
              },
              animation: {
                duration: 1000,
                easing: 'easeOutQuart'
              },
              hover: {
                mode: 'index',
                intersect: false
              }
            }
          });
        } catch (error) {
          console.error('Error creating chart:', error);
          ElMessage.error('图表创建失败');
        }
      } else {
        console.error('Chart canvas element not found');
      }
    }, 200);
  }
}

// 获取用户名称
const getUserName = (userId) => {
  const user = userList.value.find(u => u.id === userId);
  return user ? user.name : `${userId}`;
}

onMounted(() => {
  loadAllData();
})
</script>

<style scoped>
canvas {
  width: 100%;
  height: 100%;
}
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}
.card {
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}
</style>

