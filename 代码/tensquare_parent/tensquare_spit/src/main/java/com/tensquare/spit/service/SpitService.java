package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询全部记录
     *
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据主键查询实体
     *
     * @param id
     * @return
     */
    public Spit findById(String id) {
        Spit spit = spitDao.findById(id).get();
        return spit;
    }

    /**
     * 增加
     *
     * @param spit
     */
    public void add(Spit spit) {
        spit.set_id(idWorker.nextId() + "");  //主键值
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //如果存在上级ID,评论
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {
            Query query = new Query();
            Update update = new Update();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            update.inc("comment",1);
            mongoTemplate.updateFirst(query, update, "spit");
        }
        spitDao.save(spit);
    }

    /**
     * 修改
     *
     * @param spit
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽列表
     *
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return spitDao.findByParentid(parentid, pageRequest);
    }

    /**
     * 吐槽点赞
     * 方式一：查询后修改再保存（两次数据库交互）
     *
     * @param spitId
     */
    @Deprecated
    public void thumbup(String spitId) {
        Spit spit = spitDao.findById(spitId).get();
        spit.setThumbup(spit.getThumbup() == null ? 1 : spit.getThumbup() + 1);
        spitDao.save(spit);
    }

    /**
     * 吐槽点赞
     * 方式二：使用 MongoTemplate
     *
     * @param spitId
     */
    public void updateThumbup(String spitId) {
        //原生命令：db.spit.update({"_id":1},{$inc:{thumbup:NumberInt(1)}});
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(spitId));
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
