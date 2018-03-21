package com.msg.service;



import com.msg.entity.User;
import com.msg.util.Page;

import java.util.List;

/**
 * Created by wd199 on 2017/6/16.
 */
public interface UserService {
    public int modifyUser(User user);
    public User findUser(User user);
    public List<User> findUsers(Page page);
    public int findTotal();
    public List<User> findUserByCondition(String[] values);
    public int importUser(List<User> users);
    public List<User> queryUserByMan();
    public List<User> queryUserByWoman();
}
