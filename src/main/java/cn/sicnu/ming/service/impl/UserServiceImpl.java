package cn.sicnu.ming.service.impl;

import cn.sicnu.ming.dao.UserRespository;
import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author frank ming
 * @createTime 2020061717 19:14
 * @description 用户service的接口实现
 */
@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    public UserRespository userRespository;

    @Override
    public User findByUserName(String name) {
        return userRespository.findByUserName(name);
    }

    @Override
    public User findByEmail(String email) {
        return userRespository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRespository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRespository.getOne(id);
    }

    @Override
    public List<User> list(User user, String s_blatelyLoginTime, String s_elatelyLoginTime, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Page<User> userPage = userRespository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (!StringUtils.isEmpty(s_blatelyLoginTime)) {
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("latelyLoginTime").as(String.class),s_blatelyLoginTime));
                }
                if (!StringUtils.isEmpty(s_elatelyLoginTime)) {
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("latelyLoginTime").as(String.class),s_elatelyLoginTime));
                }
                if (user != null) {
                    if(!StringUtils.isEmpty(user.getSex())){
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("sex"),user.getSex()));
                    }
                    if(!StringUtils.isEmpty(user.getUserName())){
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("userName"),"%"+user.getUserName()+"%"));
                    }
                }
                return predicate;
            }
        }, PageRequest.of(page-1,pageSize,direction,properties));
        return userPage.getContent();
    }

    @Override
    public Long getCount(User user, String s_blatelyLoginTime, String s_elatelyLoginTime) {
        return userRespository.count(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (!StringUtils.isEmpty(s_blatelyLoginTime)) {
                    predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("latelyLoginTime").as(String.class),s_blatelyLoginTime));
                }
                if (!StringUtils.isEmpty(s_elatelyLoginTime)) {
                    predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("latelyLoginTime").as(String.class),s_elatelyLoginTime));
                }
                if (user != null) {
                    if(!StringUtils.isEmpty(user.getSex())){
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("sex"),user.getSex()));
                    }
                    if(!StringUtils.isEmpty(user.getUserName())){
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("userName"),"%"+user.getUserName()+"%"));
                    }
                }
                return predicate;
            }
        });
    }
}
