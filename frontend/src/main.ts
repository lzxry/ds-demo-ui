import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { localDB } from './utils/localDB'

// 初始化本地测试数据
localDB.init()

// 在开发环境中启用模拟数据
if (import.meta.env.DEV) {
  import('./mock')
    .then(() => console.log('已启用模拟数据'))
    .catch(err => console.error('模拟数据加载失败:', err))
}

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(ElementPlus)
app.mount('#app') 