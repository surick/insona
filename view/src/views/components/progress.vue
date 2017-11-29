<template>
    <div>
        <div v-for="map in maps">
            <Row>
                <Col span="3" style="padding-left: 10px">
                <span style="color: #fff;">{{map.city}}</span>
                </Col>
                <Col span="15">
                <div class="progress">
                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0"
                         aria-valuemax="100"
                         :style="{width:((map.normalProduct/total.totalProduct* 100).toFixed(0))+'%'}">
                        <span style="padding-left: 10px;color: #aab2b3">{{map.normalProduct}}</span>
                    </div>
                </div>
                </Col>
                <Col span="4" style="padding-left: 30px">
                <span style="color: #fff">{{(map.normalProduct/total.totalProduct* 100).toFixed(0)}}%</span>
                </Col>
            </Row>
        </div>
    </div>
</template>
<script>
    export default {
        props: ['maps', 'total']
    };
</script>
<style>
    .progress {
        width: auto;
        height: 18px;
        margin-bottom: 20px;
        overflow: hidden;
        background-color: lightblue;
        border-radius: 9px;
        -webkit-box-shadow: inset 0 1px 2px rgba(0, 0, 0, .1);
        box-shadow: inset 0 1px 2px rgba(0, 0, 0, .1);
    }

    .progress-bar {
        float: left;
        width: 0;
        height: 100%;
        font-size: 12px;
        line-height: 20px;
        color: #fff;
        background-color: #fff;
        -webkit-box-shadow: inset 0 -1px 0 rgba(0, 0, 0, .15);
        box-shadow: inset 0 -1px 0 rgba(0, 0, 0, .15);
        -webkit-transition: width .6s ease;
        -o-transition: width .6s ease;
        transition: width .6s ease;
    }

</style>
