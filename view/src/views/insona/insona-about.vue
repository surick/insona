<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">
               关于我们
            </div>
            <UE :defaultMsg=defaultMsg :config=UEconfig ref="ue">
            </UE>

            <div slot="extra">
                <access-ctrl :name="'SYS_INS_MATERIAL'" ref="access">
                <Button type="primary" @click="updateAbout()">
                    <Icon type="android-add"></Icon>
                    保存
                </Button>
                </access-ctrl>
            </div>
        </Card>
    </div>
</template>

<script>
    import textRow from './insona-text.vue';
    import About from '../../http/about.js';
    import VueFroala from 'vue-froala-wysiwyg';
    import ipconfig from '@/config/ipconfig';
    import UE from '../components/ueditor.vue';
    export default {
        name: 'other_about',
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
                editId: '',
                image: null,
                loadingStatus: false,
                about: {
                    id: '1',
                    content: '待编辑'
                },
                data: [],
                model: ''
            };
        },
        mounted() {
            this.getAbout();
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
                this.getAbout();
            },
            getAbout() {
                About.getAbout(this, 1).then((res) => {
                    if (res.success) {
                        this.about = res.data;
                        this.defaultMsg = res.data.content;
                        // window.UE.getEditor('editor').setContent(res.data.content);
                    }
                });
            },
            updateAbout() {
                let content = this.$refs.ue.getUEContent();
            
                About.updateAbout(this, 1, content).then(res => {
                    if (res.success) {
                        this.getAbout();
                    }
                });
            }
        }
    };

</script>
