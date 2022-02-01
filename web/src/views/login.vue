<template>
    <div class="login-container">
        <div class="login-item">
            <!-- 中南林的logo-->
            <img src="../assets/csuft.jpg" alt="图片失联中..."/>
            <!-- 教工号 或 学号 -->
            <div class="info">
                <div class="pad">
                    <i class="el-icon-user"></i>
                </div>
                <el-input
                        :placeholder="type === 0 ? '学号' : '教工号'"
                        v-model="id"
                        clearable>
                </el-input>
            </div>
            <!-- 密码 -->
            <div class="info">
                <div class="pad">
                    <i class="el-icon-lock"></i>
                </div>
                <el-input placeholder="密码, 默认: 000000" v-model="password" show-password></el-input>
            </div>
            <!-- 登录按钮 -->
            <div class="info">
                <el-dropdown @command="changeStatus" class="pad" size="small">
                    <span class="el-dropdown-link">
                        身份: {{status[selected]}}
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-for="(item, i) in status" :key="i" :disabled="i === selected" :command="i">{{item}}</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-button size="medium" @click="login">登录</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    import { mapMutations, mapState } from 'vuex'

    export default {
        name: 'login',
        data() {
            return {
                id: '20201269', // 20201269, 20010609
                password: 'lxy827543964', // lxy827543964, 000000
                status: ['学生', '教师'],
                selected: 0
            };
        },
        computed: {
            ...mapState('m_user', ['type'])
        },
        methods: {
            ...mapMutations('m_user', ['setToken', 'setUser', 'setType']),
            async login() {
                if (this.id.length !== 8) {
                    this.$message({
                        message: '学号不符合规则',
                        type: 'warning'
                    })
                    return
                }
                if (this.password.length < 6 || this.password.length > 18) {
                    this.$message({
                        message: '密码在需6~18位字符之间',
                        type: 'warning'
                    })
                    return
                }
                // console.log('退出登录后 type = ' + this.type)
                if (this.type === 0) { // 学生登录
                    const {data: res} = await axios.post('/student/login?id='+this.id+'&password='+this.password)
                    if (!res.success) { // 登陆失败
                        this.$message({
                            message: res.message,
                            type: 'warning'
                        })
                        return
                    }
                    await this.storeUser(res)
                } else { // 教师登录
                    const {data: res} = await axios.post('/teacher/login?id='+this.id+'&password='+this.password)
                    if (!res.success) { // 登陆失败
                        this.$message({
                            message: res.message,
                            type: 'warning'
                        })
                        return
                    }
                    await this.storeUser(res)
                }
            },
            async storeUser(res) {
                console.log("登录成功")
                // 否则登录成功
                this.setToken(res.content.token)
                this.setUser({
                    id: res.content.id,
                    name: res.content.name,
                    password: res.content.password
                })
                this.setType(res.content.type)
                // this.$router.push("/") // 路由跳转到首页 "/"
                await this.$router.replace("/") // 路由跳转到首页 "/"
            },
            changeStatus(i) {
                // console.log(i)
                this.selected = i
                this.setType(i)
            }
        }
    }
</script>

<style>
    .login-container {
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: #efefef;
    }
    .login-item {
        /*让 div 居中*/
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        bottom: 0;
        margin: auto;
        /*设置子元素布局*/
        width: 450px;
        height: 300px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-around;
        background-color: #ffffff;
        box-shadow: #909090 2px 2px 10px; /* 边框阴影 */
    }
    .login-item img {
        width: 400px;
    }
    .info {
        display: flex;
        align-items: center;
    }
    .pad {
        padding-right: 10px;
    }
</style>