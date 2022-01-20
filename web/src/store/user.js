export default {
    // 开启命名空间
    namespaced: true,
    state: {
        token: '',
        // user: {id: xx, name: xx}
        user: {},
        // 0 表示学生 1 表示教师
        type: 0
    },
    mutations: {
        setToken(state, token) {
            state.token = token
        },
        setUser(state, user) {
            state.user = user
        },
        setType(state, type) {
            state.type = type
        }
    }
}
