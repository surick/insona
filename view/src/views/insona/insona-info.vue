<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                信息管理
            </div>

            <div slot="extra">
                <access-ctrl :name="'SYS_INS_INFO'" ref="access">
                <Button type="primary" @click="addInfo()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="error" @click="deleteInfo()">
                    <Icon type="trash-a"></Icon>
                    删除
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

        <!-- 编辑与新增 -->
        <Modal
            v-model="addAndEditModal"
            :title="['信息新增', '信息编辑'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">信息标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="info.title" placeholder="信息标题"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">信息分类</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="info.infoType" placeholder="信息分类"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">排序</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="info.sortNo" placeholder="排序"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">详细信息</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="info.description" placeholder="详细信息"></Input>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveInfo()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import expandRow from './insona-expand.vue';
    import Info from '../../http/info.js';

    export default {
        name: 'other_info',
        components: {expandRow},
        data: function () {
            return {
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                total: 0,
                current: 1,
                infoList: [],
                info: {
                    infoType: '',
                    title: '',
                    description: '',
                    sortNo: ''
                },
                infoDetail: {
                    infoType: 0,
                    title: '',
                    description: ''
                },
                columns: [
                    {
                        type: 'selection',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '信息标题',
                        key: 'title',
                        align: 'center'
                    },
                    {
                        title: '信息分类',
                        key: 'infoType',
                        align: 'center'
                    },
                    {
                        title: '信息描述',
                        key: 'description',
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 284,
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
                                            this.editInfo(params.row);
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
            this.getInfo();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getInfo() {
                Info.getInfo(this, {
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
                this.getInfo();
            },
            addInfo() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.info = {
                    infoType: '',
                    title: '',
                    description: '',
                    sortNo: ''
                };
            },
            editInfo(info) {
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = info.id;
                this.info = {
                    title: info.title,
                    infoType: info.infoType,
                    description: info.description,
                    sortNo: info.sortNo
                };
            },
            saveInfo() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.info, ['title'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Info.addInfo(this, this.info).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getInfo();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.info, ['infoType'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Info.updateInfo(this, this.editId, this.info).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getInfo();
                        }
                    });
                }
            },
            deleteInfo(ids) {
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
                        Info.deleteInfo(this, ids).then((res) => {
                            if (res.success) this.getInfo();
                        });
                    }
                });
            }
        }
    };
</script>
