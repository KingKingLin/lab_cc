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
                        placeholder="学号"
                        v-model="id"
                        clearable>
                </el-input>
            </div>
            <!-- 密码 -->
            <div class="info">
                <div class="pad">
                    <i class="el-icon-lock"></i>
                </div>
                <el-input placeholder="密码" v-model="password" show-password></el-input>
            </div>
            <!-- 登录按钮 -->
            <div class="info">
                <el-button size="medium" @click="login">登录</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    import { mapMutations } from 'vuex'

    export default {
        name: 'login',
        data() {
            return {
                id: '20193306',
                password: '000000'
            };
        },
        methods: {
            ...mapMutations('m_user', ['setUser']),
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
                if (this.id.length === 8 && this.password.length !== 0) {
                    const {data: res} = await axios.post('/student/login?id='+this.id+'&password='+this.password)
                    if (!res.success) { // 登陆失败
                        this.$message({
                            message: '学号或密码错误',
                            type: 'warning'
                        })
                        return
                    }
                    // 否则登录成功
                    console.log("登录成功: ", res.content)
                    this.setUser(res.content)
                    // this.$router.push("/") // 路由跳转到首页 "/"
                    this.$router.replace("/") // 路由跳转到首页 "/"
                }
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