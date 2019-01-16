<template>
    <div>
        <Row class="expand-row">
            <Col span="4">
            <span class="expand-key">生日：</span>
            <span class="expand-value">{{ row.birthday }}</span>
            </Col>
            <Col span="4">
            <span class="expand-key">语言：</span>
            <span class="expand-value">{{ row.lang}}</span>
            </Col>
            <Col span="4">
            <span class="expand-key">备注：</span>
            <span class="expand-value">{{ row.remar }}</span>
            </Col>
            <Col span="4">
            <span class="expand-key">是否匿名：</span>
            <span class="expand-value" v-text=" row.isAnonymous===0?'否':'是'"></span>
            </Col>
            <Col span="6" padding="20px">
            <span class="expand-key">地址：</span>
            <span class="expand-value">{{ row.address }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">机智云用户名：</span>
            <span class="expand-value">{{ row.gizwitsUsername }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">机智云密码：</span>
            <span class="expand-value">{{ row.gizwitsPassword }}</span>
            </Col>
        </Row>
    </div>
</template>
<script>
    export default {
        props: {
            row: Object
        }
    };
</script>
