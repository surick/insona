<template>
    <div>
        <Row class="expand-row">
            <span class="expand-key">备注：</span>
            <span v-html="row.content"></span>
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
