<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
                通知管理
            </div>

            <div slot="extra">
                <Button type="primary" @click="addMessage()">
                    <Icon type="android-add"></Icon>
                    新增
                </Button>
                <Button type="error" @click="deleteMessage()">
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
            :title="['消息通知新增', '消息通知编辑'][addOrEdit]"
            :mask-closable="false"
            width="1000px">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">通知标题</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="message.title" placeholder="通知标题"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">排序</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="message.sort_no" placeholder="排序"></Input>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">是否推送</div>
                    </Col>
                    <Col span="18" style="line-height: 32px;">
                    <i-switch v-model="message.is_published">
                        <span slot="open">是</span>
                        <span slot="close">否</span>
                    </i-switch>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">内容</div>
                    </Col>
                    <Col span="18" style="line-height: 32px;">
                    <UE id="ue" :message="message.content" :config=UEconfig ref="ue"></UE>
<!--
                     <froala :tag="'textarea'" :config="config" v-model="material.content">Init text</froala>
-->
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveMessage()">确定</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import textRow from './insona-text.vue';
    import Message from '../../http/insona-message.js';
    import VueFroala from 'vue-froala-wysiwyg';
    import ipconfig from '@/config/ipconfig';
    import UE from '../components/ue.vue';
    export default {
        name: 'other_message',
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
                total: 0,
                current: 1,
                message: {
                    id: '',
                    title: '',
                    content: '待编辑',
                    is_published: false,
                    sort_no: 0
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
                        title: '状态',
                        key: 'isPublished',
                        width: 100,
                        align: 'center',
                        render: (h, params) => {
                            return params.row.isPublished === 1 ? '已推送' : '未推送';
                        }
                    },
                    {
                        title: '推送人',
                        key: 'publishUser',
                        align: 'center'
                    },
                    {
                        title: '推送时间',
                        key: 'publishTime',
                        align: 'center'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 190,
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
                                            this.editMessage(params.row);
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
                            return h(textRow, {
                                props: {
                                    row: params.row
                                }
                            });
                        }
                    }
                ],
                data: [],
                config: {
                    toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline',
                        'strikeThrough', 'subscript', 'superscript', '|', 'fontFamily',
                        'fontSize', 'color', 'inlineStyle', 'paragraphStyle', '|',
                        'paragraphFormat', 'align', 'formatOL', 'formatUL', 'outdent',
                        'indent', '-', 'insertImage',
                        'embedly', 'insertFile', 'insertTable', '|',
                        'specialCharacters', 'insertHR', 'selectAll', '|',
                        'print', 'spellChecker', 'help', 'html', '|', 'undo', 'redo'],
                    imageUploadURL: ipconfig.url + '/image/upload',
                    fileUploadURL: ipconfig.url + '/file/upload',
                    imageManagerDeleteURL: ipconfig.url + '/image/delete',
                    imageDefaultAlign: 'left',
                    imageDefaultDisplay: 'inline',
                    events: {
                        'froalaEditor.initialized': function () {
                        }
                    }
                },
                model: ''
            };
        },
        mounted() {
            this.current = 1;
            this.getMessage();
        },
        computed: {
            avatorPath() {
                return localStorage.avatorImgPath;
            }
        },
        methods: {
            getMessage() {
                Message.getMessage(this, {
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
                this.getMessage();
            },
            addMessage() {
                this.message = {
                    id: '',
                    title: '',
                    content: '待编辑',
                    is_published: false,
                    sort_no: 0
                };
                this.addOrEdit = 0;
                this.addAndEditModal = true;
            },
            editMessage(message) {
                console.log(this.$refs.ue);
                this.message = {
                    id: message.id,
                    title: message.title,
                    content: message.content,
                    is_published: message.isPublished === 1,
                    sort_no: message.sortNo
                };
                this.addOrEdit = 1;
                this.addAndEditModal = true;
                this.editId = message.id;
            },
            saveMessage() {
                let content = this.$refs.ue.getUEContent();
                if (this.addOrEdit === 0) {
                    if (this.$commonFun.checkObject(this.message, ['id'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }
                    Message.addMessage(this, this.message, content).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getMessage();
                        }
                    });
                } else {
                    if (this.$commonFun.checkObject(this.message, ['id'])) {
                        return this.$Message.warning('请将信息填写完整！');
                    }

                    Message.updateMessage(this, this.editId, this.message, content).then(res => {
                        if (res.success) {
                            this.addAndEditModal = false;
                            this.getMessage();
                        }
                    });
                }
            },
            deleteMessage(ids) {
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
                        Message.deleteMessage(this, ids).then((res) => {
                            if (res.success) this.getMessage();
                        });
                    }
                });
            }
        }
    };

</script>
