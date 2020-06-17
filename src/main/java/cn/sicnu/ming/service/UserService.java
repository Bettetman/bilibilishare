package cn.sicnu.ming.service;

import cn.sicnu.ming.entity.User;
import org.springframework.data.jpa.repository.Query;

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
}
