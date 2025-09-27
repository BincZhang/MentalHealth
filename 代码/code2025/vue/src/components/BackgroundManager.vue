<template>
  <div class="background-manager">
    <div class="manager-header">
      <h3>背景设置</h3>
      <button class="customize-btn" @click="showUploader = true">自定义背景</button>
    </div>
    
    <div class="current-background">
      <h4>当前背景</h4>
      <div class="background-preview">
        <img :src="currentBackgroundPreview" alt="当前背景" />
        <div class="background-info">
          <p>{{ backgroundInfo }}</p>
          <button class="reset-btn" @click="resetBackground" v-if="hasCustomBackground">重置为默认</button>
        </div>
      </div>
    </div>
    
    <!-- 背景上传弹窗 -->
    <el-dialog
      title="自定义背景"
      v-model="showUploader"
      width="50%"
      :before-close="handleCloseUploader"
    >
      <background-uploader
        @close="showUploader = false"
        @background-changed="handleBackgroundChanged"
      />
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import BackgroundUploader from './BackgroundUploader.vue';

export default {
  name: 'BackgroundManager',
  components: {
    BackgroundUploader
  },
  setup() {
    const showUploader = ref(false);
    const currentBackground = ref({
      type: 'default',
      className: '',
      url: ''
    });
    
    // 获取当前背景的预览图片
    const currentBackgroundPreview = computed(() => {
      if (currentBackground.value.type === 'custom' && currentBackground.value.url) {
        return currentBackground.value.url;
      } else {
        // 根据类名返回对应的预设背景预览
        const className = currentBackground.value.className || '';
        if (className === 'bg-login') return '/backgrounds/login.jpg';
        if (className === 'bg-home') return '/backgrounds/home.jpg';
        if (className === 'bg-test') return '/backgrounds/test.jpg';
        if (className === 'bg-chat') return '/backgrounds/chat.jpg';
        return '/backgrounds/default.jpg';
      }
    });
    
    // 背景信息显示
    const backgroundInfo = computed(() => {
      if (currentBackground.value.type === 'custom') {
        return '自定义背景';
      } else {
        const className = currentBackground.value.className || '';
        if (className === 'bg-login') return '登录页背景';
        if (className === 'bg-home') return '主页背景';
        if (className === 'bg-test') return '测评页背景';
        if (className === 'bg-chat') return '聊天页背景';
        return '默认背景';
      }
    });
    
    // 是否有自定义背景
    const hasCustomBackground = computed(() => {
      return currentBackground.value.type === 'custom' || currentBackground.value.className !== '';
    });
    
    // 关闭上传弹窗
    const handleCloseUploader = () => {
      showUploader.value = false;
    };
    
    // 处理背景变更
    const handleBackgroundChanged = (backgroundData) => {
      currentBackground.value = backgroundData;
      
      // 保存到本地存储
      localStorage.setItem('custom_background', JSON.stringify(backgroundData));
      
      // 应用背景
      applyBackground(backgroundData);
    };
    
    // 重置背景
    const resetBackground = () => {
      currentBackground.value = {
        type: 'default',
        className: '',
        url: ''
      };
      
      // 清除本地存储中的自定义背景
      localStorage.removeItem('custom_background');
      
      // 移除所有背景类名
      const bodyElement = document.body;
      bodyElement.classList.remove('bg-login', 'bg-home', 'bg-test', 'bg-chat');
      bodyElement.style.backgroundImage = ''; // 重置为默认背景
    };
    
    // 应用背景
    const applyBackground = (bgData) => {
      const bodyElement = document.body;
      
      // 先移除所有预设背景类
      bodyElement.classList.remove('bg-login', 'bg-home', 'bg-test', 'bg-chat');
      
      if (bgData.type === 'preset' && bgData.className) {
        // 应用预设背景类
        bodyElement.classList.add(bgData.className);
        bodyElement.style.backgroundImage = ''; // 清除自定义背景
      } else if (bgData.type === 'custom' && bgData.url) {
        // 应用自定义背景
        bodyElement.style.backgroundImage = `url('${bgData.url}')`;
      }
    };
    
    // 组件加载时，检查并应用已保存的背景
    onMounted(() => {
      const savedBackground = localStorage.getItem('custom_background');
      if (savedBackground) {
        try {
          const bgData = JSON.parse(savedBackground);
          currentBackground.value = bgData;
          applyBackground(bgData);
        } catch (e) {
          console.error('Error parsing saved background', e);
        }
      }
    });
    
    return {
      showUploader,
      currentBackground,
      currentBackgroundPreview,
      backgroundInfo,
      hasCustomBackground,
      handleCloseUploader,
      handleBackgroundChanged,
      resetBackground
    };
  }
};
</script>

<style scoped>
.background-manager {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.manager-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

h4 {
  font-size: 16px;
  color: #666;
  margin-bottom: 10px;
}

.customize-btn {
  background-color: #9bcfba;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.customize-btn:hover {
  background-color: #8ab8a7;
}

.current-background {
  margin-bottom: 20px;
}

.background-preview {
  display: flex;
  align-items: center;
  gap: 20px;
  background-color: #f9f9f9;
  border-radius: 6px;
  padding: 10px;
}

.background-preview img {
  width: 100px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.background-info {
  flex: 1;
}

.background-info p {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
}

.reset-btn {
  background-color: #f56c6c;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.reset-btn:hover {
  background-color: #e64a4a;
}
</style> 