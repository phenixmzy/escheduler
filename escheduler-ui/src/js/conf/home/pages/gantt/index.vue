<template>
  <div class="main-layout-box">
    <m-secondary-menu :type="'gantt'"></m-secondary-menu>
    <router-view></router-view>
  </div>

</template>
<script>
  import mSecondaryMenu from '@/module/components/secondaryMenu/secondaryMenu'
  export default {
    name: 'gantt-index',
    components: { mSecondaryMenu }
  }
</script>