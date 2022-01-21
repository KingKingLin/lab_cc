<template>
    <div>
        <my-head></my-head>
        <el-container>
            <el-aside width="200px">Aside</el-aside>
            <el-main>Main</el-main>
        </el-container>
    </div>
</template>

<script>
    import myHead from '../../../components/teacher/my-head.vue'

    export default {
        name: 'my-class',
        components: {
            myHead
        },
        data() {
            return {};
        },
        setup() {

        }
    }
</script>

<style>
    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
    }

    .el-main {
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
        line-height: 160px;
    }
</style>