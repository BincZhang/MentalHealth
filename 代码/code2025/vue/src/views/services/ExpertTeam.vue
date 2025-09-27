<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>专家团队</h1>
        <p>专业的心理健康服务团队，为您提供全方位支持</p>
      </div>
    </div>
    
    <div class="container">
      <div class="team-intro">
        <div class="intro-text">
          <h2>团队介绍</h2>
          <p>我们的专家团队由经验丰富的心理学教授、临床心理学家、心理咨询师和精神科医生组成，拥有扎实的专业背景和丰富的实践经验。</p>
          <p>团队成员在抑郁焦虑、人际关系、学业压力、情绪管理、生涯规划等多个方面具有专长，能够为学生提供专业、全面的心理健康服务。</p>
        </div>
      </div>
      
      <div class="expert-filter">
        <div class="filter-tabs">
          <div 
            v-for="(category, index) in categories" 
            :key="index"
            class="filter-tab"
            :class="{ active: activeCategory === category.id }"
            @click="setActiveCategory(category.id)"
          >
            {{ category.name }}
          </div>
        </div>
      </div>
      
      <div class="expert-grid">
        <div 
          v-for="(expert, index) in filteredExperts" 
          :key="index"
          class="expert-card"
        >
          <div class="expert-image">
            <img :src="expert.avatar" :alt="expert.name">
          </div>
          <div class="expert-info">
            <h3>{{ expert.name }}</h3>
            <div class="expert-title">{{ expert.title }}</div>
            <div class="expert-tags">
              <el-tag 
                v-for="(tag, tagIndex) in expert.specialties" 
                :key="tagIndex"
                size="small"
                type="info"
                effect="plain"
              >
                {{ tag }}
              </el-tag>
            </div>
            <p class="expert-brief">{{ expert.brief }}</p>
            <div class="expert-actions">
              <el-button type="primary" size="small" @click="viewExpert(expert)">查看详情</el-button>
              <el-button type="success" size="small" @click="makeAppointment(expert)">预约咨询</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 专家详情对话框 -->
    <el-dialog
      :title="currentExpert.name + ' - ' + currentExpert.title"
      v-model="dialogVisible"
      width="60%"
    >
      <div class="expert-detail">
        <div class="expert-profile">
          <div class="profile-image">
            <img :src="currentExpert.avatar" :alt="currentExpert.name">
          </div>
          <div class="profile-info">
            <div class="info-item">
              <span class="info-label">专业领域</span>
              <div class="info-content">
                <el-tag 
                  v-for="(tag, tagIndex) in currentExpert.specialties" 
                  :key="tagIndex"
                  size="small"
                  type="info"
                  effect="plain"
                  class="specialty-tag"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
            <div class="info-item">
              <span class="info-label">教育背景</span>
              <div class="info-content">{{ currentExpert.education }}</div>
            </div>
            <div class="info-item">
              <span class="info-label">咨询风格</span>
              <div class="info-content">{{ currentExpert.style }}</div>
            </div>
          </div>
        </div>
        
        <div class="expert-bio">
          <h4>个人简介</h4>
          <p v-if="currentExpert.bio">{{ currentExpert.bio }}</p>
        </div>
        
        <div class="expert-schedule" v-if="currentExpert.schedule">
          <h4>咨询时间</h4>
          <div class="schedule-list">
            <div 
              v-for="(schedule, scheduleIndex) in currentExpert.schedule" 
              :key="scheduleIndex"
              class="schedule-item"
            >
              <div class="schedule-day">{{ schedule.day }}</div>
              <div class="schedule-time">{{ schedule.time }}</div>
              <div class="schedule-status" :class="schedule.available ? 'available' : 'unavailable'">
                {{ schedule.available ? '可预约' : '已约满' }}
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="makeAppointment(currentExpert)">立即预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'ExpertTeamPage',
  setup() {
    const activeCategory = ref('all');
    const dialogVisible = ref(false);
    const currentExpert = ref({});
    
    const categories = [
      { id: 'all', name: '全部专家' },
      { id: 'counselor', name: '心理咨询师' },
      { id: 'psychologist', name: '心理学家' },
      { id: 'psychiatrist', name: '精神科医生' },
      { id: 'career', name: '职业规划师' }
    ];
    
    const experts = [
      {
        id: 1,
        name: '张教授',
        title: '资深心理咨询师',
        category: 'counselor',
        avatar: '@/assets/img/咨询.jpg',
        specialties: ['抑郁焦虑', '青少年心理', '人际关系'],
        brief: '从事心理咨询工作15年，擅长抑郁焦虑、人际关系和青少年心理问题的咨询，温暖共情的咨询风格得到来访者的广泛好评。',
        education: '北京大学心理学博士',
        style: '以人本主义为基础，结合认知行为疗法',
        bio: '张教授拥有15年的心理咨询实践经验，曾在多家知名高校心理咨询中心任职。她擅长运用人本主义和认知行为疗法相结合的方法，帮助来访者解决抑郁焦虑、人际关系和青少年心理问题。她的咨询风格温暖共情，能够与来访者建立良好的咨询关系，帮助他们探索自我、发现内在资源，实现个人成长。她曾发表多篇心理咨询相关论文，并著有《走出抑郁的阴霾》等著作。',
        schedule: [
          { day: '周一', time: '14:00-17:00', available: true },
          { day: '周三', time: '09:00-12:00', available: false },
          { day: '周五', time: '14:00-17:00', available: true }
        ]
      },
      {
        id: 2,
        name: '李博士',
        title: '临床心理学家',
        category: 'psychologist',
        avatar: '@/assets/img/咨询.jpg',
        specialties: ['创伤治疗', '情绪管理', '家庭关系'],
        brief: '临床心理学博士，专注于创伤治疗和情绪管理领域，擅长运用认知行为疗法和EMDR帮助来访者缓解心理困扰。',
        education: '清华大学临床心理学博士',
        style: '认知行为疗法、EMDR、家庭系统治疗',
        bio: '李博士是一位临床心理学博士，在创伤治疗和情绪管理领域有深入研究。他擅长运用认知行为疗法和EMDR(眼动脱敏与再处理)技术帮助来访者处理心理创伤，缓解心理困扰。在家庭关系问题方面，他采用家庭系统治疗的方法，帮助家庭成员改善沟通模式，提升家庭功能。李博士曾在国际心理学期刊发表多篇学术论文，并参与多项心理健康相关研究项目。',
        schedule: [
          { day: '周二', time: '09:00-12:00', available: true },
          { day: '周四', time: '14:00-17:00', available: true },
          { day: '周六', time: '09:00-12:00', available: false }
        ]
      },
      {
        id: 3,
        name: '王医生',
        title: '精神科医生',
        category: 'psychiatrist',
        avatar: '@/assets/img/咨询.jpg',
        specialties: ['抑郁症', '焦虑障碍', '睡眠障碍'],
        brief: '精神科主治医师，在抑郁症、焦虑障碍和睡眠障碍的诊断与治疗方面有丰富经验，注重药物治疗与心理治疗的结合。',
        education: '复旦大学医学院精神医学专业',
        style: '生物-心理-社会医学模式，药物治疗与心理治疗结合',
        bio: '',
        schedule: []
      },
      {
        id: 4,
        name: '刘老师',
        title: '职业规划师',
        category: 'career',
        avatar: '@/assets/img/咨询.jpg',
        specialties: ['职业生涯规划', '求职辅导', '职业适应性'],
        brief: '国家二级职业规划师，专注于大学生职业生涯规划和求职辅导，帮助学生明确职业目标，提升就业竞争力。',
        education: '上海交通大学心理学硕士',
        style: '以生涯发展理论为基础，注重个人兴趣、能力和价值观的匹配',
        bio: '',
        schedule: []
      }
    ];
    
    const filteredExperts = computed(() => {
      if (activeCategory.value === 'all') {
        return experts;
      } else {
        return experts.filter(expert => expert.category === activeCategory.value);
      }
    });
    
    const setActiveCategory = (categoryId) => {
      activeCategory.value = categoryId;
    };
    
    const viewExpert = (expert) => {
      currentExpert.value = expert;
      dialogVisible.value = true;
    };
    
    const makeAppointment = (expert) => {
      ElMessage.success(`已发起与${expert.name}的预约申请，请等待确认。`);
      // 这里可以添加预约逻辑，如跳转到预约页面或显示预约表单
    };
    
    return {
      activeCategory,
      categories,
      filteredExperts,
      dialogVisible,
      currentExpert,
      setActiveCategory,
      viewExpert,
      makeAppointment
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

.team-intro {
  margin-bottom: 40px;
}

.intro-text p {
  margin-bottom: 15px;
  line-height: 1.6;
}

.expert-filter {
  margin-bottom: 30px;
}

.filter-tabs {
  display: flex;
  justify-content: center;
  background-color: white;
  border-radius: 50px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  width: fit-content;
  margin: 0 auto;
}

.filter-tab {
  padding: 12px 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.filter-tab:hover {
  background-color: #f0f4ff;
}

.filter-tab.active {
  background-color: #5b7bf0;
  color: white;
}

.expert-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  margin-bottom: 60px;
}

.expert-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
  display: flex;
}

.expert-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.expert-image {
  width: 160px;
  overflow: hidden;
}

.expert-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.expert-info {
  flex: 1;
  padding: 20px;
}

.expert-info h3 {
  font-size: 1.3rem;
  margin-bottom: 5px;
  color: #2B3856;
}

.expert-title {
  color: #5b7bf0;
  font-weight: 500;
  margin-bottom: 10px;
}

.expert-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-bottom: 10px;
}

