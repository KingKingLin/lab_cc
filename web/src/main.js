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

/**
 * axios 拦截器
 */
// 拦截请求
// config 就是请求信息
axios.interceptors.request.use(config => {
  console.log('请求参数: ', config);
  const token = store.state.m_user.token;
  if(token !== '') {
    config.headers.token = token;
    console.log("请求headers增加token: ", token);
  }
  return config;
}, error => {
  return Promise.reject(error);
});

axios.interceptors.response.use(response => {
  console.log('返回结果: ', response);
  return response;
}, error => { // 返回如果报错了
  return Promise.reject(error);
})

// 解决 axios 刷新，导致拦截器不起作用的bug
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
// 拦截返回
