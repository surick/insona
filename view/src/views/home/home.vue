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
                    <h2 align="center">{{(this.total.totalOnline / this.total.totalProduct * 100).toFixed(2)}}</h2>
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
                        <ve-map :extend="option"></ve-map>
                    </div>
                </Card>
                </Col>
                <Col span="12" width="400px">
                <Card :bordered="false" style="background-color: #15c1df;margin: 10px">
                    <div>
                        <ve-bar :data="chartData" :settings="chartSettings"></ve-bar>
                    </div>
                </Card>
                </Col>
            </div>
        </Row>
    </div>
</template>


<script>
    import VeMap from 'v-charts/lib/map';
    import VeBar from 'v-charts/lib/bar';
    import Home from '../../http/home.js';
    // import { User } from '@/http';
    export default {
        components: {VeMap, VeBar},
        name: 'home',
        created() {
            this.chartData = {
                columns: ['日期', '成本', '利润'],
                rows: [
                    {'日期': '1月1号', '成本': 123},
                    {'日期': '1月2号', '成本': 1223},
                    {'日期': '1月3号', '成本': 2123},
                    {'日期': '1月4号', '成本': 4123},
                    {'日期': '1月5号', '成本': 3123},
                    {'日期': '1月6号', '成本': 7123}
                ]
            };
            this.chartSettings = {
                dimension: ['成本']
            };
            this.chartSettings = {
                position: 'china',
                label: false,
                itemStyle: {
                    normal: {
                        borderColor: '#eee'
                    }
                }
            };
        },
        data() {
            return {
                option: {
                    title: {
                        text: '设备分布',
                        left: '5px',
                        textStyle: {
                            color: '#fff',
                            fontWeight: '90'
                        }
                    },
                    tooltip: {},
                    visualMap: {
                        type: 'continuous',
                        min: 0,
                        max: 4123,
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
                            symbol: 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z',
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
                },
                table: {
                    id: '',
                    city: '',
                    normalProduct: 0,
                    errorProduct: 0,
                    userNumber: 0
                },
                tables: '',
                data: [],
                total: {
                    totalProduct: 0,
                    totalOnline: 0,
                    totalError: 0,
                    totalUser: 0
                }
            };
        },
        mounted() {
            this.getTable();
            // User.getPower(this);
        },
        methods: {
            ctrlAccess() {
                this.$refs.access.updateAccess();
            },
            getTable() {
                this.total = {
                    totalProduct: 0,
                    totalOnline: 0,
                    totalError: 0,
                    totalUser: 0
                };
                Home.getTable(this).then((res) => {
                    if (res.success) {
                        this.data = res.data;
                        console.log(this.data);
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
            }
        }
    };
</script>
