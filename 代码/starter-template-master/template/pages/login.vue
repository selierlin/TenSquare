<template>
  <div>
    <div class="wrapper loginsign">
      <div class="item signup">
        <div class="form">
          <h3 class="loginsign-title">注册新账号</h3>

          <form class="sui-form" novalidate="true">
            <div class="control-group">
              <label for="inputname" class="control-label">登录名</label>
              <div class="controls">
                <input
                  type="text"
                  id="inputname"
                  placeholder="登录名"
                  class="input-xlarge"
                  data-rules="required"
                  v-model="pojo.loginname"
                />
              </div>
            </div>
            <div class="control-group">
              <label for="inputname" class="control-label">名字</label>
              <div class="controls">
                <input
                  type="text"
                  id="inputname"
                  placeholder="真实姓名或常用昵称"
                  class="input-xlarge"
                  data-rules="required"
                  v-model="pojo.nickname"
                />
              </div>
            </div>
            <div class="different">
              <div class="radio-content">
                <div id="a1" class="phone">
                  <div class="control-group number">
                    <input
                      type="text"
                      placeholder="仅支持大陆手机号"
                      class="input-xlarge"
                      data-rules="required|mobile"
                      v-model="pojo.mobile"
                    />
                  </div>
                  <div class="control-group code">
                    <div class="input-append">
                      <input
                        id="appendedInputButton"
                        type="text"
                        placeholder="短信验证"
                        class="span2 input-large msg-input"
                        v-model="code"
                      />
                      <button type="button" @click="sendsms" class="sui-btn msg-btn">获取验证码</button>
                    </div>
                  </div>
                  <div class="control-group">
                    <label for="inputpassword" class="control-label">密码</label>
                    <div class="controls">
                      <input
                        type="text"
                        id="inputpassword"
                        placeholder="请输入6-16位密码"
                        class="input-xlarge"
                        v-model="pojo.password"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="control-group btn-signup">
              <label class="control-label"></label>
              <div class="controls">
                <label>
                  <input type="checkbox" v-model="checked" />
                  <span class="type-text" style="font-size:12px;">同意协议并接受《服务条款》</span>
                </label>
                <button type="button" class="sui-btn btn-danger btn-yes" @click="register">注 册</button>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="item">
        <div class="form">
          <h3 class="loginsign-title">用户登录</h3>
          <form class="sui-form login-form">
            <div class="control-group">
              <label for="inputname" class="control-label">手机号：</label>
              <div class="controls">
                <input
                  type="text"
                  id="inputname"
                  placeholder="11位手机号"
                  class="input-xlarge"
                  data-rules="required"
                  v-model="mobile"
                />
              </div>
            </div>
            <div class="control-group">
              <label for="inputpassword" class="control-label">密码：</label>
              <div class="controls">
                <input
                  type="text"
                  id="inputpassword"
                  placeholder="输入登录密码"
                  class="input-xlarge"
                  data-rules="required"
                  v-model="password"
                />
              </div>
            </div>
            <div class="controls">
              <label>
                <input type="checkbox" name="remember-me" />
                <span class="type-text" style="font-size:12px;">记住登录状态</span>
              </label>
              <button type="button" class="sui-btn btn-danger btn-yes" @click="login">登 录</button>
            </div>
            <div id="weixin"></div>
            <div class="other-methods"></div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import "~/assets/css/page-sj-person-loginsign.css";

import "~/assets/css/page-sj-person-loginsign.css";
import userApi from "@/api/user";
import { setUser } from "@/utils/auth";
export default {
  data() {
    return {
      pojo: {},
      code: "",
      checked: false,
      mobile: "",
      password: ""
    };
  },
  mounted() {
    var obj = new Object({
      id: "weixin",
      appid: "wx3bdb1192c22883f3",
      scope: "snsapi_login",
      redirect_uri: "http://note.java.itcast.cn/weixinlogin"
    });
  },
  head: {
    script: [
      { src: "http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js" }
    ]
  },
  methods: {
    sendsms() {
      userApi.sendsms(this.pojo.mobile).then(res => {
        // alert(res.data.message);
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
      });
    },
    login() {
      let flag = false;
      flag = this.validate2(); // 校验数据
      if (!flag) {
        return flag;
      }
      userApi.login(this.mobile, this.password).then(res => {
        if (res.data.flag) {
          setUser(
            res.data.data.token,
            res.data.data.name,
            res.data.data.avatar
          );
          location.href = "/manager"; //用户中心
        } else {
          this.$message({
            message: res.data.message,
            type: "error"
          });
          this.mobile = "";
          this.password = "";
        }
      });
    },

    register() {
      let flag = false;
      flag = this.validate(); // 校验数据
      if (!flag) {
        return flag;
      }
      userApi.register(this.pojo, this.code).then(res => {
        if (res.data.flag) {
          this.$message({
            message: "注册成功",
            type: "success"
          });
        } else {
          this.$message({
            message: "注册出错",
            type: "error"
          });
        }
        this.code = "";
        this.pojo = {};
      });
    },
    validate() {
      let loginname = this.pojo.loginname;
      let nickname = this.pojo.nickname;
      let password = this.pojo.password;
      if (!loginname) {
        this.altermessage("请输入用户名");
      } else if (!nickname) {
        this.altermessage("请输入昵称");
      } else if (!this.pojo.mobile) {
        this.altermessage("请输入手机号");
      } else if (!this.code) {
        this.altermessage("请输入验证码");
      } else if (!this.pojo.password) {
        this.altermessage("请输入密码");
      } else if (loginname.length < 6 || loginname.length > 20) {
        this.altermessage("用户名长度应位于6-20之间");
      } else if (nickname.length < 6 || nickname.length > 20) {
        this.altermessage("昵称长度应位于6-20之间");
      } else if (!this.validatePhone(this.pojo.mobile)) {
        this.altermessage("手机号码格式不正确");
      } else if (password.length < 6) {
        this.altermessage("密码长度不少于6位");
      } else if (!this.checked) {
        this.altermessage("请勾选服务条款");
      } else {
        return true;
      }
      return false;
    },
    validate2() {
      let mobile = this.mobile;
      let password = this.password;
      if (!mobile) {
        this.altermessage("请输入手机号");
      } else if (!password) {
        this.altermessage("请输入密码");
      } else if (!this.validatePhone(mobile)) {
        this.altermessage("手机号码格式不正确");
      } else if (password.length < 6) {
        this.altermessage("密码长度不少于6位");
      } else {
        return true;
      }
      return false;
    },
    validatePhone(phone) {
      var re = /^1[3|4|5|8][0-9]\d{4,8}$/;
      return re.test(phone);
    },
    altermessage(message) {
      this.$message({
        message: message,
        type: "error"
      });
    }
  }
};
</script>