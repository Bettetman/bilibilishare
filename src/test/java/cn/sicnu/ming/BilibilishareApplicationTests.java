package cn.sicnu.ming;

import cn.sicnu.ming.dao.ArcTypeRespository;
import cn.sicnu.ming.entity.ArcType;
import cn.sicnu.ming.service.ArcTypeService;
import cn.sicnu.ming.util.ConstUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class BilibilishareApplicationTests {

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    ArcTypeRespository arcTypeRespository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public static final String prefix = "*";




    @Test
    void contextLoads() {
        if (redisTemplate.hasKey(ConstUtil.ALL_ARC_TYPE_NAME)){
            System.out.println(redisTemplate.opsForList().range(ConstUtil.ALL_ARC_TYPE_NAME, 0, -1));
        }else{
            List<ArcType> list = arcTypeRespository.findAll(Sort.by(Sort.Direction.ASC,"sort"));
            if (list != null && list.size() > 0) {
                for (int i=0;i<list.size();i++){
                    redisTemplate.opsForList().rightPush(ConstUtil.ALL_ARC_TYPE_NAME,list.get(i));
                }
            }
            System.out.println(list);
        }
    }
    @Test
    void  deleteredis()
    {

        String keys="test:";
        redisTemplate.opsForValue().set(keys+1,"this is a test content!",1000, TimeUnit.SECONDS);
        String content=redisTemplate.opsForValue().get(keys+1).toString();
        System.out.println("---------》获取到缓存的内容为："+content);
        redisTemplate.delete(redisTemplate.keys(keys+"*"));
        Object msg=redisTemplate.opsForValue().get(keys+1);
        if (msg==null){
            msg="没有了";
        }
        System.out.println("---------》移除后内容为："+msg);


    }

}
