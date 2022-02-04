<template>
    <div>
        <div class="my-teach-container">
            <div class="my-teach-container-head">
                <el-button :disabled="JSON.stringify(show) === '{}'" @click="setDeadline" style="transform: translateX(63%);">新建实验</el-button>
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
                {{JSON.stringify(show) === '{}' ? '请选择上课的班级' : '切换班级'}}<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="pre" :disabled="page.pageNum === 1">上一页</el-dropdown-item>
                        <el-dropdown-item v-for="(item, i) in classes" :key="i" :command="item.id+','+i" :disabled="item.id === show.id">{{item.name}}</el-dropdown-item>
                        <el-dropdown-item command="next" :disabled="page.pageNum >= page.class_pages">下一页</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            <div style="display: flex; margin-top: 10px;">
                <div class="my-teach-aside">
                    <div v-if="experiment.length !== 0" class="my-teach-aside-container">
                        <div :class="['my-teach-aside-item', i === experimentIndex ? 'active' : '']" v-for="(item, i) in experiment" :key="i" @click="selectDetails(i)">
                            {{item.title}}
                        </div>
                    </div>
                    <el-empty :image-size="100" description="暂无任何实验信息" v-else></el-empty>
                </div>
                <div class="my-teach-main" v-if="students.length !== 0">
                    <div class="my-class-main-student-table">
                        <el-table
                                :data="students"
                                border
                                style="width: 100%;"
                                height="474">
                            <el-table-column
                                    prop="id"
                                    label="学号"
                                    width="170">
                            </el-table-column>
                            <el-table-column
                                    prop="name"
                                    label="姓名"
                                    width="170">
                            </el-table-column>
                            <el-table-column
                                    prop="results"
                                    label="实验完成情况"
                                    width="170">
                            </el-table-column>
                            <el-table-column
                                    prop="corrects"
                                    label="评阅情况"
                                    width="170">
                            </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button
                                            size="mini"
                                            @click="handle(scope.$index, scope.row)">去评阅</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <el-pagination
                            background
                            layout="prev, pager, next"
                            :total="page.student_total"
                            :page-size="page.size"
                            :pager-count="page.count"
                            @current-change="currentChange">
                    </el-pagination>
                </div>
                <div class="my-teach-upload" v-if="students.length !== 0">
                    <el-upload
                            class="upload-demo"
                            action
                            accept=".doc, .docx, .jpg, .png, .mp4, .txt"
                            :show-file-list="false"
                            :http-request="httpRequest"
                    >
                        <el-button>上传题目</el-button>
