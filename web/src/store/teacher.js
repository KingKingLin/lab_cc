export default {
    // 开启命名空间
    namespaced: true,
    state: {
        navigateTo: SessionStorage.get("navigateTo") || '/' // 初始指向首页
    },
    mutations: {
        setActiveIndex(state, navigateTo) {
            state.navigateTo = navigateTo
            SessionStorage.set("navigateTo", navigateTo)
        }
    }
}