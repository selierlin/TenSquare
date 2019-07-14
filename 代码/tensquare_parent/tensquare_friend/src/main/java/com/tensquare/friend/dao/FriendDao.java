package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String> {
    /**
     * 查询是否存在此条数据
     *
     * @param userid
     * @param friendid
     * @return
     */
    public Friend findByUseridAndFriendid(String userid, String friendid);

    /**
     * 更新喜欢
     * 莫名其妙执行不成功且控制台会显示sql语句而不提示异常，多捣鼓一会又成功了，可能是sql中带有特殊字符的原因
     *
     * @param userid
     * @param friendid
     * @param islike
     */
    @Modifying
    @Query(value = "UPDATE tb_friend SET islike=?3 WHERE userid=?1 AND friendid = ?2", nativeQuery = true)
    public void updateLike(String userid, String friendid, String islike);

    @Modifying
    @Query(value = "DELETE FROM tb_friend WHERE userid=? AND friendid=?", nativeQuery = true)
    void deleteFriend(String userid, String friendid);
}
