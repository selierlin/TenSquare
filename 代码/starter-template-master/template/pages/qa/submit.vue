<template>
  <div>
    <div class="qa-submit-form wrapper">
      <form action class="sui-form">
        <div class="input-title">
          <input
            type="text"
            placeholder="标题：一句话说清问题，用问号结尾"
            class="input-xfat input-xxlarge title"
            v-model="pojo.title"
          />
        </div>
        <div class="tags-area">
          <div class="input-tags">
            <input
              type="text"
              placeholder="标签，如:php可使用逗号，分号； 来分隔"
              class="input-xfat input-xxlarge"
              id="tags"
              v-model="pojo.tags"
            />
          </div>
          <div class="tags-box">
            <ul class="sui-tag tag-bordered">
              <li class="tag-item">javascript</li>
              <li class="tag-item">php</li>
              <li class="tag-item">css</li>
              <li class="tag-item">html</li>
              <li class="tag-item">java</li>
              <li class="tag-item">python</li>
              <li class="tag-item">html5</li>
              <li class="tag-item">node.js</li>
              <li class="tag-item">c++</li>
            </ul>
          </div>
        </div>

        <div class="editor">
          <div>
            <div
              class="quill-editor"
              :content="content"
              @change="onEditorChange($event)"
              v-quill:myQuillEditor="editorOption"
            ></div>
          </div>
        </div>
        <div class="submit">
          <span>已保存草稿</span>
          <span>
            <a class="sui-btn btn-release" @click="save">发布问题</a>
          </span>
        </div>
        <div class="clearfix"></div>
      </form>
    </div>
  </div>
</template>
<script>
import "~/assets/css/page-sj-qa-submit.css";
import problemApi from "@/api/problem";
import axios from "axios";
export default {
  data() {
    return {
      pojo: {},
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
  methods: {
    onEditorChange({ editor, html, text }) {
      console.log("editor change!", editor, html, text);
      this.content = html;
    },
    save() {
      this.pojo.content = this.content;
      problemApi.save(this.pojo).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        if (res.data.flag) {
          //刷新数据
          this.pojo = "";
          this.content = "";
        }
      });
    }
  }
};
</script>
<style>
.quill-editor {
  min-height: 300px;
  max-height: 400px;
  overflow-y: auto;
}
</style>