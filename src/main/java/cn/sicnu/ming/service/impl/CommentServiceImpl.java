package cn.sicnu.ming.service.impl;

import cn.sicnu.ming.dao.CommentRespository;
import cn.sicnu.ming.entity.Comment;
import cn.sicnu.ming.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
