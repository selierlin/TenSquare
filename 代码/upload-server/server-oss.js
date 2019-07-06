const express = require('express');
const upload = require('multer')({
  dest: 'uploads/'
});
const path = require('path');
const fs = require('fs');
var co = require("co")
var OSS = require("ali-oss")

const port = 3000;

let client = new OSS({
  region: 'oss-cn-shanghai',
  //云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，部署在服务端使用RAM子账号或STS，部署在客户端使用STS。
  accessKeyId: 'LTAIiZqUfsJ0U2ae',
  accessKeySecret: 'kdOw41biVdO0oTK2XlNvf3JT04Amkn',
  bucket: 'tensquare-up'
});

let app = express();
// app.set('port",port);
app.use(express.static(path.join(__dirname, 'uploads/')));
//处理跨域请求
app.all('*', function (req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  next();
});
app.post('/upload', upload.single('img'), (req, res) => {
  //没有附带文件
  if (!req.file) {
    res.json({
      ok: false
    });
    return;
  }
  async function putStream() {
    try {
      // use 'chunked encoding'
      // let stream = fs.createReadStream('req.file.path');
      // let result = await client.putStream('object-name', stream);
      // console.log(result);

      // don't use 'chunked encoding'
      let stream = fs.createReadStream(req.file.path);
      let size = fs.statSync(req.file.path).size;
      let result = await client.putStream(
        'object-name', stream, {
          contentLength: size
        });
      console.log(result);
      res.json({
        ok: true,
        info: result.url
      })
    } catch (e) {
      console.log(e)
    }
  }

  putStream();
  // co(function* () {
  //   var stream = fs.createReadStream(req.file.path);
  //   var result = yield client.putStream(req.file.originalname, stream);
  //   console.log("result:" + result);

  //   res.json({
  //     ok: true,
  //     info: result.url
  //   })
  // }).catch(function (err) {
  //   console.log(err);
  // });

});
app.listen(port, () => {
  console.log("[server] localhost:" + port);
});