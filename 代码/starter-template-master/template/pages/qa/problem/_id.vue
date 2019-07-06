<template>
  <div>
    <div class="wrapper qa-content">
      <div class="qa-title">
        <div class="fl title">
          <h2>{{pojo.title}}</h2>
          <p>
            <span class="tag">PHPPP</span>
            <span class="author">{{pojo.nickname}}</span>
            <span>{{pojo.createtime}} 提问 · {{pojo.updatetime}} 更新</span>
          </p>
        </div>
        <div class="fr share">
          <h4>分享到：</h4>
          <ul class="share-go">
            <li>
              <img src="~/assets/img/widget-weibo.png" alt />
            </li>
            <li>
              <img src="~/assets/img/widget-weixin.png" alt />
            </li>
            <li>
              <img src="~/assets/img/widget-weibo.png" alt />
            </li>
            <li>
              <img src="~/assets/img/widget-weixin.png" alt />
            </li>
          </ul>
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="fl left-list">
        <div class="problem-detail">
          <div class="title-intro">
            <div class="sidebar">
              <button
                type="button"
                class="like"
                data-placement="right"
                data-toggle="tooltip"
                title="问题对人有帮助，内容完整，我也想知道答案"
              >
                <i class="fa fa-caret-up" aria-hidden="true"></i>
              </button>
              <span class="count">0</span>
              <button
                type="button"
                class="hate"
                data-placement="right"
                data-toggle="tooltip"
                title="问题没有实际价值，缺少关键内容，没有改进余地"
              >
                <i class="fa fa-caret-down" aria-hidden="true"></i>
              </button>
            </div>
            <div class="title">
              <div class="detail">{{pojo.content}}</div>
              <div class="clearfix"></div>
              <div class="operate">
                <span class="time">{{pojo.createtime}} 提问</span>
                <span class="comment">评论</span>
                <span class="edit">编辑</span>
                <span class="jubao">举报</span>
              </div>
              <div class="comment-box">
                <form class="comment-form">
                  <textarea row="1" placeholder="使用评论询问更多信息或提出修改意见，请不要在评论里回答问题"></textarea>
                  <button type="submit" class="sui-btn submit-comment">提交评论</button>
                </form>
                <div class="tips">
                  <p>评论支持部分 Markdown 语法：**bold**_italic_[link](http://example.com)> 引用`code`- 列表。 同时，被你 @ 的用户也会收到通知</p>
                </div>
              </div>
            </div>
          </div>
          <div class="answer-intro">
            <h4 class="answer-num">1个回答</h4>
            <div v-for="(item,index) in commentlist" :key="index">
              <div class="sidebar">
                <button
                  type="button"
                  class="like"
                  data-placement="right"
                  data-toggle="tooltip"
                  title="问题对人有帮助，内容完整，我也想知道答案"
                >
                  <i class="fa fa-caret-up" aria-hidden="true"></i>
                </button>
                <span class="count">{{item.thumbup}}</span>
                <button
                  type="button"
                  class="hate"
                  data-placement="right"
                  data-toggle="tooltip"
                  title="问题没有实际价值，缺少关键内容，没有改进余地"
                >
                  <i class="fa fa-caret-down" aria-hidden="true"></i>
                </button>
              </div>
              <div class="title">
                <p>{{item.content}}</p>
                <div class="operate">
                  <div class="tool pull-left">
                    <span class="time">{{item.publishtime}}提问</span>
                    <span class="comment">评论</span>
                    <span class="edit">编辑</span>
                    <span class="jubao">举报</span>
                  </div>

                  <div class="myanswer pull-right">
                    <img src="~/assets/img/widget-photo.jpg" alt />
                    <a href="#">我的回答</a>
                  </div>
                  <div class="clearfix"></div>
                </div>
                <div class="comment-box">
                  <form class="comment-form">
                    <textarea row="1" placeholder="使用评论询问更多信息或提出修改意见，请不要在评论里回答问题"></textarea>
                    <button type="submit" class="sui-btn submit-comment">提交评论</button>
                  </form>
                  <div class="tips">
                    <p>评论支持部分 Markdown 语法：**bold**_italic_[link](http://example.com)> 引用`code`- 列表。 同时，被你 @ 的用户也会收到通知</p>
                  </div>
                </div>
              </div>
              <div class="clearfix"></div>
              <br />
              <br />
              <br />
            </div>
          </div>
          <div class="edit-answer">
            <h4>撰写答案</h4>
            <div class="edit-tip">
              <h4>你正在撰写答案</h4>
              <p>如果你是要对问题或其他回答进行点评或询问，请使用“评论”功能。</p>
            </div>
            <div>
              <div
                class="quill-editor"
                :content="content"
                @change="onEditorChange($event)"
                v-quill:myQuillEditor="editorOption"
              ></div>
              <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="save">提 交</el-button>
              </span>
            </div>
          </div>
        </div>
        <!-- <script>
    $(function () {
        $(".comment").click(function () {
            $(this).parents(".operate").siblings(".comment-box").toggle();
        });
        $(".sidebar .fa").hover(function () {
            $(this).css("color", "#e64620");
        }, function () {
            $(this).css("color", "#000");
        })
    })

        </script>-->
      </div>
      <div class="fl right-tag">
        <div class="similar-problem">
          <h3 class="title">相似问题</h3>
          <ul class="problem-list">
            <li class="list-item">
              <p class="list-title">求一份浏览器中html css javascript jquery ajax的渲染顺序与原理！！</p>
              <p class="list-info">
                <a href>1 回答</a> | 已解决
              </p>
            </li>
            <li class="list-item">
              <p class="list-title">求一份浏览器中html css javascript jquery ajax的渲染顺序与原理！！</p>
              <p class="list-info">
                <a href>1 回答</a> | 已解决
              </p>
            </li>
            <li class="list-item">
              <p class="list-title">求一份浏览器中html css javascript jquery ajax的渲染顺序与原理！！</p>
              <p class="list-info">
                <a href>1 回答</a> | 已解决
              </p>
            </li>
            <li class="list-item">
              <p class="list-title">求一份浏览器中html css javascript jquery ajax的渲染顺序与原理！！</p>
              <p class="list-info">
                <a href>1 回答</a> | 已解决
              </p>
            </li>
          </ul>
        </div>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</template>
<script>
import "~/assets/css/page-sj-qa-detail.css";
import problemApi from "@/api/problem";
import axios from "axios";
export default {
  data() {
    return {
      dialogVisible: false,
      content: "",
      editorOption: {
        // some quill options
        modules: {
          toolbar: [
            [{ size: ["small", false, "large"] }],
            ["bold", "italic"],
            [{ list: "ordered" }, { list: "bullet" }],
            ["link", "image"]
          ]
        }
      }
    };
  },
  asyncData({ params }) {
    return axios
      .all([problemApi.findById(params.id), problemApi.commentlist(params.id)])
      .then(
        axios.spread(function(pojo, commentlist) {
          return {
            pojo: pojo.data.data,
            commentlist: commentlist.data.data
          };
        })
      );
  },
  methods: {
    onEditorChange({ editor, html, text }) {
      console.log("editor change!", editor, html, text);
      this.content = html;
    },
    save() {
      problemApi.save({ content: this.content, id: this.pojo.id }).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        if (res.data.flag) {
          //刷新数据
          problemApi.commentlist(this.pojo.id).then(res => {
            this.commentlist = res.data.data;
          });
        }
      });
    }
  }
};
</script>
<style>
.quill-editor {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
}
</style>