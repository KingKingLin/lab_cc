<template>
    <div>
        <div class="el-container my-class-container">
            <div class="my-class-aside">
                <button class="my-class-aside-button" @click="addNewClass">添加班级</button>
                <div v-if="classes.length !== 0" class="my-class-aside-container">
                    <div :class="['my-class-aside-item', i === classesIndex ? 'active' : '']" v-for="(item, i) in classes" :key="i" @click="getStudents(i, item.id)">
                        {{item.name}}
                    </div>
                </div>
                <el-empty :image-size="100" description="暂无任何班级信息" v-else></el-empty>
            </div>
            <div class="my-class-main">
                <div v-if="classesIndex !== -1">
                    <div class="my-class-main-head">
                        <el-popconfirm
                                confirm-button-text='确定'
                                cancel-button-text='取消'
                                confirm-button-type="danger"
                                cancel-button-type="primary"
                                @confirm="dropClass"
                                icon="el-icon-info"
                                icon-color="red"
                                title="此操作后，将不可恢复，确定继续？"
                        >
                            <el-button slot="reference" type="danger" plain>删除班级</el-button>
                        </el-popconfirm>
                        <el-upload
                                class="upload-demo"
                                action
                                accept=".xlsx, .xls"
                                :show-file-list="false"
                                :http-request="httpRequest"
                        >
                            <button class="my-class-main-head-button">导入学生信息</button>
                            <template #tip>
                                <div class="el-upload__tip">
                                    xlsx/xls files
                                </div>
                            </template>
                        </el-upload>
                    </div>
                    <div v-if="students.length !== 0" style="display: flex; flex-direction: column; align-items: center">
                        <div style="margin-bottom: 5px">
                            <div class="my-class-main-student-table">
                                <el-table
                                        :data="students"
                                        border
                                        style="width: 100%;"
                                        height="434">
                                    <el-table-column
                                            prop="id"
                                            label="学号"
                                            width="180">
                                    </el-table-column>
                                    <el-table-column
                                            prop="name"
                                            label="姓名"
                                            width="180">
                                    </el-table-column>
                                </el-table>
                            </div>
                        </div>
                        <el-pagination
                                background
                                layout="prev, pager, next"
                                :total="page.total"
                                :page-size="page.size"
                                :pager-count="page.count"
                                @current-change="currentChange">
                        </el-pagination>
                    </div>
                    <el-empty :image-size="100" description="暂无学生信息, 请先导入！" v-else></el-empty>
                </div>
                <el-empty :image-size="100" description="请先选择对应的班级" v-else></el-empty>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from "axios";
    import { mapState, mapMutations } from 'vuex';
    import xlsxTools from '../../../mixins/XLSXTools.js'

    export default {
        name: 'my-class',
        mixins: [xlsxTools],
        computed: {
            ...mapState('m_user', ['user']),
            ...mapState('m_myClass', ['classes', 'classesIndex'])
        },
        data() {
            return {
                students: [],
                page: {
                    pageNum: 1,
                    size: 8,
                    count: 5,
                    total: Number, // total 是后端返回的
                }
            };
        },
        mounted() {
            // 初始时便加载班级信息
            this.getClasses()

            if (this.classesIndex !== -1) this.getStudents(this.classesIndex, this.classes[this.classesIndex].id)
        },
        methods: {
            ...mapMutations('m_myClass', ['setClassesIndex', 'setClasses']),
            // 请求 classes[] 数据
            async getClasses() {
                console.log("正在请求班级信息")
                const {data: res} = await axios.get('/teacher/all-classes', {
                    params: {
                        id: this.user.id
                    }
                })
                if (res.success) {
                    this.setClasses(res.content)
                } else {
                    this.setClasses([])
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
                    const {data: res} = await axios.get('/teacher/add-class', {
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
            async getStudents(i, id) {
                this.setClassesIndex(i)
                // 查询学生信息
                const {data: res} = await axios.get('/student/part/' + id, {
                    params: {
                        page: this.page.pageNum,
                        size: this.page.size
                    }
                })
                if (res.success) {
                    this.students = res.content.list
                    this.page.total = res.content.total
                }
            },
            dropClass() {
                console.log("删除班级")
            },
            async httpRequest(e) {
                // /student/addStudent/1?id=xxx&name=xxx
                const file = e.file
                // 用 dom 的 FileReader 以二进制形式读取该 file 文件
                const data = await xlsxTools.method.readFile(file)
                // 用 xlsx 工具以二进制形式解析该 data 数据, 最后以 JSON 形式返回
                let result = xlsxTools.method.analyse(data)
                // 最后进行属性转化, 将 中文 转换成 英文, 便于传递给后端使用
                result = xlsxTools.method.changeCharacters(xlsxTools.data().characters, result)
                console.log(result)
                console.log("正在请求添加学生信息")
                const id = this.classes[this.classesIndex].id
                const loading = this.$loading({
                    lock: true,
                    text: '请稍等片刻, 正在全力为您导入学生信息中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                for (const params of result) {
                    const {data: res} = await axios.get('/student/add/' + id, {
                        params
                    })
                    if (!res.success) {
                        this.$message.error(res.message)
                    }
                }
                loading.text = "正在为您全力加载数据..."
                // 遍历完, 如果没有错误, 则请求所有的学生数据
                await this.getStudents(this.classesIndex, id)
                loading.close(); // 不管创建与否都要关闭 loading 效果
            },
            async currentChange(e) {
                // console.log("页码: " + e)
                // console.log("currentChange")
                this.page.pageNum = e
                await this.getStudents(this.classesIndex, this.classes[this.classesIndex].id)
            }
        }
    }
</script>

<style>
    .my-class-container {
        margin-top: 20px;
        margin-left: 20px;
        margin-right: 20px;
    }

    .my-class-aside {
        width: 222px;
        height: 549px;
        display: flex;
        flex-direction: column;
        align-items: center;
        /*border: 2px solid #efefef;*/
        margin-right: 10px;
    }

    .my-class-aside-button {
        width: 100%;
        height: 50px;
        color: rgb(96, 98, 102);
        background-color: white;
        border-radius: 18px;
        border: 1px solid #efefef;
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

    /*.my-class-aside-item:hover {*/
    /*    color: rgb(64, 158, 255);*/
    /*    background-color: rgb(236, 245, 255);*/
    /*}*/

    .my-class-main {
        width: 100%;
    }

    .my-class-main-head {
        width: 100%;
        height: 45px;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin-bottom: 10px;
    }

    .my-class-main-head-button {
        width: 200px;
        color: rgb(96, 98, 102);
        background-color: white;
        border-radius: 18px;
        border: 1px solid #efefef;
    }

    .my-class-aside-button:hover, .my-class-main-head-button:hover, .active{
        color: rgb(64, 158, 255);
        background-color: rgb(236, 245, 255);
    }

    .el-upload__tip {
        transform: translateX(33.33%);
    }
</style>