<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                家庭背景
            </div>

            <div slot="extra">

                <Button type="primary" @click="addHome()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="error" @click="deleteHome()">
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
            :title="['家庭背景新增', '家庭背景编辑'][addOrEdit]"
            :mask-closable="false">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">家庭背景标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="home.title" placeholder="家庭背景标题"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">家庭背景链接</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="home.imgUrl" placeholder="家庭背景链接"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">排序</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="home.sortNo" placeholder="排序"></Input>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveHome()">确定</Button>
            </div>
        </Modal>
        <Modal
            v-model="uploadModal"
            :title="'上传图片'"
            :mask-closable="false">
            <div class="modal-body" align="center">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">家庭背景标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="home.title" placeholder="家庭背景标题"/>
                    </Col>
                </Row>
                <template>
                    <div>
                        <Upload
                            :before-upload="handleUpload"
                            :show-upload-list="false"
                            :data="this.home"
                            action="http://localhost:8080/insona/home/upload">
                            <Button type="ghost" icon="ios-cloud-upload-outline">Select the file to upload</Button>
                            <Button type="text" :loading="loadingStatus">
                                {{ loadingStatus ? 'Uploading' : 'Click to upload' }}
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
    import expandRow from './insona-expand.vue';
    import Home from '../../http/insona-home.js';

    export default {
        name: 'other_home',
        components: {expandRow},
        data: function () {
            return {
                uploadModal: false,
                addAndEditModal: false,
                addOrEdit: 0,
                editId: '',
                image: null,
                file: null,
                loadingStatus: false,
                total: 0,
                current: 1,
                home: {
                    id: '',
                    title: '',
                    imgUrl: '',
                    sortNo: ''
                },
                homeDetail: {
                    title: '',
                    imgUrl: '',
                    sortNo: ''
                },
                columns: [
                    {
                        type: 'selection',
                        width: 50,
                        align: 'center'
                    },
                    {
                        title: '家庭背景标题',
                        key: 'title',
                        width: 230,
                        align: 'center'
                    },
                    {
                        title: '家庭背景链接',
                        key: 'imgUrl',
                        width: 330,
                        align: 'center'
                    },
                    {
                        title: '家庭背景',
                        key: 'avatar',
                        columns: {
                            'width': '50px'
                        },
                        render: (h, params) => {
                            return h('div', [
                                h('img', {
                                    attrs: {
                                        src: 'http://127.0.0.1:8080' + params.row.imgUrl
                                    },
                                    style: {
                                        width: '40px',
                                        height: '40px'
                                    }
                                })
                            ]);
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 330,
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
                                            this.editHome(params.row);
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
            this.getHome();
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
                this.getHome();
            },
            uploadModel(home) {
                this.uploadModal = true;
                this.home = {
                    id: home.id,
                    title: home.title
                };
            },
            getHome() {
                Home.getHome(this, {
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
                this.getHome();
            },
            addHome() {
                this.addOrEdit = 0;
                this.addAndEditModal = true;
                this.home = {
                    title: '',
                    imgUrl: '',
                    sortNo: ''
                };
            },
            editHome(home) {
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = home.id;
                this.home = {
                    title: home.title,
                    imgUrl: home.imgUrl,
                    sortNo: home.sortNo
                };
            },
            saveHome() {
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.home, ['title'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Home.addHome(this, this.home).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getHome();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.home, ['imgUrl'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Home.updateHome(this, this.editId, this.home).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getHome();
                        }
                    });
                }
            },
            deleteHome(ids) {
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
                        Home.deleteHome(this, ids).then((res) => {
                            if (res.success) this.getHome();
                        });
                    }
                });
            }
        }
    };

</script>
