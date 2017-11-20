<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                设备管理
            </div>

            <div slot="extra">

                <Button type="primary" @click="addUserProduct()">
                    <Icon type="android-add"></Icon>
                    绑定
                </Button>
                <Button type="error" @click="deleteUserProduct()">
                    <Icon type="trash-a"></Icon>
                    解绑
                </Button>
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
                    <div class="input-label">用户id</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="userProduct.uid" placeholder="用户id"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">信息分类</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="userProduct.did" placeholder="设备did"></Input>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveUserProduct()">确定</Button>
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
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                total: 0,
                current: 1,
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
                        title: '用户uid',
                        key: 'uid',
                        width: 180,
                        align: 'center'
                    },
                    {
                        title: '设备did',
                        key: 'did',
                        width: 180,
                        align: 'center'
                    },
                    {
                        title: '设备别名',
                        key: 'alias',
                        width: 180,
                        align: 'center'
                    },
                    {
                        title: '是否在线',
                        key: 'online',
                        width: 160,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.online === 1 ? '在线' : '离线';
                        }
                    },
                    {
                        title: '设备状态',
                        key: 'disabled',
                        width: 180,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.enable === 1 ? '正常' : '冻结';
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 180,
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
                                            this.info(params.row.did);
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
                    }
                ],
                data: []
            };
        },
        mounted() {
            this.current = 1;
            this.getUserProduct();
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
                        console.log(res);
                        this.total = res.data.total;
                    }
                });
            },
            selectChange(selection) {
                this.selected = selection;
                console.log(this.selected);
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
                    if (this.$commonFun.checkObject(this.userProduct, ['uid', 'did'])) {
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
            info(did) {
                alert(did);
            },
            deleteUserProduct(ids) {
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
                        UserDT.deleteUserProduct(this, ids).then((res) => {
                            if (res.success) this.getUserProduct();
                        });
                    }
                });
            }
        }
    };
</script>
