<template>
    <i-table border :columns="columns1" :data="data"></i-table>
</template>
<script>
    import InsonaLog from '../../http/insona-log';

    export default {
        data () {
            return {
                columns1: [
                    {
                        title: '操作',
                        key: 'opt',
                        align: 'center'
                    },
                    {
                        title: '响应时间',
                        key: 'actionTime',
                        align: 'center'
                    },
                    {
                        title: '请求时间',
                        key: 'createTime',
                        align: 'center'
                    },
                    {
                        title: '请求用户',
                        key: 'createUserName',
                        align: 'center'
                    },
                    {
                        title: '日志操作主机',
                        key: 'createHost',
                        align: 'center'
                    }
                ],
                data: []
            };
        },
        mounted() {
            this.current = 1;
            this.getLog();
        },
        methods: {
            getLog() {
                InsonaLog.getInsonaLog(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10
                }).then(res => {
                    if (res.success) {
                        this.data = res.data;
                    }
                });
            }
        }
    };
</script>

