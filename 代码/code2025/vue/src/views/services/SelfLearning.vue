<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>自助学习</h1>
        <p>丰富的心理健康学习资源，随时随地提升心理素养</p>
      </div>
    </div>
    
    <div class="container">
      <div class="service-intro">
        <div class="intro-text">
          <h2>服务介绍</h2>
          <p>自助学习资源库提供丰富的心理健康学习材料，包括电子书籍、音视频课程、心理测评工具和实用练习等，帮助您随时随地学习心理健康知识，提升心理素养。</p>
          <p>我们的资源内容由专业心理学者和咨询师精心挑选和制作，涵盖情绪管理、压力应对、人际关系、自我成长等多个方面，适合不同学习需求和水平的学生。</p>
        </div>
        <div class="intro-image">
          <img src="/backgrounds/login.jpg" alt="自助学习">
        </div>
      </div>
      
      <div class="resource-search">
        <h2>资源搜索</h2>
        <div class="search-container">
          <div class="search-box">
            <el-input
              v-model="searchQuery"
              placeholder="输入关键词搜索资源..."
              class="search-input"
              clearable
              prefix-icon="el-icon-search"
            ></el-input>
            <el-button type="primary" @click="searchResources">搜索</el-button>
          </div>
          
          <div class="filter-box">
            <div class="filter-item">
              <span>资源类型:</span>
              <el-select v-model="filters.type" placeholder="全部类型" clearable>
                <el-option
                  v-for="item in resourceTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
            
            <div class="filter-item">
              <span>主题分类:</span>
              <el-select v-model="filters.category" placeholder="全部分类" clearable>
                <el-option
                  v-for="item in categories"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
            
            <div class="filter-item">
              <span>难度级别:</span>
              <el-select v-model="filters.level" placeholder="全部级别" clearable>
                <el-option
                  v-for="item in levels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
          </div>
        </div>
      </div>
      
      <div class="resource-list">
        <h2>学习资源</h2>
        <div class="cards-container">
          <div v-for="(resource, index) in filteredResources" :key="index" class="resource-card">
            <div class="resource-icon">
              <i :class="getResourceIcon(resource.type)"></i>
            </div>
            <div class="resource-content">
              <h3>{{ resource.title }}</h3>
              <div class="resource-tags">
                <el-tag size="small">{{ resource.typeLabel }}</el-tag>
                <el-tag size="small" type="success">{{ resource.categoryLabel }}</el-tag>
                <el-tag size="small" type="warning">{{ getLevelLabel(resource.level) }}</el-tag>
              </div>
              <p class="resource-desc">{{ resource.description }}</p>
              <div class="resource-meta">
                <span><i class="fas fa-eye"></i> {{ resource.views }}</span>
                <span><i class="fas fa-download"></i> {{ resource.downloads }}</span>
                <span><i class="fas fa-star"></i> {{ resource.rating }}</span>
              </div>
              <div class="resource-actions">
                <el-button type="primary" size="small" @click="viewResource(resource)">
                  <i class="fas fa-eye"></i> 查看
                </el-button>
                <el-button type="success" size="small" @click="downloadResource(resource)">
                  <i class="fas fa-download"></i> 下载
                </el-button>
                <el-button type="info" size="small" @click="addToFavorites(resource)">
                  <i class="fas fa-heart"></i> 收藏
                </el-button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="pagination-container">
          <el-pagination
            :current-page="currentPage"
            :page-size="pageSize"
            :total="totalResources"
            layout="total, prev, pager, next, jumper"
            @current-change="handlePageChange"
            class="pagination"
          ></el-pagination>
        </div>
      </div>
      
      <div class="popular-resources">
        <h2>热门推荐</h2>
        <div class="popular-grid">
          <div v-for="(resource, index) in popularResources" :key="index" class="popular-card">
            <div class="popular-header">
              <div class="popular-icon">
                <i :class="getResourceIcon(resource.type)"></i>
              </div>
              <div class="popular-type">{{ resource.typeLabel }}</div>
            </div>
            <div class="popular-content">
              <h3>{{ resource.title }}</h3>
              <p>{{ resource.description }}</p>
              <div class="popular-meta">
                <el-tag size="small" type="success">{{ resource.categoryLabel }}</el-tag>
                <span class="rating"><i class="fas fa-star"></i> {{ resource.rating }}</span>
              </div>
            </div>
            <div class="popular-footer">
              <el-button type="primary" @click="viewResource(resource)">立即学习</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'SelfLearningPage',
  setup() {
    const searchQuery = ref('');
    const currentPage = ref(1);
    const pageSize = ref(10);
    const totalResources = ref(25); // 假设共有25个资源
    
    const filters = reactive({
      type: '',
      category: '',
      level: ''
    });
    
    const resourceTypes = [
      { value: 'ebook', label: '电子书籍' },
      { value: 'video', label: '视频课程' },
      { value: 'audio', label: '音频讲座' },
      { value: 'worksheet', label: '练习工具' },
      { value: 'test', label: '自测问卷' }
    ];
    
    const categories = [
      { value: 'emotion', label: '情绪管理' },
      { value: 'stress', label: '压力应对' },
      { value: 'relationship', label: '人际关系' },
      { value: 'growth', label: '自我成长' },
      { value: 'study', label: '学习技能' }
    ];
    
    const levels = [
      { value: 'beginner', label: '入门级' },
      { value: 'intermediate', label: '中级' },
      { value: 'advanced', label: '高级' }
    ];
    
    // 模拟资源数据
    const resources = reactive([
      {
        id: 1,
        title: '情绪管理基础：识别与调节情绪',
        type: 'ebook',
        typeLabel: '电子书籍',
        category: 'emotion',
        categoryLabel: '情绪管理',
        level: 'beginner',
        description: '这本电子书介绍了情绪的基本知识，包括如何识别、理解和有效调节情绪的方法。',
        views: 1256,
        downloads: 458,
        rating: 4.7
      },
      {
        id: 2,
        title: '高效学习方法与时间管理',
        type: 'video',
        typeLabel: '视频课程',
        category: 'study',
        categoryLabel: '学习技能',
        level: 'intermediate',
        description: '这个视频课程介绍了科学的学习方法和高效的时间管理技巧，帮助你提高学习效率。',
        views: 2345,
        downloads: 890,
        rating: 4.9
      },
      {
        id: 3,
        title: '人际交往的心理学：有效沟通技巧',
        type: 'audio',
        typeLabel: '音频讲座',
        category: 'relationship',
        categoryLabel: '人际关系',
        level: 'intermediate',
        description: '这个音频讲座分享了有效的沟通技巧和人际交往策略，帮助你改善人际关系。',
        views: 1876,
        downloads: 642,
        rating: 4.5
      },
      {
        id: 4,
        title: '抑郁自测问卷',
        type: 'test',
        typeLabel: '自测问卷',
        category: 'emotion',
        categoryLabel: '情绪管理',
        level: 'beginner',
        description: '这个自测问卷可以帮助你评估自己的抑郁程度，了解是否需要寻求专业帮助。',
        views: 3254,
        downloads: 1245,
        rating: 4.6
      },
      {
        id: 5,
        title: '正念减压练习工作表',
        type: 'worksheet',
        typeLabel: '练习工具',
        category: 'stress',
        categoryLabel: '压力应对',
        level: 'beginner',
        description: '这个练习工作表提供了一系列正念减压的练习方法，帮助你缓解压力和焦虑。',
        views: 1578,
        downloads: 875,
        rating: 4.8
      }
    ]);
    
    // 过滤后的资源列表
    const filteredResources = computed(() => {
      let result = resources;
      
      // 按关键词搜索
      if (searchQuery.value.trim()) {
        const query = searchQuery.value.toLowerCase();
        result = result.filter(item => 
          item.title.toLowerCase().includes(query) || 
          item.description.toLowerCase().includes(query)
        );
      }
      
      // 按类型过滤
      if (filters.type) {
        result = result.filter(item => item.type === filters.type);
      }
      
      // 按分类过滤
      if (filters.category) {
        result = result.filter(item => item.category === filters.category);
      }
      
      // 按难度过滤
      if (filters.level) {
        result = result.filter(item => item.level === filters.level);
      }
      
      return result;
    });
    
    // 热门资源
    const popularResources = computed(() => {
      return resources.sort((a, b) => b.rating - a.rating).slice(0, 3);
    });
    
    const getResourceIcon = (type) => {
      switch (type) {
        case 'ebook': return 'fas fa-book';
        case 'video': return 'fas fa-video';
        case 'audio': return 'fas fa-headphones';
        case 'worksheet': return 'fas fa-file-alt';
        case 'test': return 'fas fa-clipboard-check';
        default: return 'fas fa-file';
      }
    };
    
    const getLevelLabel = (level) => {
      const levelObj = levels.find(item => item.value === level);
      return levelObj ? levelObj.label : level;
    };
    
    const searchResources = () => {
      // 在实际应用中，这里可能会调用API进行搜索
      currentPage.value = 1; // 重置到第一页
    };
    
    const handlePageChange = (page) => {
      currentPage.value = page;
      // 在实际应用中，这里可能会调用API获取指定页的数据
    };
    
    const viewResource = (resource) => {
      ElMessage.success(`正在查看: ${resource.title}`);
      // 实际应用中，这里可能会打开资源或跳转到资源详情页
    };
    
    const downloadResource = (resource) => {
      ElMessage.success(`开始下载: ${resource.title}`);
      // 实际应用中，这里会触发资源下载
    };
    
    const addToFavorites = (resource) => {
      ElMessage.success(`已添加到收藏: ${resource.title}`);
      // 实际应用中，这里会将资源添加到用户收藏
    };
    
    return {
      searchQuery,
      filters,
      resourceTypes,
      categories,
      levels,
      filteredResources,
      popularResources,
      currentPage,
      pageSize,
      totalResources,
      getResourceIcon,
      getLevelLabel,
      searchResources,
      handlePageChange,
      viewResource,
      downloadResource,
      addToFavorites
    };
  }
};
</script>

