package cn.sicnu.ming.service.impl;

import cn.sicnu.ming.dao.UserRepository;
import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author frank ming
 * @createTime 2020061717 19:14
 * @description 用户service的接口实现
 */
@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    public UserRepository userRepository;

    @Override
    public User findByUserName(String name) {
        return userRepository.findByUserName(name);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.getOne(id);
    }
}
