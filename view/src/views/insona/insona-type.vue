<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                设备类别
            </div>

            <div slot="extra">

                <Button type="primary" @click="addType()">
                    <Icon type="android-add"></Icon>
                    新建
                </Button>
                <Button type="error" @click="deleteType()">
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

        <!-- 绑定与解绑 -->
        <Modal
            v-model="addAndEditModal"
            :title="['新增类别', '编辑类别'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">类型id</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.type_id" placeholder="类型id"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">类型名称</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.type_name" placeholder="类型名称"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">生产商</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.maker" placeholder="生产商"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">型号</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.model_no" placeholder="型号"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">appid</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.appid" placeholder="appid"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">secret</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.appsecret" placeholder="asecret"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">key</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.product_key" placeholder="product_key"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">批次</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.batch" placeholder="批次"></Input>
                    </Col>
                </Row>

                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">技术方案</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.technology" placeholder="技术方案"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">通讯类型</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="type.communication" placeholder="通讯类型"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">生产日期</div>
                    </Col>
                    <Col span="18">
                    <DatePicker type="date" placeholder="生产日期" v-model="type.make_time"></DatePicker>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">入库时间</div>
                    </Col>
                    <Col span="18">
                    <DatePicker type="date" placeholder="入库时间" v-model="type.into_time" style="width: auto"></DatePicker>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">是否可用</div>
                    </Col>
                    <Col span="18">
                    <i-switch size="large" v-model="type.enable">
                        <span slot="open">启用</span>
                        <span slot="close">禁用</span>
                    </i-switch>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">备注</div>
                    </Col>
                    <Col span="18">
                    <Input type="textarea" v-model="type.remark" placeholder="备注"></Input>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveType()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import Type from '../../http/type.js';
    import typeRow from './insona-detail.vue';

    export default {
        name: 'insona_type',
        components: {typeRow},
        data: function () {
            return {
                addAndEditModal: false,
                addOrEdit: '',
                editId: '',
                total: 0,
                current: 1,
                type: {
                    id: '',
                    type_id: '',
                    type_name: '',
                    maker: '',
                    model_no: '',
                    make_time: '',
                    appid: '',
                    appsecret: '',
                    product_key: '',
                    into_time: '',
                    technology: '',
                    communication: '',
                    enable: '',
                    remark: '',
                    batch: ''
                },
                columns: [
                    {
                        type: 'selection',
                        key: 'id',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '类型名称',
                        key: 'type_name',
                        align: 'center'
                    },
                    {
                        title: '生产商',
                        key: 'maker',
                        align: 'center'
                    },
                    {
                        title: '型号',
                        key: 'model_no',
                        align: 'center'
                    },
                    {
                        title: '通讯类型',
                        key: 'communication',
                        align: 'center'
                    },
                    {
                        title: '生产批次',
                        key: 'batch',
                        align: 'center'
                    },
                    {
                        width: 70,
                        title: '状态',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                params.row.enable ? '启用' : '禁用'
                            ]);
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 270,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'success'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.newType(params.row);
                                        }
                                    }
                                }, [
                                    h('Icon', {
                                        style: {
                                            marginRight: '5px'
                                        }
                                    }),
                                    '新批次'
                                ]),
                                h('Button', {
                                    props: {
                                        type: 'primary'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.editType(params.row);
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
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.deleteType(params.row.id);
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
                            return h(typeRow, {
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
            this.getTypes();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getTypes() {
                Type.getTypes(this, {
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
            changePage(page) {
                this.current = page;
                this.getTypes();
            },
            addType() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.type = {
                    type_id: '',
                    type_name: '',
                    maker: '',
                    model_no: '',
                    make_time: '',
                    appid: '',
                    appsecret: '',
                    product_key: '',
                    into_time: '',
                    technology: '',
                    communication: '',
                    enable: true,
                    remark: '',
                    batch: ''
                };
            },
            saveType() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.type, ['type_id'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Type.addType(this, this.type).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getTypes();
                        }
                    });
                } else {
                    Type.updateType(this, this.editId, this.type).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getTypes();
                        }
                    });
                }
            },
            editType(obj) {
                this.type = {
                    type_id: obj.type_id,
                    type_name: obj.type_name,
                    maker: obj.maker,
                    model_no: obj.model_no,
                    make_time: obj.make_time,
                    appid: obj.appid,
                    appsecret: obj.appsecret,
                    product_key: obj.product_key,
                    into_time: obj.into_time,
                    technology: obj.technology,
                    communication: obj.communication,
                    enable: obj.enable,
                    remark: obj.remark,
                    batch: obj.batch
                };
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = obj.id;
            },
            deleteType(id) {
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
                    content: '确定删除该类别？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        Type.deleteType(this, ids).then((res) => {
                            if (res.success) this.getTypes();
                        });
                    }
                });
            },
            newType(obj) {
                this.type = {
                    type_id: obj.type_id,
                    type_name: obj.type_name,
                    maker: obj.maker,
                    model_no: obj.model_no,
                    make_time: obj.make_time,
                    appid: obj.appid,
                    appsecret: obj.appsecret,
                    product_key: obj.product_key,
                    into_time: obj.into_time,
                    technology: obj.technology,
                    communication: obj.communication,
                    enable: obj.enable,
                    remark: obj.remark,
                    batch: ''
                };
                this.addOrEdit = 0;
                this.addAndEditModal = true;
            }
        }
    };
</script>
