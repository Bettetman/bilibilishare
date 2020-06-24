package cn.sicnu.ming.dao;

import cn.sicnu.ming.entity.Article;
import cn.sicnu.ming.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRespository extends JpaRepository<Comment,Integer>,
        JpaSpecificationExecutor<Comment> {
}
