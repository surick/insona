<style lang="less">
    @import '../../styles/common.less';
    @import '../auth/auth.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                设备列表
            </div>
            <div slot="extra">
                <access-ctrl :name="'SYS_INS_ADD'" ref="access">
                    <Input v-model="productMsg" placeholder="请输入设备名" @on-click="selectPro" icon="ios-search" style="width: 200px"/>
                    <Button type="primary" @click="addProduct()">
                        <Icon type="android-add"></Icon>
                        新建
                    </Button>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_ADD'" ref="access">
                    <Upload
                        :before-upload="handleUpload"
                        :show-upload-list="false"
                        :on-success="handleSuccess"
                        :format="['xls','xlsx']"
                        :on-format-error="handleFormatError"
                        :action="this.configUrl+'/file/products'">
                        <Button type="success" icon="ios-cloud-upload-outline">批量上传</Button>
                    </Upload>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_MOVE'" ref="access">
                    <Button type="error" @click="deleteProduct()">
                        <Icon type="trash-a"></Icon>
                        删除
                    </Button>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_PAY'" ref="access">
                    <Button type="primary" @click="sale()">
                        <Icon type="android-add"></Icon>
                        销售
                    </Button>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_ADD'" ref="access">
                    <a v-bind:href=this.configUrl+this.link>
                        <Button type="primary">
                            下载模版
                        </Button>
                    </a>
                </access-ctrl>
                <!-- 上传Excel表格 -->
                <access-ctrl :name="'SYS_INS_ADD'" ref="access">
                    <Upload
                        :before-upload="handleUpload"
                        :show-upload-list="false"
                        :on-success="fileSuccess"
                        :format="['xls','xlsx']"
                        :on-format-error="handleFormatError"
                        :action="this.configUrl+'/file/example'">
                        <Button type="success">上传模板</Button>
                    </Upload>
                </access-ctrl>
            </div>
            <Table border :columns="columns" :data="data" @on-selection-change="selectChange"></Table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="total" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
        </Card>

        <!-- 设备新增/修改 -->
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
                    <div class="input-label">设备类别</div>
                    </Col>
                    <Col span="18">
                    <Select v-model="product.type" filterable>
                        <Option v-for="item in types" :value="item.type_name+item.batch"
                                :key="item.type_name+item.batch">{{ item.type_name }}{{ item.batch }}
                        </Option>
                    </Select>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">保修时间</div>
                    </Col>
                    <Col span="18">
                    <DatePicker type="date" placeholder="保修时间" v-model="product.repair_time"></DatePicker>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveProduct()">确定</Button>
            </div>
        </Modal>
        <!-- 设备销售 -->
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
                        <Option v-for="item in dealers" :value="item.name" :key="item.name">
                            {{ item.userName }}
                        </Option>
                    </Select>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">销售日期</div>
                    </Col>
                    <Col span="18">
                    <DatePicker type="date" placeholder="销售日期" v-model="sale_time"></DatePicker>
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
    import ipconfig from '@/config/ipconfig';

    export default {
        name: 'insona_product',
        components: {productRow},
        data: function () {
            return {
                productMsg: '',
                link: '',
                configUrl: ipconfig.url,
                sub_sale: '',
                sale_time: '',
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
                    ids: [],
                    sale_time: ''
                },
                product: {
                    id: '',
                    did: '',
                    name: '',
                    serial_code: '',
                    version: '',
                    sub_inter: '',
                    sub_maker: '',
                    type: '',
                    status: '0',
                    repair_time: ''
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
                        align: 'center'
                    },
                    {
                        title: '设备名称',
                        key: 'name',
                        align: 'center'
                    },
                    {
                        title: '类别批次',
                        key: 'type',
                        align: 'center'
                    },
                    {
                        title: '生产商',
                        key: 'sub_inter',
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
                                ]),
                                h('Button', {
                                    props: {
                                        type: 'error'
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
                                            type: 'trash-a'
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
            this.getLink();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            selectPro() {
                Product.getList(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10,
                    name: this.productMsg
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            getLink() {
                Product.getLink(this).then((res) => {
                    if (res.success) {
                        this.link = res.data.fileUrl;
                    }
                });
            },
            handleUpload() {
                this.loadingStatus = true;
                setTimeout(() => {
                    this.loadingStatus = false;
                }, 1000);
                return true;
            },
            handleFormatError(file) {
                this.$Notice.warning({
                    title: '文件格式错误',
                    desc: '文件： ' + file.name + ' 不是xls或xlsx格式，请重新上传！'
                });
            },
            fileSuccess(res) {
                this.link = res.link;
                this.$Notice.warning({
                    title: '成功',
                    desc: '上传模板成功'
                });
            },
            handleSuccess(res) {
                if (!res.success) {
                    this.$Notice.warning({
                        title: '错误',
                        desc: res.message + ',请检查文件格式后重试！'
                    });
                } else {
                    this.$Notice.warning({
                        title: '成功',
                        desc: res.message
                    });
                }
                this.getProducts();
            },
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
                if (!id && this.selected.length === 0) return this.$Message.warning('请先选择用户');
                if (!id && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                } else {
                    ids = [id];
                }
                this.productSale = {
                    ids: ids,
                    sub_sale: this.sub_sale,
                    sale_time: this.sale_time
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
                    serial_code: '',
                    version: '',
                    sub_inter: '',
                    sub_maker: '',
                    type: '',
                    status: '0',
                    repair_time: ''
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
                    serial_code: obj.serial_code,
                    version: obj.version,
                    sub_inter: obj.sub_inter,
                    sub_maker: obj.sub_maker,
                    type: obj.type,
                    status: obj.status,
                    repair_time: obj.repair_time
                };
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = obj.id;
            },
            deleteProduct(id) {
                var ids;
                if (!id && this.selected.length === 0) return this.$Message.warning('请先选择需要删除的内容');
                if (!id && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                } else {
                    ids = [id];
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除设备？',
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
