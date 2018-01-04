<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                设备出厂
            </div>
            <div slot="extra">
                <Button type="primary" @click="addProduct()">
                    <Icon type="android-add"></Icon>
                    新建
                </Button>
                <Button type="error" @click="deleteProduct()">
                    <Icon type="trash-a"></Icon>
                    删除
                </Button>
                    <Button type="primary" @click="sale()">
                        <Icon type="android-add"></Icon>
                        销售
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
            :title="['新增设备', '编辑设备'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">设备did</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.did" placeholder="设备did"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">设备名称</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.name" placeholder="设备名称"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">机智云appid</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.gizwit_info" placeholder="机智云appid"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">appsecret</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.gizwit_secret" placeholder="appsecret"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">序列号</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.serial_code" placeholder="序列号"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">版本号</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.version" placeholder="版本号"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">所属集成商</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.sub_inter" placeholder="所属集成商"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">所属生产商</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="product.sub_maker" placeholder="所属生产商"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">类别id</div>
                    </Col>
                    <Col span="18">
                    <Select v-model="product.type" filterable style="width: auto">
                        <Option v-for="item in types" :value="item.id" :key="item.type_id">
                            {{ item.type_name }}
                        </Option>
                    </Select>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveProduct()">确定</Button>
            </div>
        </Modal>
        <Modal
            v-model="saleDetail"
            :title="'销售设备'"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">经销商</div>
                    </Col>
                    <Col span="18">
                    <Select v-model="sub_sale" filterable style="width: 250px">
                        <Option v-for="item in dealers" :value="item.id" :key="item.name">
                            {{ item.userName }}
                        </Option>
                    </Select>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saleProduct(1)">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import Product from '../../http/product.js';
    import productRow from './product-expand.vue';

    export default {
        name: 'insona_product',
        components: {productRow},
        data: function () {
            return {
                sub_sale: '',
                saleDetail: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                selected: '',
                total: 0,
                current: 1,
                types: [],
                dealers: [],
                productSale: {
                    sub_sale: '',
                    ids: []
                },
                product: {
                    id: '',
                    did: '',
                    name: '',
                    gizwit_info: '',
                    gizwit_secret: '',
                    serial_code: '',
                    version: '',
                    sub_inter: '',
                    sub_maker: '',
                    type: '',
                    status: '0'
                },
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
                        title: '机智云appid',
                        key: 'gizwit_info',
                        width: 150,
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
                        width: 70,
                        title: '状态',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                params.row.status === '1' ? '已售' : (params.row.status === '2' ? '驳回' : (params.row.status === '3' ? '通过' : '待售'))
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
                                        type: 'ghost'
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
                                ]),
                                h('Button', {
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
                                            type: 'edit'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        }
                                    }),
                                    '删除'
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
            this.getProducts();
            this.getDealers();
            this.getTypes();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getProducts() {
                Product.getProducts(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            getDealers() {
                Product.getDealers(this).then((res) => {
                    if (res.success) {
                        this.dealers = res.data;
                    }
                });
            },
            getTypes() {
                Product.getTypes(this).then((res) => {
                    if (res.success) {
                        this.types = res.data;
                    }
                });
            },
            sale(ids) {
                console.log(ids);
                this.sub_sale = '';
                if (!ids && this.selected.length === 0) return this.$Message.warning('请先选择设备');
                if (!ids && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                }
                this.saleDetail = true;
            },
            saleProduct(status, id) {
                this.saleDetail = false;
                var ids;
                if (!id && this.selected.length === 0) return this.$Message.warning('请先选择设备');
                if (!id && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                } else {
                    ids = [id];
                }
                this.productSale = {
                    ids: ids,
                    sub_sale: this.sub_sale
                };
                Product.changeProduct(this, status, this.productSale).then(res => {
                    if (res.success) {
                        this.productSale.ids = [];
                        this.selected = '';
                        this.getProducts();
                    }
                });
            },
            selectChange(selection) {
                this.selected = selection;
            },
            changePage(page) {
                this.current = page;
                this.getProducts();
            },
            addProduct() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.product = {
                    did: '',
                    name: '',
                    gizwit_info: '',
                    gizwit_secret: '',
                    serial_code: '',
                    version: '',
                    sub_inter: '',
                    sub_maker: '',
                    type: '',
                    status: '0'
                };
            },
            saveProduct() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.product, ['did'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Product.addProduct(this, this.product).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getProducts();
                        }
                    });
                } else {
                    Product.updateProduct(this, this.editId, this.product).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getProducts();
                        }
                    });
                }
            },
            editProduct(obj) {
                this.product = {
                    did: obj.did,
                    name: obj.name,
                    gizwit_info: obj.gizwit_info,
                    gizwit_secret: obj.gizwit_secret,
                    serial_code: obj.serial_code,
                    version: obj.version,
                    sub_inter: obj.sub_inter,
                    sub_maker: obj.sub_maker,
                    type: obj.type,
                    status: obj.status
                };
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = obj.id;
            },
            deleteProduct(id) {
                var ids;
                if (!id && this.selected.length === 0) return this.$Message.warning('请先选择需要删除的用户');
                if (!id && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                } else {
                    ids = [id];
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除用户？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        Product.deleteProduct(this, ids).then((res) => {
                            if (res.success) this.getProducts();
                        });
                    }
                });
            }
        }
    };
</script>
