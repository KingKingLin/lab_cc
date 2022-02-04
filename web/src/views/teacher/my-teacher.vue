<template>
    <div>
        当前在线人数: {{onlines}}
    </div>
</template>

<script>
    import axios from "axios"
    import { mapState } from "vuex"

    export default {
        name: 'my-teacher',
        computed: {
            ...mapState('m_user', ['user'])
        },
        data() {
            return {
                onlines: 0
            };
        },
        async mounted() {
            this.getOnlines()

        },
        methods: {
            async getOnlines() {
                const {data: res} = await axios.get('/teacher/online-students', {
                    params: {
                        id: this.user.id
                    }
                })
                if (res.success) {
                    this.onlines = res.content
                }
            }
        }
    }
</script>