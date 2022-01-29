export default {
    // 开启命名空间
    namespaced: true,
    state: {
       // 暂存操作中的 experiment 信息 实验列表 {eId: xxx, title: xxx, deadline: xxx}
        experiment: SessionStorage.get('experiment') || [],
        // 选中的班级
        show: SessionStorage.get('show') || {}, // {id: xxx, name: xxx}
        // 选中的 experiment
        experimentIndex: SessionStorage.get('experimentIndex') || -1, // 请求到实验信息后，默认等于 experiment.length - 1，即最后一堂实验课
    },
    mutations: {
        setExperiment(state, experiment) {
            state.experiment = experiment
            SessionStorage.set("experiment", experiment)
        },
        setShow(state, show) {
            state.show = show
            SessionStorage.set("show", show)
        },
        setExperimentIndex(state, experimentIndex) {
            state.experimentIndex = experimentIndex
            SessionStorage.set("experimentIndex", experimentIndex)
        }
    }
}
