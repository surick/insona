<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                设备绑定
            </div>

            <div slot="extra">
                <access-ctrl :name="'SYS_INS_USERPRPDUCT'" ref="access">
                <Button type="primary" @click="addUserProduct()">
                    <Icon type="android-add"></Icon>
                    绑定
                </Button>
                <Button type="error" @click="deleteUserProduct()">
                    <Icon type="trash-a"></Icon>
                    解绑
                </Button>
                </access-ctrl>
            </div>
            <Table border :columns="columns" :data="data" @on-selection-change="selectChange"></Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="total" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
        </Card>

        <!-- 绑定与解绑 -->
        <Modal
            v-model="addAndEditModal"
            :title="'设备绑定'"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">终端用户</div>
                    </Col>
                    <Col span="18">
                    <Select v-model="userProduct.uid" filterable style="width: 250px">
                        <Option v-for="item in userDetail" :value="item.id">{{ item.username }}</Option>
                    </Select>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">可用设备</div>
                    </Col>
                    <Select v-model="userProduct.did" filterable style="width: 250px">
                        <Option v-for="item in productDetail" :value="item.did">{{ item.name }}</Option>
                    </Select>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveUserProduct()">确定</Button>
            </div>
        </Modal>
        <Modal
            v-model="productModal"
            :title="'用户详情'"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="expand-row">
                    <Col>
                    <span class="expand-key">用户名：</span>
                    <span class="expand-value">{{ userInfo.username }}</span>
                    </Col>
                </Row>
                <Row class="expand-row" >
                    <Col>
                    <span class="expand-key">姓名：</span>
                    <span class="expand-value">{{ userInfo.name }}</span>
                    </Col>
                </Row>
                <Row class="expand-row" >
                    <Col>
                    <span class="expand-key">性别：</span>
                    <span class="expand-value">{{ userInfo.gender }}</span>
                    </Col>
                </Row>
                <Row class="expand-row" >
                    <Col>
                    <span class="expand-key">电话：</span>
                    <span class="expand-value">{{ userInfo.phone }}</span>
                    </Col>
                </Row>
                <Row class="expand-row" >
                    <Col>
                    <span class="expand-key">邮箱：</span>
                    <span class="expand-value">{{ userInfo.email }}</span>
                    </Col>
                </Row>
                <Row class="expand-row" >
                    <Col>
                    <span class="expand-key">生日：</span>
                    <span class="expand-value">{{ userInfo.birthday }}</span>
                    </Col>
                </Row>
                <Row class="expand-row" >
                    <Col>
                    <span class="expand-key">地址：</span>
                    <span class="expand-value">{{ userInfo.address }}</span>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="close()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import expandRow from './insona-expand.vue';
    import UserDT from '../../http/user-product.js';

    export default {
        name: 'insona_userProduct',
        components: {expandRow},
        data: function () {
            return {
                userDetail: [],
                productModal: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                total: 0,
                current: 1,
                productDetail: [],
                product: {
                    did: '',
                    name: '',
                    insona_online: '',
                    serialCode: '',
                    lastOnline: '',
                    version: '',
                    type: ''
                },
                userInfo: {
                    username: '',
                    phone: '',
                    email: '',
                    name: '',
                    gender: '',
                    birthday: '',
                    address: ''
                },
                productList: [],
                userProductList: [],
                userProduct: {
                    uid: '',
                    did: ''
                },
                userProductDetail: {
                    uid: '',
                    did: ''
                },
                columns: [
                    {
                        type: 'selection',
                        key: 'id',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '用户id',
                        key: 'uid',
                        align: 'center'
                    },
                    {
                        title: '设备类型',
                        key: 'type',
                        align: 'center'
                    },
                    {
                        title: '设备did',
                        key: 'did',
                        align: 'center'
                    },
                    {
                        title: '设备名称',
                        key: 'name',
                        align: 'center'
                    },
                    {
                        title: '经销商',
                        key: 'dealer',
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 130,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'ghost'
                                    },
                                    on: {
                                        click: () => {
                                            this.info(params.row.uid);
                                        }
                                    }
                                }, [
                                    h('Icon', {
                                        props: {
                                            type: 'edit'
                                        },
                                        style: {
                                            marginRight: '10px'
                                        }
                                    }),
                                    '用户详情'
                                ])
                            ]);
                        }
                    }
                ],
                data: []
            };
        },
        mounted() {
            this.current = 1;
            this.getUserProduct();
            this.getProducts();
            this.getUsers();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getUserProduct() {
                UserDT.getUserProduct(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            getProducts() {
                UserDT.getTypes(this).then((res) => {
                    if (res.success) {
                        this.productDetail = res.data;
                    }
                });
            },
            getUsers() {
                UserDT.getUsers(this).then((res) => {
                    if (res.success) {
                        this.userDetail = res.data;
                    }
                });
            },
            selectChange(selection) {
                this.selected = selection;
            },
            changePage(page) {
                this.current = page;
                this.getUserProduct();
            },
            addUserProduct() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.userProduct = {
                    uid: '',
                    did: ''
                };
            },
            saveUserProduct() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.userProduct, ['did'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    UserDT.addUserProduct(this, this.userProduct).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getUserProduct();
                        }
                    });
                }
            },

            info(uid) {
                this.productModal = true;
                UserDT.getInfo(this, uid).then((res) => {
                    if (res.success) {
                        this.userInfo = res.data;
                    }
                });
            },
            close() {
                this.productModal = close;
            },
            deleteUserProduct(ids) {
                if (!ids && this.selected.length === 0) return this.$Message.warning('请先选择需要删除的内容');
                if (!ids && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        UserDT.deleteUserProduct(this, ids).then((res) => {
                            if (res.success) this.getUserProduct();
                        });
                    }
                });
            }
        }
    };
</script>
