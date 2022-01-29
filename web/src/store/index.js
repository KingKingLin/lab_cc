import Vue from 'vue'
import Vuex from 'vuex'
import userModule from './user.js'
import teacherModule from './teacher.js'
import myTeachModule from './my-teach.js'
import myClassModule from './my-class.js'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    m_user: userModule,
    m_teacher: teacherModule,
    m_myTeach: myTeachModule,
    m_myClass: myClassModule
  }
})
