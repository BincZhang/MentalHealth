<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <CommonHeader />
  
  <div class="psychological-test-container page-container">
    <div class="test-header">
      <h2>心理测评</h2>
      <p>专业的心理测评工具，帮助你更好地了解自己</p>
    </div>

    <div class="test-categories">
      <div class="category-card" v-for="(category, index) in testCategories" :key="index">
        <div class="category-icon">
          <el-icon :size="48" :color="'var(--primary)'"><component :is="category.icon" /></el-icon>
        </div>
        <h3>{{ category.title }}</h3>
        <p>{{ category.description }}</p>
        <button @click="startTest(category)" class="start-test-btn">开始测试</button>
      </div>
    </div>

    <!-- 测试弹窗 -->
    <el-dialog
      v-model="showTestDialog"
      :title="currentTest?.title"
      width="60%"
      :close-on-click-modal="false"
    >
      <div class="test-content">
        <div v-if="!testStarted && !showResult" class="test-intro">
          <h3>测试说明</h3>
          <p>{{ currentTest?.instructions }}</p>
          <p>预计用时：{{ currentTest?.estimatedTime }}</p>
          <p>题目数量：{{ currentTest?.questionCount }}题</p>
        </div>
        <div v-else-if="testStarted && !showResult" class="test-questions">
          <div class="question" v-for="(question, index) in currentTest?.questions" :key="index">
            <h4>{{ index + 1 }}. {{ question.content }}</h4>
            <div class="options">
              <el-radio-group v-model="answers[index]">
                <el-radio 
                  v-for="(option, optIndex) in question.options" 
                  :key="optIndex" 
                  :label="optIndex"
                >
                  {{ option }}
                </el-radio>
              </el-radio-group>
            </div>
          </div>
        </div>
        <div v-else-if="showResult" class="test-result">
          <h3>测试结果</h3>
          <div class="result-summary">
            <el-progress type="dashboard" :percentage="resultScore" :color="resultColor" :width="120" :stroke-width="10">
              <template #default>
                <div class="result-score">
                  <div class="score-value">{{ resultScore }}</div>
                  <div class="score-label">分</div>
                </div>
              </template>
            </el-progress>
            <div class="result-level">
              <h4>{{ resultLevel.title }}</h4>
              <p>{{ resultLevel.description }}</p>
            </div>
          </div>
          <div class="result-advice">
            <h4>专业建议</h4>
            <p>{{ resultAdvice }}</p>
            <div class="advice-actions">
              <el-button type="primary" @click="goToAiChat" class="chat-btn">
                <el-icon><ChatDotRound /></el-icon>
                与AI心理助手深入交流
              </el-button>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog" v-if="!testStarted || showResult">关闭</el-button>
          <el-button type="primary" @click="beginTest" v-if="!testStarted && !showResult">开始测试</el-button>
          <el-button type="primary" @click="submitTest" v-if="testStarted && !showResult">提交测试</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ChatDotRound } from '@element-plus/icons-vue'
import CommonHeader from '@/components/CommonHeader.vue'

