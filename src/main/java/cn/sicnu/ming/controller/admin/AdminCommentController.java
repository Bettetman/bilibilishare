package cn.sicnu.ming.controller.admin;

import cn.sicnu.ming.entity.Comment;
import cn.sicnu.ming.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 20200624 20:06
 * @description 评论的后台管理类
 */
@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {
    @Autowired
    private CommentService commentService;


    /**
     * 根据条件分页查询评论信息
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions(value = "分页查询评论信息")
    public Map<String,Object> list(Comment s_comment, @RequestParam(value = "page",required = false)Integer page,
                                   @RequestParam(value = "pageSize",required = false)Integer pageSize){
        Map<String ,Object> map = new HashMap<>();
        map.put("data",commentService.list(s_comment,page,pageSize, Sort.Direction.DESC,"commentDate").getContent());
        map.put("total",commentService.getCount(s_comment));
        map.put("errorNo",0);
        return map;
    }

    /**
     * 根据id查看评论信息
     */
    @RequestMapping(value = "/findById")
    @RequiresPermissions(value = "查看评论信息")
    public Map<String,Object> findById(@RequestParam(value = "commentId") Integer commentId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> tempMap = new HashMap<>();
        Comment comment = commentService.getById(commentId);
        tempMap.put("name",comment.getArticle().getName());
        tempMap.put("content",comment.getContent());
        tempMap.put("description",comment.getArticle().getDescription());
        map.put("data",tempMap);
        map.put("errorNo",0);
        return map;
    }
    /**
     * 根据id批量删除评论信息
     */
    @RequestMapping(value = "/delete")
    @RequiresPermissions(value = "删除评论信息")
    public Map<String,Object> delete(@RequestParam(value = "commentId") String ids){
        Map<String ,Object> map =new HashMap<>();
        String[] idsStr = ids.split(",");
        for (int i=0;i<idsStr.length;i++){
            //todo 删除评论
            commentService.delete(Integer.parseInt(idsStr[i]));             //批量删除评论
        }
        map.put("errorNo",0);
        return map;
    }

}
