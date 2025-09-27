<template>
  <div class="bg">
    <audio ref="bgMusic" :src="musicUrl" autoplay loop></audio>
    <div style="width: 500px;height:400px;background-color: #f5b5b5; border-radius: 10px;box-shadow: 0 0 10px rgba(0,0,0,0.8);padding: 20px;margin-right: 100px">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin: 15px 0;text-align: center; font-weight: 700;font-size: 40px">欢 迎 登 陆</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password="true" v-model="data.form.password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="role">
          <el-select style="width: 100%" v-model="data.form.role">
            <el-option size="large" label="管理员" value="ADMIN"></el-option>
            <el-option size="large" label="用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <div style="padding-top:15px">
          <el-button style="width: 100%" size="large" type="primary" @click="login">登 录</el-button>
        </div>
        <div style="text-align: right;padding-top: 15px;font-size: 16px">
          还没有账号？请<a style="color: #2c82ff" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const formRef = ref();
const data = reactive({
  form: { role: "ADMIN" },
  rules: {
    username: [
      {
        required: true,
        message: "请输入账号",
        trigger: "blur",
      },
      {
        min: 3,
        message: "账号最少 3 位",
        trigger: "blur",
      },
    ],
    password: [
      {
        required: true,
        message: "请输入密码",
        trigger: "blur",
      },
    ],
  },
});

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post("login", data.form).then((res) => {
        if (res.code === "200") {
          localStorage.setItem("code_user", JSON.stringify(res.data || {}));
          ElMessage.success("登陆成功");
          switch (res.data.role) {
            case "ADMIN":
              router.push("/manager"); // 管理员后台
              break;
            case "USER":
              router.push("/user"); // 教师页面
              break;
            default:
          }
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

onMounted(() => {
  const audio = document.querySelector('audio');
  if (audio) {
    audio.play();
  }
});
</script>

<style scoped>
.bg {
  height: 100vh;
  display: flex;
  justify-content: right;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/img/登陆.jpg");
  background-size: cover;
  filter: brightness(0.8);
}

.el-button {
  font-size: 25px;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>