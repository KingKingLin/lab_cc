export default {
    // 开启命名空间
    namespaced: true,
    state: {
        token: SessionStorage.get('user') || '',
        // user: {id: xx, name: xx}
        user: SessionStorage.get('user') || {},
        // 0 表示学生 1 表示教师
        type: SessionStorage.get('user') || 0
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
