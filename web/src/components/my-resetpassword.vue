<template>
    <el-dialog
            title="修改密码"
            :visible.sync="dialogVisible"
            width="30%"
            :before-close="handleClose">
        <div>
            <div class="resetPassword-item">
                <div class="resetPassword-info">
                    <span>{{type === 0 ? '学号' : '教工号'}}: </span>
                </div>
                <el-input
                        suffix-icon="el-icon-user"
                        v-model="mine.id"
                        :disabled="true">
                </el-input>
            </div>
            <div class="resetPassword-item">
                <div class="resetPassword-info">
                    <span>姓名: </span>
                </div>
                <el-input
                        suffix-icon="el-icon-postcard"
                        v-model="mine.name"
                        :disabled="true">
                </el-input>
            </div>
            <div class="resetPassword-item">
                <div class="resetPassword-info">
                    <span>当前密码: </span>
                </div>
                <el-input
                        placeholder="当前密码"
                        suffix-icon="el-icon-key"
                        v-model="mine.pre_password"
                        show-password>
                </el-input>
            </div>
            <div class="resetPassword-item">
                <div class="resetPassword-info">
                    <span>新密码: </span>
                </div>
                <el-input
                        placeholder="新密码"
                        suffix-icon="el-icon-unlock"
                        v-model="mine.new_password"
                        show-password>
                </el-input>
            </div>
            <div class="resetPassword-item">
                <div class="resetPassword-info">
                    <span>确认密码: </span>
                </div>
                <el-input
                        placeholder="确认密码"
                        suffix-icon="el-icon-lock"
                        v-model="mine.confirm_password"
                        show-password>
                </el-input>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button size="small" @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" size="small" @click="confirm">确定</el-button>
            </span>
    </el-dialog>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'my-resetpassword',
        computed: {
            ...mapState('m_user', ['user', 'type'])
        },
        data() {
            return {
                mine: {
                    id: '',
                    name: '',
                    pre_password: '',
                    new_password: '',
                    confirm_password: ''
                },
                dialogVisible: false
            }
        },
        mounted() {
            this.mine.id = this.user.id
            this.mine.name = this.user.name
        },
        methods: {
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        // console.log("确认关闭")
                        // 关闭后将数据重置
                        this.mine.pre_password = ''
                        this.mine.new_password = ''
                        this.mine.confirm_password = ''
                        //
                        done();
                    })
                    .catch(_ => {});
            },
            confirm() {
                // console.log("确认修改密码, 参数校验中...")
                if (this.mine.id !== this.user.id || this.mine.name !== this.user.name || this.mine.pre_password !== this.user.password) {
                    if (this.type === 0) {
                        this.$message.error('学号或密码错误')
                    } else {
                        this.$message.error('教工号或密码错误')
                    }
                    return
                }
                if (this.mine.new_password.length === 0) {
                    this.$message({
                        message: '【新密码】不能为空',
                        type: 'warning'
                    })
                    return
                }
                if (!this.mine.new_password.match('^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$')) {
                    this.$message({
                        message: '【密码】至少包含 数字和英文，长度6-18',
                        type: 'warning'
                    })
                    return
                }
                if (this.mine.confirm_password.length === 0) {
                    this.$message({
                        message: '【确认密码】不能为空',
                        type: 'warning'
                    })
                    return
                }
                if (this.mine.new_password !== this.mine.confirm_password) {
                    this.$message({
                        message: '前后密码不一致',
                        type: 'warning'
                    })
                    return
                }
                // 确认无误后，修改密码
                this.$emit('sendMessage', {
                    id: this.mine.id,
                    name: this.mine.name,
                    password: this.mine.new_password
                })
                // 确认后，重置数据
                this.mine.pre_password = ''
                this.mine.new_password = ''
                this.mine.confirm_password = ''
                // 对话框消失
                this.dialogVisible = false
            }
        }
    }
</script>

<style>
    .resetPassword-item {
        display: flex;
        justify-content: space-around;
        padding-bottom: 10px;
    }

    .resetPassword-info {
        width: 100px;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }
</style>