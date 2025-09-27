<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>团体辅导</h1>
        <p>小组互动式心理成长体验，共同探索心理健康</p>
      </div>
    </div>
    
    <div class="container">
      <div class="service-intro">
        <div class="intro-text">
          <h2>服务介绍</h2>
          <p>团体辅导是指由专业心理辅导老师带领下的小组活动，通过小组成员间的互动、分享和讨论，共同探索心理健康话题，促进自我成长和人际交往能力提升的辅导方式。</p>
          <p>团体辅导相比个人咨询，更加注重人际互动和集体体验，通过小组成员间的相互支持和反馈，让每位成员在安全、开放的环境中，感受到被理解、被接纳，同时学习他人的经验和视角，获得个人成长。</p>
        </div>
        <div class="intro-image">
          <img src="@/assets/img/咨询.jpg" alt="团体辅导">
        </div>
      </div>
      
      <div class="group-categories">
        <h2>团体辅导类型</h2>
        <div class="category-cards">
          <div class="category-card">
            <div class="card-icon">
              <i class="fas fa-heart"></i>
            </div>
            <h3>情绪管理</h3>
            <p>学习识别、接纳和管理情绪的技巧，提高情绪调节能力，应对焦虑、抑郁等负面情绪。</p>
          </div>
          
          <div class="category-card">
            <div class="card-icon">
              <i class="fas fa-users"></i>
            </div>
            <h3>人际关系</h3>
            <p>提升沟通技巧，学习处理冲突的方法，建立健康的人际边界，改善人际关系。</p>
          </div>
          
          <div class="category-card">
            <div class="card-icon">
              <i class="fas fa-lightbulb"></i>
            </div>
            <h3>自我探索</h3>
            <p>深入了解自我，探索个人价值观、特质和潜能，增强自我认同感和自信心。</p>
          </div>
          
          <div class="category-card">
            <div class="card-icon">
              <i class="fas fa-book"></i>
            </div>
            <h3>学习能力</h3>
            <p>分享学习策略，解决学习困难，提高学习动机和效率，减轻学业压力。</p>
          </div>
        </div>
      </div>
      
      <div class="upcoming-groups">
        <h2>近期活动</h2>
        <div class="group-list">
          <div class="group-item">
            <div class="group-date">
              <div class="date-box">
                <div class="month">五月</div>
                <div class="day">18</div>
              </div>
              <div class="time">14:00-16:00</div>
            </div>
            <div class="group-content">
              <h3>大学生压力管理工作坊</h3>
              <div class="group-info">
                <p><i class="fas fa-map-marker-alt"></i> 心理咨询中心团辅室</p>
                <p><i class="fas fa-user"></i> 张老师（心理咨询师）</p>
                <p><i class="fas fa-users"></i> 限8-12人</p>
              </div>
              <p class="group-desc">通过团体活动，学习压力来源识别、压力反应觉察和有效的减压技巧，提高压力耐受力和心理韧性。</p>
              <div class="group-actions">
                <el-button type="primary">我要报名</el-button>
                <span class="spots-left">剩余名额：5</span>
              </div>
            </div>
          </div>
          
          <div class="group-item">
            <div class="group-date">
              <div class="date-box">
                <div class="month">五月</div>
                <div class="day">25</div>
              </div>
              <div class="time">19:00-21:00</div>
            </div>
            <div class="group-content">
              <h3>人际沟通能力提升小组</h3>
              <div class="group-info">
                <p><i class="fas fa-map-marker-alt"></i> 学生活动中心</p>
                <p><i class="fas fa-user"></i> 李老师（心理学博士）</p>
                <p><i class="fas fa-users"></i> 限10-15人</p>
              </div>
              <p class="group-desc">通过角色扮演、情景模拟等互动方式，提升表达能力、倾听技巧和冲突解决能力，改善人际关系。</p>
              <div class="group-actions">
                <el-button type="primary">我要报名</el-button>
                <span class="spots-left">剩余名额：8</span>
              </div>
            </div>
          </div>
          
          <div class="group-item">
            <div class="group-date">
              <div class="date-box">
                <div class="month">六月</div>
                <div class="day">1</div>
              </div>
              <div class="time">15:00-17:00</div>
            </div>
            <div class="group-content">
              <h3>自我探索与成长小组</h3>
              <div class="group-info">
                <p><i class="fas fa-map-marker-alt"></i> 心理咨询中心团辅室</p>
                <p><i class="fas fa-user"></i> 王老师（资深心理咨询师）</p>
                <p><i class="fas fa-users"></i> 限8-10人</p>
              </div>
              <p class="group-desc">通过绘画、写作等表达性艺术手段，探索内心世界，提升自我认知，发掘个人潜能。</p>
              <div class="group-actions">
                <el-button type="primary">我要报名</el-button>
                <span class="spots-left">剩余名额：3</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="testimonials">
        <h2>学员感言</h2>
        <div class="testimonial-cards">
          <div class="testimonial-card">
            <div class="quote-icon">
              <i class="fas fa-quote-left"></i>
            </div>
            <p class="quote-text">参加团体辅导让我明白，原来我并不是一个人在面对困难。小组成员的分享和支持给了我很多力量，也学会了很多实用的情绪管理方法。</p>
            <div class="quote-author">
              <div class="author-avatar">
                <span>L</span>
              </div>
              <div class="author-info">
                <p class="author-name">李同学</p>
                <p class="author-desc">情绪管理小组成员</p>
              </div>
            </div>
          </div>
          
          <div class="testimonial-card">
            <div class="quote-icon">
              <i class="fas fa-quote-left"></i>
            </div>
            <p class="quote-text">人际关系一直是我的困扰，在团辅小组中，通过各种活动练习和反馈，我逐渐找到了自己的沟通问题所在，现在和室友、同学的关系明显改善了。</p>
            <div class="quote-author">
              <div class="author-avatar">
                <span>Z</span>
              </div>
              <div class="author-info">
                <p class="author-name">张同学</p>
                <p class="author-desc">人际关系小组成员</p>
              </div>
            </div>
          </div>
          
          <div class="testimonial-card">
            <div class="quote-icon">
              <i class="fas fa-quote-left"></i>
            </div>
            <p class="quote-text">团体辅导的氛围很温暖，老师很专业，组员们也很真诚。最大的收获是学会了接纳自己，不再对自己苛刻，内心变得更平静了。</p>
            <div class="quote-author">
              <div class="author-avatar">
                <span>W</span>
              </div>
              <div class="author-info">
                <p class="author-name">王同学</p>
                <p class="author-desc">自我探索小组成员</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GroupGuidancePage'
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

