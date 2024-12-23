<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                资料管理
            </div>

            <div slot="extra">
                <access-ctrl :name="'SYS_INS_MATERIAL'" ref="access">
                <Button type="primary" @click="addMaterial()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="error" @click="deleteMaterial()">
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
            :title="['图片新增', '图片编辑'][addOrEdit]"
            :mask-closable="false"
            width="1000px">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">资料标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="material.title" placeholder="资料标题"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">资料类型</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="material.type" placeholder="资料类型"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">生效</div>
                    </Col>
                    <Col span="18" style="line-height: 32px;">
                    <i-switch v-model="material.enabled">
                        <span slot="open">是</span>
                        <span slot="close">否</span>
                    </i-switch>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">备注</div>
                    </Col>
                    <Col span="18" style="line-height: 32px;">
                    <UE :defaultMsg=defaultMsg :config=UEconfig ref="ue">
                    </UE>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveMaterial()">确定</Button>
            </div>
        </Modal>
        <!-- 上传图片 -->
        <Modal
            v-model="uploadModal"
            :title="'上传图片'"
            :mask-closable="false">
            <div class="modal-body" align="center">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">资料标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="material.title" placeholder="资料标题"/>
                    </Col>
                </Row>
                <template>
                    <div>
                        <Upload
                            :before-upload="handleUpload"
                            :show-upload-list="false"
                            :data="this.material"
                            :action="this.configUrl+'/image/materialUpload'">
                            <Button type="ghost" icon="ios-cloud-upload-outline">选择图片</Button>
                            <Button type="text" :loading="loadingStatus">
                                {{ loadingStatus ? '正在上传' : '待上传 ' }}
                            </Button>
                        </Upload>
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
    import textRow from './insona-text.vue';
    import Material from '../../http/material.js';
    import VueFroala from 'vue-froala-wysiwyg';
    import ipconfig from '@/config/ipconfig';
    import UE from '../components/ueditor.vue';
    export default {
        name: 'other_material',
        components: {textRow, VueFroala, UE},
        data: function () {
            return {
                defaultMsg: '',
                UEconfig: {
                    initialFrameWidth: null,
                    initialFrameHeight: 350
                },
                configUrl: ipconfig.url,
                uploadModal: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                image: null,
                loadingStatus: false,
                total: 0,
                current: 1,
                material: {
                    id: '',
                    title: '',
                    imgUrl: '待上传',
                    type: '',
                    enabled: 1,
                    content: '待编辑'
                },
                materialDetail: {
                    title: '',
                    imgUrl: '',
                    type: ''
                },
                columns: [
                    {
                        type: 'selection',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '标题',
                        key: 'title',
                        align: 'center'
                    },
                    {
                        title: '类型',
                        key: 'type',
                        align: 'center'
                    },
                    {
                        title: '状态',
                        key: 'enabled',
                        width: 100,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.enabled === 1 ? '正常' : '冻结';
                        }
                    },
                    {
                        title: '图片',
                        align: 'center',
                        key: 'avatar',
                        width: 135,
                        render: (h, params) => {
                            return h('div', [
                                h('img', {
                                    attrs: {
                                        src: this.configUrl + params.row.imgUrl.replace('material/', 'material/thumb/')
                                    },
                                    style: {
                                        width: '100px',
                                        height: '100px'
                                    }
                                })
                            ]);
                        }
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
                                            this.editMaterial(params.row);
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
                                            this.uploadModel(params.row);
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
                            return h(textRow, {
                                props: {
                                    row: params.row
                                }
                            });
                        }
                    }
                ],
                data: [],
                model: ''
            };
        },
        mounted() {
            this.current = 1;
            this.getMaterial();
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
                this.uploadModal = false;
                this.getMaterial();
            },
            uploadModel(material) {
                this.uploadModal = true;
                this.material = {
                    id: material.id,
                    title: material.title,
                    content: material.content
                };
            },
            getMaterial() {
                Material.getMaterial(this, {
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
                this.getMaterial();
            },
            addMaterial() {
                window.UE.getEditor('editor').setContent(this.defaultMsg);
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.material = {
                    title: '',
                    imgUrl: '待上传',
                    type: '',
                    enabled: 1,
                    content: '待编辑'
                };
            },
            editMaterial(material) {
                window.UE.getEditor('editor').setContent(material.content);
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = material.id;
                this.material = {
                    title: material.title,
                    type: material.type,
                    enabled: material.enabled,
                    content: material.content
                };
            },
            saveMaterial() {
                let content = this.$refs.ue.getUEContent();
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.material, ['title'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Material.addMaterial(this, this.material, content).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getMaterial();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.material, ['title'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Material.updateMaterial(this, this.editId, this.material, content).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getMaterial();
                        }
                    });
                }
            },
            deleteMaterial(ids) {
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
                        Material.deleteMaterial(this, ids).then((res) => {
                            if (res.success) this.getMaterial();
                        });
                    }
                });
            }
        }
    };

</script>
