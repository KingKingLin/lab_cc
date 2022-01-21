import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/home.vue'
import Login from '../views/login.vue'
import MyTeach from '../views/teacher/manager/my-teach.vue'
import MyClass from '../views/teacher/manager/my-class.vue'
import store from '../store/index.js'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      loginRequire: true // 需要登录校验
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      loginRequire: false // 不需要登录校验
    }
  },
  {
    path: '/teacher/manager/teach',
    name: 'MyTeach',
    component: MyTeach,
    meta: {
      loginRequire: true // 需要登录校验
    }
  },
  {
    path: '/teacher/manager/class',
    name: 'MyClass',
    component: MyClass,
    meta: {
      loginRequire: true // 需要登录校验
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由登录拦截
// to 跳到哪一个路由
// from 旧路由
// next 是一个方法
router.beforeEach((to, from, next) => {
  if (to.matched.some(function (item) { // 如果返回 true，代表需要做路由的校验
    return item.meta.loginRequire;
  })) {
    if (store.state.m_user.token === '') {
      next('/login'); // 跳到登录页
    } else {
      next();
    }
  } else {
    next()
  }
});

export default router
