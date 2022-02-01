<template>
  <div id="app">
    <my-head v-if="token !== ''"></my-head>
    <router-view/>
  </div>
</template>

<script>
  import myHead from './components/my-head.vue'
  import { mapState } from 'vuex'

  export default {
    name: 'home',
    components: {
      myHead
    },
    computed: {
      ...mapState('m_user', ['token'])
    }
  }
</script>