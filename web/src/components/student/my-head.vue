<template>
    <el-container>
        <el-header>
            <img src="../../assets/csuft.jpg" alt="图片失联中..." class="logo"/>
            <div class="el-breadcrumb">
                <span>欢迎 {{user.name}} 同学</span>
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
    import myResetpassword from '../../components/my-resetpassword.vue'
    import axios from "axios";

    export default {
        name: 'my-head',
        components: {
            myResetpassword
        },
        computed: {
            ...mapState('m_user', ['user'])
        },
        methods: {
            ...mapMutations('m_user', ['setUser']),
            resetPassword() {
                this.$refs.child.dialogVisible = true
            },
            async logout() {
                console.log("退出登录")
                SessionStorage.clearAll()
                location.reload() // 强制刷新页面, 清除页面缓存
            },
            async sendMessage(student) {
                const {data: res} = await axios.post('/student/reset-password?id='+student.id+'&name='+student.name+'&password='+student.password)
                if (res.success) {
                    // 密码修改成功
                    this.setUser(student)
                    this.$message.success(res.message)
                } else {
                    this.$message.error(res.message)
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