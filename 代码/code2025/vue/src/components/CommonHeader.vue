<template>
  <div class="header">
    <div class="wrapper">
      <!-- logo -->
      <div class="logo">
        <h1 class="heartbeat"><a href="#">心晴</a></h1>
      </div>
      <!-- 导航 -->
      <div class="nav">
        <ul>
          <li><router-link to="/user">首页</router-link></li>
          <li><router-link to="/ai-chat">小愿聊天</router-link></li>
          <li><router-link to="/psychological-test">心理测评</router-link></li>
          <li><router-link to="/knowledge-base">心理健康知识库</router-link></li>
          <li><router-link to="/notice-list">公告列表</router-link></li>
        </ul>
      </div>
      <!-- 搜索 -->
      <div class="search">
        <input type="text" placeholder="请输入关键词">
        <i class="fas fa-search search-icon"></i>
      </div>
      <!-- 用户 -->
      <div style="width: fit-content;display: flex;align-items: center;padding-right: 20px;border-bottom:1px solid #ddd">
        <el-dropdown>
          <div style="display: flex;align-items: center;">
            <img v-if="data.user?.avatar" style="width: 40px;height: 40px;border-radius: 50%" :src="data.user?.avatar" alt="">
            <img v-else style="width: 40px;height: 40px;border-radius: 50%" src="@/assets/img/机器人头像.png/" alt="">
            <span style="padding-left: 5px">{{ data.user.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import router from "@/router/index.js"

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}")
})

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

onMounted(() => {
  // 确保只有在对应路由时才显示下划线
  const links = document.querySelectorAll('.nav li a');
  links.forEach(link => {
    link.classList.remove('active');
  });
})
</script>

<style scoped>
.header {
  height: 100px;
  background-color: #fff;
  box-shadow: 0 2px 12px var(--shadow-color);
  display: flex;
  align-items: center;
}

.header .wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 1200px;
  margin: 0 auto;
}

.logo a {
  display: block;
  width: 150px;
  height: 60px;
  background-image: url(../assets/img/logo.jpg);
  background-size: contain;
  background-repeat: no-repeat;
  font-size: 0;
}

.nav ul {
  display: flex;
  gap: 16px;
  flex-wrap: nowrap;
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav li a {
  font-size: 24px;
  font-weight: 700;
  position: relative;
  white-space: nowrap;
  color: #333;
  text-decoration: none;
}

.nav li a::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -2px;
  width: 0;
  height: 3px;
  background: var(--primary);
  transition: width 0.3s ease;
}

.nav li a:hover::after {
  width: 100%;
}

.nav li .router-link-active::after,
.nav li .active::after {
  width: 100%;
  background: #4CAF50;
}

.nav li a:visited {
  color: #333;
}

.header .search {
  display: flex;
  align-items: center;
  padding: 0 16px;
  width: 300px;
  height: 40px;
  background-color: var(--secondary);
  border-radius: 24px !important;
  box-shadow: 0 2px 12px var(--shadow-color);
  border: 2px solid #007bff !important;
}

.search input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  font-weight: normal;
  color: var(--text-light);
  padding-right: 30px;
}

.search .search-icon {
  color: var(--text-light);
  transition: color 0.3s ease;
}

.search-icon:hover {
  color: var(--primary);
}

.heartbeat {
  animation: heartbeat 1.5s ease-in-out infinite;
}

@keyframes heartbeat {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

/* 去掉头像框悬停时的黑框效果 */
img {
  border: none;
  transition: transform 0.3s ease;
}

img:hover {
  transform: scale(1.05);
  box-shadow: none;
}

.dropdown-menu a {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-dark);
  transition: color 0.3s ease;
}
</style> 