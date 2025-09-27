<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <CommonHeader />
  
  <div class="user-home-wrapper page-container">
    <!-- banner区域 -->
    <div class="banner">
      <div class="wrapper">
        <div class="left">
          <h3 class="nav-title">功能导航</h3>
          <ul class="nav-list">
            <li><router-link to="/consultation" class="nav-link"><i class="fas fa-comments"></i> 心理咨询</router-link></li>
            <li><router-link to="/lectures" class="nav-link"><i class="fas fa-chalkboard-teacher"></i> 心理讲座</router-link></li>
            <li><router-link to="/group-guidance" class="nav-link"><i class="fas fa-users"></i> 团体辅导</router-link></li>
            <li><router-link to="/health-courses" class="nav-link"><i class="fas fa-book-medical"></i> 健康课程</router-link></li>
            <li><router-link to="/self-learning" class="nav-link"><i class="fas fa-laptop"></i> 自助学习</router-link></li>
            <li><router-link to="/case-sharing" class="nav-link"><i class="fas fa-file-alt"></i> 案例分享</router-link></li>
            <li><router-link to="/expert-team" class="nav-link"><i class="fas fa-user-md"></i> 专家团队</router-link></li>
            <li><router-link to="/faq" class="nav-link"><i class="fas fa-question-circle"></i> 常见问题</router-link></li>
          </ul>
        </div>
        <!-- 轮播图 -->
        <div class="slider">
          <div class="slider-indicators">
            <span 
              v-for="(slide, index) in data.slides" 
              :key="index"
              :class="{ active: data.currentSlide === index }"
              @click="setCurrentSlide(index)"
            ></span>
          </div>
          <img
            v-for="(slide, index) in data.slides"
            :key="index"
            :src="slide.src"
            :alt="slide.alt"
            :class="{ active: data.currentSlide === index }"
          >
        </div>
        <div class="right">
          <h3 class="feature-title">快捷功能</h3>
          <div class="feature-content">
            <div class="feature-card" @click="router.push('/ai-chat')">
              <div class="feature-icon">
                <i class="fas fa-robot"></i>
              </div>
              <div class="feature-info">
                <h4 class="feature-name">小愿聊天</h4>
                <p class="feature-desc">AI智能对话</p>
              </div>
            </div>
            <div class="feature-card" @click="router.push('/psychological-test')">
              <div class="feature-icon">
                <i class="fas fa-clipboard-check"></i>
              </div>
              <div class="feature-info">
                <h4 class="feature-name">心理测评</h4>
                <p class="feature-desc">心理健康评估</p>
              </div>
            </div>
            <div class="feature-card" @click="router.push('/knowledge-base')">
              <div class="feature-icon">
                <i class="fas fa-brain"></i>
              </div>
              <div class="feature-info">
                <h4 class="feature-name">心理知识库</h4>
                <p class="feature-desc">心理健康知识</p>
              </div>
            </div>
            <div class="feature-card" @click="router.push('/notice-list')">
              <div class="feature-icon">
                <i class="fas fa-bullhorn"></i>
              </div>
              <div class="feature-info">
                <h4 class="feature-name">公告列表</h4>
                <p class="feature-desc">系统公告信息</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="content-section">
      <div class="section-header">
        <h2>我们的服务</h2>
        <p>为大学生提供全方位的心理咨询服务，帮助你解决心理困扰，拥抱美好大学生活。</p>
      </div>
      <div class="service-list-container">
        <div class="service-list">
          <div class="service-card" v-for="(service, index) in data.services" :key="index">
            <div class="service-image">
              <img :src="service.src" :alt="service.title">
            </div>
            <div class="service-content">
              <h3>{{ service.title }}</h3>
              <p>{{ service.description }}</p>
              <el-button type="primary" @click="goToService(service.path, service.externalUrl)">了解更多</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 添加公告提醒组件 -->
  <NoticeAlert />
</template>

<script>
import CommonHeader from '@/components/CommonHeader.vue'
import NoticeAlert from '@/components/NoticeAlert.vue'
import loginImage from '@/assets/img/轮1.jpg';
import loginImage2 from '@/assets/img/轮2.jpg';
import loginImage3 from '@/assets/img/轮3.jpg';
import { reactive, onMounted, onBeforeUnmount, computed } from "vue";
import { useRouter } from 'vue-router';
import { ElNotification } from 'element-plus'

