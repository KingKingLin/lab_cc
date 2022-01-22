<template>
    <div>
        <my-head></my-head>
        <div class="el-container my-class-container">
            <div class="my-class-aside">
                <button class="my-class-aside-button" @click="addNewClass">添加班级</button>
                <div v-if="classes.length !== 0" class="my-class-aside-container">
                    <div :class="['my-class-aside-item', i === activeIndex ? 'active' : '']" v-for="(item, i) in classes" :key="i" @click="getStudents(i, item.id)">
                        {{item.name}}
                    </div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                    <div class="my-class-aside-item">xxxx</div>
                </div>
                <el-empty :image-size="100" description="暂无任何班级信息" v-else></el-empty>
            </div>
            <div class="my-class-main">
                Main
            </div>
        </div>
    </div>
</template>

<script>
    import myHead from '../../../components/teacher/my-head.vue'
    import axios from "axios";
    import { mapState } from 'vuex';

    export default {
        name: 'my-class',
        components: {
            myHead
        },
        computed: {
            ...mapState('m_user', ['user'])
        },
        data() {
            return {
                classes: [],
                activeIndex: -1
            };
        },
        mounted() {
            // 初始时便加载班级信息
            this.getClasses()
        },
        methods: {
            // 请求 classes[] 数据
            async getClasses() {
                console.log("正在请求班级信息")
                const {data: res} = await axios.get('/teacher/getAllClasses', {
                    params: {
                        id: this.user.id
                    }
                })
                if (res.success) {
                    this.classes = res.content
                } else {
                    this.classes = []
                    console.error("请求班级信息失败")
                }
            },
            addNewClass() {
                console.log("请求新建班级")
                this.$prompt('班级名', '新建班级', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(async ({value}) => {
                    const loading = this.$loading({
                        lock: true,
                        text: '请稍等片刻, 正在全力为您创建班级信息中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    const {data: res} = await axios.get('/teacher/addClasses', {
                        params: {
                            id: this.user.id,
                            name: value
                        }
                    })
                    if (res.success) { // 创建成功
                        this.$message({
                            type: 'success',
                            message: res.message
                        })
                        loading.text = "正在为您全力加载数据..."
                        // 创建成功后, 重新请求列表 classes 列表数据
                        await this.getClasses()
                    }
                    else {
                        this.$message({
                            type: 'error',
                            message: res.message
                        })
                    }
                    loading.close(); // 不管创建与否都要关闭 loading 效果
                }).catch(() => {
                    console.log("用户取消新建班级")
                })
            },
            getStudents(i, id) {
                console.log("需要有点击记录的组件的索引: ", i)
                this.activeIndex = i
                console.log("查询班级 "+ id +" 的学生列表: ")
            }
        }
    }
</script>

<style>
    .my-class-container {
        margin: 20px 20px;
    }

    .my-class-aside {
        width: 222px;
        height: 540px;
        display: flex;
        flex-direction: column;
        align-items: center;
        /*border: 2px solid #efefef;*/
        margin-right: 20px;
    }

    .my-class-aside-button {
        width: 100%;
        height: 50px;
        color: rgb(96, 98, 102);
        background-color: white;
        border-radius: 18px;
        border: 1px solid #efefef;
    }

    .my-class-aside-button:hover {
        color: rgb(64, 158, 255);
        background-color: rgb(236, 245, 255);
    }

    .my-class-aside-container {
        border: 1px solid #efefef;
        margin-top: 10px;
        overflow-y: scroll;
        height: 100%;
    }

    /* 修改滚动条容器的宽度 */
    .my-class-aside-container::-webkit-scrollbar {
        width: 1px;
        background-color: #F5F5F5;
    }

    /* 修改滚动条的样式 */
    .my-class-aside-container::-webkit-scrollbar-thumb {
        border-radius: 20px;
        background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0.44, rgb(122,153,217)), color-stop(0.72, rgb(73,125,189)), color-stop(0.86, rgb(28,58,148)));
    }

    .my-class-aside-item {
        width: 100%;
        height: 40px;
        font-size: 13px;
        line-height: 40px;
        text-align: center;
        color: rgb(96, 98, 102);
        background-color: #ffffff;
        /*background-color: lightblue;*/
        border-bottom: 1px solid #efefef;
        white-space: nowrap; /* 不换行显示 */
        text-overflow: ellipsis; /* 超过一行则显示... */
    }

    .my-class-aside-item:hover {
        color: rgb(64, 158, 255);
        background-color: rgb(236, 245, 255);
    }

    .my-class-main {
        width: 100%;
    }

    .active {
        color: rgb(64, 158, 255);
        background-color: rgb(236, 245, 255);
    }
</style>