<!--                        <span style="font-size: 5px;">-->
<!--                            doc/docx/jpg/png/mp4/txt files-->
<!--                        </span>-->
                    </el-upload>
                    <router-link :to="'/teacher/manager/release-answer?e_id='+experiment[experimentIndex].eId"><el-button>发布答案</el-button></router-link>
                </div>
            </div>
        </div>
        <el-dialog
                title="设置实验的截止时间"
                :visible.sync="dialogVisible"
                width="30%"
                :before-close="handleClose">
            <div style="display: flex; flex-direction: row; align-items: center">
                <el-date-picker
                        v-model="deadline"
                        type="datetime"
                        placeholder="选择截止时间"
                        align="center"
                        :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
                <span style="margin-left: 20px; font-size: 10px">tips: 默认没有时间限制</span>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="addNewExperiment">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import { mapState, mapMutations } from 'vuex'
    import axios from "axios";

    export default {
        name: 'my-teach',
        computed: {
            ...mapState('m_user', ['user']),
            ...mapState('m_myTeach', ['show', 'experiment', 'experimentIndex'])
        },
        data() {
            return {
                // 班级列表数据
                classes: [],
                page: {
                    pageNum: 1,
                    size: 8,
                    class_pages: Number, // 总页数, 从后端获取
                    class_total: Number, // 总条目数，从后端获取
                    student_pages: Number, // 总页数, 从后端获取
                    student_total: Number, // 总条目数，从后端获取
                },
                dialogVisible: false, // 用来展示 【创建实验】 选择截止日期
                deadline: null, // 截止时间 Date()
                pickerOptions: {
                    shortcuts: [{
                        text: '今天',
                        onClick(picker) {
                            picker.$emit('pick', new Date());
                        }
                    }, {
                        text: '三天后',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24 * 3);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '五天后',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24 * 5);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '一周后',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', date);
                        }
                    }],
                },
                students: [],
            }
        },
        mounted() {
            // 初始时便加载班级信息
            this.getPartClasses()

            if (this.experimentIndex !== -1) this.selectDetails(this.experimentIndex)
        },
        methods: {
            ...mapMutations('m_myTeach', ['setExperiment', 'setShow', 'setExperimentIndex']),
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
                    this.page.class_total = res.content.total
                    this.page.class_pages = res.content.size
                } else {
                    this.classes = []
                    console.error("请求班级信息失败")
                }
            },
            // 选择上课的班级
            async handleCommand(e) {
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

                this.setShow(this.classes[Number(split[1])])

                await this.getAllExperiments(id)
            },
            // 得到所有的实验
            async getAllExperiments(id) {
                // /all-experiments/{c_id}
                const {data: res} = await axios.get('/teacher/all-experiments/' + id)
                if (res.success) {
                    this.setExperiment(res.content)
                    // 默认去展开最新的实验情况
                    const lastIndex = this.experiment.length - 1
                    // 模拟点击最新的实验情况
                    if (lastIndex === -1) { // 得到的是没有实验信息的
                        this.students = []
                        return
                    }
                    await this.selectDetails(lastIndex)
                }
            },
            // 设置截止时间
            setDeadline() {
                this.dialogVisible = true
                this.deadline = null
            },
            // 新增实验表
            async addNewExperiment() {
                // /teacher/add-experiment/8?title=实验1&deadline=
                this.dialogVisible = false
                if (JSON.stringify(this.show) === '{}') {
                    this.$message.error("请先选择要上课的班级")
                    return
                }
                const loading = this.$loading({
                    lock: true,
                    text: '请稍等片刻, 正在全力为您新建实验信息中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                const title = "实验" + (this.experiment.length + 1)
                const {data: res} = await axios.get('/teacher/add-experiment/' + this.show.id, {
                    params: {
                        title,
                        deadline: this.deadline
                    }
                })
                if (res.success) {
                    this.$message.success("【" + title + "】 创建成功")
                    loading.text = "正在为您全力加载数据..."
                    await this.getAllExperiments(this.show.id)
                } else {
                    this.$message.error(res.message)
                }
                loading.close(); // 不管创建与否都要关闭 loading 效果
            },
            // 关闭对话框的处理函数
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done(); // 隐藏对话框
                    })
                    .catch(_ => {});
            },
            async selectDetails(i) {
                this.setExperimentIndex(i)
                const {data: res} = await axios.get('/teacher/get-details', {
                    params: {
                        c_id: this.show.id,
                        e_id: this.experiment[this.experimentIndex].eId,
                        page: this.page.pageNum,
                        size: this.page.size
                    }
                })
                if (res.success) {
                    this.students = res.content.list

                    this.students.forEach(item => {
                        if (item.total === 0) {
                            item.corrects = "未发布题目"
                            item.results = "未发布题目"
                        } else {
                            item.corrects = (item.corrects / item.total * 100).toFixed(2) + " %"
                            item.results = (item.results / item.total * 100).toFixed(2) + " %"
                        }
                        return item
                    })

                    this.page.student_pages = res.content.size
                    this.page.student_total = res.content.total
                }
                console.log(res)
            },
            async currentChange(e) {
                this.page.pageNum = e
                await this.selectDetails(this.experimentIndex)
            },
            // 去评阅
            handle(index, row) {
                // console.log(index, row);
                this.$router.replace('/teacher/manager/correct?e_id=' + this.experiment[this.experimentIndex].eId + "&s_id=" + row.id)
            },
            // 发布答案 or 上传题目
            async httpRequest(e) {
                // 要以 表单的形式提交 才可以！！
                const form = new FormData();
                // 将 e_id 带上，即是那个实验的题目
                form.append('e_id', this.experiment[this.experimentIndex].eId)
                // 将上传的文件附加到表单上
                form.append('file', e.file)
                // console.log(form.get('file'))
                const {data: res} = await axios.post('/teacher/upload', form, {
                    headers: {'Content-Type': 'multipart/form-data'}
                })
                this.$message.success(res.message)
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
        align-items: center;
    }

    .my-teach-aside {
        width: 222px;
        height: 493px;
        display: flex;
        flex-direction: column;
        align-items: center;
        /*border: 2px solid #efefef;*/
    }

    .my-teach-aside-container {
        border: 1px solid #efefef;
        overflow-y: scroll;
        height: 100%;
    }

    /* 修改滚动条容器的宽度 */
    .my-teach-aside-container::-webkit-scrollbar {
        width: 1px;
        background-color: #F5F5F5;
    }

    /* 修改滚动条的样式 */
    .my-teach-aside-container::-webkit-scrollbar-thumb {
        border-radius: 20px;
        background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0.44, rgb(122,153,217)), color-stop(0.72, rgb(73,125,189)), color-stop(0.86, rgb(28,58,148)));
    }

    .my-teach-aside-item {
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

    .my-teach-main {
        margin-left: 10px;
        margin-bottom: 2px;
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .my-teach-upload {
        margin-left: 10px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
</style>