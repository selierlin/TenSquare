package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;
import util.JwtUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
    }

    /**
     * 增加
     *
     * @param user
     * @param code
     */
    public void add(User user, String code) {
        //得到缓存中的验证码
        String checkcodeRedis = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
        if (checkcodeRedis == null) {
            throw new RuntimeException("请点击获取验证码");
        }
        if (!checkcodeRedis.equals(code)) {
            throw new RuntimeException("请输入正确的验证码");
        }
        user.setId(idWorker.nextId() + "");
        user.setPassword(encoder.encode(user.getPassword()));//密码加密
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数        
        user.setOnline(0L);//在线时长        
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期        
        user.setLastdate(new Date());//最后登陆日期 
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        String token = (String) request.getAttribute("claims_admin");
        System.out.println("token = " + token);
        if (token == null || "".equals(token)) {
            throw new RuntimeException("权限不足");
        }
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 登录名
                if (searchMap.get("loginname") != null && !"".equals(searchMap.get("loginname"))) {
                    predicateList.add(cb.like(root.get("loginname").as(String.class), "%" + (String) searchMap.get("loginname") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 头像
                if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                    predicateList.add(cb.like(root.get("image").as(String.class), "%" + (String) searchMap.get("image") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     */
    public void sendSms(String mobile) {
        //生成六位数字随机数
        String checkCode = RandomStringUtils.randomNumeric(6);
        //向缓存中存放一份
        redisTemplate.opsForValue().set("checkcode_" + mobile, checkCode, 5, TimeUnit.MINUTES);
        //给用户发一份
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("checkcode", checkCode);
        //TODO 测试其它模块，暂时关闭
        //rabbitTemplate.convertAndSend("sms", map);
        //在控制台输出[方便测试]
        System.out.println("验证码为：" + checkCode);
    }

    public User login(User user) {
        //先根据用户名查询对象
        List<User> userList = userDao.findByLoginnameOrMobile(user.getLoginname(), user.getMobile());
        //将数据库中的密码与用户输入的密码进行比较
        if (userList != null && userList.size() == 1 && encoder.matches(user.getPassword(), userList.get(0).getPassword())) {
            //登录成功
            return userList.get(0);
        }
        //登录失败
        return null;
    }

    /**
     * 检查用户的手机号或用户名是否被注册
     *
     * @param user
     * @return
     */
    public List<User> checkUser(User user) {
        return userDao.findByLoginnameOrMobile(user.getLoginname(), user.getMobile());
    }

    /**
     * 更新好友粉丝和用户关注数
     *
     * @param userid
     * @param friendid
     * @param x
     */
    @Transactional
    public void updateFanscountAndFollowcount(String userid, String friendid, int x) {
        userDao.updateFanscount(x, friendid);
        userDao.updateFollowcount(x, userid);

    }
}
