<template>
    <div class="my-homework-container">
        <div v-for="(item, i) in homework" :key="i" class="my-homework-item">
            <!-- 题目区域 -->
            <div class="my-homework-item-title">
                题目 {{i + 1}}
            </div>
            <div v-if="item.contentType === 'image'" class="my-homework-item-common">
                <img :src="item.content"/>
            </div>
            <div v-else-if="item.contentType === 'video'">
                <video width="100%" height="240" controls>
                    <source :src="item.content"
                            type="video/mp4">
                </video>
            </div>
            <div v-else-if="item.contentType === 'html'" class="my-homework-item-common">
                <p v-html="item.content"></p>
            </div>
            <div v-else-if="item.contentType === 'txt'">
                <p class="my-homework-item-txt">{{item.content}}</p>
            </div>
            <!-- 答案区域 -->
            <div style="margin-bottom: 6px;">
                <div class="my-homework-item-title">
                    回答
                </div>
                <el-input
                        type="textarea"
                        :rows="10"
                        v-model="item.result">
                </el-input>
            </div>
            <!-- 教师评语区域 -->
            <div v-if="item.correct || type === 1">
                <div class="my-homework-item-title red">
                    教师评语
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
            <div style="display: flex; justify-content: space-around;" v-if="(type === 0 && item.redo !== false) || type === 1">
                <!-- 提交按钮 -->
                <el-button icon="el-icon-edit" round @click="submit(item)">确认提交</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'my-homework',
        computed: {
            ...mapState('m_user', ['type'])
        },
        props: {
            homework: {
                type: Array,
                default: () => []
            }
        },
        /*mounted() {
            // console.log("屏幕可用空间: ", screen.availHeight)
            const item = document.getElementById("my-homework-container")
            item.style.height = screen.availHeight + 'px'
        },*/
        methods: {
            submit(item) {
                this.$emit('click', item)
            }
        }
    }
</script>

<style>
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
        text-align: center;
        padding: 10px 20px;
        margin-bottom: 6px;
    }
</style>