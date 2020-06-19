package cn.sicnu.ming.service;

import cn.sicnu.ming.entity.ArcType;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ArcTypeService {


    /**
     * 分页查询资源类型列表
     * @param page            当前页
     * @param pageSize        每页记录数
     * @param properties      排序字段
     * @return
     */
    public List<ArcType> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties);

    /**
     * 查询资源类型列表
     * @param properties      排序字段
     * @return
     */
    public List listAll(Sort.Direction direction, String... properties);

    /**
     *获取总记录数
     */
    public Long getCount();

    /**
     * 添加或修改资源类型
     */
    public void save(ArcType arcType);

    /**
     * 根据ID删除一条资源类型
     */
    public void delete(Integer id);

    /**ArcTypeService
     * 根据id查询一条资源类型
     */
    public ArcType getById(Integer id);

    public ArcType getByName(String name);
}
