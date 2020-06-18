package cn.sicnu.ming.dao;

import cn.sicnu.ming.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


/**
 * @author frank ming
 * @createTime 20200617 17:05
 * @description 用户jpa
 */

public interface UserRespository extends JpaRepository<User,Integer>,
        JpaSpecificationExecutor<User> {

    @Query(value = "SELECT * FROM user WHERE user_name=?1", nativeQuery = true)
    public User findByUserName(String name);

    @Query(value = "SELECT * FROM user WHERE email=?1", nativeQuery = true)
    public User findByEmail(String email);


}
