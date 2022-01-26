<template>
    <div>
        <my-head></my-head>
        <div class="my-teach-container">
            <div class="my-teach-container-head">
                <button @click="addNewExperiment">新建实验</button>
                <span>{{show.name}}</span>
                <!-- 下拉列表，选择上课的班级，即需要一开始就去请求班级信息 -->
                <!--
                    用下拉列表的问题： 如果班级信息很多，会导致选项条超出页面范围？
                    如何解决？
                    用表格？分页请求班级数据？
                    或者下拉列表中添加一条记录：下一页，上一页，或者搜索功能
                -->
                <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                {{!show ? '请选择上课的班级' : '切换班级'}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="pre" :disabled="page.pageNum === 1">上一页</el-dropdown-item>
                        <el-dropdown-item v-for="(item, i) in classes" :key="i" :command="item.id+','+i" :disabled="item.id === show.id">{{item.name}}</el-dropdown-item>
                        <el-dropdown-item command="next" :disabled="page.pageNum === page.pages">下一页</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
    </div>
</template>

<script>
    import myHead from '../../../components/teacher/my-head.vue'
    import { mapState } from 'vuex'
    import axios from "axios";

    export default {
        name: 'my-teach',
        components: {
            myHead
        },
        computed: {
            ...mapState('m_user', ['user'])
        },
        data() {
            return {
                // 班级列表数据
                classes: [],
                // 选中的班级
                show: {}, // {id: xxx, name: xxx}
                experiment: [], // 实验列表 {eId: xxx, title: xxx, deadline: xxx}
                activeIndex: -1, // 请求到实验信息后，默认等于 experiment.length - 1，即最后一堂实验课
                page: {
                    pageNum: 1,
                    size: 10,
                    pages: Number, // 总页数, 从后端获取
                    total: Number, // 总条目数，从后端获取
                },
            }
        },
        mounted() {
            // 初始时便加载班级信息
            this.getPartClasses()
        },
        methods: {
            // 请求 classes[] 数据
            async getPartClasses() {
                console.log("正在请求班级信息")
                const {data: res} = await axios.get('/teacher/part-classes', {
                    params: {
                        id: this.user.id,
                        page: this.page.pageNum,
                        size: this.page.size
                    }
                })
                if (res.success) {
                    // this.classes = [...this.classes, ...res.content.list]
                    this.classes = res.content.list
                    this.page.total = res.content.total
                    this.page.pages = res.content.size
                } else {
                    this.classes = []
                    console.error("请求班级信息失败")
                }
            },
            // 选择上课的班级
            async handleCommand(e) {
                console.log(e)
                if (e === 'pre') { // 点击上一页
                    this.page.pageNum -= 1
                    await this.getPartClasses()
                    return
                }
                if (e === 'next') { // 下一页
                    this.page.pageNum += 1
                    await this.getPartClasses()
                    return
                }
                // 请求该班级的实验信息，如 实验一，实验二，实验三
                const split = String(e).split(',');
                const id = split[0]

                this.show = this.classes[Number(split[1])]

                await this.getAllExperiments(id)
            },
            // 得到所有的实验
            async getAllExperiments(id) {
                // /all-experiments/{c_id}
                const {data: res} = await axios.get('/teacher/all-experiments/' + id)
                console.log("班级【" + this.show.name + "】的所有实验记录", res)
                if (res.success) {
                    this.experiment = res.content
                    // 默认去展开最新的实验情况
                    this.activeIndex = this.experiment.length - 1
                    console.log("activeIndex: " + this.activeIndex)
                    // 模拟点击最新的实验情况
                    if (this.activeIndex !== -1) { // 即得到的实验列表不为空
                        console.log("do something... 默认展开最新的实验")
                    }
                }
            },
            // 新增实验表
            async addNewExperiment() {
                // /teacher/add-experiment/8?title=实验1&deadline=
                if (JSON.stringify(this.show) === '{}') return
                const {data: res} = await axios.get('/teacher/add-experiment/' + this.show.id, {
                    params: {
                        title: "实验" + (this.experiment.length + 1),
                        // deadline: 暂时不给了
                    }
                })
                if (res.success) {
                    await this.getAllExperiments(this.show.id)
                }
            }
        }
    }
</script>

<style>
    .my-teach-container {
        margin-top: 20px;
        margin-left: 20px;
        margin-right: 20px;
    }

    .my-teach-container-head {
        display: flex;
        justify-content: space-between;
    }
</style>