.group-categories {
  margin-bottom: 60px;
}

.category-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.category-card {
  background-color: white;
  padding: 30px 20px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: transform 0.3s ease;
}

.category-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.card-icon {
  width: 70px;
  height: 70px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #5b7bf0, #3a5acf);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
}

.category-card h3 {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #2B3856;
}

.category-card p {
  color: #666;
  line-height: 1.5;
}

.upcoming-groups {
  margin-bottom: 60px;
}

.group-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.group-item {
  display: flex;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.group-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.group-date {
  width: 140px;
  padding: 20px;
  background-color: #5b7bf0;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.date-box {
  text-align: center;
  margin-bottom: 10px;
}

.month {
  font-size: 1rem;
  text-transform: uppercase;
  margin-bottom: 5px;
}

.day {
  font-size: 2.2rem;
  font-weight: bold;
  line-height: 1;
}

.time {
  font-size: 0.9rem;
}

.group-content {
  flex: 1;
  padding: 20px;
}

.group-content h3 {
  font-size: 1.3rem;
  margin-bottom: 15px;
  color: #2B3856;
}

.group-info {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 15px;
}

.group-info p {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 0.9rem;
}

.group-info i {
  color: #5b7bf0;
  margin-right: 8px;
  width: 16px;
}

.group-desc {
  margin-bottom: 20px;
  line-height: 1.5;
}

.group-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.spots-left {
  color: #666;
  font-size: 0.9rem;
}

.testimonials {
  margin-bottom: 60px;
}

.testimonial-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.testimonial-card {
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  position: relative;
}

.quote-icon {
  color: #5b7bf0;
  font-size: 24px;
  margin-bottom: 15px;
  opacity: 0.3;
}

.quote-text {
  font-style: italic;
  line-height: 1.6;
  margin-bottom: 20px;
}

.quote-author {
  display: flex;
  align-items: center;
}

.author-avatar {
  width: 40px;
  height: 40px;
  background-color: #5b7bf0;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 15px;
}

.author-name {
  font-weight: 600;
  color: #2B3856;
  margin-bottom: 3px;
}

.author-desc {
  font-size: 0.8rem;
  color: #666;
}

@media (max-width: 991px) {
  .service-intro {
    flex-direction: column;
  }
  
  .category-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .testimonial-cards {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .group-info {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 767px) {
  .category-cards {
    grid-template-columns: 1fr;
  }
  
  .group-item {
    flex-direction: column;
  }
  
  .group-date {
    width: 100%;
    flex-direction: row;
    justify-content: space-between;
    padding: 15px;
  }
  
  .date-box {
    margin-bottom: 0;
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .month, .day {
    font-size: 1.2rem;
  }
}
</style> 