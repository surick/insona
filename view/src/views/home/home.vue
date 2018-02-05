<style lang="less">
    @import "./home.less";
    @import "../../styles/common.less";
</style>
<template>
    <div>
        <Row style="background: #eeeeee;margin-bottom: 10px;color: #ffffff">
            <Col span="6">
            <Card :bordered="false" style="background-color: #15c1df;margin: 10px;">
                <div style="line-height: 40px">
                    <p align="center">激活设备总数(台)</p>
                    <h2 align="center">{{this.total.totalProduct}}</h2>
                </div>
            </Card>
            </Col>
            <Col span="6">
            <Card :bordered="false" style="background-color: #15c1df; margin: 10px">
                <div style="line-height: 40px">
                    <p align="center">设备在线率(%)</p>
                    <h2 align="center">{{(this.total.totalOnline / this.total.totalProduct * 100).toFixed()}}</h2>
                </div>
            </Card>
            </Col>
            <Col span="6">
            <Card :bordered="false" style="background-color: #15c1df;margin: 10px">
                <div style="line-height: 40px">
                    <p align="center">故障设备总数(台)</p>
                    <h2 align="center">{{this.total.totalError}}</h2>
                </div>
            </Card>
            </Col>
            <Col span="6">
            <Card :bordered="false" style="background-color: #15c1df;margin: 10px">
                <div style="line-height: 40px">
                    <p align="center">用户总数(人)</p>
                    <h2 align="center">{{this.total.totalUser}}</h2>
                </div>
            </Card>
            </Col>
        </Row>
        <Row class="margin-bottom-5">
            <div>
                <Col span="12" width="400px">
                <Card :bordered="false" style="background-color: rgba(0,20,41,0.78);margin: 10px">
                    <div style="width: 600px;">
                        <ve-map :extend="extend"></ve-map>
                    </div>
                </Card>
                </Col>
                <Col span="12" width="400px">
                <Card :bordered="false" style="background-color: #15c1df;margin: 10px">
                    <div style="width: 600px;height: 400px">
                        <div class="margin-bottom-10" style="color: #fff">分布排行</div>
                        <br/>
                        <MyProgress :maps="this.maps" :total="this.total"></MyProgress>
                    </div>
                </Card>
                </Col>
            </div>
        </Row>
        <Modal v-model="editPasswordModal" :title="'初次登录修改密码'" :mask-closable=false :width="500">
            <div class="modal-body">
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">旧密码</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.password" placeholder="旧密码"/>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">新密码</div>
                    </Col>
                    <Col span="18">
                    <Input v-model="user.newPassword" placeholder="新密码"/>
                    </Col>
                </Row>
                <Row class="margin-bottom-10">
                    <Col span="6">
                    <div class="input-label">确认新密码</div>
                    </Col>
                    <Col span="18">
                    <Input placeholder="确认新密码"/>
                    </Col>
                </Row>
            </div>
            <div slot="footer">
                <Button type="primary" size="large" @click="saveEditPass">确定</Button>
            </div>
        </Modal>
    </div>
</template>


