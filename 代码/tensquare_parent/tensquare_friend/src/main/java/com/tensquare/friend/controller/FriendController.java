package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserClient userClient;

    @PutMapping("/test/{userid}/{friendid}")
    public Result test(@PathVariable String userid, @PathVariable String friendid) {
        friendService.updateTest(userid, friendid);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 添加好友或添加非好友
     *
     * @param friendid 好友ID
     * @param type     是否喜欢
     * @return
     */
    @PutMapping("/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid, @PathVariable String type) {
        //验证是否登录，如果登录就获取当前登录用户的ID
        Claims claims = (Claims) request.getAttribute("claims_user");
        if (claims == null) {
            //说明当前用户没有user角色
            throw new RuntimeException("权限不足");
        }
        //得到当前登录用户的ID
        String userId = claims.getId();

        //判断是添加好友还是添加非好友
        if (type != null) {
            if ("1".equals(type)) {
                //添加好友
                int flag = friendService.addFriend(userId, friendid);
                if (flag == 0) {
                    return new Result(false, StatusCode.ERROR, "不能重复喜欢！");
                } else if (flag == 1) {
                    //userid的用户关注数+1
                    //friendid的关注数+1
                    userClient.updateFansountAndFollowcount(userId, friendid, 1);
                    return new Result(false, StatusCode.ERROR, "喜欢成功！");
                } else {
                    return new Result(false, StatusCode.ERROR, "操作异常！");
                }
            } else if ("2".equals(type)) {
                //添加非好友
                int flag = friendService.addNoFriend(userId, friendid);
                if (flag == 0) {
                    return new Result(false, StatusCode.ERROR, "不能重复添加非好友！");
                } else if (flag == 1) {
                    return new Result(false, StatusCode.ERROR, "添加成功！");
                } else {
                    return new Result(false, StatusCode.ERROR, "操作异常！");
                }
            } else {
                return new Result(false, StatusCode.ERROR, "参数异常");
            }
        }
        return new Result(true, StatusCode.OK, "未知异常");
    }

    @DeleteMapping("/{friendid}")
    public Result deleteFriend(@PathVariable String friendid) {
        Claims claims = (Claims) request.getAttribute("claims_user");
        if (claims == null) {
            throw new RuntimeException("权限不足");
        }
        String userid = claims.getId();
        friendService.deleteFriend(userid, friendid);
        userClient.updateFansountAndFollowcount(userid, friendid, -1);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
