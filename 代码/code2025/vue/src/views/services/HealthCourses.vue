<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>健康课程</h1>
        <p>系统学习心理健康知识，培养健康生活方式</p>
      </div>
    </div>
    
    <div class="container">
      <div class="service-intro">
        <div class="intro-text">
          <h2>课程介绍</h2>
          <p>健康课程是由专业心理学者和健康教育工作者设计的系列课程，旨在帮助学生系统学习心理健康知识，培养健康的生活习惯和应对压力的能力。</p>
          <p>我们的课程内容涵盖心理健康、情绪管理、人际关系、学习技巧、生涯规划等多个方面，通过理论讲解、案例分析、互动练习等多种方式，帮助学生掌握实用的心理健康技能。</p>
        </div>
        <div class="intro-image">
          <img src="@/assets/img/测评.jpg" alt="健康课程">
        </div>
      </div>
      
      <div class="course-categories">
        <h2>课程分类</h2>
        <div class="tab-container">
          <div class="tab-header">
            <div 
              v-for="(category, index) in categories" 
              :key="index"
              class="tab-item"
              :class="{ active: activeCategory === index }"
              @click="activeCategory = index"
            >
              <i :class="category.icon"></i>
              <span>{{ category.name }}</span>
            </div>
          </div>
          
          <div class="tab-content">
            <div 
              v-for="(category, index) in categories" 
              :key="index"
              class="category-content"
              v-show="activeCategory === index"
            >
              <div class="category-desc">
                <h3>{{ category.name }}</h3>
                <p>{{ category.description }}</p>
              </div>
              
              <div class="course-grid">
                <div 
                  v-for="(course, courseIndex) in category.courses" 
                  :key="courseIndex"
                  class="course-card"
                >
                  <div class="course-image">
                    <img :src="course.image" :alt="course.title">
                    <span class="course-badge" :class="getCourseStatusClass(course.status)">{{ course.status }}</span>
                  </div>
                  <div class="course-content">
                    <h4>{{ course.title }}</h4>
                    <div class="course-meta">
                      <span><i class="fas fa-user"></i> {{ course.instructor }}</span>
                      <span><i class="fas fa-clock"></i> {{ course.duration }}</span>
                    </div>
                    <p class="course-desc">{{ course.description }}</p>
                    <el-button 
                      :type="course.status === '正在招生' ? 'primary' : 'info'"
                      :disabled="course.status === '已结束'"
                      class="course-action"
                    >
                      {{ getCourseButtonText(course.status) }}
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="course-features">
        <h2>课程特色</h2>
        <div class="features-grid">
          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-chalkboard-teacher"></i>
            </div>
            <div class="feature-content">
              <h3>专业师资</h3>
              <p>课程由具有丰富教学和实践经验的心理学教授、心理咨询师和健康教育专家授课。</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-tasks"></i>
            </div>
            <div class="feature-content">
              <h3>实用内容</h3>
              <p>课程内容紧密结合大学生常见心理问题和需求，注重实用性和可操作性。</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-hands-helping"></i>
            </div>
            <div class="feature-content">
              <h3>互动教学</h3>
              <p>采用讲授、讨论、案例分析、角色扮演等多种教学方法，增强学习参与度和效果。</p>
            </div>
          </div>
          
          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-certificate"></i>
            </div>
            <div class="feature-content">
              <h3>学分认证</h3>
              <p>部分课程可获得学校认证的选修学分，完成考核后颁发结业证书。</p>
            </div>
          </div>
        </div>
      </div>
      
      <div class="enrollment-guide">
        <h2>报名指南</h2>
        <div class="guide-steps">
          <div class="guide-step">
            <div class="step-number">01</div>
            <div class="step-content">
              <h3>选择课程</h3>
              <p>浏览课程信息，选择感兴趣的课程。</p>
            </div>
          </div>
          
          <div class="guide-step">
            <div class="step-number">02</div>
            <div class="step-content">
              <h3>在线报名</h3>
              <p>点击"立即报名"，填写个人信息。</p>
            </div>
          </div>
          
          <div class="guide-step">
            <div class="step-number">03</div>
            <div class="step-content">
              <h3>确认缴费</h3>
              <p>根据提示完成课程费用支付。</p>
            </div>
          </div>
          
          <div class="guide-step">
            <div class="step-number">04</div>
            <div class="step-content">
              <h3>参加课程</h3>
              <p>按时参加课程学习和相关活动。</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'HealthCoursesPage',
  setup() {
    const activeCategory = ref(0);
    
    const categories = [
      {
        name: '心理健康',
        icon: 'fas fa-brain',
        description: '这些课程帮助学生了解心理健康基础知识，学习识别常见心理问题的方法，掌握维护心理健康的技能。',
        courses: [
          {
            title: '心理健康ABC',
            instructor: '张教授',
            duration: '8课时',
            status: '正在招生',
            description: '介绍心理健康的基本概念、常见心理问题的识别与预防，以及心理健康的自我维护方法。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '心理压力管理',
            instructor: '李老师',
            duration: '6课时',
            status: '即将开始',
            description: '学习识别压力来源、压力反应的表现，以及有效的减压方法和技巧。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '积极心理学入门',
            instructor: '王博士',
            duration: '10课时',
            status: '已结束',
            description: '介绍积极心理学的理念和方法，学习如何培养积极情绪、发掘个人优势和建立幸福感。',
            image: '@/assets/img/测评.jpg'
          }
        ]
      },
      {
        name: '情绪管理',
        icon: 'fas fa-heart',
        description: '这些课程关注情绪识别、接纳和管理，帮助学生学习应对各种情绪状态的方法，提升情绪调节能力。',
        courses: [
          {
            title: '情绪ABC',
            instructor: '刘老师',
            duration: '6课时',
            status: '正在招生',
            description: '学习识别基本情绪、理解情绪的功能，以及有效表达情绪的方法。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '走出抑郁与焦虑',
            instructor: '陈博士',
            duration: '8课时',
            status: '即将开始',
            description: '了解抑郁和焦虑的表现、成因，学习应对抑郁和焦虑的认知行为技术。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '情绪调节高级技术',
            instructor: '赵教授',
            duration: '10课时',
            status: '已结束',
            description: '学习高级情绪调节技术，包括正念练习、接纳承诺疗法和辩证行为疗法的技巧。',
            image: '@/assets/img/测评.jpg'
          }
        ]
      },
      {
        name: '人际关系',
        icon: 'fas fa-users',
        description: '这些课程帮助学生提升人际交往能力，学习有效沟通、冲突解决等技巧，建立健康的人际关系。',
        courses: [
          {
            title: '有效沟通的艺术',
            instructor: '杨老师',
            duration: '8课时',
            status: '正在招生',
            description: '学习语言和非语言沟通技巧，提高表达能力和倾听能力，增强沟通的效果。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '人际冲突与协商',
            instructor: '郑博士',
            duration: '6课时',
            status: '即将开始',
            description: '了解冲突的类型和成因，学习有效的冲突解决策略和协商技巧。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '亲密关系的建立与维护',
            instructor: '孙教授',
            duration: '10课时',
            status: '已结束',
            description: '探讨亲密关系中的心理需求、沟通方式和常见问题，学习建立和维护健康亲密关系的方法。',
            image: '@/assets/img/测评.jpg'
          }
        ]
      },
      {
        name: '学习技能',
        icon: 'fas fa-book',
        description: '这些课程关注学习方法和技巧，帮助学生提高学习效率，减轻学习压力，培养良好的学习习惯。',
        courses: [
          {
            title: '高效学习方法',
            instructor: '周老师',
            duration: '6课时',
            status: '正在招生',
            description: '介绍科学的学习方法和记忆技巧，学习如何制定学习计划和提高学习效率。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '时间管理与自律',
            instructor: '吴博士',
            duration: '8课时',
            status: '即将开始',
            description: '学习时间管理原则和方法，培养自律习惯，提高时间利用效率。',
            image: '@/assets/img/测评.jpg'
          },
          {
            title: '应对考试焦虑',
            instructor: '钱教授',
            duration: '6课时',
            status: '已结束',
            description: '了解考试焦虑的表现和影响，学习减轻考试焦虑的方法和应对策略。',
            image: '@/assets/img/测评.jpg'
          }
        ]
      }
    ];
    
    const getCourseStatusClass = (status) => {
      if (status === '正在招生') return 'badge-success';
      if (status === '即将开始') return 'badge-warning';
      if (status === '已结束') return 'badge-secondary';
      return '';
    };
    
    const getCourseButtonText = (status) => {
      if (status === '正在招生') return '立即报名';
      if (status === '即将开始') return '预约提醒';
      if (status === '已结束') return '查看回顾';
      return '了解详情';
    };
    
    return {
      activeCategory,
      categories,
      getCourseStatusClass,
      getCourseButtonText
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

.course-categories {
  margin-bottom: 60px;
}

.tab-container {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.tab-header {
  display: flex;
  background-color: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

.tab-item {
  padding: 15px 20px;
  font-weight: 600;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
}

.tab-item i {
  font-size: 1.2rem;
  color: #5b7bf0;
}

.tab-item.active {
  background-color: white;
  color: #5b7bf0;
  box-shadow: 0 2px 0 #5b7bf0;
}

.tab-content {
  padding: 30px;
}

.category-desc {
  margin-bottom: 30px;
}

.category-desc h3 {
  font-size: 1.5rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.category-desc p {
  color: #666;
  line-height: 1.6;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.course-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.course-image {
  height: 160px;
  position: relative;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.course-card:hover .course-image img {
  transform: scale(1.1);
}

.course-badge {
  position: absolute;
  top: 15px;
  right: 15px;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  color: white;
}

.badge-success {
  background-color: #28a745;
}

.badge-warning {
  background-color: #ffc107;
  color: #333;
}

.badge-secondary {
  background-color: #6c757d;
}

.course-content {
  padding: 20px;
}

.course-content h4 {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.course-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 10px;
  color: #666;
  font-size: 0.9rem;
}

.course-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.course-desc {
  margin-bottom: 15px;
  color: #666;
  line-height: 1.5;
}

.course-action {
  width: 100%;
}

.course-features {
  margin-bottom: 60px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
}

.feature-item {
  display: flex;
  background-color: white;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.feature-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #5b7bf0, #3a5acf);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  color: white;
  font-size: 30px;
  flex-shrink: 0;
}

.feature-content h3 {
  font-size: 1.3rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.feature-content p {
  color: #666;
  line-height: 1.5;
}

.enrollment-guide {
  margin-bottom: 60px;
}

.guide-steps {
  display: flex;
  justify-content: space-between;
  gap: 30px;
}

.guide-step {
  flex: 1;
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: transform 0.3s ease;
}

.guide-step:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.step-number {
  font-size: 2.5rem;
  font-weight: bold;
  color: #5b7bf0;
  margin-bottom: 15px;
}

.guide-step h3 {
  font-size: 1.3rem;
  margin-bottom: 10px;
  color: #2B3856;
}

.guide-step p {
  color: #666;
  line-height: 1.5;
}

@media (max-width: 991px) {
  .service-intro {
    flex-direction: column;
  }
  
  .course-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .guide-steps {
    flex-wrap: wrap;
    gap: 20px;
  }
  
  .guide-step {
    flex: 0 0 calc(50% - 10px);
  }
}

@media (max-width: 767px) {
  .tab-header {
    flex-wrap: wrap;
  }
  
  .tab-item {
    flex: 1 0 calc(50% - 1px);
  }
  
  .course-grid {
    grid-template-columns: 1fr;
  }
  
  .guide-step {
    flex: 0 0 100%;
  }
}
</style> 