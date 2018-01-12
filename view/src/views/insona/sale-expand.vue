<template>
    <div>
        <Row class="expand-row">
            <Col>
            <span class="expand-key">详细信息：</span>
            <span class="expand-value">{{ row.extract }}</span>
            </Col>
        </Row>
        <Row class="expand-row">
            <Col>
            <span class="expand-key">驳回原因：</span>
            <span class="expand-value">{{ row.reason=='' ?'审批中': row.reason }}</span>
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