export default {
  name: 'PsychologicalTest',
  components: {
    CommonHeader
  },
  setup() {
    const router = useRouter()
    const testCategories = ref([
      {
        title: '抑郁自评量表',
        description: '评估抑郁症状的严重程度',
        icon: 'Cloudy',
        instructions: '请根据最近一周的大学生活情况，选择最符合你的选项。',
        estimatedTime: '5-10分钟',
        questionCount: 10,
        questions: [
          {
            content: '我在上课或自习时感到情绪低落，无法集中注意力',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我感到早上的第一节课是最容易应付的',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '考试或作业压力让我想哭或已经哭过',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我在宿舍夜间难以入睡或睡眠质量差',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我在食堂吃饭像平时一样有胃口',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我对恋爱或亲密关系仍有兴趣',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我发现自己最近体重减轻，衣服变松了',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我的肠胃问题影响了我的课堂出勤',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '上台演讲或回答问题时，我的心跳比平时快',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '即使休息了一整个周末，我仍感到疲惫不堪',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          }
        ]
      },
      {
        title: '焦虑自评量表',
        description: '评估焦虑症状的严重程度',
        icon: 'Lightning',
        instructions: '请根据最近一周的大学学习生活情况，选择最符合你的选项。',
        estimatedTime: '5-10分钟',
        questionCount: 10,
        questions: [
          {
            content: '面对课程deadline时，我比平常更容易紧张和着急',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '在没有特别原因的情况下，我在上课时感到害怕',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '室友或同学的突发情况容易让我心里烦乱',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '在期末考试季，我觉得自己可能会崩溃',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '我对自己的学业规划感到乐观，不担心挂科',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '在重要课程汇报时，我的手会发抖',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '长时间伏案学习后，我因头痛、颈痛感到困扰',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '下午上课时我总感觉特别疲惫，难以集中精力',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '即使在人多的自习室，我也能保持心平气和地学习',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          },
          {
            content: '看到未读的课程群消息，我的心跳会加速',
            options: ['没有或很少时间', '小部分时间', '相当多时间', '绝大部分或全部时间']
          }
        ]
      },
      {
        title: '压力测试',
        description: '评估当前压力水平',
        icon: 'Histogram',
        instructions: '请根据最近一个月的大学生活情况，选择最符合你的选项。',
        estimatedTime: '5-10分钟',
        questionCount: 10,
        questions: [
          {
            content: '我感到难以应付各科作业和考试的压力',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '选课、实习申请或就业相关事项让我感到紧张',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '即使在课堂上，我也难以集中注意力听讲',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '我熬夜完成作业后感到极度疲惫',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '宿舍环境或室友问题影响了我的睡眠质量',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '学业压力让我对室友或同学感到烦躁易怒',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '我对毕业后的就业前景感到担忧',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '高强度学习后，我的身体出现不适症状',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '在学校休息日，我仍然难以放松自己',
            options: ['从不', '偶尔', '经常', '总是']
          },
          {
            content: '我感到自己的大学生活完全被学业所控制',
            options: ['从不', '偶尔', '经常', '总是']
          }
        ]
      },
      {
        title: '性格测试',
        description: '了解你的性格特征和优势',
        icon: 'User',
        instructions: '请根据你在大学期间的表现，选择最符合你的选项。',
        estimatedTime: '10-15分钟',
        questionCount: 10,
        questions: [
          {
            content: '在班级或社团活动中，你通常：',
            options: ['主动组织和参与', '等待他人邀请', '视活动性质而定', '倾向于不参加']
          },
          {
            content: '面对新学期的课程任务时，你倾向于：',
            options: ['立即开始预习', '制定详细学习计划', '询问学长学姐经验', '等到开课再说']
          },
          {
            content: '在小组作业或项目中，你通常：',
            options: ['担任组长角色', '积极参与讨论', '完成分配的任务', '默默观察记录']
          },
          {
            content: '面对考试压力时，你通常：',
            options: ['制定复习计划并执行', '与同学一起复习', '感到焦虑但努力应对', '拖延复习计划']
          },
          {
            content: '选择选修课程时，你更倾向于：',
            options: ['跟随个人兴趣爱好', '选择好评率高的课', '参考朋友的意见', '考虑学分和难度']
          },
          {
            content: '对于学校规章制度，你通常：',
            options: ['严格遵守', '视情况灵活处理', '选择性地遵守', '有时会挑战规则']
          },
          {
            content: '在课余时间，你更喜欢：',
            options: ['参加社团或聚会', '独自在图书馆看书', '从事创意或兴趣活动', '参加体育锻炼']
          },
          {
            content: '面对老师或同学的批评，你通常：',
            options: ['虚心接受并改进', '感到沮丧难过', '理性分析其合理性', '为自己的行为辩护']
          },
          {
            content: '在解决学习或生活困难时，你更倾向于：',
            options: ['寻找创新解决方法', '按照常规方法处理', '寻求辅导员或学长帮助', '分步骤慢慢解决']
          },
          {
            content: '对于大学毕业后的规划，你通常：',
            options: ['有明确目标并为之努力', '对未来感到一些担忧', '随遇而安不做太多规划', '有多套详细备选计划']
          }
        ]
      },
      {
        title: '情绪管理测试',
        description: '评估你的情绪管理能力',
        icon: 'Sunny',
        instructions: '请根据你在大学生活中的表现，选择最符合你的选项。',
        estimatedTime: '8-12分钟',
        questionCount: 10,
        questions: [
          {
            content: '当遇到考试失利或挂科时，你通常：',
            options: ['很快调整心态继续学习', '需要几天时间平复心情', '难以释怀影响后续学习', '情绪崩溃无法面对']
          },
          {
            content: '面对室友或同学的负面情绪，你通常：',
            options: ['能够理解并给予安慰', '感到不适尽量回避', '试图转移话题', '被影响导致自己也情绪低落']
          },
          {
            content: '在期末考试周的高压下，你通常：',
            options: ['保持冷静有序复习', '感到焦虑但能应对', '容易情绪失控', '不知所措拖延学习']
          },
          {
            content: '当对课程安排或成绩感到愤怒时，你通常：',
            options: ['理性沟通表达不满', '压抑情绪不表达', '向朋友发泄情绪', '转移注意力做其他事情']
          },
          {
            content: '面对学术竞赛或奖学金申请失败时，你通常：',
            options: ['总结经验为下次做准备', '感到沮丧影响自信', '责怪评委或制度不公', '放弃再次尝试的想法']
          },
          {
            content: '在学业或人际关系让你情绪低落时，你通常：',
            options: ['积极寻找解决方法', '寻找朋友或辅导员倾诉', '独自承受不愿分享', '长时间沉浸在负面情绪中']
          },
          {
            content: '对于学习压力带来的各种情绪，你通常：',
            options: ['能够清晰识别并应对', '感到困惑不知如何处理', '情绪波动大难以控制', '尽量忽视不去面对']
          },
          {
            content: '在与老师或同学产生分歧时，你通常：',
            options: ['理性沟通寻求共识', '情绪激动难以冷静', '回避问题不再讨论', '表面妥协但内心不满']
          },
          {
            content: '面对专业课学习压力时，你通常：',
            options: ['积极调整学习方法', '感到焦虑但继续努力', '回避困难课程', '寻求同学或老师帮助']
          },
          {
            content: '在情绪波动影响学习效率时，你通常：',
            options: ['能够自我调节恢复状态', '需要朋友开导才能恢复', '难以控制导致学习中断', '任由情绪影响不去调整']
          }
        ]
      },
      {
        title: '人际关系测试',
        description: '评估你的人际交往能力',
        icon: 'Connection',
        instructions: '请根据你在大学校园中的表现，选择最符合你的选项。',
        estimatedTime: '8-12分钟',
        questionCount: 10,
        questions: [
          {
            content: '在新生交流会或班级活动中，你通常：',
            options: ['主动与人交流结交新友', '等待他人来接近', '感到不自在局促', '尽量避免参加此类活动']
          },
          {
            content: '当同学请你帮忙解答课程问题时，你通常：',
            options: ['乐于提供帮助详细讲解', '视自己时间和能力而定', '感到压力不想被打扰', '委婉拒绝建议其问老师']
          },
          {
            content: '在小组课题或班级活动中，你通常：',
            options: ['积极参与贡献想法', '认真完成分内工作', '被动等待指派任务', '感到团队合作困难']
          },
          {
            content: '与室友或同学意见不合时，你通常：',
            options: ['理性沟通寻求共识', '坚持自己的观点', '妥协退让避免冲突', '回避讨论保持沉默']
          },
          {
            content: '在大学交友或建立人脉时，你通常：',
            options: ['主动结交不同专业朋友', '保持适当社交距离', '等待志同道合者主动接近', '感到社交困难压力大']
          },
          {
            content: '面对老师或同学的学业批评，你通常：',
            options: ['虚心接受并积极改进', '感到受伤但会思考', '为自己的方法辩护', '表面接受实则忽视']
          },
          {
            content: '在课堂讨论或班会发言时，你通常：',
            options: ['清晰自信地表达观点', '有所保留简短发言', '除非点名否则不发言', '尽量避免任何发言']
          },
          {
            content: '在听取同学或老师建议时，你通常：',
            options: ['专注倾听并认真思考', '经常走神难以集中', '急于表达自己的观点', '缺乏耐心应付了事']
          },
          {
            content: '在维持大学友谊与人脉时，你通常：',
            options: ['主动组织聚会保持联系', '等待对方发起活动', '随缘相处不刻意联系', '感到维系关系很困难']
          },
          {
            content: '在处理宿舍或学习小组冲突时，你通常：',
            options: ['寻求对话解决双赢', '坚持自己的立场', '为了和谐主动妥协', '尽量回避矛盾']
          }
        ]
      }
    ])

    const showTestDialog = ref(false)
    const currentTest = ref(null)
    const testStarted = ref(false)
    const answers = ref([])
    const showResult = ref(false)
    const resultScore = ref(0)
    const resultLevel = ref({})
    const resultAdvice = ref('')
    const resultColor = ref('')

    const startTest = (category) => {
      console.log('startTest called with:', category)
      if (category && typeof category === 'object') {
        // 点击测试卡片时的逻辑
        currentTest.value = category
        showTestDialog.value = true
        testStarted.value = false
        showResult.value = false
        // 确保answers数组被正确初始化，即使category.questions暂时不可用
        if (category.questions && Array.isArray(category.questions)) {
          answers.value = new Array(category.questions.length).fill(undefined)
        } else {
          // 如果questions不可用，初始化为空数组，待数据加载完成后再更新
          answers.value = []
        }
        console.log('Test initialized:', {
          currentTest: currentTest.value,
          testStarted: testStarted.value,
          showResult: showResult.value,
          answersLength: answers.value.length
        })
      } else if (currentTest.value) {
        // 点击对话框中的"开始测试"按钮时的逻辑
        testStarted.value = true
        showResult.value = false
        showTestDialog.value = true
        console.log('Test started:', {
          currentTest: currentTest.value,
          testStarted: testStarted.value,
          showResult: showResult.value
        })
      }
    }

    const beginTest = () => {
      console.log('beginTest called, currentTest:', currentTest.value)
      // 修改条件判断，即使questions暂时不可用也能启动测试
      if (currentTest.value) {
        testStarted.value = true
        showResult.value = false
        // 如果此时questions已加载，则初始化答案数组
        if (currentTest.value.questions && Array.isArray(currentTest.value.questions)) {
          answers.value = new Array(currentTest.value.questions.length).fill(undefined)
        }
        console.log('Test started:', {
          testStarted: testStarted.value,
          showResult: showResult.value
        })
      }
    }

    // 保存测评结果到用户数据
    const saveTestResult = async () => {
      if (!currentTest.value || !resultLevel.value) return
      
      // 获取用户信息
      const user = JSON.parse(localStorage.getItem('code_user') || '{}')
      if (!user || !user.id) {
        ElMessage.warning('请先登录后再进行测评')
        return
      }
      
      // 准备测评结果数据
      const testResult = {
        userId: user.id,
        testType: currentTest.value.title,
        score: resultScore.value,
        resultLevel: resultLevel.value.title,
        resultDescription: resultLevel.value.description,
        resultAdvice: resultAdvice.value,
        testDate: new Date().toISOString(),
        answers: JSON.stringify(answers.value) // 将数组转换为JSON字符串
      }
      
      try {
        // 发送测评结果到后端
        console.log('Saving test result:', testResult)
        console.log('User token:', user.token)
        
        const response = await axios.post('http://localhost:9999/api/test/save', testResult, {
          headers: {
            'Authorization': `Bearer ${user.token}`,
            'Content-Type': 'application/json'
          }
        })
        
        console.log('Save response:', response.data)
        
        // 保存到 localStorage 中作为临时存储
        const savedResults = JSON.parse(localStorage.getItem('psychologicalTestResults') || '[]')
        savedResults.push(testResult)
        localStorage.setItem('psychologicalTestResults', JSON.stringify(savedResults))
        
        // 通知 AI 聊天助手
        notifyAiAssistant(testResult)
        
        ElMessage.success('测评结果已保存')
      } catch (error) {
        console.error('保存测评结果失败:', error)
        console.error('Error response:', error.response)
        if (error.response && error.response.status === 401) {
          ElMessage.error('登录已过期，请重新登录')
          router.push('/login')
        } else {
          ElMessage.error('保存测评结果失败，请稍后重试')
        }
      }
    }
    
    // 通知 AI 聊天助手
    const notifyAiAssistant = (testResult) => {
      try {
        // 构建消息内容
        const message = {
          type: 'psychological_test_result',
          data: testResult
        }
        
        // 保存消息到 localStorage，供 AI 聊天组件读取
        localStorage.setItem('aiAssistantContext', JSON.stringify(message))
        
        console.log('已将测评结果传给 AI 聊天助手', message)
      } catch (error) {
        console.error('通知 AI 聊天助手失败', error)
      }
    }
    
    // 跳转到 AI 聊天界面
    const goToAiChat = () => {
      router.push('/ai-chat')
    }

    const submitTest = () => {
      // 计算得分
      const totalQuestions = currentTest.value.questions.length
      const answeredQuestions = answers.value.filter(a => a !== undefined).length
      
      if (answeredQuestions < totalQuestions) {
        ElMessage.warning(`请完成所有题目后再提交（还有 ${totalQuestions - answeredQuestions} 题未回答）`)
        return
      }
      
      // 根据测试类型计算得分
      let score = 0
      if (currentTest.value.title === '抑郁自评量表') {
        // 抑郁量表得分计算
        answers.value.forEach((answer, index) => {
          // 注意：第2、5、6题是反向计分的
          if (index === 1 || index === 4 || index === 5) {
            score += (3 - answer) * 25
          } else {
            score += answer * 25
          }
        })
        score = Math.round(score / totalQuestions)
        
        // 设置结果等级
        if (score <= 25) {
          resultLevel.value = { 
            title: '正常', 
            description: '您目前的情绪状态良好，没有明显的抑郁症状。' 
          }
          resultAdvice.value = '继续保持健康的生活方式，定期进行适当的体育锻炼，保持良好的社交活动。'
          resultColor.value = '#67c23a'
        } else if (score <= 50) {
          resultLevel.value = { 
            title: '轻度抑郁', 
            description: '您可能存在轻微的情绪低落，但这可能是暂时的。' 
          }
          resultAdvice.value = '建议增加户外活动和社交互动，学习简单的放松技巧，如深呼吸和冥想。如果症状持续超过两周，可以考虑咨询专业人士。'
          resultColor.value = '#e6a23c'
        } else if (score <= 75) {
          resultLevel.value = { 
            title: '中度抑郁', 
            description: '您可能正在经历一些明显的情绪困扰，这可能影响到日常生活。' 
          }
          resultAdvice.value = '建议尽快咨询心理医生或心理咨询师。同时，保持规律的作息，避免酒精和药物滥用，多与家人朋友交流。'
          resultColor.value = '#f56c6c'
        } else {
          resultLevel.value = { 
            title: '重度抑郁', 
            description: '您目前可能正在经历严重的情绪问题，这已经明显影响到您的日常生活。' 
          }
          resultAdvice.value = '强烈建议您立即寻求专业的心理或精神科医生帮助。抑郁是可以治愈的，及时的专业干预非常重要。'
          resultColor.value = '#f56c6c'
        }
      } else if (currentTest.value.title === '焦虑自评量表') {
        // 焦虑量表得分计算
        answers.value.forEach((answer, index) => {
          // 注意：第5、9题是反向计分的
          if (index === 4 || index === 8) {
            score += (3 - answer) * 25
          } else {
            score += answer * 25
          }
        })
        score = Math.round(score / totalQuestions)
        
        // 设置结果等级
        if (score <= 25) {
          resultLevel.value = { 
            title: '正常', 
            description: '您目前没有明显的焦虑症状。' 
          }
          resultAdvice.value = '继续保持健康的生活方式，定期进行放松活动，如瑜伽或冥想。'
          resultColor.value = '#67c23a'
        } else if (score <= 50) {
          resultLevel.value = { 
            title: '轻度焦虑', 
            description: '您可能存在轻微的焦虑感，但这在正常范围内。' 
          }
          resultAdvice.value = '学习一些放松技巧，如深呼吸、渐进性肌肉放松等。减少咖啡因摄入，保持规律作息。'
          resultColor.value = '#e6a23c'
        } else if (score <= 75) {
          resultLevel.value = { 
            title: '中度焦虑', 
            description: '您的焦虑程度已经达到中等水平，可能会对日常生活产生一定影响。' 
          }
          resultAdvice.value = '建议咨询心理咨询师获取专业支持。同时，可以尝试一些认知行为疗法的技巧，如挑战消极思想。'
          resultColor.value = '#f56c6c'
        } else {
          resultLevel.value = { 
            title: '重度焦虑', 
            description: '您目前可能正在经历严重的焦虑问题，这已经显著影响到您的日常功能。' 
          }
          resultAdvice.value = '强烈建议您尽快咨询专业的心理医生或精神科医生，获取专业评估和治疗方案。'
          resultColor.value = '#f56c6c'
        }
      } else if (currentTest.value.title === '性格测试') {
        // 性格测试得分计算
        answers.value.forEach((answer, index) => {
          score += answer * 25
        })
        score = Math.round(score / totalQuestions)
        
        // 设置结果等级
        if (score <= 25) {
          resultLevel.value = { 
            title: '内向型', 
            description: '你倾向于内向、谨慎，喜欢独处和深度思考。' 
          }
          resultAdvice.value = '建议适当增加社交活动，培养表达和沟通能力。同时，发挥你善于思考和分析的优势，在需要专注和深度思考的领域发展。'
          resultColor.value = '#67c23a'
        } else if (score <= 50) {
          resultLevel.value = { 
            title: '平衡型', 
            description: '你具有较好的平衡性，能够根据情况调整自己的行为方式。' 
          }
          resultAdvice.value = '继续保持这种平衡性，在需要时可以适当调整自己的行为方式。建议多尝试新事物，拓展自己的舒适区。'
          resultColor.value = '#e6a23c'
        } else if (score <= 75) {
          resultLevel.value = { 
            title: '外向型', 
            description: '你倾向于外向、活跃，喜欢社交和表达。' 
          }
          resultAdvice.value = '发挥你善于社交和表达的优势，在需要沟通和协调的领域发展。同时，注意培养耐心和专注力，适当给自己一些独处的时间。'
          resultColor.value = '#f56c6c'
        } else {
          resultLevel.value = { 
            title: '非常外向型', 
            description: '你非常外向、活跃，具有很强的社交能力和领导力。' 
          }
          resultAdvice.value = '你具有很强的社交能力和领导力，建议在需要团队协作和领导力的领域发展。同时，注意培养耐心和倾听能力，学会在适当的时候放慢节奏。'
          resultColor.value = '#f56c6c'
        }
      } else if (currentTest.value.title === '情绪管理测试') {
        // 情绪管理测试得分计算
        answers.value.forEach((answer, index) => {
          score += answer * 25
        })
        score = Math.round(score / totalQuestions)
        
        // 设置结果等级
        if (score <= 25) {
          resultLevel.value = { 
            title: '情绪管理能力较弱', 
            description: '你在情绪管理方面还有提升空间，容易受到情绪的影响。' 
          }
          resultAdvice.value = '建议学习情绪管理技巧，如深呼吸、冥想等。可以尝试写情绪日记，记录自己的情绪变化。必要时寻求专业的心理咨询帮助。'
          resultColor.value = '#67c23a'
        } else if (score <= 50) {
          resultLevel.value = { 
            title: '情绪管理能力一般', 
            description: '你具有一定的情绪管理能力，但在某些情况下仍会受到影响。' 
          }
          resultAdvice.value = '继续培养情绪管理能力，学习更多的情绪调节技巧。建议保持规律的运动和作息，这对情绪管理很有帮助。'
          resultColor.value = '#e6a23c'
        } else if (score <= 75) {
          resultLevel.value = { 
            title: '情绪管理能力良好', 
            description: '你具有良好的情绪管理能力，能够较好地应对各种情绪。' 
          }
          resultAdvice.value = '继续保持良好的情绪管理习惯，可以尝试帮助他人提升情绪管理能力。建议学习更高级的情绪管理技巧，如认知行为疗法等。'
          resultColor.value = '#f56c6c'
        } else {
          resultLevel.value = { 
            title: '情绪管理能力优秀', 
            description: '你具有优秀的情绪管理能力，能够很好地应对各种情绪挑战。' 
          }
          resultAdvice.value = '你具有优秀的情绪管理能力，建议在需要情绪管理的领域发展，如心理咨询、团队管理等。同时，可以尝试帮助他人提升情绪管理能力。'
          resultColor.value = '#f56c6c'
        }
      } else if (currentTest.value.title === '人际关系测试') {
        // 人际关系测试得分计算
        answers.value.forEach((answer, index) => {
          score += answer * 25
        })
        score = Math.round(score / totalQuestions)
        
        // 设置结果等级
        if (score <= 25) {
          resultLevel.value = { 
            title: '人际关系能力较弱', 
            description: '你在人际交往方面还有提升空间，可能需要更多的社交技巧。' 
          }
          resultAdvice.value = '建议学习基本的社交技巧，如倾听、表达等。可以从小范围的社交活动开始，逐步提升社交能力。必要时寻求专业的社交技能培训。'
          resultColor.value = '#67c23a'
        } else if (score <= 50) {
          resultLevel.value = { 
            title: '人际关系能力一般', 
            description: '你具有一定的人际交往能力，但在某些情况下仍会感到困难。' 
          }
          resultAdvice.value = '继续提升人际交往能力，学习更多的社交技巧。建议多参加社交活动，在实践中提升能力。注意培养同理心和倾听能力。'
          resultColor.value = '#e6a23c'
        } else if (score <= 75) {
          resultLevel.value = { 
            title: '人际关系能力良好', 
            description: '你具有良好的人际交往能力，能够较好地处理各种人际关系。' 
          }
          resultAdvice.value = '继续保持良好的人际交往习惯，可以尝试帮助他人提升社交能力。建议学习更高级的人际交往技巧，如冲突管理、团队建设等。'
          resultColor.value = '#f56c6c'
        } else {
          resultLevel.value = { 
            title: '人际关系能力优秀', 
            description: '你具有优秀的人际交往能力，能够很好地处理各种人际关系。' 
          }
          resultAdvice.value = '你具有优秀的人际交往能力，建议在需要人际交往的领域发展，如销售、管理、咨询等。同时，可以尝试帮助他人提升社交能力。'
          resultColor.value = '#f56c6c'
        }
      } else {
        // 压力测试得分计算
        answers.value.forEach(answer => {
          score += answer * 33.3
        })
        score = Math.round(score / totalQuestions)
        
        // 设置结果等级
        if (score <= 25) {
          resultLevel.value = { 
            title: '压力较低', 
            description: '您目前的压力水平较低，能够有效应对日常生活中的压力。' 
          }
          resultAdvice.value = '继续保持良好的压力管理习惯，定期进行自我放松和休息。'
          resultColor.value = '#67c23a'
        } else if (score <= 50) {
          resultLevel.value = { 
            title: '压力中等', 
            description: '您的压力水平处于中等范围，这在现代生活中比较常见。' 
          }
          resultAdvice.value = '建议学习一些压力管理技巧，如时间管理、设定合理目标、放松训练等。保持规律的运动习惯也有助于缓解压力。'
          resultColor.value = '#e6a23c'
        } else if (score <= 75) {
          resultLevel.value = { 
            title: '压力较大', 
            description: '您目前面临较大的压力，这可能对健康和生活质量产生负面影响。' 
          }
          resultAdvice.value = '应当重视当前的压力水平，适当调整工作和生活节奏，学习有效的压力管理技巧。必要时寻求专业的心理咨询支持。'
          resultColor.value = '#f56c6c'
        } else {
          resultLevel.value = { 
            title: '压力严重', 
            description: '您正在经历严重的压力状态，这可能已经对身心健康造成了明显影响。' 
          }
          resultAdvice.value = '强烈建议您尽快调整当前的生活状态，必要时寻求专业的心理健康服务，学习有效的压力应对策略，并考虑是否需要对工作和生活进行重大调整。'
          resultColor.value = '#f56c6c'
        }
      }
      
      resultScore.value = score
      showResult.value = true
      testStarted.value = false
      
      // 保存测评结果
      saveTestResult()
    }

    const closeDialog = () => {
      showTestDialog.value = false
      showResult.value = false
      testStarted.value = false
    }

    return {
      testCategories,
      showTestDialog,
      currentTest,
      testStarted,
      answers,
      showResult,
      resultScore,
      resultLevel,
      resultAdvice,
      resultColor,
      startTest,
      beginTest,
      submitTest,
      closeDialog,
      goToAiChat
    }
  }
}
</script>

