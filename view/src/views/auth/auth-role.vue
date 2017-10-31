<style lang="less">
    @import '../../styles/common.less';
    @import './auth.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                角色管理
            </div>
            <div slot="extra">
                <Button type="primary" @click="addRole()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="error" @click="deleteRoles()">
                    <Icon type="trash-a"></Icon>
                    删除
                </Button>
            </div>

            <Row :gutter="16">
                <Col span="5">
                    <Tree ref="roleTree" :data="roleData" show-checkbox @on-select-change="treeNodeSelect"></Tree>
                </Col>
                <Col span="19">
                    <Card>
                        <Row class="margin-bottom-10">
                            <Col span="3">
                                <div class="input-label">父级</div>
                            </Col>
                            <Col span="21">
                                <Select v-model="roleDetail.parent" filterable style="width: 250px">
                                    <Option v-for="item in roleList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                        </Row>
                        <Row class="margin-bottom-10">
                            <Col span="3">
                                <div class="input-label">名称</div>
                            </Col>
                            <Col span="21">
                                <Input v-model="roleDetail.name" placeholder="名称"></Input>
                            </Col>
                        </Row>
                        <Row class="margin-bottom-10">
                            <Col span="3">
                                <div class="input-label">生效</div>
                            </Col>
                            <Col span="21" style="line-height: 32px;">
                                <i-switch v-model="roleDetail.status">
                                    <span slot="open">是</span>
                                    <span slot="close">否</span>
                                </i-switch>
                            </Col>
                        </Row>
                        <Row>
                            <Col span="3">&nbsp;</Col>
                            <Col span="21">
                                <Button type="primary">
                                    修改
                                </Button>
                                <Button type="info" @click="authSet()">
                                    <Icon type="android-settings"></Icon>
                                    配置权限
                                </Button>
                                <Button type="error" @click="deleteRole()">
                                    <Icon type="trash-a"></Icon>
                                    删除
                                </Button>
                            </Col>
                        </Row>
                    </Card>
                </Col>
            </Row>
        </Card>

        <Modal
            v-model="addModal"
            title="新增角色"
            @on-ok="saveAdd">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="3">
                        <div class="input-label">父级</div>
                    </Col>
                    <Col span="21">
                        <Select v-model="addRoleDetail.parent" filterable style="width: 250px">
                            <Option v-for="item in roleList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                        </Select>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="3">
                        <div class="input-label">名称</div>
                    </Col>
                    <Col span="21">
                        <Input v-model="addRoleDetail.name" placeholder="名称"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="3">
                        <div class="input-label">生效</div>
                    </Col>
                    <Col span="21" style="line-height: 32px;">
                        <i-switch v-model="addRoleDetail.status">
                            <span slot="open">是</span>
                            <span slot="close">否</span>
                        </i-switch>
                    </Col>
                </Row>
            </div>
        </Modal>

        <Modal
            v-model="authModal"
            title="配置权限"
            @on-ok="saveAuth">
            <div class="modal-body">
                <Tree ref="authTree" :data="authData" show-checkbox></Tree>
            </div>
        </Modal>
    </div>
</template>

<script>
    export default {
        name: 'auth_role',
        data() {
            return {
                roleData: [
                    {
                        title: '超级管理员',
                        expand: false
                    },
                    {
                        title: '客服管理',
                        expand: false,
                        children: [
                            {
                                title: '售后客服',
                                expand: false
                            },
                            {
                                title: '售前客服',
                                expand: false
                            }
                        ]
                    }
                ],
                roleDetail: {
                    parent: '',
                    name: '',
                    status: true
                },
                addModal: false,
                addRoleDetail: {
                    parent: '',
                    name: '',
                    status: true
                },
                authModal: false,
                authData: [
                    {
                        title: '超级管理员',
                        expand: false
                    },
                    {
                        title: '客服管理',
                        expand: false,
                        children: [
                            {
                                title: '售后客服',
                                expand: false
                            },
                            {
                                title: '售前客服',
                                expand: false
                            }
                        ]
                    }
                ],
                roleList: [
                    {
                        value: 'beijing',
                        label: '北京市'
                    },
                    {
                        value: 'shanghai',
                        label: '上海市'
                    },
                    {
                        value: 'shenzhen',
                        label: '深圳市'
                    },
                    {
                        value: 'hangzhou',
                        label: '杭州市'
                    },
                    {
                        value: 'nanjing',
                        label: '南京市'
                    },
                    {
                        value: 'chongqing',
                        label: '重庆市'
                    }
                ]
            };
        },
        mounted() {

        },
        methods: {
            treeNodeSelect(node) {
                console.log(node);
            },

            saveAdd() {

            },

            addRole() {
                this.addModal = true;
            },

            authSet() {
                this.authModal = true;
            },

            saveAuth() {

            },

            deleteRole() {
                this.delete();
            },

            deleteRoles() {
                let roles = this.$refs.roleTree.getCheckedNodes();
                if (roles.length === 0) {
                    this.$Message.warning('请先选择需要删除的角色');
                } else {
                    this.delete();
                }
            },

            delete() {
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除角色？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        alert(1);
                    }
                });
            }
        }
    };
</script>
