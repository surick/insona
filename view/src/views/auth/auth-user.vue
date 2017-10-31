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
                <Button type="primary">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="info">
                    <Icon type="android-settings"></Icon>
                    配置角色
                </Button>
                <Button type="error">
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
            v-model="editModal"
            title="用户编辑"
            @on-ok="saveEdit">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                        <div class="input-label">头像</div>
                    </Col>
                    <Col span="18">
                        <Avatar :src="user.head_img_url" />
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
                        <Input v-model="user.mobile_phone" placeholder="手机号"></Input>
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
        </Modal>

        <!-- 配置角色 -->
        <Modal
            v-model="roleModal"
            title="配置角色"
            @on-ok="saveRole">
            <div class="modal-body">
                <Tree ref="roleTree" :data="roleData" show-checkbox></Tree>
            </div>
        </Modal>
    </div>
</template>

<script>
import expandRow from './auth-user-expand.vue';
export default {
    name: 'auth_user',
    components: { expandRow },
    data () {
        return {
            access: this.$store.state.access,
            editModal: false,
            selectIndex: 0,
            roleModal: false,
            roleIndexs: [],
            total: 50,
            current: 1,
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
            user: {
                name: '',
                head_img_url: 'https://i.loli.net/2017/08/21/599a521472424.jpg',
                birthday: '',
                address: '',
                mobile_phone: '',
                email: '',
                sex: '女',
                status: true,
                crt_time: '',
                crt_name: '',
                upd_time: '',
                upd_name: ''
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
                    key: 'mobile_phone',
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
                                        this.edit(params.index);
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
                                        this.setRole(params.index);
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
                                        this.delete(params.index);
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
            data: [
                {
                    name: '王小明',
                    sex: 0,
                    address: '北京市朝阳区芍药居',
                    birthday: '2017-09-09',
                    mobile_phone: '18860852448',
                    email: '121211211@qq.com',
                    status: 1,
                    crt_name: '郝建',
                    crt_time: '2016-10-10 10:10',
                    upd_name: '郝建',
                    upd_time: '2016-10-10 10:10'
                },
                {
                    name: '张小刚',
                    age: 25,
                    address: '北京市海淀区西二旗'
                },
                {
                    name: '李小红',
                    age: 30,
                    address: '上海市浦东新区世纪大道'
                },
                {
                    name: '周小伟',
                    age: 26,
                    address: '深圳市南山区深南大道'
                }
            ]
        };
    },
    computed: {
        avatorPath () {
            return localStorage.avatorImgPath;
        }
    },
    methods: {
        selectChange(selection) {
            console.log(selection);
        },

        setRole(index) {
            this.roleModal = true;
            this.roleIndexs = index;
        },

        edit(index) {
            this.editModal = true;
            this.editIndex = index;
        },

        saveEdit() {

        },

        delete(index) {
            this.$Modal.confirm({
                title: '提示',
                content: '确定删除用户？',
                okText: '确定',
                cancelText: '取消',
                onOk: () => {
                    alert(1);
                }
            });
        },

        changePage(page) {
            console.log(page);
        },

        saveRole() {
            console.log(this.$refs.roleTree.getCheckedNodes());
        }
    }
};
</script>
