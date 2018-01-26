<style lang="less">
    @import '../../styles/common.less';
    @import '../auth/auth.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                设备管理
            </div>
            <div slot="extra">
                <access-ctrl :name="'SYS_INS_PASS'" ref="access">
                <Button type="primary" @click="passProduct()">
                    <Icon type="android-add"></Icon>
                    待审批
                </Button>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_BACK_HIDDEN'" ref="access">
                <Button type="primary" @click="backProduct()">
                    <Icon type="android-add"></Icon>
                    已退回
                </Button>
                </access-ctrl>
            </div>
            <Table border :columns="columns" :data="data" @on-selection-change="selectChange"></Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="this.total" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
        </Card>
        <Modal
            v-model="addAndEditModal"
            :title="['修改设备信息'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">设备did</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.did" placeholder="设备did" disabled></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">设备名称</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.name" placeholder="设备名称" disabled></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">所属用户</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.sub_user" placeholder="所属用户"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">所属家庭</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.sub_home" placeholder="所属家庭"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">地址</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.address" placeholder="地址"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">机智云在线</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.insona_online" placeholder="机智云在线"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">last_online</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.last_online" placeholder="上次在线"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">其他信息</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="saleProduct.other" placeholder="其他信息"></Input>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="addProduct()">确定</Button>
            </div>
        </Modal>
        <Modal
            v-model="pendingProduct"
            :title="'待审批'"
            :mask-closable="false"
            width="1144px">
            <Table border :columns="pending" :data="pendData"></Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="pendTotal" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saleProduct()">关闭</Button>
            </div>
        </Modal>
        <Modal
            v-model="backedProduct"
            :title="'已退回'"
            :mask-closable="false"
            width="944px">
            <Table border :columns="backed" :data="backData"></Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="backTotal" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saleProduct()">关闭</Button>
            </div>
        </Modal>
        <Modal
            v-model="reasonProduct"
            :title="'退回原因'"
            :mask-closable="false">
            <Input v-model="reason.text" placeholder="其他信息" type="textarea"></Input>
            <div slot="footer">
                <Button type="primary" size="large" @click="backedPro()">退回</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import Dealer from '../../http/product-sale.js';
    import productRow from './sale-expand.vue';

    export default {
        name: 'insona_saleProduct',
        components: {productRow},
        data: function () {
            return {
                reasonProduct: false,
                pendTotal: 0,
                backTotal: 0,
                pendingProduct: false,
                backedProduct: false,
                sub_sale: '',
                saleDetail: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                selected: '',
                total: 0,
                pendData: [],
                backData: [],
                current: 1,
                dealers: [],
                productSale: {
                    sub_sale: '',
                    ids: []
                },
                reason: {
                    id: '',
                    text: ''
                },
                saleProduct: {
                    id: '',
                    did: '',
                    name: '',
                    insona_online: '',
                    serial_code: '',
                    last_online: '',
                    version: '',
                    sub_user: '',
                    sub_home: '',
                    address: '',
                    other: '',
                    near_status: '',
                    near_order: '',
                    near_event: '',
                    sub_inter: '',
                    sub_maker: '',
                    remark: '',
                    sort_no: '',
                    type: '',
                    status: '',
                    extract: ''
                },
                backed: [
                    {
                        type: 'selection',
                        key: 'id',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '设备did',
                        key: 'did',
                        width: 150,
                        align: 'center'
                    },
                    {
                        title: '设备名称',
                        key: 'name',
                        width: 150,
                        align: 'center'
                    },
                    {
                        title: '设备类别',
                        key: 'type',
                        width: 130,
                        align: 'center'
                    },
                    {
                        title: '序列号',
                        key: 'serial_code',
                        width: 130,
                        align: 'center'
                    },
                    {
                        title: '版本号',
                        key: 'version',
                        width: 140,
                        align: 'center'
                    },
                    {
                        width: 90,
                        title: '状态',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                params.row.status === '1' ? '已售' : (params.row.status === '2' ? '退回' : '待批')
                            ]);
                        }
                    },
                    {
                        type: 'expand',
                        width: 70,
                        title: '更多',
                        align: 'center',
                        render: (h, params) => {
                            return h(productRow, {
                                props: {
                                    row: params.row
                                }
                            });
                        }
                    }
                ],
                pending: [
                    {
                        type: 'selection',
                        key: 'id',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '设备did',
                        key: 'did',
                        width: 150,
                        align: 'center'
                    },
                    {
                        title: '设备名称',
                        key: 'name',
                        width: 150,
                        align: 'center'
                    },
                    {
                        title: '设备类别',
                        key: 'type',
                        width: 130,
                        align: 'center'
                    },
                    {
                        title: '序列号',
                        key: 'serial_code',
                        width: 130,
                        align: 'center'
                    },
                    {
                        title: '版本号',
                        key: 'version',
                        width: 140,
                        align: 'center'
                    },
                    {
                        width: 90,
                        title: '状态',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                params.row.status === '1' ? '待批' : (params.row.status === '2' ? '退回' : '待批')
                            ]);
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 200,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'success'
                                    },
                                    style: {
                                        marginRight: '10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.saveProduct(params.row.id);
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
                                    '通过'
                                ]),
                                h('access-ctrl', {
                                    props: {
                                        name: 'SYS_USER_REMOVE',
                                        ref: 'access'
                                    }
                                }, [h('Button', {
                                    props: {
                                        type: 'ghost'
                                    },
                                    style: {
                                        marginRight: '10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.deleteProduct(params.row.id);
                                        }
                                    }
                                }, [
                                    h('Icon', {
                                        props: {
                                            type: 'edit',
                                            disabled: 'true'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        }
                                    }),
                                    '退回'
                                ])
                                ])],
                            );
                        }
                    },
                    {
                        type: 'expand',
                        width: 70,
                        title: '更多',
                        align: 'center',
                        render: (h, params) => {
                            return h(productRow, {
                                props: {
                                    row: params.row
                                }
                            });
                        }
                    }
                ],
                columns: [
                    {
                        type: 'selection',
                        key: 'id',
                        width: 50,
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
                        width: 150,
                        align: 'center'
                    },
                    {
                        title: '序列号',
                        key: 'serial_code',
                        align: 'center'
                    },
                    {
                        title: '设备类别',
                        key: 'type',
                        align: 'center'
                    },
                    {
                        title: '版本号',
                        key: 'version',
                        align: 'center'
                    },
                    {
                        width: 70,
                        title: '状态',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                params.row.status === '3' ? '通过' : '未知'
                            ]);
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 110,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary'
                                    },
                                    style: {
                                        marginRight: '10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.editProduct(params.row);
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
                                    '修改'
                                ])],
                            );
                        }
                    },
                    {
                        type: 'expand',
                        width: 70,
                        title: '更多',
                        align: 'center',
                        render: (h, params) => {
                            return h(productRow, {
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
            this.getList();
            this.getPass();
            this.getBack();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getList() {
                Dealer.getList(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            getPass() {
                Dealer.getPass(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.pendData = res.data.list;
                        this.pendTotal = res.data.total;
                    }
                });
            },
            backedPro() {
                this.reasonProduct = false;
                Dealer.backProduct(this, this.reason).then(res => {
                    if (res.success) {
                        this.getPass();
                        this.getBack();
                    }
                });
            },
            getBack() {
                Dealer.getBack(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.backData = res.data.list;
                        this.backTotal = res.data.total;
                    }
                });
            },
            passProduct() {
                this.pendingProduct = true;
            },
            backProduct() {
                this.backedProduct = true;
            },
            selectChange(selection) {
                this.selected = selection;
            },
            changePage(page) {
                this.current = page;
                this.getProducts();
            },
            saveProduct(id) {
                var ids;
                if (!id && this.selected.length === 0) return this.$Message.warning('请先选择设备');
                if (!id && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                } else {
                    ids = [id];
                }
                Dealer.passProduct(this, ids).then(res => {
                    if (res.success) {
                        this.getList();
                        this.getPass();
                        this.getBack();
                    }
                });
            },
            deleteProduct(id) {
                this.reasonProduct = true;
                this.reason = {
                    id: id,
                    text: ''
                };
                this.getList();
                this.getPass();
                this.getBack();
            },
            editProduct(obj) {
                this.saleProduct = {
                    did: obj.did,
                    name: obj.name,
                    gizwit_info: obj.gizwit_info,
                    insona_online: obj.insona_online,
                    serial_code: obj.serial_code,
                    last_online: obj.last_online,
                    version: obj.version,
                    sub_user: obj.sub_user,
                    sub_home: obj.sub_home,
                    address: obj.address,
                    other: obj.other,
                    near_status: obj.near_status,
                    near_order: obj.near_order,
                    near_event: obj.near_event,
                    sub_inter: obj.sub_inter,
                    sub_maker: obj.sub_maker,
                    remark: obj.remark,
                    sort_no: obj.sort_no,
                    type: obj.type,
                    status: obj.status,
                    extract: obj.extract
                };
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = obj.id;
            },
            addProduct() {
                this.addAndEditModal = false;
                Dealer.updateProduct(this, this.editId, this.saleProduct).then(res => {
                    if (res.success) {
                        this.getList();
                        this.getPass();
                        this.getBack();
                    }
                });
            }
        }
    };
</script>
