<template>
  <div>
    <div class="wrapper tag-item">
      <div class="fl left-list">
        <div class="tc-data-list">
          <div class="tc-list">
            <ul
              class="detail-list"
              v-infinite-scroll="loadMore"
              infinite-scroll-disabled="busy"
              infinite-scroll-distance="10"
            >
              <li class="qa-item" v-for="(item,index) in items" :key="index">
                <div class="fl record">
                  <div class="number">
                    <div class="border useful">
                      <p class="usenum" @click="thumbup(index)">
                        <a class="zan">
                          <i :class="'fa fa-thumbs-up '+item.zan" aria-hidden="true"></i>
                        </a>
                      </p>
                      <p class="zannum">{{item.thumbup}}</p>
                    </div>
                    <div class="border answer">
                      <a href="#" class="star">
                        <i class="fa fa-star-o" aria-hidden="true"></i>
                      </a>
                    </div>
                  </div>
                </div>
                <div class="info">
                  <p class="text">
                    <router-link :to="'/spit/'+item.id">{{item.content}}</router-link>
                  </p>
                  <div class="other">
                    <div class="fl date">
                      <span>{{item.publishtime}}</span>
                    </div>
                    <div class="fr remark">
                      <a href="#" data-toggle="modal" data-target="#shareModal" class="share">
                        <i class="fa fa-share-alt" aria-hidden="true"></i> 分享
                      </a>
                      <a href="#" data-toggle="modal" data-target="#remarkModal" class="comment">
                        <i class="fa fa-commenting" aria-hidden="true"></i> 回复
                      </a>
                    </div>
                  </div>
                </div>
                <div class="clearfix"></div>
              </li>
            </ul>
          </div>
        </div>
        <!-- <script>
    $(function () {
        $(".zan").click(function () {
            $(this).children(".fa").toggleClass("color");
            var c = parseInt($(this).parent().siblings(".zannum").html());
            c = c++;
        });
        $(".star").click(function () {
            console.log("eeee");
            $(this).children(".fa").removeClass("fa-star-o").addClass("fa-star color");
        });
        $(".detail-list").unbind("scroll").bind("scroll", function (e) {
            var sum = this.scrollHeight;
            if (sum - 10 <= $(this).scrollTop() + $(this).height()) {
                $(".detail-list").append($("li").clone());
            }
        });
    })

        </script>-->
      </div>
      <div class="fl right-tag">
        <div class="block-btn">
          <p>来个匿名吐槽，发泄一下你心中的怒火吧！</p>
          <router-link class="sui-btn btn-block btn-share" to="/spit/submit">发吐槽</router-link>
        </div>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</template>
<script>
import "~/assets/plugins/bootstrap/dist/css/bootstrap.min.css";
import "~/assets/css/page-sj-spit-index.css";
import spitApi from "@/api/spit";
import { getUser } from "@/utils/auth";
export default {
  data() {
    return {
      pageNo: 1
    };
  },
  asyncData() {
    return spitApi.search(1, 10, { state: "1" }).then(res => {
      let tmp = res.data.data.rows.map(item => {
        return {
          ...item,
          zan: ""
        };
      });
      return { items: tmp };
    });
  },
  methods: {
    loadMore() {
      this.pageNo++;
      spitApi.search(this.pageNo, 10, { state: "1" }).then(res => {
        let tmp = res.data.data.rows.map(item => {
          return {
            ...item,
            zan: ""
          };
        });
        this.items = this.items.concat(tmp);
      });
    },
    thumbup(index) {
      if (getUser().name === undefined) {
        this.$message({
          message: "必须登陆才可以点赞哦~",
          type: "warning"
        });
        return;
      }
      if (this.items[index].zan === "color") {
        this.$message({
          message: "不可以重复点赞哦~",
          type: "warning"
        });
        return;
      }
      this.items[index].zan = "color";
      spitApi.thumbup(this.items[index].id).then(res => {
        if (res.data.flag) {
          this.items[index].thumbup++;
        }
      });
    }
  }
};
</script>
