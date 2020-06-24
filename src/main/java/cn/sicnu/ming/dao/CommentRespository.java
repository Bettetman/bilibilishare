package cn.sicnu.ming.dao;

import cn.sicnu.ming.entity.Article;
import cn.sicnu.ming.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRespository extends JpaRepository<Comment,Integer>,
        JpaSpecificationExecutor<Comment> {

    @Query(value = "delete from comment where article_id=?1",nativeQuery = true)
    @Modifying
    public void deleteCommentByArticleId(Integer articleId);
}
