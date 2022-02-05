<template>
    <div class="my-homework-container">
        <div v-for="(item, i) in homework" :key="i" class="my-homework-item">
            <!-- 题目区域 -->
            <div class="my-homework-item-title goto-correct">
                <div>
                    题目 {{i + 1}}
                </div>
                <div v-if="deadline !== null" class="red">
                    {{new Date() > new Date(deadline) ? '已截止' : deadline + ' 截止'}}
                </div>
            </div>
            <div v-if="item.contentType === 'image'" class="my-homework-item-common">
                <img :src="item.content"/>
            </div>
            <div v-else-if="item.contentType === 'video'" class="my-homework-item-common">
                <div style="width: 80%;">
                    <vue-core-video-player :src="item.content"></vue-core-video-player>
                </div>
            </div>
            <div v-else-if="item.contentType === 'html'" class="my-homework-item-common">
                <p v-html="item.content"></p>
            </div>
            <div v-else-if="item.contentType === 'txt'">
                <p class="my-homework-item-txt">{{item.content}}</p>
            </div>
            <!-- 答案区域 -->
            <div style="margin-bottom: 6px;">
                <div class="my-homework-item-title goto-correct">
                    <div>
                        回答
                    </div>
                    <div v-if="type === 1"> <!-- 教师才显示 -->
                        <a href="cfree://" style="color: red;">去评阅</a>
                    </div>
                </div>
                <el-input
                        type="textarea"
                        :rows="10"
                        v-model="item.result">
                </el-input>
            </div>
            <!-- 教师评语区域 -->
            <div v-if="item.correct || type === 1">
                <div class="my-homework-item-title red goto-correct">
                    <div>
                        教师评语
                    </div>
                    <el-button type="text" v-if="type === 0" @click="showStandard(item, i)">查看标准答案</el-button>
                </div>
                <div class="my-homework-item-txt red" v-if="type === 0">{{item.correct}}</div>
                <div style="margin-bottom: 6px" v-if="type === 1">
                    <el-input
                            type="textarea"
                            :rows="10"
                            v-model="item.correct">
                    </el-input>
                </div>
            </div>
            <div style="display: flex; justify-content: space-around;" v-if="(type === 0 && item.redo !== false && deadline !== null && new Date() <= new Date(deadline)) || type === 1">
                <!-- 提交按钮 -->
                <el-button icon="el-icon-edit" round @click="submit(item)">{{type === 0 ? '提交答案' : '提交评语'}}</el-button>
            </div>
        </div>

        <!-- 答案区域-->
        <el-drawer
                :title="'题目 ' + index + ' 的标准答案'"
                :visible.sync="drawer"
                size="50%">
            <!-- 标准答案内容 -->
            <div v-if="item.standard" class="my-homework-standard">
                <div v-if="item.standardType === 'image'">
                    <img :src="item.standard" width="100%"/>
                </div>
                <div v-else-if="item.standardType === 'video'">
                    <div style="width: 80%;">
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
            <el-empty v-else :image-size="100" :image="empty" description="还未发布标准答案"></el-empty>
        </el-drawer>
    </div>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'my-homework',
        computed: {
            ...mapState('m_user', ['type'])
        },
        data() {
            return {
                drawer: false,
                empty: '',
                item: {},
                index: null
            }
        },
        props: {
            homework: {
                type: Array,
                default: () => []
            },
            deadline: {
                type: String,
                default: () => null
            }
        },
        mounted() {
            /*// console.log("屏幕可用空间: ", screen.availHeight)
            const item = document.getElementById("my-homework-container")
            item.style.height = screen.availHeight + 'px'*/
            this.empty = process.env.VUE_APP_SERVER + "/image/empty.png"
        },
        methods: {
            submit(item) {
                this.$emit('click', item)
            },
            showStandard(item, i) {
                this.drawer = true
                this.item = item
                this.index = i + 1
            }
        }
    }
</script>

<style>
    .my-homework-container {
        width: 100%;
    }

    .my-homework-item {
        margin-bottom: 20px;
        /*border: 1px solid rgb(212, 212, 213);*/
    }

    .my-homework-item-title {
        font-size: 15px;
        font-weight: bold;
        margin-bottom: 6px;
        background-color: rgb(244, 243, 245);
        padding: 8px 18px 8px;
        border: 1px solid rgb(212, 212, 213);
    }

    .goto-correct {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .red {
        color: red !important;
        margin-bottom: 6px !important;
    }

    .my-homework-item-txt {
        font-size: 15px;
        color: #1a1a1a;
        padding: 10px 20px;
        border: 1px solid rgb(212, 212, 213);
    }

    .my-homework-item-common {
        border: 1px solid rgb(212, 212, 213);
        /*text-align: center;*/
        padding: 10px 20px;
        margin-bottom: 6px;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .my-homework-standard {
        padding: 10px;
    }
</style>