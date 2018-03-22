<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div class="access">
        <Card>
            <div slot="title">日志管理</div>
            <div slot="extra">
                <Select v-model="opt" style="width:200px" clearable>
                    <Option v-for="item in optList" :value="item.name" :key="">{{ item.name }}</Option>
                </Select>
                <Select v-model="userId" style="width:200px" clearable>
                    <Option v-for="item in userList" :value="item.createUser" :key="">{{ item.createUserName }}</Option>
                </Select>                       
                <DatePicker type="date" placement="bottom-end" placeholder="开始时间" style="width: 200px" v-model="start"></DatePicker>
                <DatePicker type="date" placement="bottom-end" placeholder="结束时间" style="width: 200px" v-model="end"></DatePicker>
                <Button type="primary" shape="circle" icon="ios-search" @click="getLog()">查询</Button>
            </div> 
            <i-table border :columns="columns1" :data="data"></i-table>
            <div style="margin: 10px;overflow: hidden">
                <div style="float: right;">
                    <Page :total="total" :current="current" @on-change="changePage"></Page>
                </div>
            </div>
         </Card>
    </div>
</template>

<script>
    import InsonaLog from '../../http/insona-log';
    export default {
        data () {
            return {
                current: 1,
                total: 0,
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
                opt: '',
                start: '',
                userId: '',
                end: '',
                data: [],
                optList: [],
                userList: []
            };
        },
        mounted() {
            this.current = 1;
            this.getLog();
            this.getOpt();
            this.getUserName();
        },
        methods: {
            getLog() {
                console.log(this.end + '====');
                InsonaLog.getInsonaLog(this, {
                    pageIndex: this.current - 1,
                    pageSize: 10,
                    opt: this.opt,
                    createUser: this.userId,
                    startTime: this.start ? this.start.getTime() : '',
                    endTime: this.end ? this.end.getTime() : ''
                }).then(res => {
                    if (res.success) {
                        this.data = res.data.list;
                        this.total = res.data.total;
                    }
                });
            },
            changePage(page) {
                this.current = page;
                this.getLog();
            },
            getOpt() {
                InsonaLog.getOperation(this, {
                }).then(res => {
                    if (res.success) {
                        this.optList = res.data;
                    }
                });
            },
            getUserName() {
                InsonaLog.getUserName(this, {
                }).then(res => {
                    if (res.success) {
                        this.userList = res.data;
                    }
                });
            }
        }
    };
</script>

