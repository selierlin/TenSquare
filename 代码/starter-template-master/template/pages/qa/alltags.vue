<template>
  <div>
    <div class="wrapper all-tags">
      <div class="tags-head">
        <h2 class="tag-title">标签</h2>
        <p class="tag-second">标签是最有效的内容组织形式，正确的使用标签能更快的发现和解决你的问题。</p>
      </div>
      <div class="alltags-card">
        <ul class="yui3-g tags-list" style="display:block;">
          <li class="tag-item yui3-u-1-4" v-for="(tag,index) in tags" :key="index">
            <div class="inner">
              <h5 class="title">
                <a :href="'/qa/tag/'+tag.id" target="_blank">{{tag.name}}</a>
              </h5>

              <div class="guanzhu">
                <a class="sui-btn btn-guanzhu color" @click="thumbup(index)">加关注</a>
                <span class="guannum">
                  <i class="num">{{tag.focuscount}}</i>人关注
                </span>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>
import "~/assets/css/page-sj-qa-allTag.css";
import { getUser } from "@/utils/auth";
import tagApi from "@/api/tag";
export default {
  asyncData() {
    return tagApi.search(1, 10, { state: "1" }).then(res => {
      return { tags: res.data.data.rows };
    });
  },
  methods: {
    thumbup(index) {
      if (getUser().name === undefined) {
        this.$message({
          message: "必须登陆才可以关注哦~",
          type: "warning"
        });
        return;
      }
    }
  }
};
</script>
