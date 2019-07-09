package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findOne(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", spitService.findById(id));
    }

    /**
     * 增加
     *
     * @param spit
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit) {
        spitService.add(spit);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param spit
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit, @PathVariable String id) {
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        spitService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result comment(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pageList = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 吐槽点赞
     *
     * @param spitId
     * @return
     */
    @PutMapping("/thumbup/{spitId}")
    public Result thumbup(@PathVariable String spitId) {
        //判断当前用户是否已经点赞，但是现在我们没有做认证，暂时先把userid写死
        String userid = "111";
        if (redisTemplate.opsForValue().get("thumbup_" + userid + "_" + spitId) != null) {
            return new Result(false, StatusCode.OK, "不能重复点赞");
        }
        spitService.updateThumbup(spitId);
        redisTemplate.opsForValue().set("thumbup_" + userid + "_" + spitId, 1);
        return new Result(true, StatusCode.OK, "点赞成功");
    }

}
