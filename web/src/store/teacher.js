export default {
    // 开启命名空间
    namespaced: true,
    state: {
        activeIndex: SessionStorage.get("activeIndex") || '/' // 初始指向首页
    },
    mutations: {
        setActiveIndex(state, activeIndex) {
            // console.log("修改 activeIndex")
            state.activeIndex = activeIndex
            SessionStorage.set("activeIndex", activeIndex)
        }
    }
}