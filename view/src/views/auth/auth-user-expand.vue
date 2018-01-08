<template>
    <div>
        <Table border :columns="columns" :data="row.children"></Table>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                columns: [
                    {
                        title: '用户名',
                        key: 'userName',
                        width: 120,
                        align: 'center'
                    },
                    {
                        title: '联系人',
                        key: 'name',
                        width: 120,
                        align: 'center'
                    },
                    {
                        title: '手机号',
                        key: 'mobilePhone',
                        width: 120,
                        align: 'center'
                    },
                    {
                        title: '用户分类',
                        key: 'type',
                        width: 120,
                        align: 'center'
                    },
                    {
                        title: '详细地址',
                        key: 'address',
                        align: 'center'
                    }
                ]
            };
        },
        props: {
            row: Object
        }
    };
</script>
