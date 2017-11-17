<style lang="less">
    @import '../../styles/common.less';
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
            :title="['文件新增', '文件编辑'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">文件标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="document.name" placeholder="文件标题"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">文件链接</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="document.fileUrl" placeholder="文件链接"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">文件类型</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="document.fileType" placeholder="文件类型"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">排序</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="document.sortNo" placeholder="排序"></Input>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveDocument()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import expandRow from './insona-expand.vue';
    import Document from '../../http/document.js';

    export default {
        name: 'other_document',
        components: {expandRow},
        data: function () {
            return {
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                total: 0,
                current: 1,
                documentList: [],
                document: {
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
                        width: 300,
                        align: 'center'
                    },
                    {
                        title: '文件类型',
                        key: 'fileType',
                        width: 220,
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 230,
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
                                            this.editDocument(params.row);
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
            this.getDocument();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
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
            editDocument(document) {
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = document.id;
                this.document = {
                    name: document.name,
                    fileUrl: document.fileUrl,
                    fileType: document.fileType,
                    sortNo: document.sortNo,
                    isDelete: 0
                };
            },
            saveDocument() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.document, ['name'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Document.addDocument(this, this.document).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getDocument();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.document, ['fileUrl'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Document.updateDocument(this, this.editId, this.document).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getDocument();
                        }
                    });
                }
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
                    content: '确定删除用户？',
                    okText: '确定',
                    cancelText: '取消',
                    onOk: () => {
                        Document.deleteDocument(this, ids).then((res) => {
                            if (res.success) this.getDocument();
                        });
                    }
                });
            }
        }
    };
</script>
