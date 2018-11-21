package com.memory.service;

import com.memory.domain.entity.Demo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/2
 * @Description:
 */
public interface DemoService {
    Object add(String name, Integer sex, Integer age);
    Page<Demo> findAll(Pageable pageable);
    Page<Demo> findByName(Pageable pageable, String name);
}
