<template>
  <div class="announcement-page page-container">
    <CommonHeader />
    
    <div class="announcement-header">
      <h1 class="page-title">校园公告</h1>
      <div class="page-description">显示来自管理员的最新公告信息</div>
      <!-- 添加刷新按钮 -->
      <div class="refresh-container">
        <el-button 
          type="primary" 
          size="small" 
          :loading="loading" 
          @click="loadAnnouncements"
          icon="Refresh" 
          round
        >
          {{ loading ? '加载中...' : '刷新公告' }}
        </el-button>
      </div>
    </div>

    <div class="announcement-container">
      <!-- 公告加载状态 -->
      <div v-if="loading" class="announcement-loading">
        <el-skeleton :rows="3" animated />
        <el-skeleton style="margin-top: 20px" :rows="2" animated />
      </div>
      
      <!-- 没有公告时的展示 -->
      <div v-else-if="announcements.length === 0" class="empty-announcements">
        <i class="el-icon-bell empty-icon"></i>
        <p>暂时没有公告</p>
        <p class="empty-sub">管理员发布公告后将显示在此处</p>
      </div>
      
      <!-- 公告列表 -->
      <div v-else class="announcement-list">
        <TransitionGroup name="announcement-fade">
          <div 
            v-for="announcement in announcements" 
            :key="announcement.id" 
            class="announcement-card"
            :class="{'announcement-new': isNewAnnouncement(announcement)}"
          >
            <div class="announcement-card-header">
              <h2 class="announcement-title">{{ announcement.title }}</h2>
              <el-tag v-if="isNewAnnouncement(announcement)" type="danger" size="small" effect="dark">新</el-tag>
            </div>
            
            <div class="announcement-content" v-html="formatContent(announcement.content)"></div>
            
            <div class="announcement-footer">
              <span class="announcement-time">发布时间: {{ formatTime(announcement.time) }}</span>
            </div>
          </div>
        </TransitionGroup>
      </div>
    </div>
    
    <!-- 回到顶部 -->
    <el-backtop :right="20" :bottom="20"></el-backtop>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeUnmount } from 'vue'
import axios from 'axios'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import CommonHeader from '@/components/CommonHeader.vue'

// 状态
const announcements = ref([])
const loading = ref(true)
const lastCheckTime = ref(new Date()) // 上次查看时间
const refreshTimer = ref(null)

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return '未知时间'
  
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now - date // 时间差（毫秒）
  
  // 1小时内显示"X分钟前"
  if (diff < 60 * 60 * 1000) {
    const minutes = Math.floor(diff / (60 * 1000))
    return minutes <= 0 ? '刚刚' : `${minutes}分钟前`
  }
  
  // 24小时内显示"今天 HH:MM"
  if (diff < 24 * 60 * 60 * 1000 && 
      date.getDate() === now.getDate() && 
      date.getMonth() === now.getMonth() && 
      date.getFullYear() === now.getFullYear()) {
    return `今天 ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  }
  
  // 一周内显示"周X HH:MM"
  if (diff < 7 * 24 * 60 * 60 * 1000) {
    const weekdays = ['日', '一', '二', '三', '四', '五', '六']
    return `周${weekdays[date.getDay()]} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
  }
  
  // 其他显示完整日期
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 格式化内容（处理换行和链接）
const formatContent = (content) => {
  if (!content) return ''
  
  // 将换行符转换为<br>
  let formatted = content.replace(/\n/g, '<br>')
  
  // 将URL转换为可点击的链接
  const urlRegex = /(https?:\/\/[^\s]+)/g
  formatted = formatted.replace(urlRegex, '<a href="$1" target="_blank" class="announcement-link">$1</a>')
  
  return formatted
}

// 判断是否为新公告（7天内发布）
const isNewAnnouncement = (announcement) => {
  if (!announcement || !announcement.time) return false
  
  const announcementTime = new Date(announcement.time)
  const now = new Date()
  const diff = now - announcementTime // 时间差（毫秒）
  
  // 2天内的公告标记为新
  return diff < 2 * 24 * 60 * 60 * 1000
}

