<template>
    <div>
        <script id="editor" type="text/plain"></script>
    </div>
</template>
<script>
import '../../../static/ue/ueditor.config.js';
import '../../../static/ue/ueditor.all.min.js';
import '../../../static/ue/lang/zh-cn/zh-cn.js';
import '../../../static/ue/ueditor.parse.min.js';

export default {
    name: 'ue',
    data() {
        return {
            editor: null
        };
    },
    props: {
        defaultMsg: {
            type: String
        },
        config: {
            type: Object
        }
    },
    mounted() {
        this.editor = window.UE.getEditor('editor', this.config); // 初始化UE
    },
    methods: {
        getUEContent() { // 获取内容方法
            return this.editor.getContent();
        }
    },
    destroyed() {
        this.editor.destroy();
    }
};
</script>
