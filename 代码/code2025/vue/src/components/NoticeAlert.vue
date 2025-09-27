<template>
  <div v-if="latestNotice" class="notice-alert" :class="{ 'notice-alert-visible': visible }">
    <div class="notice-alert-header">
      <i class="el-icon-bell"></i>
      <span class="notice-alert-title">最新公告</span>
      <div class="notice-alert-actions">
        <el-button type="text" size="small" @click="viewAll">查看全部</el-button>
        <el-button type="text" size="small" @click="closeAlert">关闭</el-button>
      </div>
    </div>
    <div class="notice-alert-content">
      <div class="notice-alert-item">
        <div class="notice-alert-item-title">{{ latestNotice.title }}</div>
        <div class="notice-alert-item-content">{{ truncatedContent }}</div>
        <div class="notice-alert-item-time">{{ formatTime(latestNotice.time) }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'NoticeAlert',
  setup() {
    const router = useRouter();
    const latestNotice = ref(null);
    const visible = ref(false);
    const lastCheckedId = ref(0);
    const pollTimer = ref(null);
    const POLL_INTERVAL = 60000; // 1分钟检查一次
    
    // 计算截断后的内容
    const truncatedContent = computed(() => {
      if (!latestNotice.value || !latestNotice.value.content) return '';
      return latestNotice.value.content.length > 100 
        ? latestNotice.value.content.substring(0, 100) + '...' 
        : latestNotice.value.content;
    });
    
    // 格式化时间
    const formatTime = (timeStr) => {
      if (!timeStr) return '';
      const date = new Date(timeStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    };
    
    // 检查最新公告
    const checkLatestNotice = async () => {
      try {
        // 获取用户token
        const userStr = localStorage.getItem('code_user');
        let token = '';
        if (userStr) {
          const user = JSON.parse(userStr);
          token = user.token || '';
        }
        
        // 使用selectPage接口获取公告
        const res = await axios.get('http://localhost:9999/api/notice/selectPage', {
          params: {
            pageNum: 1,
            pageSize: 10 // 只需要最新的几条
          },
          headers: {
            'Authorization': `Bearer ${token}`,
            'token': token
          }
        });
        
        let latestNoticeItem = null;
        
        // 判断返回结果格式
        if (res.data && res.data.code === '200' && res.data.data && Array.isArray(res.data.data.list)) {
          // 管理员接口返回的格式是 { code, data: { list, total } }
          const sortedNotices = res.data.data.list.sort((a, b) => new Date(b.time) - new Date(a.time));
          if (sortedNotices.length > 0) {
            latestNoticeItem = sortedNotices[0];
          }
        } else if (Array.isArray(res.data) && res.data.length > 0) {
          // 如果直接返回数组，也兼容处理
          const sortedNotices = res.data.sort((a, b) => new Date(b.time) - new Date(a.time));
          if (sortedNotices.length > 0) {
            latestNoticeItem = sortedNotices[0];
          }
        } else {
          // 尝试使用另一个接口
          try {
            const backupRes = await axios.get('http://localhost:9999/api/notice/selectAll', {
              headers: {
                'Authorization': `Bearer ${token}`,
                'token': token
              }
            });
            
            if (backupRes.data && backupRes.data.code === '200' && Array.isArray(backupRes.data.data)) {
              const sortedNotices = backupRes.data.data.sort((a, b) => new Date(b.time) - new Date(a.time));
              if (sortedNotices.length > 0) {
                latestNoticeItem = sortedNotices[0];
              }
            }
          } catch (backupError) {
            console.error('备选接口调用失败', backupError);
          }
        }
        
        // 如果有新公告且与上次检查的不同，则显示提醒
        if (latestNoticeItem && latestNoticeItem.id !== lastCheckedId.value) {
          latestNotice.value = latestNoticeItem;
          lastCheckedId.value = latestNoticeItem.id;
          
          // 显示提醒
          visible.value = true;
          
          // 延迟自动关闭
          setTimeout(() => {
            visible.value = false;
          }, 10000); // 10秒后自动关闭
        }
      } catch (error) {
        console.error('检查最新公告失败', error);
      }
    };
    
    // 开始轮询
    const startPolling = () => {
      // 先停止可能已存在的轮询
      stopPolling();
      
      // 立即检查一次
      checkLatestNotice();
      
      // 设置定时检查
      pollTimer.value = setInterval(checkLatestNotice, POLL_INTERVAL);
    };
    
    // 停止轮询
    const stopPolling = () => {
      if (pollTimer.value) {
        clearInterval(pollTimer.value);
        pollTimer.value = null;
      }
    };
    
    // 查看全部公告
    const viewAll = () => {
      router.push('/notice-list');
      visible.value = false;
    };
    
    // 关闭提醒
    const closeAlert = () => {
      visible.value = false;
    };
    
    // 组件挂载时开始轮询
    onMounted(() => {
      startPolling();
    });
    
    // 组件销毁前停止轮询
    onBeforeUnmount(() => {
      stopPolling();
    });
    
    // 监听用户登录状态
    watch(() => localStorage.getItem('code_user'), (newVal) => {
      if (newVal) {
        // 用户登录状态变化时，立即检查一次
        checkLatestNotice();
      }
    });
    
    return {
      latestNotice,
      visible,
      truncatedContent,
      formatTime,
      viewAll,
      closeAlert
    };
  }
};
</script>

<style scoped>
.notice-alert {
  position: fixed;
  bottom: -200px;
  right: 20px;
  width: 350px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  z-index: 2000;
  overflow: hidden;
  transition: bottom 0.3s ease;
  border-top: 3px solid #409EFF;
}

.notice-alert-visible {
  bottom: 20px;
}

.notice-alert-header {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  background-color: #ecf5ff;
  color: #409EFF;
  border-bottom: 1px solid #d9ecff;
}

.notice-alert-title {
  margin-left: 5px;
  font-weight: bold;
  flex: 1;
}

.notice-alert-actions {
  display: flex;
  gap: 5px;
}

.notice-alert-content {
  padding: 10px 15px;
  max-height: 200px;
  overflow-y: auto;
}

.notice-alert-item {
  padding: 5px 0;
}

.notice-alert-item-title {
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.notice-alert-item-content {
  font-size: 12px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 5px;
  white-space: pre-wrap;
}

.notice-alert-item-time {
  font-size: 11px;
  color: #999;
  text-align: right;
}
</style> 