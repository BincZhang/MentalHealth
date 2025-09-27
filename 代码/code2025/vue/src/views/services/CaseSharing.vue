<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>案例分享</h1>
        <p>真实心理案例分享，启发思考与成长</p>
      </div>
    </div>
    
    <div class="container">
      <div class="case-filter">
        <div class="filter-tabs">
          <div 
            v-for="(category, index) in categories" 
            :key="index"
            class="filter-tab"
            :class="{ active: activeCategory === category.id }"
            @click="setActiveCategory(category.id)"
          >
            <i :class="category.icon"></i>
            <span>{{ category.name }}</span>
          </div>
        </div>
        
        <div class="filter-search">
          <el-input
            v-model="searchQuery"
            placeholder="搜索案例..."
            prefix-icon="el-icon-search"
            clearable
          ></el-input>
        </div>
      </div>
      
      <div class="case-list">
        <div v-for="(caseItem, index) in filteredCases" :key="index" class="case-item">
          <div class="case-image">
            <img :src="caseItem.image" :alt="caseItem.title">
            <div class="case-tags">
              <el-tag size="small" effect="dark" :type="getCategoryType(caseItem.category)">{{ getCategoryName(caseItem.category) }}</el-tag>
            </div>
          </div>
          <div class="case-content">
            <h3>{{ caseItem.title }}</h3>
            <div class="case-meta">
              <span><i class="fas fa-user"></i> {{ caseItem.author }}</span>
              <span><i class="fas fa-calendar"></i> {{ caseItem.date }}</span>
              <span><i class="fas fa-eye"></i> {{ caseItem.views }}</span>
            </div>
            <p class="case-summary">{{ caseItem.summary }}</p>
            <div class="case-actions">
              <el-button type="primary" @click="viewCase(caseItem)">阅读全文</el-button>
              <div class="case-likes">
                <span @click="toggleLike(caseItem)" class="like-btn" :class="{ 'liked': caseItem.isLiked }">
                  <i class="fas" :class="caseItem.isLiked ? 'fa-heart' : 'fa-heart-o'"></i>
                  {{ caseItem.likes }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="totalCases"
          :current-page="currentPage"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
      
      <div class="featured-cases">
        <h2>推荐案例</h2>
        <div class="featured-grid">
          <div v-for="(caseItem, index) in featuredCases" :key="index" class="featured-case">
            <div class="featured-image">
              <img :src="caseItem.image" :alt="caseItem.title">
              <div class="featured-overlay">
                <span>{{ getCategoryName(caseItem.category) }}</span>
              </div>
            </div>
            <div class="featured-content">
              <h3>{{ caseItem.title }}</h3>
              <p>{{ caseItem.summary }}</p>
              <el-button type="text" @click="viewCase(caseItem)">阅读全文 <i class="el-icon-arrow-right"></i></el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 案例详情对话框 -->
    <el-dialog
      :title="currentCase.title"
      v-model="dialogVisible"
      width="70%"
      class="case-dialog"
    >
      <div class="case-detail">
        <div class="case-detail-header">
          <div class="case-detail-meta">
            <span><i class="fas fa-user"></i> {{ currentCase.author }}</span>
            <span><i class="fas fa-calendar"></i> {{ currentCase.date }}</span>
            <span><i class="fas fa-tag"></i> {{ getCategoryName(currentCase.category) }}</span>
          </div>
        </div>
        <div class="case-detail-content" v-if="currentCase.content">
          <p v-for="(paragraph, i) in currentCase.content" :key="i">{{ paragraph }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue';
import request from "@/utils/request.js";
import { ElMessage } from 'element-plus';

export default {
  name: 'CaseSharingPage',
  setup() {
    const searchQuery = ref('');
    const activeCategory = ref('all');
    const currentPage = ref(1);
    const totalCases = ref(0);
    const dialogVisible = ref(false);
    const currentCase = ref({});
    
    const categories = [
      { id: 'all', name: '全部案例', icon: 'fas fa-th-large' },
      { id: 'emotion', name: '情绪管理', icon: 'fas fa-heart' },
      { id: 'relationship', name: '人际关系', icon: 'fas fa-users' },
      { id: 'career', name: '职业发展', icon: 'fas fa-briefcase' },
      { id: 'study', name: '学业问题', icon: 'fas fa-book' }
    ];
    
    const cases = reactive([]);
    
    // 加载后台案例数据
    const loadCases = () => {
      request.get('/introduction/selectPage', {
        params: {
          pageNum: currentPage.value,
          pageSize: 10,
          title: searchQuery.value.trim() || null
        }
      }).then(res => {
        if (res.code === '200') {
          // 清空现有数据并更新
          cases.length = 0;
          
          // 将后台数据转换为前端所需格式
          if (res.data?.list && res.data.list.length > 0) {
            res.data.list.forEach(item => {
              cases.push({
                id: item.id,
                title: item.title,
                category: getCategoryFromTitle(item.categoryTitle),
                author: item.userName || '心理咨询师',
                date: item.time,
                views: Math.floor(Math.random() * 2000) + 500,
                likes: Math.floor(Math.random() * 300) + 100,
                isLiked: false,
                summary: item.title,
                image: item.img || '@/assets/img/测评.jpg',
                content: item.content ? [item.content] : []
              });
            });
            
            totalCases.value = res.data.total;
          } else {
            // 如果没有数据，显示一条默认消息
            ElMessage.info('暂无案例数据');
          }
        } else {
          ElMessage.error(res.msg || '获取案例数据失败');
        }
      }).catch(err => {
        console.error('获取案例数据失败:', err);
        ElMessage.error('获取案例数据失败，请稍后再试');
      });
    };
    
    // 根据分类标题获取对应的分类ID
    const getCategoryFromTitle = (categoryTitle) => {
      if (!categoryTitle) return 'emotion';
      
      // 根据分类标题猜测分类
      if (categoryTitle.includes('情绪')) return 'emotion';
      if (categoryTitle.includes('关系')) return 'relationship';
      if (categoryTitle.includes('职业') || categoryTitle.includes('工作')) return 'career';
      if (categoryTitle.includes('学习') || categoryTitle.includes('学业')) return 'study';
      
      // 默认返回情绪管理
      return 'emotion';
    };

    // 初始化时加载数据
    onMounted(() => {
      loadCases();
    });
    
    const filteredCases = computed(() => {
      let result = cases;
      
      // 按类别过滤
      if (activeCategory.value !== 'all') {
        result = result.filter(item => item.category === activeCategory.value);
      }
      
      return result;
    });
    
    const featuredCases = computed(() => {
      // 取最新的两条案例作为推荐
      return cases.slice(0, 2);
    });
    
    const setActiveCategory = (categoryId) => {
      activeCategory.value = categoryId;
    };
    
    const getCategoryName = (categoryId) => {
      const category = categories.find(item => item.id === categoryId);
      return category ? category.name : categoryId;
    };
    
    const getCategoryType = (categoryId) => {
      switch (categoryId) {
        case 'emotion': return 'danger';
        case 'relationship': return 'success';
        case 'career': return 'warning';
        case 'study': return 'info';
        default: return '';
      }
    };
    
    const handlePageChange = (page) => {
      currentPage.value = page;
      loadCases();
    };
    
    const viewCase = (caseItem) => {
      currentCase.value = caseItem;
      dialogVisible.value = true;
    };
    
    const toggleLike = (caseItem) => {
      caseItem.isLiked = !caseItem.isLiked;
      caseItem.likes += caseItem.isLiked ? 1 : -1;
    };
    
    return {
      searchQuery,
      activeCategory,
      categories,
      filteredCases,
      featuredCases,
      currentPage,
      totalCases,
      dialogVisible,
      currentCase,
      setActiveCategory,
      getCategoryName,
      getCategoryType,
      handlePageChange,
      viewCase,
      toggleLike,
      loadCases
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

.case-filter {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
}

.filter-tabs {
  display: flex;
  background-color: white;
  border-radius: 50px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.filter-tab {
  padding: 12px 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.filter-tab:hover {
  background-color: #f0f4ff;
}

.filter-tab.active {
  background-color: #5b7bf0;
  color: white;
}

.filter-tab i {
  font-size: 1rem;
}

.filter-search {
  width: 250px;
}

.case-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
  margin-bottom: 40px;
}

.case-item {
  display: flex;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.case-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.case-image {
  width: 280px;
  height: 210px;
  position: relative;
  overflow: hidden;
}

.case-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.case-item:hover .case-image img {
  transform: scale(1.1);
}

.case-tags {
  position: absolute;
  top: 15px;
  right: 15px;
}

.case-content {
  flex: 1;
  padding: 25px;
  display: flex;
  flex-direction: column;
}

.case-content h3 {
  font-size: 1.4rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.case-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  color: #666;
  font-size: 0.9rem;
}

.case-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.case-summary {
  margin-bottom: 20px;
  line-height: 1.6;
  flex: 1;
}

.case-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.like-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: #666;
  transition: color 0.3s ease;
}

.like-btn:hover, .like-btn.liked {
  color: #ff4757;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-bottom: 60px;
}

.featured-cases {
  margin-bottom: 60px;
}

.featured-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
}

.featured-case {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.featured-case:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.featured-image {
  height: 200px;
  position: relative;
  overflow: hidden;
}

.featured-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.featured-case:hover .featured-image img {
  transform: scale(1.1);
}

.featured-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px;
  background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
  color: white;
}

.featured-content {
  padding: 20px;
}

.featured-content h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.featured-content p {
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

.case-detail-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.case-detail-meta {
  display: flex;
  gap: 20px;
  color: #666;
}

.case-detail-content p {
  margin-bottom: 15px;
  line-height: 1.8;
  text-indent: 2em;
}

@media (max-width: 991px) {
  .case-filter {
    flex-direction: column;
    gap: 20px;
  }
  
  .filter-tabs {
    overflow-x: auto;
    width: 100%;
  }
  
  .filter-search {
    width: 100%;
  }
  
  .case-item {
    flex-direction: column;
  }
  
  .case-image {
    width: 100%;
    height: 200px;
  }
  
  .featured-grid {
    grid-template-columns: 1fr;
  }
}
</style> 