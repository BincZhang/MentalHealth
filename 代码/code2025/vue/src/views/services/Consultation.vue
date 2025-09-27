<template>
  <div class="service-page">
    <div class="page-header">
      <div class="container">
        <h1>心理咨询</h1>
        <p>专业心理咨询师一对一服务，为您解决心理困扰</p>
      </div>
    </div>
    
    <div class="container">
      <div class="service-intro">
        <div class="intro-text">
          <h2>服务介绍</h2>
          <p>心理咨询是一种专业的心理帮助形式，由专业培训的心理咨询师通过与来访者的交谈和互动，帮助来访者认识自己，了解自己的问题所在，发掘自身潜能，寻找解决问题的方法，从而促进心理健康的过程。</p>
          <p>我们的心理咨询服务由经验丰富的专业心理咨询师提供，针对学生常见的心理问题，如学习压力、人际关系、情绪管理、职业规划等问题提供有效的帮助。</p>
        </div>
        <div class="intro-image">
          <img src="@/assets/img/咨询.jpg" alt="心理咨询">
        </div>
      </div>
      
      <div class="service-features">
        <h2>服务特点</h2>
        <div class="feature-cards">
          <div class="feature-card">
            <i class="fas fa-user-shield"></i>
            <h3>专业保密</h3>
            <p>严格遵守心理咨询伦理，确保个人信息和咨询内容的保密性</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-certificate"></i>
            <h3>资质认证</h3>
            <p>所有咨询师均持有专业资质证书，经过严格培训</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-comments"></i>
            <h3>个性化服务</h3>
            <p>根据个人需求定制咨询方案，关注个体差异</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-calendar-check"></i>
            <h3>灵活预约</h3>
            <p>提供线上线下多种咨询方式，时间灵活可调整</p>
          </div>
        </div>
      </div>
      
      <div class="service-process">
        <h2>咨询流程</h2>
        <div class="process-steps">
          <div class="step">
            <div class="step-number">1</div>
            <div class="step-content">
              <h3>预约登记</h3>
              <p>填写基本信息和咨询需求</p>
            </div>
          </div>
          <div class="step">
            <div class="step-number">2</div>
            <div class="step-content">
              <h3>匹配咨询师</h3>
              <p>根据问题类型匹配专业咨询师</p>
            </div>
          </div>
          <div class="step">
            <div class="step-number">3</div>
            <div class="step-content">
              <h3>初次咨询</h3>
              <p>了解问题，建立咨询关系</p>
            </div>
          </div>
          <div class="step">
            <div class="step-number">4</div>
            <div class="step-content">
              <h3>持续跟进</h3>
              <p>定期咨询，评估进展</p>
            </div>
          </div>
          <div class="step">
            <div class="step-number">5</div>
            <div class="step-content">
              <h3>结案评估</h3>
              <p>问题解决后的效果评估</p>
            </div>
          </div>
        </div>
      </div>
      
      <div class="appointment-section">
        <h2>在线预约</h2>
        <div class="appointment-form">
          <el-form :model="form" label-width="80px">
            <el-form-item label="姓名">
              <el-input v-model="form.name" placeholder="请输入您的姓名"></el-input>
            </el-form-item>
            <el-form-item label="联系方式">
              <el-input v-model="form.contact" placeholder="请输入您的联系方式"></el-input>
            </el-form-item>
            <el-form-item label="预约日期">
              <el-date-picker v-model="form.date" type="date" placeholder="选择日期"></el-date-picker>
            </el-form-item>
            <el-form-item label="咨询类型">
              <el-select v-model="form.type" placeholder="请选择咨询类型">
                <el-option label="学业压力" value="academic"></el-option>
                <el-option label="人际关系" value="relationship"></el-option>
                <el-option label="情绪管理" value="emotion"></el-option>
                <el-option label="生涯规划" value="career"></el-option>
                <el-option label="其他问题" value="other"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="问题描述">
              <el-input v-model="form.description" type="textarea" rows="4" placeholder="请简要描述您的问题"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm">立即预约</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'ConsultationPage',
  setup() {
    const form = reactive({
      name: '',
      contact: '',
      date: '',
      type: '',
      description: ''
    })
    
    const submitForm = () => {
      // 这里添加表单提交逻辑
      ElMessage.success('预约已提交，我们会尽快与您联系!')
    }
    
    const resetForm = () => {
      form.name = ''
      form.contact = ''
      form.date = ''
      form.type = ''
      form.description = ''
    }
    
    return {
      form,
      submitForm,
      resetForm
    }
  }
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

.service-features {
  margin-bottom: 60px;
}

.feature-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.feature-card {
  background-color: white;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
}

.feature-card i {
  font-size: 2.5rem;
  color: #5b7bf0;
  margin-bottom: 15px;
}

.feature-card h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.service-process {
  margin-bottom: 60px;
}

.process-steps {
  display: flex;
  justify-content: space-between;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 18%;
  position: relative;
}

.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 30px;
  right: -50%;
  width: 100%;
  height: 2px;
  background-color: #e0e0e0;
  z-index: 0;
}

.step-number {
  width: 60px;
  height: 60px;
  background-color: #5b7bf0;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 15px;
  position: relative;
  z-index: 1;
}

.step-content {
  text-align: center;
}

.step-content h3 {
  font-size: 1.1rem;
  margin-bottom: 8px;
}

.appointment-section {
  background-color: #f9f9f9;
  padding: 40px;
  border-radius: 10px;
  margin-bottom: 60px;
}

.appointment-form {
  max-width: 600px;
  margin: 0 auto;
}

@media (max-width: 991px) {
  .service-intro {
    flex-direction: column;
  }
  
  .feature-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .process-steps {
    flex-direction: column;
    align-items: center;
    gap: 30px;
  }
  
  .step {
    width: 100%;
    max-width: 350px;
  }
  
  .step:not(:last-child)::after {
    display: none;
  }
}

@media (max-width: 767px) {
  .feature-cards {
    grid-template-columns: 1fr;
  }
}
</style> 