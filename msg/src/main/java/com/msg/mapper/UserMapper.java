package com.msg.mapper;


import com.msg.entity.User;
import com.msg.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wd199 on 2017/6/16.
 */
public interface UserMapper {
    public int updateUser(User user);
    public User selectUser(String userId);
    public List<User> selectUsers(Page page);
    public int selectTotal();
    public List<User> findUserByCondition(@Param("condition") String condition);
    public int insertUsers(@Param("list") List<User> users);
    public List<User> selectManUser();
    public List<User> selectWomanUser();

}
