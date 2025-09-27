<template>
  <div><!--头部区域 -->
    <div style="height:60px;background-color:#eee;display: flex;align-items: center">
      <div style="display: flex;align-items: center;padding-left:20px ">
        <img style="width: 100px;height: 40px;border-radius:50%" src="@/assets/img/logo.jpg">
        <span style="font-size: 20px;font-weight: bold;color: #000000">校园心理健康助手</span>
      </div>
      <div style="flex: 1;display: flex;align-items: center;padding-left:20px;border-bottom:1px solid #ddd">
        首页/{{router.currentRoute.value.meta.name}}
      </div>
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
  <div style="display: flex">
    <div style="width: 240px"><!--菜单区域 -->
      <el-menu router :default-openeds="['1','2']" default-active="/manager/home" style="min-height: calc(100vh - 60px)">
        <el-menu-item index="/manager/home">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="1">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>信息管理</span>
          </template>
          <el-menu-item index="/manager/notice">系统公告</el-menu-item>
          <el-menu-item index="/manager/category">类型分类</el-menu-item>
          <el-menu-item index="/manager/introduction">治疗案例</el-menu-item>
          <el-menu-item index="/manager/test-results">测评结果</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
          <el-menu-item index="/manager/user">用户信息</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <div style="flex:1;width:0;padding: 10px;background-color:#e9ecfb "><!--数据渲染区域 -->
      <RouterView @updateUser="updateUser"/>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js"
import {reactive, ref, onMounted} from "vue";
import { House, Monitor, User } from '@element-plus/icons-vue';

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user') || "{}")
})
const logout = () =>{
  localStorage.removeItem('code_user')
  location.href ='/login'
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}

// if(!data.user?.id){
//   location.href ='/login'
// }

onMounted(() => {
  // 确保第一次加载时导航到home页面
  if (router.currentRoute.value.path === '/manager') {
    router.push('/manager/home');
  }
});
</script>
<style>
.el-menu {
  background-color: #5b627a;
  border: none;
  font-size: 22px;
  font-weight: 700;
}
.el-sub-menu__title {
  background-color: #3a456b;
  color: #ddd;
  font-size: 22px;
  font-weight: 700;
}
.el-menu-item {
  height: 50px;
  color: #ddd;
  font-size: 22px;
  font-weight: 700;
}
.el-menu .is-active {
  background-color: #537bee;
  color: #fff;
  font-size: 22px;
  font-weight: 700;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}
.el-menu-item:not(.is-active):hover {
  background-color: #7a9fff;
  color: #333;
}
.el-dropdown{
  cursor: pointer;
  font-size: 19px;
}
.el-tooltip__trigger{
  outline: none;
  font-size: 19px;
}
.el-menu--inline .el-menu-item{
  padding-left: 48px !important;
  font-size: 22px;
  font-weight: 700;
}
</style>
<script lang="ts">
</script>