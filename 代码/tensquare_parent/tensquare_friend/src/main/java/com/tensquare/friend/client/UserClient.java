package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("tensquare-user")
public interface UserClient {
    /**
     * 更新好友粉丝和用户关注数
     *
     * @param userid
     * @param friendid
     * @param x
     */
    @PutMapping("/user/{userid}/{friendid}/{x}")
    public void updateFansountAndFollowcount(@PathVariable String userid, @PathVariable String friendid, @PathVariable int x);
}
