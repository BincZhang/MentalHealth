<template>
  <div class="background-uploader">
    <h3>自定义背景图片</h3>
    <div class="upload-container">
      <div class="preview-container" v-if="imagePreview">
        <img :src="imagePreview" alt="背景预览" class="preview-image" />
        <button class="remove-btn" @click="removeImage">
          <i class="el-icon-delete"></i>
        </button>
      </div>
      <div class="upload-box" v-else @click="triggerFileInput">
        <i class="el-icon-plus"></i>
        <span>点击上传</span>
      </div>
      <input
        type="file"
        ref="fileInput"
        style="display: none"
        accept="image/*"
        @change="handleFileChange"
      />
    </div>
    <div class="action-buttons">
      <button class="apply-btn" @click="applyBackground" :disabled="!imagePreview || isUploading">
        <span v-if="!isUploading">应用背景</span>
        <span v-else class="loading-spinner"></span>
      </button>
      <button class="cancel-btn" @click="$emit('close')" :disabled="isUploading">取消</button>
    </div>
    <div class="preset-backgrounds">
      <h4>预设背景</h4>
      <div class="background-grid">
        <div 
          v-for="(bg, index) in presetBackgrounds" 
          :key="index" 
          class="preset-item"
          @click="selectPresetBackground(bg)"
        >
          <img :src="bg.src" :alt="bg.name" />
          <span>{{ bg.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  name: 'BackgroundUploader',
  emits: ['close', 'background-changed'],
  setup(props, { emit }) {
    const imagePreview = ref('');
    const fileInput = ref(null);
    const uploadedFile = ref(null);
    const isUploading = ref(false);
    
    // 预设背景图片
    const presetBackgrounds = ref([
      { name: '默认', src: '/backgrounds/default.jpg', className: '' },
      { name: '登录', src: '/backgrounds/login.jpg', className: 'bg-login' },
      { name: '主页', src: '/backgrounds/home.jpg', className: 'bg-home' },
      { name: '测评', src: '/backgrounds/test.jpg', className: 'bg-test' },
      { name: '聊天', src: '/backgrounds/chat.jpg', className: 'bg-chat' }
    ]);

    // 触发文件选择
    const triggerFileInput = () => {
      fileInput.value.click();
    };

    // 处理文件变化
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        uploadedFile.value = file;
        const reader = new FileReader();
        reader.onload = (e) => {
          imagePreview.value = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    };

    // 移除选择的图片
    const removeImage = () => {
      imagePreview.value = '';
      uploadedFile.value = null;
      if (fileInput.value) {
        fileInput.value.value = '';
      }
    };

    // 选择预设背景
    const selectPresetBackground = (bg) => {
      // 如果是预设背景，直接使用类名
      if (bg.className) {
        emit('background-changed', { type: 'preset', className: bg.className });
        emit('close');
      } else {
        imagePreview.value = bg.src;
      }
    };

    // 应用背景
    const applyBackground = async () => {
      if (uploadedFile.value) {
        isUploading.value = true;
        
        try {
          // 创建FormData对象上传文件
          const formData = new FormData();
          formData.append('background', uploadedFile.value);
          
          // 获取用户token
          const user = JSON.parse(localStorage.getItem('code_user') || '{}');
          
          // 调用后端API上传背景图
          const response = await axios.post('http://localhost:9999/files/background/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Authorization': `Bearer ${user.token}`
            }
          });
          
          if (response.data.code === '200') {
            const data = response.data.data;
            // 通知父组件背景已更改
            emit('background-changed', { 
              type: 'custom', 
              url: data.url,
              fileName: data.fileName
            });
            
            // 关闭上传器
            emit('close');
          } else {
            console.error('背景上传失败:', response.data.msg);
            // 如果上传失败，使用本地预览URL作为备选
            emit('background-changed', { 
              type: 'custom', 
              url: imagePreview.value,
              fileName: `custom_bg_${Date.now()}.${uploadedFile.value.name.split('.').pop()}`
            });
            emit('close');
          }
        } catch (error) {
          console.error('背景上传出错:', error);
          // 如果上传出错，使用本地预览URL作为备选
          emit('background-changed', { 
            type: 'custom', 
            url: imagePreview.value,
            fileName: `custom_bg_${Date.now()}.${uploadedFile.value.name.split('.').pop()}`
          });
          emit('close');
        } finally {
          isUploading.value = false;
        }
      }
    };

    onMounted(() => {
      // 这里可以加载已保存的用户自定义背景
      const savedBackground = localStorage.getItem('custom_background');
      if (savedBackground) {
        try {
          const bgData = JSON.parse(savedBackground);
          if (bgData.type === 'custom' && bgData.url) {
            imagePreview.value = bgData.url;
          }
        } catch (e) {
          console.error('Error parsing saved background', e);
        }
      }
    });

    return {
      imagePreview,
      fileInput,
      uploadedFile,
      isUploading,
      presetBackgrounds,
      triggerFileInput,
      handleFileChange,
      removeImage,
      selectPresetBackground,
      applyBackground
    };
  }
};
</script>

<style scoped>
.background-uploader {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  width: 100%;
  max-width: 600px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

h3 {
  font-size: 20px;
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
}

h4 {
  font-size: 16px;
  margin-top: 20px;
  margin-bottom: 10px;
  color: #666;
}

.upload-container {
  margin-bottom: 20px;
}

.upload-box {
  border: 2px dashed #dcdfe6;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  transition: border-color 0.3s;
}

.upload-box:hover {
  border-color: #9bcfba;
}

.upload-box i {
  font-size: 28px;
  color: #c0c4cc;
  margin-bottom: 8px;
}

.upload-box span {
  font-size: 14px;
  color: #909399;
}

.preview-container {
  position: relative;
  width: 100%;
  height: 150px;
  margin-bottom: 10px;
  border-radius: 6px;
  overflow: hidden;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
}

.remove-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s;
}

.remove-btn:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.apply-btn, .cancel-btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.apply-btn {
  background-color: #9bcfba;
  color: white;
}

.apply-btn:hover {
  background-color: #8ab8a7;
}

.apply-btn:disabled {
  background-color: #c8c8c8;
  cursor: not-allowed;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #666;
}

.cancel-btn:hover {
  background-color: #e8e8e8;
}

.background-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 10px;
  margin-top: 10px;
}

.preset-item {
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  position: relative;
  height: 70px;
  transition: transform 0.3s;
}

.preset-item:hover {
  transform: scale(1.05);
}

.preset-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preset-item span {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  font-size: 12px;
  padding: 4px;
  text-align: center;
}

/* 加载动画 */
.loading-spinner {
  display: inline-block;
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 0.8s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style> 