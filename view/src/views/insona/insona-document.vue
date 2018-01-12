<style lang="less">
    @import '../../styles/common.less';
    @import '../auth/auth.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                文件服务
            </div>

            <div slot="extra">
                <Button type="primary" @click="addDocument()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="error" @click="deleteDocument()">
                    <Icon type="trash-a"></Icon>
                    删除
                </Button>
                <access-ctrl :name="'SYS_INS_SALE'" ref="access">
                    <Button type="primary" icon="ios-search" @click="saleDocument()">
                        经销商文件
                    </Button>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_MAKE'" ref="access">
                    <Button type="info" icon="ios-search" @click="makeDocument()">
                        生产商文件
                    </Button>
                </access-ctrl>
                <access-ctrl :name="'SYS_INS_OTHER'" ref="access">
                    <Button type="warning" icon="ios-search" @click="otherDocument()">
                        其他文件
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

        <Modal
            v-model="EditModal"
            :title="'设备绑定'"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">文件名称</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="document.name" placeholder="文件名称"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">可用设备</div>
                    </Col>
                    <Select v-model="document.fileType" filterable style="width: 250px">
                        <Option v-for="item in types" :value="item.type_name+item.batch" :key="item.type_name+item.batch">{{ item.type_name }}{{ item.batch }}</Option>
                    </Select>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveFileProduct()">确定</Button>
            </div>
        </Modal>
        <!-- 上传 -->
        <Modal
            v-model="addAndEditModal"
            :title="'文件新增'"
            :mask-closable="false">
            <div class="modal-body" align="center">
                <template>
                    <div>
                        <Row class="margin-bottom-10">
                            <div>
                                <Upload
                                    :before-upload="handleUpload"
                                    :show-upload-list="false"
                                    :action="this.configUrl+'/file/DocumentUpload'">
                                    <Button type="ghost" icon="ios-cloud-upload-outline">选择文件</Button>
                                    <Button type="text" :loading="loadingStatus">
                                        {{ loadingStatus ? '正在上传' : '待上传 ' }}
                                    </Button>
                                </Upload>
                            </div>
                        </Row>
                    </div>
                </template>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="doUpload()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import expandRow from './insona-expand.vue';
    import Document from '../../http/document.js';
    import ipconfig from '@/config/ipconfig';

    export default {
        name: 'other_document',
        components: {expandRow},
        data: function () {
            return {
                configUrl: ipconfig.url,
                types: [],
                EditModal: false,
                uploadModal: false,
                file: null,
                loadingStatus: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                total: 0,
                selected: '',
                productDetail: [],
                current: 1,
                documentList: [],
                document: {
                    id: '',
                    name: '',
                    fileUrl: '',
                    fileType: '',
                    sortNo: '',
                    isDelete: 0
                },
                documentDetail: {
                    name: '',
                    fileUrl: '',
                    fileType: '',
                    sortNo: '',
                    isDelete: 0
                },
                columns: [
                    {
                        type: 'selection',
                        width: 50,
                        key: 'id',
                        align: 'center'
                    },
                    {
                        title: '文件标题',
                        key: 'name',
                        width: 230,
                        align: 'center'
                    },
                    {
                        title: '文件链接',
                        key: 'fileUrl',
                        width: 500,
                        align: 'center'
                    },
                    {
                        title: '文件类型',
                        key: 'fileType',
                        width: 150,
                        align: 'center'
                    }, {
                        title: '操作',
                        key: 'action',
                        width: 110,
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
                                            this.editFile(params.row);
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
            this.getDocument();
            this.getTypes();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            handleUpload() {
                this.loadingStatus = true;
                setTimeout(() => {
                    this.loadingStatus = false;
                    this.$Message.success('Success');
                }, 1000);
                return true;
            },
            doUpload() {
                this.getDocument();
                this.addAndEditModal = false;
            },
            uploadModel(document) {
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = document.id;
                this.uploadModal = true;
                this.document = {
                    name: document.name
                };
            },
            getTypes() {
                Document.types(this).then((res) => {
                    if (res.success) {
                        this.types = res.data;
                        console.log(this.types);
                    }
                });
            },
            getDocument() {
                Document.getDocument(this, {
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
                this.getDocument();
            },
            addDocument() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.document = {
                    name: '',
                    fileUrl: '',
                    fileType: '',
                    sortNo: '',
                    isDelete: 0
                };
            },
            editFile(obj) {
                this.document = {
                    id: obj.id,
                    name: obj.name,
                    fileUrl: obj.fileUrl,
                    fileType: obj.fileType,
                    sortNo: obj.sortNo,
                    isDelete: 0
                };
                this.editId = obj.id;
                this.EditModal = true;
            },
            saveFileProduct() {
                Document.updateDocument(this, this.editId, this.document).then(res => {
                    if (res.success) {
                        this.EditModal = false;
                        this.getDocument();
                    }
                });
            },
            deleteDocument(ids) {
                if (!ids && this.selected.length === 0) return this.$Message.warning('请先选择需要删除的用户');
                if (!ids && this.selected.length > 0) {
                    ids = this.selected.map(item => {
                        return item.id;
                    });
                }
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定删除文件？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        Document.deleteDocument(this, ids).then((res) => {
                            if (res.success) this.getDocument();
                        });
                    }
                });
            },
            saleDocument() {
                Document.fileList(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10,
                    label: '00100010001'
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            makeDocument() {
                Document.fileList(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10,
                    label: '0010001'
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            otherDocument() {
                Document.fileList(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10,
                    label: '001'
                }).then((res) => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            }
        }
    };
</script>
