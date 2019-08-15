package com.memory.controller;

import com.memory.common.controller.BaseController;
import com.memory.common.controller.Message;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/1
 * @Description:
 */
@RestController
public class DemoController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DemoService demoService;
    /*@Autowired
    private RabbitMQUtil rabbitMQUtil;*/

    @RequestMapping("test")
    public Message test() {
        msg = Message.success();
        Map<String, Object> map = new HashMap<>();

        try {
            /*logger.debug("这是DEBUG");
            logger.info("这是INFO");
            logger.error("这是ERROR");
            throw new NullPointerException("自定义空指针异常");*/

//            rabbitMQUtil.send("一条新的群消息");

            map.put("obj", null);
            map.put("list", null);
            map.put("fileUrl", this.getFileUrl());
            msg.setResult(map);
            msg.setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    @RequestMapping(value = "add/{name}/{sex}/{age}")
    public Message add(@PathVariable String name, @PathVariable Integer sex, @PathVariable int age) {
        msg = Message.success();
        Map<String, Object> map = new HashMap<>();
        try {
            Object object = demoService.add(name, sex, age);
            map.put("obj", object);
            map.put("list", null);
            map.put("fileUrl", this.getFileUrl());
            msg.setResult(map);
            msg.setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
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
    public Message list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "desc") String direction, @RequestParam(defaultValue = "id") String sorts) {
        msg = Message.success();
        Map<String, Object> map = new HashMap<>();
        try {
            Pageable pageable =PageRequest.of(page, size, direction.toLowerCase().equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sorts);
            Page<Demo> list = demoService.findAll(pageable);
            map.put("obj", null);
            map.put("list", list);
            map.put("fileUrl", this.getFileUrl());
            msg.setResult(map);
            msg.setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
    @RequestMapping(value = "listName")
    public Message listName(@RequestParam String name, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "desc") String direction, @RequestParam(defaultValue = "id") String sorts) {
        msg = Message.success();
        Map<String, Object> map = new HashMap<>();

        try {
            Pageable pageable =PageRequest.of(page, size, direction.toLowerCase().equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sorts);
            Page<Demo> list = demoService.findByName(pageable, name);

            map.put("obj", null);
            map.put("list", list);
            map.put("fileUrl", this.getFileUrl());
            msg.setResult(map);
            msg.setMsg("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }
}