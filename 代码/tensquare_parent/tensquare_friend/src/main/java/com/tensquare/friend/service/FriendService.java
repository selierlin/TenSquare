package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    /**
     * 出现问题：从数据库获取到的Friend对象，使用set方法时会出现异常
     * 原因：从数据库获取到的对象为持久化对象，当执行set方法时会同步到数据库
     * 解决方法：https://blog.csdn.net/weixin_33975951/article/details/86973426
     *
     * @param userId
     * @param friendid
     * @return
     */
    public int addFriend(String userId, String friendid) {
        //先判断userid到friendid是否有数据
        Friend friend = friendDao.findByUseridAndFriendid(userId, friendid);
        if (friend != null) {
            return 0;
        } else {
            //直接添加好友，让好友表中userid到friendid方向的type为0
            friend = new Friend(userId, friendid, "0");
        }
        //判断从friendid到userid是否有数据，如果有将双方的type都设置为1
        Friend re_friend = friendDao.findByUseridAndFriendid(friendid, userId);
        if (re_friend != null) {
            friend.setIslike("1");
            friendDao.updateLike(re_friend.getUserid(), re_friend.getFriendid(), "1");
        }
        friendDao.save(friend);
        return 1;

    }

    public void updateTest(String userId, String friendid) {
        Friend friend = friendDao.findByUseridAndFriendid(userId, friendid);
        if (friend != null) {
            //friend.setIslike("1");//持久化对象
            friendDao.updateLike(friend.getUserid(), friend.getFriendid(), friend.getIslike());
        }
    }

    public int addNoFriend(String userId, String friendid) {
        //先判断userid到friendid是否有数据
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userId, friendid);
        if (noFriend != null) {
            return 0;
        } else {
            //直接添加数据
            noFriend = new NoFriend(userId, friendid);
        }
        noFriendDao.save(noFriend);
        //userid的用户关注数+1
        //friendid的关注数+1
        return 1;
    }

    /**
     * 删除好友
     *
     * @param userid
     * @param friendid
     */
    public void deleteFriend(String userid, String friendid) {
        //删除还存在userid到friendid这条数据
        friendDao.deleteFriend(userid, friendid);
        //更新friendid到userid的islike为0
        friendDao.updateLike(friendid, userid, "0");
        //非好友表中添加数据
        noFriendDao.save(new NoFriend(userid, friendid));
    }
}
