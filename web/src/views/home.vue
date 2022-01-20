<template>
    <my-student v-if="type === 0"></my-student>
    <my-teacher v-else></my-teacher>
</template>

<script>
    import { mapState } from 'vuex'
    import myStudent from './student/my-student.vue'
    import myTeacher from './teacher/my-teacher.vue'

    export default {
        name: 'home',
        components: {
            myStudent,
            myTeacher
        },
        computed: {
            ...mapState('m_user', ['type'])
        }
    }
</script>

<style></style>