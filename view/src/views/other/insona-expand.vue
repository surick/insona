<template>
    <div>
        <Row class="expand-row">
            <Col span="6">
            <span class="expand-key">创建人：</span>
            <span class="expand-value">{{ row.createUser }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">创建时间：</span>
            <span class="expand-value">{{ row.createTime }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">更新人：</span>
            <span class="expand-value">{{ row.updateUser}}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">更新时间：</span>
            <span class="expand-value">{{ row.updateTime }}</span>
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
