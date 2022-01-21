<template>
    <el-container>
        <el-header>
            <img src="../../assets/csuft.jpg" alt="图片失联中..." class="logo"/>
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" router @select="handleSelect">
                <el-menu-item index="/">首页</el-menu-item>
                <el-menu-item index="/teacher/manager/teach">教学管理</el-menu-item>
                <el-menu-item index="/teacher/manager/class">班级管理</el-menu-item>
            </el-menu>
            <div class="el-breadcrumb">
                <span>欢迎 {{user.name}} 老师</span>
                <el-divider direction="vertical"></el-divider>
                <span @click="resetPassword">修改密码</span>
                <el-divider direction="vertical"></el-divider>
                <span @click="logout">退出登录</span>
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
            ...mapState('m_user', ['user']),
            ...mapState('m_teacher', ['activeIndex'])
        },
        methods: {
            ...mapMutations('m_teacher', ['setActiveIndex']),
            ...mapMutations('m_user', ['setUser']),
            handleSelect(key, keyPath) {
                this.setActiveIndex(key)
            },
            resetPassword() {
                this.$refs.child.dialogVisible = true
            },
            async logout() {
                console.log("退出登录")
                SessionStorage.clearAll()
                await this.$router.replace("/login")
                // window.location.reload()
            },
            async sendMessage(teacher) {
                // console.log(teacher)
                const {data: res} = await axios.post('/teacher/reset-password?id='+teacher.id+'&name='+teacher.name+'&password='+teacher.password)
                if (res.success) {
                    // 密码修改成功
                    this.setUser(teacher)
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