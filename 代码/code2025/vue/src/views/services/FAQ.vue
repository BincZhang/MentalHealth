<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>常见问题</h1>
        <p>解答您关于心理健康与咨询服务的常见疑问</p>
      </div>
    </div>
    
    <div class="container">
      <div class="faq-search">
        <div class="search-box">
          <el-input 
            v-model="searchQuery" 
            placeholder="输入关键词搜索问题..." 
            clearable 
            prefix-icon="el-icon-search"
            @input="handleSearch"
          ></el-input>
        </div>
      </div>
      
      <div class="faq-categories">
        <div 
          v-for="(category, index) in categories" 
          :key="index"
          class="category-item"
          :class="{ active: activeCategory === category.id }"
          @click="setActiveCategory(category.id)"
        >
          <i :class="category.icon"></i>
          <span>{{ category.name }}</span>
        </div>
      </div>
      
      <div class="faq-list">
        <el-collapse v-model="activeNames">
          <el-collapse-item 
            v-for="(item, index) in filteredFaqItems" 
            :key="index"
            :name="index"
          >
            <template #title>
              <span class="faq-question">{{ item.question }}</span>
            </template>
            <div class="faq-answer" v-html="item.answer"></div>
          </el-collapse-item>
        </el-collapse>
        
        <div v-if="filteredFaqItems.length === 0" class="no-results">
          <i class="fas fa-search"></i>
          <p>没有找到与"{{ searchQuery }}"相关的问题</p>
          <el-button type="primary" @click="clearSearch">清除搜索</el-button>
        </div>
      </div>
      
      <div class="contact-section">
        <h2>没有找到您的问题？</h2>
        <p>如果您有其他问题，欢迎随时联系我们。我们的专业团队将竭诚为您服务。</p>
        <div class="contact-buttons">
          <el-button type="primary" icon="el-icon-message">在线咨询</el-button>
          <el-button icon="el-icon-phone">电话咨询: 029-12345678</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, reactive } from 'vue';

export default {
  name: 'FAQPage',
  setup() {
    const searchQuery = ref('');
    const activeCategory = ref('all');
    const activeNames = ref([0, 1]); // 默认展开前两个问题
    
    const categories = reactive([
      { id: 'all', name: '全部问题', icon: 'fas fa-list' },
      { id: 'service', name: '服务咨询', icon: 'fas fa-handshake' },
      { id: 'appointment', name: '预约流程', icon: 'fas fa-calendar-alt' },
      { id: 'privacy', name: '隐私保护', icon: 'fas fa-user-shield' },
      { id: 'psychological', name: '心理问题', icon: 'fas fa-brain' }
    ]);
    
    const faqItems = reactive([
      {
        question: '如何预约心理咨询服务？',
        answer: '您可以通过以下方式预约心理咨询服务：<br>1. 在线预约：访问我们的"心理咨询"页面，填写预约表单<br>2. 电话预约：拨打咨询热线029-12345678<br>3. 现场预约：直接到心理健康中心前台登记',
        category: 'appointment'
      },
      {
        question: '心理咨询的收费标准是什么？',
        answer: '我们为在校学生提供免费的心理咨询服务。每位学生每学期可以享受最多6次免费咨询，超出部分将收取适当费用。具体收费标准请咨询前台。',
        category: 'service'
      },
      {
        question: '我的咨询内容会保密吗？',
        answer: '是的，我们严格遵守心理咨询伦理准则，对所有咨询内容严格保密。除非涉及以下情况：<br>1. 来访者有伤害自己或他人的风险<br>2. 法律要求披露的情况<br>3. 来访者授权披露的情况',
        category: 'privacy'
      },
      {
        question: '一次咨询的时间是多久？',
        answer: '每次咨询时间通常为50分钟，特殊情况可能会有所调整。',
        category: 'service'
      },
      {
        question: '如果错过预约怎么办？',
        answer: '如果您无法按时参加预约，请提前24小时取消或重新安排。频繁未通知取消可能会影响您未来的预约权利。',
        category: 'appointment'
      },
      {
        question: '心理咨询能解决我所有的问题吗？',
        answer: '心理咨询不是万能的，但它可以帮助您更好地理解自己，学习有效的应对策略，发展健康的思维模式和行为习惯。咨询效果取决于多种因素，包括问题性质、个人参与度和咨询师与来访者的匹配程度等。',
        category: 'psychological'
      },
      {
        question: '我需要准备什么才能开始心理咨询？',
        answer: '您只需带上学生证即可。在心态上，保持开放、诚实的态度会让咨询更有效果。您可以提前思考您希望在咨询中解决的问题或达成的目标。',
        category: 'service'
      },
      {
        question: '如何判断我是否需要心理咨询？',
        answer: '如果您经历以下情况，可能需要寻求心理咨询：<br>1. 持续的情绪困扰（如焦虑、抑郁）<br>2. 睡眠或饮食习惯显著变化<br>3. 学业或人际关系困难<br>4. 对曾经喜欢的活动失去兴趣<br>5. 有自伤或自杀的想法',
        category: 'psychological'
      }
    ]);
    
    const filteredFaqItems = computed(() => {
      let result = faqItems;
      
      // 按类别筛选
      if (activeCategory.value !== 'all') {
        result = result.filter(item => item.category === activeCategory.value);
      }
      
      // 按搜索关键词筛选
      if (searchQuery.value.trim()) {
        const query = searchQuery.value.toLowerCase();
        result = result.filter(item => 
          item.question.toLowerCase().includes(query) || 
          item.answer.toLowerCase().includes(query)
        );
      }
      
      return result;
    });
    
    const setActiveCategory = (categoryId) => {
      activeCategory.value = categoryId;
    };
    
    const handleSearch = () => {
      // 如果有搜索内容，重置类别为全部
      if (searchQuery.value.trim()) {
        activeCategory.value = 'all';
      }
    };
    
    const clearSearch = () => {
      searchQuery.value = '';
      activeCategory.value = 'all';
    };
    
    return {
      searchQuery,
      activeCategory,
      activeNames,
      categories,
      filteredFaqItems,
      setActiveCategory,
      handleSearch,
      clearSearch
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

.faq-search {
  margin-bottom: 30px;
}

.search-box {
  max-width: 600px;
  margin: 0 auto;
}

.faq-categories {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 15px;
  margin-bottom: 40px;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background-color: #f5f7fa;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-item i {
  margin-right: 8px;
  color: #5b7bf0;
}

.category-item.active {
  background-color: #5b7bf0;
  color: white;
}

.category-item.active i {
  color: white;
}

.category-item:hover:not(.active) {
  background-color: #e8ecf5;
}

.faq-list {
  margin-bottom: 60px;
}

.faq-question {
  font-weight: 600;
  color: #2B3856;
}

.faq-answer {
  line-height: 1.6;
  padding: 15px;
  background-color: #f9fafc;
  border-radius: 5px;
}

.no-results {
  text-align: center;
  padding: 40px 0;
}

.no-results i {
  font-size: 3rem;
  color: #ccc;
  margin-bottom: 15px;
}

.no-results p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 20px;
}

.contact-section {
  background-color: #f5f7fa;
  padding: 40px;
  border-radius: 10px;
  text-align: center;
  margin-bottom: 40px;
}

.contact-section p {
  max-width: 600px;
  margin: 0 auto 30px;
}

.contact-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

@media (max-width: 767px) {
  .faq-categories {
    flex-direction: column;
    align-items: center;
  }
  
  .category-item {
    width: 100%;
    max-width: 300px;
    justify-content: center;
  }
  
  .contact-buttons {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
}
</style> 