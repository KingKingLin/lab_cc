import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';

Vue.config.productionTip = false
Vue.use(ElementUI);

axios.defaults.baseURL = process.env.VUE_APP_SERVER; // 即，后端接口http://localhost:8880

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')