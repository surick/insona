<template>
    <div>
        <Row class="expand-row">
            <Col span="6">
                <span class="expand-key">生日：</span>
                <span class="expand-value">{{ row.birthday }}</span>
            </Col>
            <Col span="6">
                <span class="expand-key">邮箱：</span>
                <span class="expand-value">{{ row.email }}</span>
            </Col>
            <Col span="6">
                <span class="expand-key">创建人：</span>
                <span class="expand-value">{{ row.crt_name }}</span>
            </Col>
            <Col span="6">
                <span class="expand-key">创建时间：</span>
                <span class="expand-value">{{ row.crt_time }}</span>
            </Col>
            <Col span="6" class="margin-top-10">
                <span class="expand-key">更新人：</span>
                <span class="expand-value">{{ row.upd_name }}</span>
            </Col>
            <Col span="6" class="margin-top-10">
                <span class="expand-key">更新时间：</span>
                <span class="expand-value">{{ row.upd_time }}</span>
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
