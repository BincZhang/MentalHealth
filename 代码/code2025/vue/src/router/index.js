import { createRouter, createWebHistory } from 'vue-router'
import AIChat from '@/views/AIChat.vue'
import PsychologicalTest from '@/views/PsychologicalTest.vue'
import KnowledgeBase from '@/views/KnowledgeBase.vue'
import Community from '../views/Community.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {path: '/', redirect:'/login'},
    {path: '/manager', component: ()=>import('../views/Manager.vue'),
        children:[
        {path: 'home', meta:{name:'主页'},component: ()=>import('../views/Home.vue'),},
        {path: 'admin', meta:{name:'管理员信息'},component: ()=>import('../views/Admin.vue'),},
        {path: 'user', meta:{name:'用户信息'},component: ()=>import('../views/User.vue'),},
        {path: 'person', meta:{name:'个人信息'},component: ()=>import('../views/Person.vue'),},
        {path: 'updatePassword', meta:{name:'修改密码'},component: ()=>import('../views/UpdatePassword.vue'),},
        {path: 'introduction', meta:{name:'治疗案例'},component: ()=>import('../views/Introduction.vue'),},
        {path: 'notice', meta:{name:'系统公告'},component: ()=>import('../views/Notice.vue'),},
        {path: 'category', meta:{name:'类型分类'},component: ()=>import('../views/Category.vue'),},
        {path: 'test-results', meta:{name:'测评结果'}, component: () => import('../views/TestResults.vue'),}
        ]
    },
      {path: '/user', component: () => import('../views/UserHome.vue'),},
    {path: '/notFound', component: () => import('../views/404.vue'),},
    {path: '/register', component: () => import('../views/Register.vue'),},
    {path: '/login', component: () => import('../views/Login.vue'),},
    {path: '/ai-chat', name: 'AIChat', component: AIChat},
    {path: '/psychological-test', name: 'PsychologicalTest', component: PsychologicalTest},
    {path: '/knowledge-base', name: 'KnowledgeBase', component: KnowledgeBase},
    {path: '/community', name: 'Community', component: Community, meta: { requiresAuth: true }},
    {path: '/notice-list',
      name: 'NoticeList',
      component: () => import('../views/NoticeList.vue'),
      meta: { title: '公告列表' }
    },
    {path: '/consultation',
      name: 'Consultation',
      component: () => import('../views/services/Consultation.vue'),
      meta: { title: '心理咨询', requiresAuth: true }
    },
    {path: '/lectures',
      name: 'Lectures',
      component: () => import('../views/services/Lectures.vue'),
      meta: { title: '心理讲座', requiresAuth: true }
    },
    {path: '/group-guidance',
      name: 'GroupGuidance',
      component: () => import('../views/services/GroupGuidance.vue'),
      meta: { title: '团体辅导', requiresAuth: true }
    },
    {path: '/health-courses',
      name: 'HealthCourses',
      component: () => import('../views/services/HealthCourses.vue'),
      meta: { title: '健康课程', requiresAuth: true }
    },
    {path: '/self-learning',
      name: 'SelfLearning',
      component: () => import('../views/services/SelfLearning.vue'),
      meta: { title: '自助学习', requiresAuth: true }
    },
    {path: '/case-sharing',
      name: 'CaseSharing',
      component: () => import('../views/services/CaseSharing.vue'),
      meta: { title: '案例分享', requiresAuth: true }
    },
    {path: '/expert-team',
      name: 'ExpertTeam',
      component: () => import('../views/services/ExpertTeam.vue'),
      meta: { title: '专家团队', requiresAuth: true }
    },
    {path: '/faq',
      name: 'FAQ',
      component: () => import('../views/services/FAQ.vue'),
      meta: { title: '常见问题' }
    },
    {path: '/external-content',
      name: 'ExternalContent',
      component: () => import('../views/ExternalContent.vue'),
      meta: { title: '外部内容', requiresAuth: true }
    },
    {path: '/:pathMatch(.*)', redirect:'/notFound'},
  ],
})

export default router