<script>
    import VeMap from 'v-charts/lib/map';
    import VeBar from 'v-charts/lib/bar';
    import Home from '../../http/home.js';
    import MyProgress from '../components/progress.vue';
    // import { User } from '@/http';
    export default {
        components: {VeMap, VeBar, MyProgress},
        name: 'home',
        data() {
            return {
                editPasswordModal: false,
                user: {
                    password: '',
                    newPassword: '',
                    isFirst: true
                },
                table: {
                    id: '',
                    city: '',
                    normalProduct: 0,
                    errorProduct: 0,
                    userNumber: 0
                },
                tables: '',
                data: [
                    {name: '北京', value: null},
                    {name: '天津', value: null},
                    {name: '上海', value: null},
                    {name: '重庆', value: null},
                    {name: '河北', value: null},
                    {name: '河南', value: null},
                    {name: '云南', value: null},
                    {name: '辽宁', value: null},
                    {name: '黑龙江', value: null},
                    {name: '湖南', value: null},
                    {name: '安徽', value: null},
                    {name: '山东', value: null},
                    {name: '新疆', value: null},
                    {name: '江苏', value: null},
                    {name: '浙江', value: null},
                    {name: '江西', value: null},
                    {name: '湖北', value: null},
                    {name: '广西', value: null},
                    {name: '甘肃', value: null},
                    {name: '山西', value: null},
                    {name: '内蒙古', value: null},
                    {name: '陕西', value: null},
                    {name: '吉林', value: null},
                    {name: '福建', value: null},
                    {name: '贵州', value: null},
                    {name: '广东', value: null},
                    {name: '青海', value: null},
                    {name: '西藏', value: null},
                    {name: '四川', value: null},
                    {name: '宁夏', value: null},
                    {name: '海南', value: null},
                    {name: '台湾', value: null},
                    {name: '香港', value: null},
                    {name: '澳门', value: null}
                ],
                extend: {},
                map: [],
                maps: [],
                max: 0,
                total: {
                    totalProduct: 0,
                    totalOnline: 0,
                    totalError: 0,
                    totalUser: 0
                }
            };
        },
        mounted() {
            this.getUser();
            this.getTable();
            this.getMap();
            // User.getPower(this);
        },
        methods: {
            ctrlAccess() {
                this.$refs.access.updateAccess();
            },
            getMap() {
                Home.getMap(this).then((res) => {
                    if (res.success) {
                        this.map = res.data.list;
                        this.max = res.data.max;
                        this.data.forEach(item => {
                            this.map.forEach(res => {
                                if (item.name === res.name) {
                                    item.value = res.value;
                                }
                            });
                        });
                    }
                    this.extend = {
                        title: {
                            text: '设备分布',
                            left: '5px',
                            textStyle: {
                                color: '#fff',
                                fontSize: 12
                            }
                        },
                        tooltip: {},
                        visualMap: {
                            type: 'continuous',
                            min: 0,
                            max: this.max,
                            top: 'bottom',
                            zlevel: '3',
                            text: ['高', '低'],
                            realtime: false,
                            calculable: true,
                            color: ['#15c1df', 'lightskyblue', 'lightblue'],
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        geo: {
                            map: 'china',
                            label: {
                                normal: {
                                    show: false,
                                    textStyle: {
                                        color: 'rgba(0,0,0,0.4)'
                                    }
                                }
                            },
                            itemStyle: {
                                normal: {
                                    borderColor: '#ffffff'
                                },
                                emphasis: {
                                    areaColor: null,
                                    shadowOffsetX: 0,
                                    shadowOffsetY: 0,
                                    shadowBlur: 20,
                                    borderWidth: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        },
                        series: [
                            {
                                type: 'scatter',
                                coordinateSystem: 'geo',
                                symbolSize: 20,
                                symbolRotate: 35,
                                label: {
                                    normal: {
                                        formatter: '{b}',
                                        position: 'right',
                                        show: false
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                itemStyle: {
                                    normal: {
                                        color: '#F06C00'
                                    }
                                }
                            },
                            {
                                name: 'categoryA',
                                type: 'map',
                                geoIndex: 0,
                                zoom: 0.5,
                                data: this.data
                            }
                        ]
                    };
                });
            },
            getTable() {
                Home.getTable(this).then((res) => {
                    if (res.success) {
                        this.maps = res.data;
                        res.data.forEach(item => {
                            this.total = {
                                totalProduct: this.total.totalProduct + item.normalProduct,
                                totalOnline: this.total.totalOnline + item.onlineProduct,
                                totalError: this.total.totalError + item.errorProduct,
                                totalUser: this.total.totalUser + item.userNumber
                            };
                        });
                    }
                });
            },
            getUser() {
                Home.getUser(this).then((res) => {
                    if (res.success) {
                        this.user = {
                            isFirst: !res.data.isFirst
                        };
                    }
                    this.editPasswordModal = this.user.isFirst;
                });
            },
            saveEditPass() {
                Home.putPassword(this, this.user).then(res => {
                    if (res.success) {
                        this.editPasswordModal = false;
                        this.getTable();
                        this.getMap();
                    }
                });
            }
        }
    };
</script>