<style scoped>
.psychological-test-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.test-header {
  text-align: center;
  margin-bottom: 40px;
}

.test-header h2 {
  font-size: 32px;
  color: var(--text-dark);
  margin-bottom: 10px;
}

.test-header p {
  font-size: 16px;
  color: var(--text-light);
}

.test-categories {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 30px;
}

.category-card {
  background: #fff;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 24px var(--shadow-color);
  text-align: center;
  transition: transform 0.3s ease;
}

.category-card:hover {
  transform: translateY(-10px);
}

.category-icon {
  margin-bottom: 20px;
}

.category-card h3 {
  font-size: 24px;
  color: var(--text-dark);
  margin-bottom: 10px;
}

.category-card p {
  font-size: 16px;
  color: var(--text-light);
  margin-bottom: 20px;
}

.start-test-btn {
  background: var(--primary);
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 24px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: inline-block !important;
  font-size: 16px;
  font-weight: 600;
  min-width: 120px;
  margin-top: 10px;
}

.start-test-btn:hover {
  background: #8abba7;
}

.test-content {
  padding: 20px;
}

.test-intro {
  text-align: center;
}

.test-intro h3 {
  font-size: 24px;
  margin-bottom: 20px;
}

.test-intro p {
  margin-bottom: 10px;
  color: var(--text-light);
}

