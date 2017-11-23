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
                <access-ctrl :name="'SYS_ROLE_SAVE'" ref="access">
                    <Button type="primary" @click="addRole()">
                        <Icon type="android-add"></Icon>
                        新增
                    </Button>
                </access-ctrl>
            </div>
            <Row :gutter="16">
                <Col span="5">
                <Tree ref="roleTree" :data="roleData" @on-select-change="treeNodeSelect"></Tree>
                </Col>
                <Col span="19">
                <Card>
                    <Row class="margin-bottom-10">
                        <Col span="3">
                        <div class="input-label">父级</div>
                        </Col>
                        <Col span="21">
                        <Select v-model="roleDetail.parentId" filterable style="width: 250px">
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
                        <Input v-model="roleDetail.title" placeholder="名称"></Input>
                        </Col>
                    </Row>
                    <Row class="margin-bottom-10">
                        <Col span="3">
                        <div class="input-label">Code</div>
                        </Col>
                        <Col span="21">
                        <Input v-model="roleDetail.code" placeholder="code"></Input>
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
                        <access-ctrl :name="'SYS_ROLE_UPDATE'" ref="access">
                            <Button type="primary" @click="updateRole()">
                                修改
                            </Button>
                        </access-ctrl>
                        <access-ctrl :name="'SYS_ROLE_AUTH'" ref="access">
                            <Button type="info" @click="authSet()">
                                <Icon type="android-settings"></Icon>
                                配置权限
                            </Button>
                        </access-ctrl>
                        <access-ctrl :name="'SYS_ROLE_REMOVR'" ref="access">
                            <Button type="error" @click="deleteRoles()">
                                <Icon type="trash-a"></Icon>
                                删除
                            </Button>
                        </access-ctrl>
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
                <Tree ref="authTree" :data="authData" show-checkbox @on-select-change="treeAuthSelect"></Tree>
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
                    id: '',
                    parentId: '',
                    title: '',
                    code: '',
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
                authData: [],
                authDetail: {
                    codes: '',
                    roleId: ''
                },
                roleList: []
            };
        },
        addRole() {
            this.addRoleDetail = {
                roleName: '',
                roleCode: '',
                parentId: '',
                orderNum: '',
                enabled: true
            };
            this.addRoleEdit = 0;
            this.addModal = true;
        },
        mounted() {
            this.getRoleTree();
        },
        methods: {
            ctrlAccess() {
                this.$refs.access.updateAccess();
            },
            getRoleTree() {
                Role.getRoleTree(this).then(res => {
                    if (res.success) {
                        this.roleData = Role.dealRoleTree(res.data);
                        this.roleList = Role.dealRoleTreeToList(res.data);
                    }
                });
            },
            getAuthTree() {
                Role.getAuthTree(this).then(res => {
                    if (res.success) {
                        this.authData = Role.dealAuthTree(res.data);
                    }
                });
            },
            treeAuthSelect(node) {
                this.authDetail = node;
                console.log(node);
            },
            updateRole() {
                console.log(this.roleDetail);
                Role.updateRole(this, this.roleDetail);
                this.getRoleTree();
            },
            treeNodeSelect(node) {
                this.roleDetail = {
                    title: node[0].title,
                    parentId: node[0].parentId,
                    code: node[0].code,
                    id: node[0].id,
                    status: true
                };
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
                            this.addRoleDetail = {};
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
                this.getAuthTree();
                this.authModal = true;
            },
            saveAuth() {
                debugger;
                let role = this.$refs.roleTree.getSelectedNodes();
                let roleId = role.map(item => {
                    return item.id;
                });
                let auth = this.$refs.authTree.getCheckedNodes();
                let codes = auth.map(item => {
                    return item.code;
                });
                console.log('code=' + codes);
                this.authDetail = {
                    codes: codes,
                    roleId: roleId[0]
                };
                console.log(this.authDetail);
                Role.saveAuth(this, this.authDetail);
                this.getRoleTree();
            },
            deleteRoles() {
                this.delete(this.roleDetail.id);
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
