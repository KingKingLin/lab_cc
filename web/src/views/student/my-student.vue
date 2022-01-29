<template>
    <div>
        <my-head></my-head>
        <div style="display: flex; margin-top: 10px;">
            <div class="my-teach-aside">
                <div v-if="experiment.length !== 0" class="my-teach-aside-container">
                    <div :class="['my-teach-aside-item', i === activeIndex ? 'active' : '']" v-for="(item, i) in experiment" :key="i" @click="selectDetails(i)">
                        {{item.title}}
                    </div>
                </div>
                <el-empty :image-size="100" description="暂无任何实验信息" v-else></el-empty>
            </div>
        </div>
    </div>
</template>

<script>
    import myHead from '../../components/student/my-head.vue'
    import { mapState } from 'vuex'
    import axios from "axios";

    export default {
        name: 'my-student',
        components: {
            myHead
        },
        computed: {
            ...mapState('m_user', ['user'])
        },
        data() {
            return {
                experiment: [], // [eId: xxx, title: xxx, deadline: xxx]
                activeIndex: -1, // 选中的实验，默认展开最后一个实验
                homework: [] // 实验的题目列表
            };
        },
        mounted() {
            this.getAllExperiments()
        },
        methods: {
            async getAllExperiments() {
                const {data: res} = await axios.get('/student/all/experiments', {
                    params: {
                        id: this.user.id
                    }
                })
                if (res.success) {
                    this.experiment = res.content
                    const lastIndex = this.experiment.length - 1
                    if (lastIndex === -1) {
                        this.homework = []
                        return
                    }
                    // 还需要做点事情
                    this.selectDetails(lastIndex)
                }
            },
            selectDetails(i) {
                this.activeIndex = i;
            }
        }
    }
</script>

<style></style>