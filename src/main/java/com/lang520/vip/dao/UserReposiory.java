package com.lang520.vip.dao;

import com.lang520.vip.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author LSX
 * @version 1.0
 * @date 2019/11/29 17:21
 */
public interface UserReposiory extends CrudRepository<UserEntity,Integer> {

    List<UserEntity> findByUsername(String userName);

}
