package cn.sicnu.ming.service.impl;

import cn.sicnu.ming.dao.ArcTypeRespository;
import cn.sicnu.ming.entity.ArcType;
import cn.sicnu.ming.prepare.LoadData;
import cn.sicnu.ming.service.ArcTypeService;
import cn.sicnu.ming.util.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author frank ming
 * @createTime 20200618 12:38
 * @description arctype的方法实现
 */
@Service(value = "arcTypeService")
public class ArcTypeServiceImpl implements ArcTypeService {

    @Autowired
    private ArcTypeRespository arcTypeRespository;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private LoadData loadData;

    @Override
    public List<ArcType> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Page<ArcType> arcTypes = arcTypeRespository.findAll(PageRequest.of(page-1,pageSize,direction,properties));
        return arcTypes.getContent();
    }

    @Override
    public List listAll(Sort.Direction direction, String... properties) {
        if (redisTemplate.hasKey(ConstUtil.ALL_ARC_TYPE_NAME)){
            return redisTemplate.opsForList().range(ConstUtil.ALL_ARC_TYPE_NAME,0,-1);
        }else{
            List<ArcType> list = arcTypeRespository.findAll(Sort.by(direction, properties));
            if (list != null && list.size() > 0) {
                for (int i=0;i<list.size();i++){
                    redisTemplate.opsForList().rightPush(ConstUtil.ALL_ARC_TYPE_NAME,list.get(i));
                }
            }
            return list;
        }
    }

    @Override
    public Long getCount() {
        return arcTypeRespository.count();
    }

    @Override
    public void save(ArcType arcType) {
        boolean flag = false;
        if(arcType.getArcTypeId()==null){
            flag = true;
        }
        arcTypeRespository.save(arcType);
        if(flag){               //新增类型
            redisTemplate.opsForList().rightPush(ConstUtil.ALL_ARC_TYPE_NAME,arcType);
        }else{                  //修改类型
            redisTemplate.delete(ConstUtil.ALL_ARC_TYPE_NAME);
        }
        loadData.loadDate();
    }

    @Override
    public void delete(Integer id) {
        arcTypeRespository.deleteById(id);

    }

    @Override
    public ArcType getById(Integer id) {
        return arcTypeRespository.getOne(id);
    }

    @Override
    public ArcType getByName(String name) {
        return arcTypeRespository.findByArcTypeName(name);
    }
}
