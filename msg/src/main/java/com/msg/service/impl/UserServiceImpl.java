package com.msg.service.impl;


import com.msg.entity.User;
import com.msg.mapper.UserMapper;
import com.msg.service.UserService;
import com.msg.util.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wd199 on 2017/6/16.
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Resource(name = "userMapper")
    UserMapper userMapper;


    public int modifyUser(User user) {
        return userMapper.updateUser(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public User findUser(User user) {
        return userMapper.selectUser(user.getUserId());
    }
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> findUsers(Page page) {
        return userMapper.selectUsers(page);
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public int findTotal() {
        return userMapper.selectTotal();
    }

    public List<User> findUserByCondition(String[] values) {
        String condition="";
        for (int i = 0; i < values.length; i++) {
            if (i==values.length-1) condition+=values[i];
            else condition+=values[i]+",";
        }
        return userMapper.findUserByCondition(condition);
    }

    public int importUser(List<User> users) {
        return userMapper.insertUsers(users);



    }

    public List<User> queryUserByMan() {


        return userMapper.selectManUser();
    }

    public List<User> queryUserByWoman() {
        return userMapper.selectWomanUser();
    }


}
