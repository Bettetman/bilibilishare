package cn.sicnu.ming.service;

import cn.sicnu.ming.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface CommentService {
    //保存
    public void save(Comment comment);

    //根据条件分页查询评论信息
    public Page<Comment> list(Comment s_comment, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    //根据条件获取总记录数
    public Long getCount(Comment s_comment);

    //根据资源id查询评论
    public Comment getById(Integer id);

    //根据id删除评论
    public void delete(Integer id);

    //删除指定资源的所有评论
    public void deleteCommentByArticleId(Integer articleId);
}
