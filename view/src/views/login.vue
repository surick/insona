<template>
    <div class="login-background">
        <div class="login">
            <img src="https://app.astralapp.com/images/logo.svg" alt="">
            <div class="input-box">
                <input type="text" placeholder="账号" v-model="form.loginName" @keyup.enter="login">
            </div>
            <div class="input-box">
                <input type="password" placeholder="密码" v-model="form.password" @keyup.enter="login">
            </div>
            <Button type="primary" :loading="loading" class="login-btn" @click="login">
                <span v-if="!loading">登 录</span>
                <span v-else>登 录</span>
            </Button>
        </div>
    </div>
</template>

<script>
import { User } from '@/http';
export default {
    data() {
        return {
            form: {
                loginName: '',
                password: ''
            },
            loading: false
        };
    },
    methods: {
        login() {
            if (this.form.loginName === '' || this.form.password === '') {
                this.$Message.warning('请将信息填写完整！');
            } else {
                this.loading = true;
                User.login(this, this.form)
                    .then(res => {
                        if (res.success) {
                            let user = res.data;
                            localStorage.token = user.token;
                            localStorage.user = user.name;
                            this.$store.commit('setAvator', user.headImgUrl);
                            User.getPower(this).then(res => {
                                this.$store.state.access = res;
                                this.$store.commit('updateMenulist');
                                this.$router.push({
                                    name: 'home'
                                });
                            });
                        } else {
                            this.$Message.error(res.message);
                            this.loading = false;
                        }
                    }).catch(() => {
                        this.loading = false;
                    });
            }
        }
    }
};
</script>

<style>
.login {
    width: 400px;
    position: absolute;
    left: 50%;
    top: 40%;
    transform: translate(-50%, -50%);
    text-align: center;
}

.login img {
    width: 100%;
}

.login input {
    margin: 0 auto;
    padding-top: 15px;
    display: block;
    background: 0;
    border: 0;
    border-bottom: 1px solid #fff;
    color: #fff;
    font-weight: 500;
    outline: 0;
    padding: 15px;
    text-align: center;
    width: 100%;
    font-size: 16px;
    box-sizing: border-box;
}

.login input::placeholder {
    color: #fff;
}

.login .input-box {
    padding: 5px 50px;
}

.login-btn {
    background: hsla(0, 0%, 100%, .08);
    border: 1px solid hsla(0, 0%, 100%, .65);
    border-radius: 3px;
    -webkit-box-shadow: 0 0 8px hsla(0, 0%, 100%, .3);
    box-shadow: 0 0 8px hsla(0, 0%, 100%, .3);
    color: #fff;
    display: inline-block;
    font-size: 18px;
    padding: .78rem 1.3rem;
    text-decoration: none;
    text-shadow: none;
    width: 300px;
    box-sizing: border-box;
    margin-top: 20px;
    cursor: pointer;
}

.login-btn:hover {
    background: hsla(0, 0%, 100%, .12);
    border: 1px solid #fff;
    color: #fff;
}

.login-background {
    height: 100vh;
    overflow: hidden;
    background: url(/static/img/login_bg.jpg) no-repeat;
    background-size: cover;
}
</style>
