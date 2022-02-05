<template>
    <div class="my-student-container">
        <div style="display: flex; margin-top: 10px;">
            <div class="my-student-aside">
                <div v-if="experiment.length !== 0" class="my-student-aside-container">
                    <div :class="['my-student-aside-item', i === activeIndex ? 'active' : '']" v-for="(item, i) in experiment" :key="i" @click="selectDetails(i)">
                        {{item.title}}
                    </div>
                </div>
                <el-empty :image-size="100" description="暂无任何实验信息" v-else></el-empty>
            </div>
            <div id="my-student-main">
                <my-homework :homework="homework" :deadline="deadline" @click="click"></my-homework>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex'
    import axios from "axios"
    import myHomework from '../../components/my-homework.vue'
    import moment from 'moment-timezone'

    export default {
        name: 'my-student',
        components: {
            myHomework
        },
        computed: {
            ...mapState('m_user', ['user'])
        },
        data() {
            return {
                experiment: [], // [eId: xxx, title: xxx, deadline: xxx]
                activeIndex: -1, // 选中的实验，默认展开最后一个实验
                homework: [], // 实验的题目列表
                deadline: null // 选中实验的截止时间
            };
        },
        async mounted() {
            const element = document.getElementById("my-student-main")
            console.log(screen.availHeight)
            element.style.height = (screen.availHeight - 180) + 'px'

            await this.getAllExperiments()
            const lastIndex = this.experiment.length - 1
            if (lastIndex === -1) {
                this.homework = []
                return
            }
            // 还需要做点事情
            await this.selectDetails(lastIndex)
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
                }
            },
            async selectDetails(i) {
                if (i < 0) return // 如果 i < 0 则返回

                this.activeIndex = i;
                const entity = this.experiment[this.activeIndex]
                const {data: res} = await axios.get('/student/homeworks', {
                    params: {
                        e_id: entity.eId,
                        s_id: this.user.id
                    }
                })
                if (res.success) { // 查询成功
                    this.homework = res.content.map(item => {
                        if (item.contentType === 'html') {
                            // doc 和 docx 都被转成了 html 文件
                            item.content = item.content.replace(/<img src="/g, '<img src="' + process.env.VUE_APP_SERVER + '/')
                        } else if (item.contentType === 'image' || item.contentType === 'video') {
                            // 图片 或 视频
                            item.content = process.env.VUE_APP_SERVER + '/' + item.content
                        }
                        if (item.standardType === 'html') {
                            // doc 和 docx 都被转成了 html 文件
                            item.standard = item.standard.replace(/<img src="/g, '<img src="' + process.env.VUE_APP_SERVER + '/')
                        } else if (item.standardType === 'image' || item.standardType === 'video') {
                            // 图片 或 视频
                            item.standard = process.env.VUE_APP_SERVER + '/' + item.standard
                        }
                        return item
                    })
                    // 格式化截止时间
                    const tz = 'Asia/Shanghai'  //时区
                    this.deadline = moment.utc(entity.deadline).tz(tz).format('YYYY-MM-DD HH:mm:ss')
                }
            },
            async click(item) {
                // console.log(this.user.name + "提交答案" + result)
                // 提交答案
                const {data: res} = await axios.post("/student/put-answer", {
                    sId: this.user.id,
                    hId: item.hId,
                    result: item.result
                })
                if (res.success) { // 如果提交答案成功，则重新请求数据
                    await this.selectDetails(this.activeIndex)
                    this.$message.success(res.message)
                } else {
                    this.$message.error(res.message)
                }
            }
        }
    }
</script>

<style scoped>
    .my-student-container {
        margin-top: 20px;
        margin-left: 20px;
        margin-right: 20px;
    }

    .my-student-aside {
        width: 222px;
        height: 493px;
        display: flex;
        flex-direction: column;
        align-items: center;
        /*border: 2px solid #efefef;*/
        margin-right: 10px;
    }

    .my-student-aside-container {
        border: 1px solid #efefef;
        overflow-y: scroll;
        height: 100%;
    }

    /* 修改滚动条容器的宽度 */
    .my-student-aside-container::-webkit-scrollbar {
        width: 1px;
        background-color: #F5F5F5;
    }

    /* 修改滚动条的样式 */
    .my-student-aside-container::-webkit-scrollbar-thumb {
        border-radius: 20px;
        background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0.44, rgb(122,153,217)), color-stop(0.72, rgb(73,125,189)), color-stop(0.86, rgb(28,58,148)));
    }

    .my-student-aside-item {
        width: 222px;
        height: 40px;
        font-size: 13px;
        line-height: 40px;
        text-align: center;
        color: rgb(96, 98, 102);
        background-color: #ffffff;
        /*background-color: lightblue;*/
        border-bottom: 1px solid #efefef;
        overflow: hidden;
        white-space: nowrap; /* 不换行显示 */
        text-overflow: ellipsis; /* 超过一行则显示... */
    }

    .active{
        color: rgb(64, 158, 255);
        background-color: rgb(236, 245, 255);
    }

    #my-student-main {
        width: 100%;
        overflow-y: scroll;
    }

    /* 修改题目区域的滚动条宽度 */
    #my-student-main::-webkit-scrollbar {
        width: 5px;
        background-color: #F5F5F5;
    }

    /* 修改滚动条的样式 */
    #my-student-main::-webkit-scrollbar-thumb {
        border-radius: 20px;
        background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0.44, rgb(122,153,217)), color-stop(0.72, rgb(73,125,189)), color-stop(0.86, rgb(28,58,148)));
    }
</style>