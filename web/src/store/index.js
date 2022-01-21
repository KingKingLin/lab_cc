import Vue from 'vue'
import Vuex from 'vuex'
import userModule from './user.js'
import teacherModule from './teacher.js'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    m_user: userModule,
    m_teacher: teacherModule
  }
})
