<template>
    <div>
        <my-head></my-head>
        <div class="my-teach-container">
            <!-- 下拉列表，选择上课的班级，即需要一开始就去请求班级信息 -->
            <!--
                用下拉列表的问题： 如果班级信息很多，会导致选项条超出页面范围？
                如何解决？
                用表格？分页请求班级数据？
                或者下拉列表中添加一条记录：下一页，上一页，或者搜索功能
            -->
            <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                {{activeIndex === -1 ? '请选择上课的班级' : classes[activeIndex].name}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="pre" :disabled="page.pageNum === 1">上一页</el-dropdown-item>
                    <el-dropdown-item v-for="(item, i) in classes" :key="i" :command="item.id+','+i" :disabled="i === activeIndex">{{item.name}}</el-dropdown-item>
                    <el-dropdown-item command="next" :disabled="page.pageNum === page.pages">下一页</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
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
                classes: [],
                activeIndex: -1,
                page: {
                    pageNum: 1,
                    size: 10,
                    pages: Number, // 总页数, 从后端获取
                    total: Number, // 总条目数，从后端获取
                }
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
                    this.classes = res.content.list
                    this.page.total = res.content.total
                    this.page.pages = res.content.size
                } else {
                    this.classes = []
                    console.error("请求班级信息失败")
                }
            },
            handleCommand(e) {
                console.log(e)
                if (e === 'pre') { // 点击上一页
                    this.page.pageNum -= 1
                    this.getPartClasses()
                    return
                }
                if (e === 'next') { // 下一页
                    this.page.pageNum += 1
                    this.getPartClasses()
                    return
                }
                // 请求改班级的实验信息，如 实验一，实验二，实验三
                const split = String(e).split(',');
                const id = split[0]
                this.activeIndex = Number(split[1])
            }
        }
    }
</script>

<style>
    .my-teach-container {
        margin-top: 20px;
    }
</style>