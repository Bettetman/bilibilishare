package cn.sicnu.ming.service;

import cn.sicnu.ming.entity.UserDownload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

/**
 * @author frank ming
 * @createTime 20200623 16:16
 * @description 用户下载
 */
public interface UserDownService {

    public Integer getCountByUidAndByAid(Integer userId,Integer articleId);

    //分页查询某个用户下载的所有资源
    public Page<UserDownload> listAll(Integer userId, Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    //统计某个用户下载的资源数
    public Long getCount(Integer userId);

    //添加或修改用户的下载信息
    public void save(UserDownload userDownload);
}
