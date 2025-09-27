<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>心理讲座</h1>
        <p>定期举办专业心理讲座，传授实用心理知识</p>
      </div>
    </div>
    
    <div class="container">
      <div class="service-intro">
        <div class="intro-text">
          <h2>服务介绍</h2>
          <p>心理讲座是由专业心理学者和实践者围绕特定心理健康主题进行的系统讲解和知识分享活动。通过参加心理讲座，学生可以系统学习心理健康知识，了解自我成长技巧，提高心理素质和应对压力的能力。</p>
          <p>我们定期邀请心理学领域的专家学者和优秀实践工作者，围绕大学生关注的心理健康话题开展讲座，内容丰富多样，既有理论知识，也有实用技能。</p>
        </div>
        <div class="intro-image">
          <img src="@/assets/img/讲座.jpg" alt="心理讲座">
        </div>
      </div>
      
      <div class="upcoming-lectures">
        <h2>近期讲座</h2>
        <div class="lecture-cards">
          <div class="lecture-card">
            <div class="lecture-date">
              <span class="month">五月</span>
              <span class="day">15</span>
            </div>
            <div class="lecture-info">
              <h3>高效学习与时间管理</h3>
              <p class="lecture-details"><i class="fas fa-map-marker-alt"></i> 心理健康中心报告厅</p>
              <p class="lecture-details"><i class="fas fa-clock"></i> 14:00-16:00</p>
              <p class="lecture-details"><i class="fas fa-user"></i> 张教授（教育心理学专家）</p>
              <p class="lecture-desc">讲座将介绍科学的学习方法和时间管理技巧，帮助学生提高学习效率，减轻学习压力。</p>
              <el-button type="primary" class="register-btn">立即报名</el-button>
            </div>
          </div>
          
          <div class="lecture-card">
            <div class="lecture-date">
              <span class="month">五月</span>
              <span class="day">22</span>
            </div>
            <div class="lecture-info">
              <h3>人际沟通与冲突管理</h3>
              <p class="lecture-details"><i class="fas fa-map-marker-alt"></i> 大学生活动中心</p>
              <p class="lecture-details"><i class="fas fa-clock"></i> 19:00-21:00</p>
              <p class="lecture-details"><i class="fas fa-user"></i> 李老师（心理咨询师）</p>
              <p class="lecture-desc">讲座将分享有效的人际沟通技巧和冲突解决方法，帮助学生建立和谐的人际关系。</p>
              <el-button type="primary" class="register-btn">立即报名</el-button>
            </div>
          </div>
          
          <div class="lecture-card">
            <div class="lecture-date">
              <span class="month">五月</span>
              <span class="day">29</span>
            </div>
            <div class="lecture-info">
              <h3>情绪管理与压力缓解</h3>
              <p class="lecture-details"><i class="fas fa-map-marker-alt"></i> 图书馆报告厅</p>
              <p class="lecture-details"><i class="fas fa-clock"></i> 15:00-17:00</p>
              <p class="lecture-details"><i class="fas fa-user"></i> 王教授（临床心理学专家）</p>
              <p class="lecture-desc">讲座将教授情绪识别和管理的方法，以及缓解压力的实用技巧，提高心理韧性。</p>
              <el-button type="primary" class="register-btn">立即报名</el-button>
            </div>
          </div>
        </div>
      </div>
      
      <div class="past-lectures">
        <h2>往期讲座</h2>
        <div class="past-lecture-list">
          <div class="past-lecture-item">
            <h3>大学生恋爱心理与情感教育</h3>
            <p class="lecture-time">2025年4月18日</p>
            <p class="lecture-desc">探讨大学生恋爱中的常见心理问题，提供健康恋爱关系的建立和维护方法。</p>
            <a href="#" class="view-materials">查看讲座资料 <i class="fas fa-arrow-right"></i></a>
          </div>
          
          <div class="past-lecture-item">
            <h3>考前心理调适与应考技巧</h3>
            <p class="lecture-time">2025年4月5日</p>
            <p class="lecture-desc">分享考前心理调适方法和应考技巧，帮助学生以最佳状态迎接考试。</p>
            <a href="#" class="view-materials">查看讲座资料 <i class="fas fa-arrow-right"></i></a>
          </div>
          
          <div class="past-lecture-item">
            <h3>职业生涯规划与自我认知</h3>
            <p class="lecture-time">2025年3月22日</p>
            <p class="lecture-desc">帮助学生认识自我、明确职业目标、规划职业发展路径。</p>
            <a href="#" class="view-materials">查看讲座资料 <i class="fas fa-arrow-right"></i></a>
          </div>
        </div>
        
        <div class="view-more">
          <el-button plain>查看更多往期讲座</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LecturesPage'
}
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

.upcoming-lectures {
  margin-bottom: 60px;
}

.lecture-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.lecture-card {
  display: flex;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.lecture-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.lecture-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #5b7bf0;
  color: white;
  padding: 20px 30px;
  text-align: center;
}

.month {
  font-size: 1rem;
  text-transform: uppercase;
}

.day {
  font-size: 2rem;
  font-weight: bold;
}

.lecture-info {
  flex: 1;
  padding: 20px;
}

.lecture-info h3 {
  font-size: 1.3rem;
  margin-bottom: 15px;
  color: #2B3856;
}

.lecture-details {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #666;
}

.lecture-details i {
  margin-right: 10px;
  color: #5b7bf0;
  width: 20px;
}

.lecture-desc {
  margin: 15px 0;
  line-height: 1.5;
}

.register-btn {
  margin-top: 10px;
}

.past-lectures {
  margin-bottom: 60px;
}

.past-lecture-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.past-lecture-item {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.past-lecture-item:hover {
  transform: translateX(5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
}

.past-lecture-item h3 {
  font-size: 1.2rem;
  margin-bottom: 8px;
  color: #2B3856;
}

.lecture-time {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 10px;
}

.view-materials {
  display: inline-block;
  margin-top: 10px;
  color: #5b7bf0;
  font-weight: 600;
  transition: color 0.3s ease;
}

.view-materials:hover {
  color: #3a5acf;
}

.view-materials i {
  margin-left: 5px;
  transition: transform 0.3s ease;
}

.view-materials:hover i {
  transform: translateX(5px);
}

.view-more {
  text-align: center;
  margin-top: 30px;
}

@media (max-width: 991px) {
  .service-intro {
    flex-direction: column;
  }
}

@media (max-width: 767px) {
  .lecture-card {
    flex-direction: column;
  }
  
  .lecture-date {
    flex-direction: row;
    justify-content: center;
    gap: 10px;
    padding: 10px;
  }
  
  .month, .day {
    font-size: 1.2rem;
  }
}
</style> 