<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <CommonHeader />
  
  <div class="knowledge-base-container">
    <h1>心理健康知识库</h1>
    <p>这里是心理健康相关的知识和资源。</p>
    <ul class="knowledge-list">
      <li v-for="(item, index) in knowledgeItems" :key="index" class="knowledge-item">
        <h3>{{ item.title }}</h3>
        <p>{{ item.description }}</p>
        <a :href="item.link" target="_blank">阅读更多</a>
      </li>
    </ul>
  </div>
</template>

<script>
import CommonHeader from '@/components/CommonHeader.vue'

export default {
  name: 'KnowledgeBase',
  setup() {
    const knowledgeItems = [
      {
        title: '心理健康的重要性',
        description: '了解心理健康对个人生活和社会的影响。',
        link: 'https://m.baidu.com/bh/m/detail/ar_9932084358474389860'
      },
        {
          title: '如何管理压力',
          description: '学习有效的压力管理技巧，保持心理健康。',
          link: 'https://www.xinli001.com/info/100473887'
        },

        {
          title: '孤独，是最不可忽视的心理问题',
          description: '孤独，是我们每个人生命中不可或缺的一部分。',
          link: 'https://www.cnnmh.cn/article.php?aid=645'
        },
        {
          title: '给学生的4个健康心理调节建议',
          description: '给学生的4个健康心理调节建议，请看下面的详细内容：',
          link: 'https://www.cnnmh.cn/article.php?aid=635'
        },
        {
          title: '心理亚健康的五大表现',
          description: '心理亚健康的五大表现是：',
          link: 'https://www.cnnmh.cn/article.php?aid=627'
        },

    ];

    return {
      knowledgeItems
    };
  }
}
</script>

<style scoped>
.knowledge-base-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

h1 {
  font-size: 24px;
  color: #333;
}

p {
  font-size: 16px;
  color: #666;
}

.knowledge-list {
  list-style: none;
  padding: 0;
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.knowledge-item {
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: var(--item-color);
  transition: transform 0.3s ease;
}

.knowledge-item:nth-child(odd) {
  --item-color: #e0f7fa;
}

.knowledge-item:nth-child(even) {
  --item-color: #f1f8e9;
}

.knowledge-item:hover {
  transform: translateY(-5px);
}

.knowledge-item h3 {
  font-size: 20px;
  color: #333;
}

.knowledge-item p {
  font-size: 14px;
  color: #666;
}

.knowledge-item a {
  color: #007bff;
  text-decoration: none;
}

.knowledge-item a:hover {
  text-decoration: underline;
}
</style> 