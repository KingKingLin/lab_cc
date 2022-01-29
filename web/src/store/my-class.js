export default {
    // 开启命名空间
    namespaced: true,
    state: {
        classes: [],
        classesIndex: -1,
    },
    mutations: {
        setClasses(state, classes) {
            state.classes = classes
            SessionStorage.set("classes", classes)
        },
        setClassesIndex(state, classesIndex) {
            state.classesIndex = classesIndex
            SessionStorage.set("classesIndex", classesIndex)
        }
    }
}
