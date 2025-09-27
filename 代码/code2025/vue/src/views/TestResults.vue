<template>
  <div class="admin-container">
    <div class="page-header-card">
      <h2>用户测评结果</h2>
    </div>

    <div class="table-card">
      <el-table 
        :data="testResults" 
        class="custom-table"
        :header-cell-style="headerCellStyle"
        :cell-style="cellStyle"
        :row-class-name="tableRowClassName">
        <el-table-column prop="userId" label="用户ID" min-width="19%" />
        <el-table-column prop="testType" label="测评类型" min-width="19%" />
        <el-table-column prop="score" label="得分" min-width="19%" />
        <el-table-column prop="resultLevel" label="结果等级" min-width="19%" />
        <el-table-column prop="testDate" label="测评日期" min-width="24%" />
      </el-table>
    </div>
    
    <div class="action-card" v-if="loadFailed">
      <el-button type="primary" class="action-btn" @click="loadTestResults">
        <el-icon><Refresh /></el-icon> 重新加载数据
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'

const testResults = ref([])
const loadFailed = ref(false)

// 表格样式函数
const headerCellStyle = () => {
  return {
    backgroundColor: '#3a456b',
    color: '#ffffff',
    fontWeight: 'bold',
    fontSize: '16px',
    padding: '15px 0',
    textAlign: 'center'
  }
}

const cellStyle = () => {
  return {
    fontSize: '15px',
    padding: '15px 0',
    textAlign: 'center'
  }
}

const tableRowClassName = ({rowIndex}) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

const loadTestResults = async () => {
  try {
    loadFailed.value = false
    const user = JSON.parse(localStorage.getItem('code_user') || '{}')
    if (!user || !user.token) {
      ElMessage.error('未登录或token无效')
      loadFailed.value = true
      return
    }

    const response = await axios.get('http://localhost:9999/api/test/results', {
      headers: {
        'Authorization': `Bearer ${user.token}`
      }
    })
    console.log('获取到测评结果数据:', response.data)
    testResults.value = response.data
  } catch (error) {
    console.error('获取测评结果失败:', error)
    loadFailed.value = true
    if (error.response && error.response.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
    } else {
      ElMessage.error('获取测评结果失败，请稍后重试')
    }
  }
}

onMounted(() => {
  loadTestResults()
})
</script>

<style>
.admin-container {
  padding: 15px;
}

/* 卡片样式 */
.page-header-card, .action-card, .table-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 15px;
  margin-bottom: 15px;
  transition: box-shadow 0.3s ease;
}

.page-header-card:hover, .action-card:hover, .table-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.page-header-card h2 {
  margin: 0;
  text-align: center;
  font-size: 24px;
  color: #3a456b;
  font-weight: bold;
}

/* 操作按钮区域样式 */
.action-card {
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn {
  padding: 8px 16px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  opacity: 0.9;
}

.action-btn .el-icon {
  margin-right: 5px;
}

/* 表格样式 */
.custom-table {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  border: none;
}

.even-row {
  background-color: #f8f9fe;
}

.odd-row {
  background-color: #ffffff;
}

.even-row:hover, .odd-row:hover {
  background-color: #eaecff !important;
}
</style> 