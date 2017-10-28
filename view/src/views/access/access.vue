<style lang="less">
    @import '../../styles/common.less';
    @import './access.less';
</style>

<template>
    <div class="access">
        <Row>
            <Col span="8">
                <Card>
                    <p slot="title">
                        <Icon type="android-contact"></Icon>
                        当前用户
                    </p>
                    <div class="access-user-con access-current-user-con">
                        <img :src="avatorPath" alt="">
                    </div>
                </Card>
            </Col>
            <Col span="16" class="padding-left-10">
                <Card>
                    <p slot="title">
                        <Icon type="android-contacts"></Icon>
                        不同权限用户的不同菜单
                    </p>
                    <div class="access-user-con access-change-access-con">
                        <Col span="8">
                            <Row type="flex" justify="center" align="middle" class="access-change-access-con-row">
                                <i-switch :value="access.test" @on-change="changeAccess" size="large"></i-switch>
                            </Row>
                        </Col>
                        <Col span="8">
                            <Row type="flex" justify="center" align="middle" class="access-change-access-con-row">
                                <i-switch :value="access.test2" @on-change="changeAccess2" size="large"></i-switch>
                            </Row>
                        </Col>
                    </div>
                </Card>
            </Col>
        </Row>
    </div>
</template>

<script>
export default {
    name: 'access_index',
    data () {
        return {
            access: JSON.parse(localStorage.access || '{}')
        };
    },
    computed: {
        avatorPath () {
            return localStorage.avatorImgPath;
        }
    },
    methods: {
        changeAccess (res) {
            this.access.test = res;
            localStorage.access = JSON.stringify(this.access);
            this.$store.commit('updateMenulist');
        },

        changeAccess2 (res) {
            this.access.test2 = res;
            localStorage.access = JSON.stringify(this.access);
            this.$store.commit('updateMenulist');
        }
    }
};
</script>

<style>

</style>
