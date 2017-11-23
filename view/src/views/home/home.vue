<style lang="less">
    @import "./home.less";
    @import "../../styles/common.less";
</style>
<template>
    <div class="home-main">
        <access-ctrl :name="'SYS_USER_GET'" ref="access">
            <button>获取用户信息</button>
        </access-ctrl>

        <button @click="ctrlAccess()">控制权限</button>
    </div>
</template>

<script>
    // import { User } from '@/http';
    export default {
        name: 'home',

        mounted() {
            // User.getPower(this);
        },

        methods: {
            ctrlAccess() {
                this.$refs.access.updateAccess();
            }
        }
    };
</script>
