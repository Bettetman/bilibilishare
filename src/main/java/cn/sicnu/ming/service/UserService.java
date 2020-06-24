package cn.sicnu.ming.service;

import cn.sicnu.ming.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author frank ming
 * @createTime 2020061717 19:10
 * @description 用户的Service接口层
 */
public interface UserService {

    public User findByUserName(String name);

    public User findByEmail(String email);

    public void save(User user);

    public User findById(Integer id);






    /**
     * 根据分页条件查询用户列表
     */
    public List<User> list(User user, String s_blatelyLoginTime, String s_elatelyLoginTime, Integer page, Integer pageSize, Sort.Direction direction, String...properties);

    /**
     * 根据条件获取用户总数
     */
    public Long getCount(User user, String s_blatelyLoginTime,String s_elatelyLoginTime);
}
