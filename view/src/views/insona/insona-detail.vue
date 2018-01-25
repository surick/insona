<template>
    <div>
        <Row class="expand-row">
            <Col span="6">
            <span class="expand-key">生产日期：</span>
            <span class="expand-value">{{ row.make_time }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">入库日期：</span>
            <span class="expand-value">{{ row.into_time }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">技术方案：</span>
            <span class="expand-value">{{ row.technology }}</span>
            </Col>
            <Col span="6">
            <span class="expand-key">备注：</span>
            <span class="expand-value">{{ row.remark }}</span>
            </Col>
            <Col span="6" class="margin-top-10">
            <span class="expand-key">创建人：</span>
            <span class="expand-value">{{ row.createUserName }}</span>
            </Col>
            <Col span="6" class="margin-top-10">
            <span class="expand-key">创建时间：</span>
            <span class="expand-value">{{ row.createTime }}</span>
            </Col>
            <Col span="6" class="margin-top-10">
            <span class="expand-key">修改人：</span>
            <span class="expand-value">{{ row.updateUserName }}</span>
            </Col>
            <Col span="6" class="margin-top-10">
            <span class="expand-key">修改时间：</span>
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