export default {
  name: "UserHome",
  components: {
    CommonHeader,
    NoticeAlert
  },
  setup() {
    const router = useRouter();
    
    const data = reactive({
      user: JSON.parse(localStorage.getItem('code_user') || "{}"),
      currentSlide: 0,
      showDropdown: false,
      slides: [
        {
          src: loginImage,
          alt: '登录页面截图'
        },
        {
          src: loginImage2,
          alt: '产品功能展示'
        },
        {
          src: loginImage3,
          alt: '用户案例'
        }
      ],
      services: [
        {
          src: '/backgrounds/login.jpg',
          title: '心理咨询',
          description: '专业心理咨询师一对一服务，为你排解心理压力。',
          path: '/services/expert-team',
          externalUrl: 'http://www.cn-psy.com/'
        },
        {
          src: '/backgrounds/chat.jpg',
          title: '心理讲座',
          description: '定期举办心理讲座，传授实用心理知识。',
          path: '/services/health-courses',
          externalUrl: 'https://xgb.uestc.edu.cn/article/detail/ebd80163168643008ba3b9d6fa93f71b'
        },
        {
          src: '/backgrounds/pexels-fotios-photos-3972441.jpg',
          title: '心理测评',
          description: '科学的心理测评工具，了解自己的心理状态。',
          path: '/services/psychological-test',
          externalUrl: 'https://eap.psych.cn/a/'
        }
      ]
    });

    let sliderInterval;

    const logout = () => {
      localStorage.removeItem('code_user')
      location.href = '/login'
    }

    const updateUser = () => {
      data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
    }

    const initSlider = () => {
      // 清除任何可能存在的旧定时器
      if (sliderInterval) {
        clearInterval(sliderInterval);
      }
      // 创建新的定时器
      sliderInterval = setInterval(() => {
        data.currentSlide = (data.currentSlide + 1) % data.slides.length;
      }, 5000);
    }
    
    const setCurrentSlide = (index) => {
      data.currentSlide = index;
      // 重置轮播图计时器
      if (sliderInterval) {
        clearInterval(sliderInterval);
      }
      initSlider();
    }

    const getStatusClass = (status) => {
      if (status === '正在学习') return 'learning';
      if (status === '即将开始') return 'upcoming';
      if (status === '待学习') return 'unstarted';
      return '';
    }

    const goToService = (path, externalUrl) => {
      if (externalUrl) {
        // 使用内部页面展示外部内容，保留导航栏
        router.push({
          path: '/external-content',
          query: {
            url: encodeURIComponent(externalUrl),
            title: '心理服务 - 外部资源'
          }
        });
      } else {
        // 使用内部路由
        router.push(path);
      }
    }

    onMounted(() => {
      // 在组件挂载时初始化轮播图
      initSlider();
      // 确保用户数据是最新的
      updateUser();
    });

    onBeforeUnmount(() => {
      // 组件卸载前清除定时器，防止内存泄漏
      if (sliderInterval) {
        clearInterval(sliderInterval);
      }
    });

    return {
      data,
      logout,
      updateUser,
      router,
      getStatusClass,
      setCurrentSlide,
      currentSlide: () => data.currentSlide,
      goToService
    }
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'PingFang SC', sans-serif;
}

.user-home-wrapper {
  position: relative;
  z-index: 1;
  /* 添加背景图 */
  background-image: url('/backgrounds/home.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
}

/* 添加一个半透明的遮罩，使内容更易于阅读 */
.user-home-wrapper::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.85);
  z-index: -1;
}

:root {
  --primary: #5b7bf0;
  --primary-dark: #3a5acf;
  --secondary: #F7FFF7;
  --accent: #ffbc2c;
  --text-dark: #2B3856;
  --text-light: #577590;
  --background-light: #f9f9f9;
  --shadow-color: rgba(0, 0, 0, 0.1);
  --card-bg: rgba(255, 255, 255, 0.9);
  --hover-bg: #f0f5ff;
  --gradient-start: #5b7bf0;
  --gradient-end: #3a5acf;
}

