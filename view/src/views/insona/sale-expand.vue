<template>
    <div>
        <Row class="expand-row">
            <Col>
            <span class="expand-key">详细信息：</span>
            <span class="expand-value">{{ row.extract }}</span>
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
