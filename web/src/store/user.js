export default {
    // 开启命名空间
    namespaced: true,
    state: {
        token: SessionStorage.get('token') || '',
        // user: {id: xx, name: xx, password: xx}
        user: SessionStorage.get('user') || {},
        // 0 表示学生 1 表示教师
        type: SessionStorage.get('type') || 0
    },
    mutations: {
        setToken(state, token) {
            state.token = token
            SessionStorage.set('token', token)
        },
        setUser(state, user) {
            state.user = user
            SessionStorage.set('user', user)
        },
        setType(state, type) {
            state.type = type
            SessionStorage.set('type', type)
        }
    }
}
