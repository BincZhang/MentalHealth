<template>
  <div>
    <CommonHeader />
    <div class="external-content-container">
      <div class="toolbar">
        <el-button type="primary" plain size="small" icon="ArrowLeft" @click="goBack">返回上一页</el-button>
        <div class="url-display">
          <el-tooltip :content="url" placement="bottom" effect="light">
            <span>{{ displayUrl }}</span>
          </el-tooltip>
        </div>
        <el-button type="primary" size="small" icon="Right" @click="openInNewTab">在新标签页打开</el-button>
      </div>
      <div class="iframe-container">
        <iframe :src="url" ref="iframe" class="content-iframe"></iframe>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { ArrowLeft, Right } from '@element-plus/icons-vue';
import CommonHeader from '@/components/CommonHeader.vue';

const route = useRoute();
const router = useRouter();
const iframe = ref(null);

const url = ref('');
const title = ref('');

onMounted(() => {
  if (route.query.url) {
    url.value = decodeURIComponent(route.query.url);
    title.value = route.query.title || '外部内容';
    document.title = title.value;
  } else {
    ElMessage.error('无效的URL参数');
    router.push('/user');
  }
});

const displayUrl = computed(() => {
  if (!url.value) return '';
  // 显示简化的URL，最多40个字符
  if (url.value.length > 40) {
    return url.value.substring(0, 37) + '...';
  }
  return url.value;
});

const goBack = () => {
  router.go(-1);
};

const openInNewTab = () => {
  window.open(url.value, '_blank');
};
</script>

<style scoped>
.external-content-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
  margin: 0 auto;
  padding: 15px;
  background-color: #f5f7fa;
}

.toolbar {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  background-color: #fff;
  border-radius: 8px 8px 0 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.url-display {
  flex: 1;
  margin: 0 15px;
  padding: 6px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #606266;
  font-size: 14px;
}

.iframe-container {
  flex: 1;
  position: relative;
  border-radius: 0 0 8px 8px;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.content-iframe {
  width: 100%;
  height: 100%;
  border: none;
}
</style> 