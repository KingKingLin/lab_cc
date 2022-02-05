<template>
    <div class="my-release-answer-container">
        <div v-for="(item, i) in homework" :key="i" style="width: 80%;">
            <!-- 题目区域 -->
            <div class="my-release-answer-title">
                题目 {{i + 1}}
            </div>
            <div class="my-release-answer-content">
                <!-- 题目内容 -->
                <div class="my-release-answer-common my-margin">
                    <div v-if="item.contentType === 'image'">
                        <img :src="item.content" width="100%"/>
                    </div>
                    <div v-else-if="item.contentType === 'video'">
                        <div class="my-release-answer-video">
                            <vue-core-video-player :src="item.content"></vue-core-video-player>
                        </div>
                    </div>
                    <div v-else-if="item.contentType === 'html'">
                        <p style="font-size: 15px; font-weight: bold;" v-html="item.content"></p>
                    </div>
                    <div v-else-if="item.contentType === 'txt'">
                        <p>{{item.content}}</p>
                    </div>
                </div>
                <!-- 标准答案内容 -->
                <div v-if="item.standard" class="my-release-answer-common">
                    <div v-if="item.standardType === 'image'">
                        <img :src="item.standard" width="100%"/>
                    </div>
                    <div v-else-if="item.standardType === 'video'">
                        <div class="my-release-answer-video">
                            <vue-core-video-player :src="item.content"></vue-core-video-player>
                        </div>
                    </div>
                    <div v-else-if="item.standardType === 'html'">
                        <p v-html="item.standard"></p>
                    </div>
                    <div v-else-if="item.standardType === 'txt'">
                        <p style="font-size: 15px; font-weight: bold;">{{item.standard}}</p>
                    </div>
                </div>
                <div style="width: 48%; align-items: center;" v-else>
                    <el-empty :image-size="100" :image="empty" description="还未发布标准答案"></el-empty>
                </div>
            </div>
            <div style="display: flex; flex-direction: column; align-items: center; margin-bottom: 10px;">
                <el-upload
                        class="upload-demo"
                        action
                        accept=".doc, .docx, .jpg, .png, .mp4, .txt"
                        :show-file-list="false"
                        :http-request="httpRequest"
                >
                    <el-button @click="upload(item.hId)">{{item.standard === null ? '发布' : '修改'}}答案</el-button>
                    <!-- <span style="font-size: 5px;">-->
                    <!--  doc/docx/jpg/png/mp4/txt files-->
                    <!-- </span>-->
                </el-upload>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        name: 'my-release-answer',
        data() {
            return {
                e_id: null,
                homework: [],
                h_id: null,
                empty: ''
            }
        },
        mounted() {
            this.e_id = this.$route.query.e_id
            this.empty = process.env.VUE_APP_SERVER + "/image/empty.png"
            this.getHomeworks()
        },
        methods: {
            async getHomeworks() {
                const {data: res} = await axios.get('/teacher/get-homeworks', {
                    params: {
                        e_id: this.e_id
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
                }
            },
            upload(h_id) {
                this.h_id = h_id
            },
            // 发布答案 or 上传题目
            async httpRequest(e) {
                // 要以 表单的形式提交 才可以！！
                const form = new FormData();
                // 将 e_id 带上，即是那个实验的题目
                form.append('h_id', this.h_id)
                // 将上传的文件附加到表单上
                form.append('file', e.file)
                // console.log(form.get('file'))
                const {data: res} = await axios.post('/teacher/release', form, {
                    headers: {'Content-Type': 'multipart/form-data'}
                })
                if (res.success) {
                    this.$message.success(res.message)
                    await this.getHomeworks()
                } else {
                    this.$message.error(res.message)
                }
            }
        }
    }
</script>

<style>
    .my-release-answer-container {
        margin-top: 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .my-release-answer-title {
        margin-bottom: 6px;
        background-color: rgb(244, 243, 245);
        padding: 8px 18px 8px;
        border: 1px solid rgb(212, 212, 213);
    }

    .my-release-answer-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .my-release-answer-common {
        width: 48%;
        border: 1px solid rgb(212, 212, 213);
        /*text-align: center;*/
        padding: 10px 20px;
        margin-bottom: 6px;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .my-margin {
        margin-right: 10px;
    }

    .my-release-answer-video {
        display: block !important;
        height: 300px;
    }
</style>