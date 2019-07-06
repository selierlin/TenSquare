<template>
  <div>
    <div class="tab-nav">
      <div class="wrapper">
        <ul class="fl sui-nav nav-tabs navbar-dark">
          <router-link to="/qa/label/0" active-class="active" tag="li">
            <a>首页</a>
          </router-link>
          <router-link
            :to="'/qa/label/'+item.id"
            active-class="active"
            tag="li"
            v-for="(item,index) in labelList"
            :key="index"
          >
            <a>{{item.labelname}}</a>
          </router-link>
        </ul>
        <span class="fr more">
          <a href="/qa/alltags" target="_blank">更多</a>
        </span>
        <div class="clearfix"></div>
      </div>
    </div>
    <nuxt-child />
  </div>
</template>
<script>
import "~/assets/css/page-sj-qa-logined.css";
import labelApi from "@/api/label";
export default {
  asyncData({ params, error }) {
    return labelApi.toplist().then(res => {
      return { labelList: res.data.data };
    });
  }
};
</script>