.expert-brief {
  margin-bottom: 15px;
  line-height: 1.5;
  color: #666;
}

.expert-actions {
  display: flex;
  gap: 10px;
}

.expert-detail {
  padding: 20px 0;
}

.expert-profile {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
}

.profile-image {
  width: 160px;
  height: 200px;
  overflow: hidden;
  border-radius: 5px;
}

.profile-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info {
  flex: 1;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  display: block;
  color: #999;
  margin-bottom: 5px;
}

.info-content {
  color: #333;
  line-height: 1.5;
}

.specialty-tag {
  margin-right: 5px;
  margin-bottom: 5px;
}

.expert-bio {
  margin-bottom: 30px;
}

.expert-bio h4, .expert-schedule h4 {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #2B3856;
  position: relative;
  padding-bottom: 8px;
}

.expert-bio h4::after, .expert-schedule h4::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 2px;
  background-color: #5b7bf0;
}

.expert-bio p {
  line-height: 1.6;
  color: #666;
}

.schedule-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.schedule-item {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 5px;
  text-align: center;
}

.schedule-day {
  font-weight: 600;
  margin-bottom: 5px;
}

.schedule-time {
  color: #666;
  margin-bottom: 5px;
}

.schedule-status {
  font-size: 0.9rem;
  font-weight: 500;
}

.available {
  color: #28a745;
}

.unavailable {
  color: #dc3545;
}

@media (max-width: 991px) {
  .expert-grid {
    grid-template-columns: 1fr;
  }
  
  .schedule-list {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 767px) {
  .filter-tabs {
    flex-wrap: wrap;
  }
  
  .filter-tab {
    flex: 0 0 50%;
    text-align: center;
  }
  
  .expert-card {
    flex-direction: column;
  }
  
  .expert-image {
    width: 100%;
    height: 200px;
  }
  
  .expert-profile {
    flex-direction: column;
  }
  
  .profile-image {
    width: 100%;
    height: auto;
    max-height: 300px;
    margin-bottom: 20px;
  }
}
</style> 