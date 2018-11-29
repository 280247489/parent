package com.memory.domain.repository;


import com.memory.domain.entity.Demo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: cui.Memory
 * @Date: 2018/11/1
 * @Description:
 */
public interface DemoRepository extends JpaRepository<Demo, Integer> {
    Page<Demo> findByName(Pageable pageable, String name);

}
