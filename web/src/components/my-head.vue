<template>
    <el-container>
        <el-header>
            <img src="../assets/csuft.jpg" alt="图片失联中..." class="logo"/>
            <el-menu :default-active="navigateTo" class="el-menu-demo" mode="horizontal" router @select="handleSelect" v-if="type === 1">
                <el-menu-item index="/">首页</el-menu-item>
                <el-menu-item index="/teacher/manager/teach">教学管理</el-menu-item>
                <el-menu-item index="/teacher/manager/class">班级管理</el-menu-item>
            </el-menu>
            <div class="el-breadcrumb">
                <span>欢迎 {{user.name}} {{type === 0 ? '同学' : '老师'}}</span>
                <el-divider direction="vertical"></el-divider>
                <el-button type="text" @click="resetPassword" style="color: black;">修改密码</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-button type="text" @click="logout" style="color: black;">退出登录</el-button>
            </div>
        </el-header>
        <my-resetpassword @sendMessage="sendMessage" ref="child"></my-resetpassword>
    </el-container>
</template>

<script>
    import { mapState, mapMutations } from 'vuex'
    import myResetpassword from '../components/my-resetpassword.vue'
    import axios from "axios"

    export default {
        name: 'my-head',
        components: {
            myResetpassword
        },
        computed: {
            ...mapState('m_user', ['user', 'type', 'token']),
            ...mapState('m_teacher', ['navigateTo'])
        },
        mounted() {
            // WebSocket
            if ('WebSocket' in window) {
                // 链接地址: ws//127.0.0.1:8880/ws/xxx
                const websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + this.token);
                this.initWebSocket(websocket);

                // 关闭
                // websocket.close()
            } else {
                alert('当前浏览器 不支持');
            }
        },
        methods: {
            ...mapMutations('m_teacher', ['setActiveIndex']),
            ...mapMutations('m_user', ['setUser', 'type']),
            handleSelect(key, keyPath) {
                this.setActiveIndex(key)
            },
            resetPassword() {
                this.$refs.child.dialogVisible = true
            },
            async logout() {
                console.log("退出登录")
                SessionStorage.clearAll()
                // await this.$router.replace("/login") // 路由到 '/login' 页面, 仍存在显示问题, 是因为每个页面都有缓存
                location.reload() // 强制刷新页面, 清除页面缓存
                // console.log("user = " + JSON.stringify(this.user))
                // console.log("navigateTo = " + this.navigateTo)
            },
            async sendMessage(user) {
                if (this.type === 0) { // 学生
                    const {data: res} = await axios.post('/student/reset-password?id='+user.id+'&name='+user.name+'&password='+user.password)
                    this.tips(res, user)
                } else if (this.type === 1) { // 教师
                    const {data: res} = await axios.post('/teacher/reset-password?id='+user.id+'&name='+user.name+'&password='+user.password)
                    this.tips(res, user)
                }
            },
            tips(res, user) {
                if (res.success) {
                    // 密码修改成功
                    this.setUser(user)
                    this.$message.success(res.message)
                } else {
                    this.$message.error(res.message)
                }
            },
            initWebSocket(websocket) {
                // 连接成功
                websocket.onopen = () => {
                    console.log('WebSocket连接成功, 状态码: ', websocket.readyState);
                }
                // 收到消息的回调
                websocket.onmessage = (event) => {
                    console.log('WebSocket收到消息: ', event.data);
                    // 收到消息
                }
                // 连接失败
                websocket.onerror = () => {
                    console.log('WebSocket连接错误, 状态码: ', websocket.readyState);
                }
                // 连接关闭
                websocket.onclose = () => {
                    console.log('WebSocket连接关闭, 状态码: ', websocket.readyState);
                }
            }
        }
    }
</script>

<style>
    .el-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .logo {
        height: 100%;
    }
</style>