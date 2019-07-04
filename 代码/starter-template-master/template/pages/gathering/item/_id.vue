<template>
  <div>
    活动详情
    {{item.id}}
    <hr />
    {{item.name}}
  </div>
</template>
<script>
import axios from "axios";
export default {
  asyncData({ params }) {
    //params.id
    return axios
      .get(
        `http://127.0.0.1:7300/mock/5d19dcb24d5d201f8f110b89/gathering/gathering/${params.id}`
      )
      .then(res => {
        return { item: res.data.data };
      });
  }
};
</script>