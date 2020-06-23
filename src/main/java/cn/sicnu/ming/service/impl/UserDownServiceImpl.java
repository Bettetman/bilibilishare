package cn.sicnu.ming.service.impl;

import cn.sicnu.ming.dao.UserDownRespository;
import cn.sicnu.ming.entity.User;
import cn.sicnu.ming.entity.UserDownload;
import cn.sicnu.ming.service.UserDownService;
import cn.sicnu.ming.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author frank ming
 * @createTime 20200623 16:18
 * @description 用户下载实现的逻辑层
 */
@Service("userDownloadService")
public class UserDownServiceImpl implements UserDownService {

    @Autowired
    private UserDownRespository userDownRespository;

    @Override
    public Integer getCountByUidAndByAid(Integer userId, Integer articleId) {
        return userDownRespository.getCountByUidAndByAid(userId,articleId);
    }

    @Override
    public Page<UserDownload> listAll(Integer userId, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        return userDownRespository.findAll(new Specification<UserDownload>() {
            @Override
            public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(userId!=null){
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("user").get("userId"),userId));
                }
                return predicate;
            }
        }, PageRequest.of(page-1,pageSize,direction,properties));
    }

    @Override
    public Long getCount(Integer userId) {
        return userDownRespository.count(new Specification<UserDownload>() {
            @Override
            public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(userId!=null){
                    predicate.getExpressions().add(criteriaBuilder.equal(root.get("user").get("userId"),userId));
                }
                return predicate;
            }
        });
    }

    @Override
    public void save(UserDownload userDownload) {
        userDownRespository.save(userDownload);
    }
}
