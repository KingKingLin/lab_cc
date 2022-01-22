<template>
    <my-student v-if="type === 0"></my-student>
    <my-teacher v-else></my-teacher>
</template>

<script>
    import myStudent from './student/my-student.vue'
    import myTeacher from './teacher/my-teacher.vue'
    import { mapState } from 'vuex'

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