<style scoped>
.service-page {
  font-family: 'PingFang SC', sans-serif;
  color: #333;
}

.page-header {
  background: linear-gradient(135deg, #5b7bf0, #3a5acf);
  color: white;
  padding: 60px 0;
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 2.5rem;
  margin-bottom: 15px;
}

.page-header p {
  font-size: 1.2rem;
  opacity: 0.9;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

h2 {
  font-size: 1.8rem;
  margin-bottom: 25px;
  color: #2B3856;
  position: relative;
  padding-bottom: 10px;
}

h2::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 60px;
  height: 3px;
  background-color: #5b7bf0;
}

.service-intro {
  display: flex;
  gap: 40px;
  margin-bottom: 60px;
}

.intro-text {
  flex: 1;
}

.intro-text p {
  margin-bottom: 15px;
  line-height: 1.6;
}

.intro-image {
  flex: 1;
}

.intro-image img {
  width: 100%;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.resource-search {
  margin-bottom: 40px;
}

.search-container {
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.search-box {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
}

.filter-box {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.resource-list {
  margin-bottom: 60px;
}

.cards-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.resource-card {
  display: flex;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.resource-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.resource-icon {
  width: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #5b7bf0, #3a5acf);
  color: white;
  font-size: 40px;
}

.resource-content {
  flex: 1;
  padding: 20px;
}

.resource-content h3 {
  font-size: 1.3rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.resource-tags {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.resource-desc {
  margin-bottom: 15px;
  color: #666;
  line-height: 1.5;
}

.resource-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  color: #666;
}

.resource-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.resource-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.popular-resources {
  margin-bottom: 60px;
}

.popular-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.popular-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
}

.popular-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.popular-header {
  padding: 15px;
  background: linear-gradient(135deg, #5b7bf0, #3a5acf);
  color: white;
  position: relative;
}

.popular-icon {
  width: 60px;
  height: 60px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
}

.popular-type {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
}

.popular-content {
  padding: 20px;
  flex: 1;
}

.popular-content h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.popular-content p {
  color: #666;
  line-height: 1.5;
  margin-bottom: 15px;
  height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.popular-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #ff9500;
  font-weight: 600;
}

.popular-footer {
  padding: 20px;
  border-top: 1px solid #f0f0f0;
  text-align: center;
}

@media (max-width: 991px) {
  .service-intro {
    flex-direction: column;
  }
  
  .popular-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 767px) {
  .filter-box {
    flex-direction: column;
    gap: 10px;
  }
  
  .filter-item {
    width: 100%;
  }
  
  .resource-card {
    flex-direction: column;
  }
  
  .resource-icon {
    width: 100%;
    height: 100px;
  }
  
  .popular-grid {
    grid-template-columns: 1fr;
  }
}
</style> 