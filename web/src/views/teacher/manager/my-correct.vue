<template>
    <div style="margin-top: 20px; display: flex; justify-content: center; align-items: center;">
        <div class="my-correct-container">
            <my-homework :homework="homework" :deadline="deadline" @click="click"></my-homework>
        </div>
    </div>

</template>

<script>
    import myHomework from '../../../components/my-homework.vue'
    import axios from "axios"

    export default {
        name: 'my-correct',
        components: {
            myHomework
        },
        data() {
            return {
                homework: [],
                e_id: null,
                s_id: null,
                deadline: null
            }
        },
        async mounted() {
            // console.log(this.$route.query.e_id)
            // console.log(this.$route.query.s_id)
            this.e_id = this.$route.query.e_id
            this.s_id = this.$route.query.s_id
            await this.selectDetails()
            await this.getDeadline()
        },
        methods: {
            async selectDetails() {
                const {data: res} = await axios.get('/student/homeworks', {
                    params: {
                        e_id: this.e_id,
                        s_id: this.s_id
                    }
                })
                if (res.success) {
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
                }
            },
            async click(item) {
                // 提交答案
                const {data: res} = await axios.post("/teacher/put-correct", {
                    sId: this.s_id,
                    hId: item.hId,
                    correct: item.correct
                })
                if (res.success) { // 如果提交答案成功，则重新请求数据
                    await this.selectDetails()
                    this.$message.success(res.message)
                } else {
                    this.$message.error(res.message)
                }
            },
            async getDeadline() {
                const {data: res} = await axios.get('/teacher/get/experiment/'+this.e_id)
                if (res.success) {
                    this.deadline = res.content.deadline
                }
            }
        }
    }
</script>

<style>
    .my-correct-container {
        width: 80%;
    }
</style>