.test-questions {
  max-height: 400px;
  overflow-y: auto;
}

.question {
  margin-bottom: 30px;
}

.question h4 {
  font-size: 18px;
  margin-bottom: 15px;
  color: var(--text-dark);
}

.options {
  margin-left: 20px;
}

:deep(.el-radio) {
  display: block;
  margin-bottom: 10px;
}

:deep(.el-radio__label) {
  font-size: 16px;
}

.test-result {
  text-align: center;
}

.test-result h3 {
  font-size: 24px;
  margin-bottom: 30px;
  color: var(--text-dark);
}

.result-summary {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
  gap: 30px;
}

.result-score {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.score-value {
  font-size: 24px;
  font-weight: bold;
}

.score-label {
  font-size: 14px;
  margin-top: -5px;
}

.result-level {
  text-align: left;
  max-width: 300px;
}

.result-level h4 {
  font-size: 20px;
  margin-bottom: 10px;
  color: var(--text-dark);
}

.result-level p {
  font-size: 14px;
  color: var(--text-light);
  line-height: 1.6;
}

.result-advice {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
  text-align: left;
}

.result-advice h4 {
  font-size: 18px;
  margin-bottom: 10px;
  color: var(--text-dark);
}

.result-advice p {
  font-size: 14px;
  color: var(--text-light);
  line-height: 1.6;
}

.advice-actions {
  margin-top: 20px;
  text-align: center;
}

.chat-btn {
  background: var(--primary);
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 24px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.chat-btn:hover {
  background: #8abba7;
}
</style> 