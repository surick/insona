<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                终端用户管理
            </div>
            <access-ctrl :name="'SYS_INS_USERS'" ref="access">
            <Table border :columns="columns" :data="data" @on-selection-change="selectChange"></Table>
            </access-ctrl>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="total" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
        </Card>
        <Modal
            v-model="productModal"
            :title="'设备信息'"
            :mask-closable="false"
            :width="753">
                <Table border :columns="column" :data="row"></Table>
            <div slot="footer">
                <Button type="primary" size="large" @click="close()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import expandData from './insona-users-expand.vue';
    import UserDT from '../../http/user-product.js';
    import InsonaUser from '../../http/insona-user';

    export default {
        name: 'insona_userProduct',
        components: {expandData},
        data: function () {
            return {
                productModal: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                total: 0,
                current: 1,
                productDetail: [],
                product: {
                    did: '',
                    product_key: '',
                    mac: '',
                    insona_online: '',
                    passcode: '',
                    host: '',
                    remark: '',
                    is_disabled: '',
                    type: '',
                    dev_alias: '',
                    dev_label: '',
                    role: ''
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
                column: [
                    {
                        title: '设备did',
                        key: 'did',
                        align: 'center'
                    },
                    {
                        title: '设备名',
                        key: 'name',
                        width: 180,
                        align: 'center'
                    },
                    {
                        title: '是否在线',
                        key: 'insonaOnline',
                        width: 180,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.insonaOnline === 1 ? '在线' : '离线';
                        }
                    },
                    {
                        title: '设备类型',
                        key: 'type',
                        width: 180,
                        align: 'center'
                    }
                ],
                row: [],
                columns: [
                    {
                        type: 'selection',
                        key: 'uid',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '用户uid',
                        key: 'uid',
                        align: 'center'
                    },
                    {
                        title: '用户名',
                        key: 'username',
                        align: 'center'
                    },
                    {
                        title: '手机号',
                        key: 'phone',
                        align: 'center'
                    },
                    {
                        title: '邮箱',
                        key: 'email',
                        align: 'center'
                    },
                    {
                        title: '姓名',
                        key: 'name',
                        align: 'center'
                    },
                    {
                        title: '性别',
                        key: 'gender',
                        width: 80,
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 100,
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
                                            this.getProducts(params.row.id);
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
                                    '详情'
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
                            return h(expandData, {
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
            this.getUsers();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getUsers() {
                InsonaUser.getUsers(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                        console.log(this.data);
                    }
                });
            },
            getProducts(uid) {
                this.productModal = true;
                InsonaUser.getProducts(this, uid).then((res) => {
                    if (res.success) {
                        this.row = res.data;
                        console.log(this.row);
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
            close() {
                this.productModal = false;
            },
            deleteUserProduct(ids) {
                if (!ids && this.selected.length === 0) return this.$Message.warning('请先选择需要删除的用户');
                if (!ids && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.uid;
                    });
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除用户？',
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
