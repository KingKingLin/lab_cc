export default {
    // 开启命名空间
    namespaced: true,
    state: {
        user: {}
    },
    mutations: {
        setUser(state, user) {
            state.user = user
        }
    }
}
