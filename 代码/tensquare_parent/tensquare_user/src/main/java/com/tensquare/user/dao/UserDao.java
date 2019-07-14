package com.tensquare.user.dao;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     *
     * @param loginname
     * @return
     */
    User findByLoginname(String loginname);

    /**
     * 查询数据库是否已经有相应的用户名或手机号
     *
     * @param loginname 用户名
     * @param mobile    手机号
     * @return
     */
    List<User> findByLoginnameOrMobile(String loginname, String mobile);

    @Modifying
    @Query(value = "UPDATE tb_user SET fanscount=fanscount+? WHERE id = ?", nativeQuery = true)
    void updateFanscount(int x, String friendid);


    @Modifying
    @Query(value = "UPDATE tb_user SET followcount=followcount+? WHERE id = ?", nativeQuery = true)
    void updateFollowcount(int x, String userid);
}