a {
  color: var(--text-dark);
  text-decoration: none;
}

ul, ol {
  list-style: none;
}

/* Banner区域 */
.banner {
  height: 480px;
  margin: 30px auto;
  background-color: var(--card-bg);
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  width: 1200px;
  overflow: hidden;
}

.banner .wrapper {
  display: flex;
  align-items: stretch;
  height: 100%;
}

/* 左侧导航 */
.banner .left {
  width: 220px;
  border-radius: 0;
  background: linear-gradient(to bottom, var(--gradient-start), var(--gradient-end));
  padding: 0;
  overflow: hidden;
}

.nav-title {
  color: white;
  font-size: 20px;
  padding: 20px;
  font-weight: 600;
  text-align: center;
  background-color: rgba(0, 0, 0, 0.15);
  margin: 0;
}

.nav-list {
  padding: 10px 0;
}

.nav-link {
  display: flex;
  align-items: center;
  color: white;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link i {
  margin-right: 10px;
  font-size: 18px;
  width: 24px;
  text-align: center;
}

.nav-link:hover, .nav-link.active {
  background-color: rgba(255, 255, 255, 0.15);
  padding-left: 25px;
}

.nav-link:hover::after, .nav-link.active::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-color: var(--accent);
}

/* 轮播图区域 */
.slider {
  flex: 1;
  margin: 0;
  border-radius: 0;
  overflow: hidden;
  position: relative;
  height: 480px;
}

.slider img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  opacity: 0;
  transition: opacity 0.8s ease;
  z-index: 1;
}

.slider img.active {
  opacity: 1;
  z-index: 2;
}

.slider-indicators {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
  z-index: 3;
}

.slider-indicators span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;
}

.slider-indicators span.active {
  width: 30px;
  border-radius: 5px;
  background-color: white;
}

/* 右侧功能区 */
.banner .right {
  width: 280px;
  background: white;
  border-radius: 0;
  overflow: hidden;
  box-shadow: -5px 0 15px rgba(0, 0, 0, 0.05);
}

.feature-title {
  color: var(--text-dark);
  font-size: 20px;
  font-weight: 600;
  padding: 20px;
  text-align: center;
  background-color: #f8f9fa;
  margin: 0;
  border-bottom: 1px solid #eee;
}

.feature-content {
  padding: 15px;
}

.feature-card {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 12px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.12);
  background-color: var(--hover-bg);
}

.feature-icon {
  width: 45px;
  height: 45px;
  border-radius: 10px;
  background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  color: white;
  font-size: 20px;
}

.feature-info {
  flex: 1;
}

.feature-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-dark);
  margin: 0 0 5px 0;
}

.feature-desc {
  font-size: 14px;
  color: var(--text-light);
  margin: 0;
}

/* 服务部分 */
.content-section {
  background-color: var(--card-bg);
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  margin-bottom: 40px;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-header h2 {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-dark);
  margin-bottom: 15px;
}

.section-header p {
  font-size: 16px;
  color: var(--text-light);
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.service-list-container {
  overflow: hidden;
}

.service-list {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  justify-content: center;
}

.service-card {
  flex: 0 0 calc(33.333% - 20px);
  background-color: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.service-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

.service-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.service-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.service-card:hover .service-image img {
  transform: scale(1.1);
}

.service-content {
  padding: 20px;
}

.service-content h3 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 10px;
}

.service-content p {
  font-size: 15px;
  color: var(--text-light);
  margin-bottom: 20px;
  line-height: 1.6;
}

.service-btn {
  display: inline-block;
  padding: 8px 20px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.service-btn:hover {
  background-color: var(--primary-dark);
  transform: translateY(-2px);
}

/* 页面容器 */
.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .banner, .page-container {
    width: 95%;
  }
  
  .service-card {
    flex: 0 0 calc(50% - 15px);
  }
}

@media (max-width: 768px) {
  .banner .wrapper {
    flex-direction: column;
    height: auto;
  }
  
  .banner .left, .banner .right {
    width: 100%;
  }
  
  .slider {
    height: 300px;
  }
  
  .service-card {
    flex: 0 0 100%;
  }
}
</style>
