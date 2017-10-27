<template>
    <div class="m-page-loader">
        <div class="m-blockui">
            <span>
                {{ message || '加载中...' }}
            </span>
            <span>
                <div class="m-loader m-loader--brand"></div>
            </span>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        message: String
    }
};
</script>

<style>
.m-page-loader {
    background: transparent;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 9999;
}

.m-blockui {
    background: #ffffff;
    -webkit-box-shadow: 0px 0px 15px 1px rgba(113, 106, 202, 0.2);
    -moz-box-shadow: 0px 0px 15px 1px rgba(113, 106, 202, 0.2);
    box-shadow: 0px 0px 15px 1px rgba(113, 106, 202, 0.2);
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -ms-border-radius: 4px;
    -o-border-radius: 4px;
    border-radius: 4px;
    top: 50%;
    position: fixed;
    left: 50%;
    margin-top: -18px;
    margin-left: -61px;
    z-index: 10000;
    display: table;
    table-layout: fixed;
}

.m-blockui>span {
    display: table-cell;
    vertical-align: middle;
    padding: 8px 15px;
    font-size: 14px;
    font-weight: 400;
}

@-webkit-keyframes m-loader-rotate {
    to {
        transform: rotate(360deg);
    }
}

@-moz-keyframes m-loader-rotate {
    to {
        transform: rotate(360deg);
    }
}

@-o-keyframes m-loader-rotate {
    to {
        transform: rotate(360deg);
    }
}

@keyframes m-loader-rotate {
    to {
        transform: rotate(360deg);
    }
}

.m-blockui>span>.m-loader,
.m-blockui>span>.m-spinner {
    margin-right: 10px;
}

.m-loader {
    position: relative;
}

.m-loader:before {
    width: 16px;
    height: 16px;
    margin-top: -8px;
    margin-left: -8px;
    border-top-width: 2px;
    border-right-width: 2px;
}

.m-loader:before {
    content: '';
    box-sizing: border-box;
    position: absolute;
    top: 50%;
    left: 50%;
    border-top: 2px solid #07d;
    border-right: 2px solid transparent;
    -webkit-border-radius: 50%;
    -moz-border-radius: 50%;
    -ms-border-radius: 50%;
    -o-border-radius: 50%;
    border-radius: 50%;
    -webkit-animation: m-loader-rotate 0.6s linear infinite;
    -moz-animation: m-loader-rotate 0.6s linear infinite;
    -ms-animation: m-loader-rotate 0.6s linear infinite;
    -o-animation: m-loader-rotate 0.6s linear infinite;
    animation: m-loader-rotate 0.6s linear infinite;
}
</style>