// 加载公告
const loadAnnouncements = async () => {
  try {
    loading.value = true
    console.log('开始加载公告数据...')
    
    console.log('使用selectPage接口获取公告...')
    // 使用统一的request工具发起请求
    const res = await request.get('/api/notice/selectPage', {
      params: {
        pageNum: 1,
        pageSize: 100 // 获取足够多的公告
      }
    })
    
    console.log('获取到的公告数据:', res)
    
    // 判断返回结果格式
    if (res.code === '200' && res.data && Array.isArray(res.data.list)) {
      // 管理员接口返回的格式是 { code, data: { list, total } }
      console.log('成功从selectPage接口获取公告数据，条数:', res.data.list.length)
      announcements.value = res.data.list.sort((a, b) => new Date(b.time) - new Date(a.time))
      
      // 记录当前时间为最后检查时间
      lastCheckTime.value = new Date()
    } else if (Array.isArray(res)) {
      // 如果直接返回数组，也兼容处理
      console.log('接口直接返回数组格式，条数:', res.length)
      announcements.value = res.sort((a, b) => new Date(b.time) - new Date(a.time))
      lastCheckTime.value = new Date()
    } else {
      console.error('公告数据格式不符合预期:', res)
      ElMessage.warning('获取公告数据格式不正确，尝试使用备选接口')
      
      // 尝试使用另一个接口
      console.log('尝试使用selectAll接口获取公告...')
      const backupRes = await request.get('/api/notice/selectAll')
      
      console.log('备选接口返回数据:', backupRes)
      
      if (backupRes.code === '200' && Array.isArray(backupRes.data)) {
        console.log('成功从selectAll接口获取公告数据，条数:', backupRes.data.length)
        announcements.value = backupRes.data.sort((a, b) => new Date(b.time) - new Date(a.time))
        lastCheckTime.value = new Date()
      } else {
        console.error('所有接口都无法获取有效的公告数据')
        // 显示调试信息
        ElMessage.error({
          message: '无法获取公告数据，请检查网络或联系管理员',
          duration: 5000
        })
        
        // 创建一些测试数据，以便测试界面显示
        console.log('创建测试数据用于界面显示...')
        announcements.value = [
          {
            id: 1, 
            title: '测试公告1', 
            content: '这是一条测试公告内容，用于界面显示测试。\n\n包含多行内容和https://example.com链接。', 
            time: new Date().toISOString()
          },
          {
            id: 2, 
            title: '测试公告2', 
            content: '这是另一条测试公告。', 
            time: new Date(Date.now() - 3*24*60*60*1000).toISOString()
          }
        ]
      }
    }
  } catch (error) {
    console.error('获取公告失败', error)
    // 显示详细错误信息
    ElMessage.error({
      message: `获取公告列表失败: ${error.message}`,
      duration: 5000
    })
    
    // 创建一些测试数据，以便测试界面显示
    console.log('创建测试数据用于界面显示...')
    announcements.value = [
      {
        id: 1, 
        title: '测试公告1', 
        content: '这是一条测试公告内容，用于界面显示测试。\n\n包含多行内容和https://example.com链接。', 
        time: new Date().toISOString()
      }
    ]
  } finally {
    loading.value = false
    console.log('公告数据加载完成')
  }
}

// 定时刷新公告（每60秒）
const startAutoRefresh = () => {
  stopAutoRefresh() // 先清除可能存在的定时器
  
  refreshTimer.value = setInterval(() => {
    loadAnnouncements()
  }, 60000) // 每60秒刷新一次
}

// 停止自动刷新
const stopAutoRefresh = () => {
  if (refreshTimer.value) {
    clearInterval(refreshTimer.value)
    refreshTimer.value = null
  }
}

// 组件挂载时加载公告
onMounted(() => {
  loadAnnouncements()
  startAutoRefresh()
})

// 组件卸载前清除定时器
onBeforeUnmount(() => {
  stopAutoRefresh()
})
</script>

<style scoped>
.announcement-page {
  min-height: 100vh;
  padding: 20px;
  padding-top: 0;
}

.announcement-header {
  text-align: center;
  margin: 30px 0;
}

.page-title {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  position: relative;
  display: inline-block;
}

.page-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 4px;
  background: #9bcfba;
  border-radius: 2px;
}

.page-description {
  color: #666;
  font-size: 16px;
  margin-bottom: 15px;
}

/* 刷新按钮容器 */
.refresh-container {
  margin-top: 15px;
}

.announcement-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.announcement-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: all 0.3s;
}

.announcement-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.announcement-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.announcement-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.announcement-content {
  font-size: 16px;
  color: #444;
  line-height: 1.6;
  margin-bottom: 15px;
  white-space: pre-wrap;
}

.announcement-footer {
  display: flex;
  justify-content: flex-end;
  font-size: 14px;
  color: #999;
}

.announcement-time {
  font-style: italic;
}

.announcement-new {
  border-left: 4px solid #f56c6c;
  background-color: rgba(255, 248, 197, 0.2);
}

.announcement-loading {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.empty-announcements {
  text-align: center;
  padding: 60px 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  color: #909399;
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 20px;
  color: #c0c4cc;
}

.empty-sub {
  font-size: 14px;
  margin-top: 5px;
  color: #c0c4cc;
}

.announcement-link {
  color: #409EFF;
  text-decoration: underline;
}

/* 过渡动画 */
.announcement-fade-enter-active,
.announcement-fade-leave-active {
  transition: all 0.5s ease;
}

.announcement-fade-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.announcement-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style> 