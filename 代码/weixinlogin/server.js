var http = require('http');
var https = require('https');
var url = require('url');
http.createServer(function (request, response) {
    var params = url.parse(request.url, true).query;
    var appid = 'wx3bdb1192c22883f3';
    var secret = 'db9d6b88821df403e5ff11742e799105';
    if (params.operation === 'token') {
        https.get(`https://api.weixin.qq.com/sns/oauth2/access_token?appid=${appid}&secret=${secret}&code=${params.code}&grant_type=authorization_code`, function (res) {
            res.on('data', function (chunk) {
                response.writeHead(200, {
                    'Content-Type': 'application/json;charset=utf-8',
                    "Access-Control-Allow-Origin": "*"
                });
                response.end(chunk);
            });
        })
    }
    if (params.operation === 'userinfo') {
        https.get(`https://api.weixin.qq.com/sns/userinfo?access_token=${params.access_token}&openid=${params.openid}`, function (res) {
            res.on('data', function (chunk) {
                // 发送响应数据 "Hello World"
                response.writeHead(200, {
                    'Content-Type': 'application/json;charset=utf-8',
                    "Access-Control-Allow-Origin": "*"
                });
                response.end(chunk);
            });
        })
    }
}).listen(8888);
// 终端打印如下信息
console.log('Server running at http://127.0.0.1:8888/');