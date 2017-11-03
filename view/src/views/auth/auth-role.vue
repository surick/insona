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
                            <Option v-for="item in roleList" :value="item.id" :key="item.id">{{ item.roleName }}
                            </Option>
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
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="3">
                    <div class="input-label">父级</div>
                    </Col>
                    <Col span="21">
                    <Select v-model="addRoleDetail.parentId" filterable style="width: 250px">
                        <Option v-for="item in roleList" :value="item.id" :key="item.id">{{ item.roleName }}</Option>
                    </Select>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="3">
                    <div class="input-label">名称</div>
                    </Col>
                    <Col span="21">
                    <Input v-model="addRoleDetail.roleName" placeholder="名称"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="3">
                    <div class="input-label">CODE</div>
                    </Col>
                    <Col span="21">
                        <Input v-model="addRoleDetail.roleCode" placeholder="CODE"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="3">
                    <div class="input-label">排序</div>
                    </Col>
                    <Col span="21">
                    <Input v-model="addRoleDetail.orderNum" placeholder="排序号"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="3">
                    <div class="input-label">生效</div>
                    </Col>
                    <Col span="21" style="line-height: 32px;">
                    <i-switch v-model="addRoleDetail.enabled">
                        <span slot="true">是</span>
                        <span slot="false">否</span>
                    </i-switch>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveAdd">确定</Button>
            </div>
        </Modal>

        <Modal
            v-model="authModal"
            title="配置权限"
            :mask-closable="false"
            @on-ok="saveAuth">
            <div class="modal-body">
                <Tree ref="authTree" :data="authData" show-checkbox></Tree>
            </div>
        </Modal>
    </div>
</template>

<script>
    import {Role} from '@/http';
    export default {
        name: 'auth_role',
        data() {
            return {
                access: this.$store.state.access,
                addRoleModel: false,
                addRoleEdit: 0,
                editId: '',
                selected: [],
                roleModal: false,
                roleIndexs: [],
                total: 0,
                current: 1,
                roleData: [],
                roleDetail: {
                    parent: '',
                    name: '',
                    status: true
                },
                addModal: false,
                addRoleDetail: {
                    roleName: '',
                    roleCode: '',
                    parentId: '',
                    orderNum: '',
                    enabled: true
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
                roleList: []
            };
        },
        addRole() {
            this.addRoleEdit = 0;
            this.addModal = true;
            this.addRoleDetail = {
                roleName: '',
                roleCode: '',
                parentId: '',
                orderNum: '',
                enabled: true
            };
        },
        mounted() {
            this.getRoleTree();
        },
        methods: {
            getRoleTree() {
                Role.getRoleTree(this).then(res => {
                    if (res.success) {
                        this.roleData = Role.dealRoleTree(res.data);
                        this.roleList = Role.dealRoleTreeToList(res.data);
                    }
                });
            },
            treeNodeSelect(node) {
                console.log(node);
            },
            saveAdd() {
                if (this.addRoleEdit === 0) {
                    if (this.$commonFun.checkObject(this.role, ['id'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Role.addRole(this, this.addRoleDetail).then(res => {
                        if (res.success) {
                            console.log(this.addRoleDetail);
                            this.addModal = false;
                            this.getRoleTree();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.role, ['id'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Role.updateRole(this, this.addRoleDetail).then(res => {
                        if (res.success) {
                            this.addModal = false;
                            this.getRoleTree();
                        }
                    });
                }
            },
            addRole() {
                this.addModal = true;
            },
            authSet() {
                this.authModal = true;
            },
            saveAuth() {

            },
            deleteRoles() {
                let roles = this.$refs.roleTree.getCheckedNodes();
                let ids = roles.map(item => { return item.id; });
                console.log(ids);
                if (roles.length === 0) {
                    this.$Message.warning('请先选择需要删除的角色');
                } else {
                    this.delete(ids);
                }
            },

            delete(ids) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除角色？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        Role.deleteRole(this, ids).then((res) => {
                            if (res.success) this.getRoleTree();
                        });
                    }
                });
            }
        }
    };
</script>
