package com.memory.controller;

import com.memory.common.utils.Result;
import com.memory.common.utils.ResultUtil;
import com.memory.domain.entity.Demo;
import com.memory.rabbitmq.utils.RabbitMQUtil;
import com.memory.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/1
 * @Description:
 */
@RestController
public class DemoController {

    private final static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DemoService demoService;

    @RequestMapping("test")
    public Result test() {
        logger.debug("这是DEBUG");
        logger.info("这是INFO");
        logger.error("这是ERROR");
        //throw new NullPointerException("自定义空指针异常");
        return ResultUtil.success();
    }

    @RequestMapping(value = "add/{name}/{sex}/{age}")
    public Result add(@PathVariable String name, @PathVariable Integer sex, @PathVariable int age) {
        Object object = demoService.add(name, sex, age);
        return ResultUtil.success(object);
    }

    /**
     * 分页查询
     * @param page 查询页数 默认0页开始
     * @param size 查询每页条数
     * @param direction 查询排序描述,(asc,desc)
     * @param sorts 查询排序字段
     * @return
     */
    @RequestMapping(value = "list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "desc") String direction, @RequestParam(defaultValue = "id") String sorts) {
        Pageable pageable =PageRequest.of(page, size, direction.toLowerCase().equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sorts);
        Page<Demo> list = demoService.findAll(pageable);
        return ResultUtil.success(list);
    }
    @RequestMapping(value = "listName")
    public Result listName(@RequestParam String name, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "desc") String direction, @RequestParam(defaultValue = "id") String sorts) {
        Pageable pageable =PageRequest.of(page, size, direction.toLowerCase().equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sorts);
        Page<Demo> list = demoService.findByName(pageable, name);
        return ResultUtil.success(list);
    }
}