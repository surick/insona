<style lang="less">
    @import '../../styles/common.less';
    @import './auth.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                用户管理
            </div>
            <div slot="extra">
                <Button type="primary" @click="addUser()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="info">
                    <Icon type="android-settings"></Icon>
                    配置角色
                </Button>
                <Button type="error" @click="deleteUser()">
                    <Icon type="trash-a"></Icon>
                    删除
                </Button>
            </div>
            <Table border :columns="columns" :data="data" @on-selection-change="selectChange"></Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="total" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
        </Card>

        <!-- 编辑与新增 -->
        <Modal
            v-model="addAndEditModal"
            :title="['用户新增', '用户编辑'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10" v-if="addOrEdit === 1">
                    <Col span="6">
                    <div class="input-label">头像</div>
                    </Col>
                    <Col span="18">
                    <Avatar :src="user.headImgUrl"/>
                    </Col>
                </Row>
                <Row class="margin-bottom-10" v-if="addOrEdit === 0">
                    <Col span="6">
                    <div class="input-label">账号</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.userName" placeholder="账号"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10" v-if="addOrEdit === 0">
                    <Col span="6">
                    <div class="input-label">密码</div>
                    </Col>
                    <Col span="18">
                    <Input type="password" v-model="user.password" placeholder="密码"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">姓名</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.name" placeholder="姓名"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">性别</div>
                    </Col>
                    <Col span="18" style="line-height: 32px;">
                    <RadioGroup v-model="user.sex">
                        <Radio label="男"></Radio>
                        <Radio label="女"></Radio>
                    </RadioGroup>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">生日</div>
                    </Col>
                    <Col span="18">
                    <DatePicker type="date" placeholder="生日" style="width: 100%" v-model="user.birthday"></DatePicker>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">手机号</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.mobilePhone" placeholder="手机号"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">邮箱</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.email" placeholder="邮箱"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">详细地址</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.address" placeholder="详细地址"></Input>
                    </Col>
                </Row>
                <Row>
                    <Col span="6">
                    <div class="input-label">状态</div>
                    </Col>
                    <Col span="18" style="line-height: 32px;">
                    <i-switch size="large" v-model="user.status">
                        <span slot="open">正常</span>
                        <span slot="close">冻结</span>
                    </i-switch>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveUser">确定</Button>
            </div>
        </Modal>

        <!-- 配置角色 -->
        <Modal
            v-model="roleModal"
            title="配置角色"
            :mask-closable="false"
            @on-ok="saveRole">
            <div class="modal-body">
                <Tree ref="roleTree" :data="roleData" show-checkbox></Tree>
            </div>
        </Modal>
    </div>
</template>

<script>
    import expandRow from './auth-user-expand.vue';
    import {User, Role} from '@/http';

    export default {
        name: 'auth_user',
        components: {expandRow},
        data() {
            return {
                access: this.$store.state.access,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                selected: [],
                roleModal: false,
                roleIndexs: [],
                total: 0,
                current: 1,
                roleData: [],
                roleList: [],
                user: {
                    userName: '',
                    password: '',
                    name: '',
                    headImgUrl: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                    birthday: '',
                    address: '',
                    mobilePhone: '',
                    email: '',
                    sex: '女',
                    status: true
                },
                roleInfo: {
                    userId: '',
                    roleId: []
                },
                columns: [
                    {
                        type: 'selection',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '姓名',
                        key: 'name',
                        width: 120,
                        align: 'center'
                    },
                    {
                        title: '性别',
                        key: 'sex',
                        width: 80,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.sex === 1 ? '男' : '女';
                        }
                    },
                    {
                        title: '手机号',
                        key: 'mobilePhone',
                        width: 120,
                        align: 'center'
                    },
                    {
                        title: '状态',
                        key: 'status',
                        width: 80,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.status === 1 ? '正常' : '冻结';
                        }
                    },
                    {
                        title: '详细地址',
                        key: 'address',
                        // width: 250,
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 300,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'ghost'
                                    },
                                    style: {
                                        marginRight: '10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.editUser(params.row);
                                        }
                                    }
                                }, [
                                    h('Icon', {
                                        props: {
                                            type: 'edit'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        }
                                    }),
                                    '编辑'
                                ]),
                                h('Button', {
                                    props: {
                                        type: 'info'
                                    },
                                    style: {
                                        marginRight: '10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.setRole(params.index, params.row);
                                        }
                                    }
                                }, [
                                    h('Icon', {
                                        props: {
                                            type: 'android-settings'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        }
                                    }),
                                    '配置角色'
                                ]),
                                h('Button', {
                                    props: {
                                        type: 'error'
                                    },
                                    on: {
                                        click: () => {
                                            this.deleteUser([params.row.id]);
                                        }
                                    }
                                }, [
                                    h('Icon', {
                                        props: {
                                            type: 'trash-a'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        }
                                    }),
                                    '删除'
                                ])
                            ]);
                        }
                    },
                    {
                        type: 'expand',
                        width: 80,
                        title: '更多',
                        align: 'center',
                        render: (h, params) => {
                            return h(expandRow, {
                                props: {
                                    row: params.row
                                }
                            });
                        }
                    }
                ],
                data: []
            };
        },
        mounted() {
            this.current = 1;
            this.getUser();
            Role.getRoleTree(this).then(res => {
                console.log(res);
                if (res.success) {
                    this.roleData = Role.dealRoleTree(res.data);
                    this.roleList = Role.dealRoleTreeToList(res.data);
                }
            });
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getUser() {
                User.getUser(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            selectChange(selection) {
                this.selected = selection;
            },

            setRole(index, user) {
                this.roleModal = true;
                this.roleIndexs = index;
                let role = this.$refs.roleTree.getCheckedNodes();
                let roleId = role.map(item => {
                    return item.id;
                });
                this.roleInfo = {
                    userId: user.id,
                    roleId: roleId
                };
                console.log(user.id);
            },
            editUser(user) {
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = user.id;
                this.user = {
                    userName: user.userName,
                    password: '',
                    name: user.name,
                    headImgUrl: user.headImgUrl || 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                    birthday: user.birthday,
                    address: user.address,
                    mobilePhone: user.mobilePhone,
                    email: user.email,
                    sex: user.sex === 1 ? '男' : '女',
                    status: user.status === 1
                };
            },

            addUser() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.user = {
                    userName: '',
                    password: '',
                    name: '',
                    headImgUrl: '',
                    birthday: '',
                    address: '',
                    mobilePhone: '',
                    email: '',
                    sex: '男',
                    status: true
                };
            },

            saveUser() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.user, ['headImgUrl'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    User.addUser(this, this.user).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getUser();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.user, ['password'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    User.updateUser(this, this.editId, this.user).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getUser();
                        }
                    });
                }
            },

            deleteUser(ids) {
                if (!ids && this.selected.length === 0) return this.$Message.warning('请先选择需要删除的用户');
                if (!ids && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除用户？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        User.deleteUser(this, ids).then((res) => {
                            if (res.success) this.getUser();
                        });
                    }
                });
            },

            changePage(page) {
                this.current = page;
                this.getUser();
            },
            saveRole() {
                console.log(this.$refs.roleTree.getCheckedNodes());
                console.log(this.roleInfo);
                User.saveRole(this, this.roleInfo);
            }
        }
    };
</script>
