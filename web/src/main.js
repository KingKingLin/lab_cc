import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';
import VueCoreVideoPlayer from 'vue-core-video-player'


Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueCoreVideoPlayer, {
  lang: 'zh-CN'
})

axios.defaults.baseURL = process.env.VUE_APP_SERVER; // 即，后端接口http://localhost:8880

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
