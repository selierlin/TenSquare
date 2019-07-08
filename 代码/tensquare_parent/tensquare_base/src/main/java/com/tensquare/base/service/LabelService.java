package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部标签
     *
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据ID查询标签
     *
     * @param id
     * @return
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     * 增加标签
     *
     * @param label
     */
    public void add(Label label) {
        //生成ID
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    /**
     * 修改标签
     *
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     * 删除标签
     *
     * @param id
     */
    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(createSpecification(label));
    }

    /**
     * 构建查询条件
     *
     * @param label
     * @return
     */
    private Specification<Label> createSpecification(Label label) {
        return new Specification<Label>() {
            /**
             * @param root 根对象，也就是要把条件封装到哪个对象中。where 类名=label.getId
             * @param criteriaQuery 封装的都是查询关键字，比如 group by , order by 等
             * @param cb 用来封装条件对象的，如果直接返回null，表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    //labelname【第一个参数】 like "%"小明"%"【第二个参数】
                    predicateList.add(cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%"));
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    //state【第一个参数】="1"【第二个参数】
                    predicateList.add(cb.equal(root.get("state").as(String.class), label.getState()));
                }
                if (label.getRecommend() != null && !"".equals(label.getRecommend())) {
                    //recommend【第一个参数】="1"【第二个参数】
                    predicateList.add(cb.equal(root.get("recommend").as(String.class), label.getRecommend()));
                }
                //where labelname like "%"小明"%" and【调用的and方法】  state="1" and ...
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        Specification<Label> specification = createSpecification(label);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return labelDao.findAll(specification, pageRequest);
    }
}
