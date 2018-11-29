package com.memory.service.impl;

import com.memory.domain.repository.DemoRepository;
import com.memory.domain.entity.Demo;
import com.memory.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/2
 * @Description:
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoRepository userRepository;
    @Transient
    @Override
    public Object add(String name, Integer sex, Integer age) {
        Demo demo = new Demo();
        demo.setAge(age);
        demo.setName(name);
        demo.setSex(sex);
        return userRepository.save(demo);
    }

    @Override
    public Page<Demo> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<Demo> findByName(Pageable pageable, String name) {
        return userRepository.findByName(pageable, name);
    }
}
