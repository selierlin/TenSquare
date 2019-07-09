package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽数据访问层
 */
public interface SpitDao extends MongoRepository<Spit, String> {
    /**
     * 根据上级ID查询吐槽列表（分页）
     *
     * @param parentid 上级ID
     * @param pageable 分页对象
     * @return
     */
    public Page<Spit> findByParentid(String parentid, Pageable pageable);
}
