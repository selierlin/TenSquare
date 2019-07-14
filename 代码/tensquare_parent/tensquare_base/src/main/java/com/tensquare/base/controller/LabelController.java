package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部列表
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        String token = request.getHeader("Authorization");
        System.out.println("token = " + token);
        return new Result(true, StatusCode.OK, "查询成功", labelService.findAll());
    }

    /**
     * 根据ID查询标签
     *
     * @param labelId
     * @return
     */
    @GetMapping("/{labelId}")
    public Result findById(@PathVariable String labelId) {
        System.out.println("9001被使用");
        return new Result(true, StatusCode.OK, "查询成功", labelService.findById(labelId));
    }

    /**
     * 增加标签
     *
     * @param label
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Label label) {
        labelService.add(label);
        return new Result(true, StatusCode.OK, "查询成功");
    }

    /**
     * 修改标签
     *
     * @param labelId
     * @param label
     * @return
     */
    @PutMapping("/{labelId}")
    public Result update(@PathVariable String labelId, @RequestBody Label label) {
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "更新成功");
    }

    /**
     * 删除标签
     *
     * @param labelId
     * @return
     */
    @PostMapping("/{labelId}")
    public Result deleteById(@PathVariable String labelId) {
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label) {
        List<Label> list = labelService.findSearch(label);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@RequestBody Label label, @PathVariable int page, @PathVariable int size) {
        Page<Label> pageData = labelService.pageQuery(label, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageData.getTotalElements(), pageData.getContent()));
    }


}
