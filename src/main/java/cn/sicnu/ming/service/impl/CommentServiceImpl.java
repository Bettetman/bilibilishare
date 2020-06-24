package cn.sicnu.ming.service.impl;

import cn.sicnu.ming.dao.CommentRespository;
import cn.sicnu.ming.entity.Comment;
import cn.sicnu.ming.service.CommentService;
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
import javax.transaction.Transactional;

/**
 * @author frank ming
 * @createTime 20200624 16:49
 * @description 评论实现
 */
@Service("comment")
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRespository commentRespository;

    /**
     * 添加评论
     * @param comment
     */
    @Override
    public void save(Comment comment) {
        commentRespository.save(comment);
    }

    @Override
    public Page<Comment> list(Comment s_comment, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        return commentRespository.findAll(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(s_comment!=null){
                    if (s_comment.getState() != null) {                 //审核状态
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("state"),s_comment.getState()));
                    }
                    if(s_comment.getArticle()!=null&&s_comment.getArticle().getArticleId()!=null){      //所属资源
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("article").get("articleId"),s_comment.getArticle().getArticleId()));
                    }
                }
                return predicate;
            }
        }, PageRequest.of(page-1,pageSize,direction,properties));
    }

    @Override
    public Long getCount(Comment s_comment) {
        return commentRespository.count(new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(s_comment!=null){
                    if (s_comment.getState() != null) {                 //审核状态
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("state"),s_comment.getState()));
                    }
                    if(s_comment.getArticle()!=null&&s_comment.getArticle().getArticleId()!=null){      //所属资源
                        predicate.getExpressions().add(criteriaBuilder.equal(root.get("article").get("articleId"),s_comment.getArticle().getArticleId()));
                    }
                }
                return predicate;
            }
        });
    }

    @Override
    public Comment getById(Integer id) {
        return commentRespository.getOne(id);
    }

    @Override
    public void delete(Integer id) {
        commentRespository.deleteById(id);
    }

    @Override
    public void deleteCommentByArticleId(Integer articleId) {
        commentRespository.deleteCommentByArticleId(articleId);
    }
}
