package cn.sicnu.ming.controller.admin;

import cn.sicnu.ming.service.ArcTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frank ming
 * @createTime 20200621 11:41
 * @description 资源类型管理
 */
@RestController
@RequestMapping("/admin/arcType")
public class AdminArcTypeController {
    @Autowired
    private ArcTypeService arcTypeService;

    @RequestMapping("/list")
    public Map<String,Object> list(@RequestParam(value = "page",required =false) Integer page,
                                   @RequestParam(value = "pageSize",required =false) Integer pageSize){
        Map<String,Object> map = new HashMap<>();
        int count = arcTypeService.getCount().intValue();
        if(page == null && pageSize == null){
            page=1;
            pageSize = count >1?count:1;
        }
        map.put("data",arcTypeService.list(page,pageSize, Sort.Direction.ASC,"sort"));
        map.put("total",count);
        map.put("errorNo",0);
        return map;
    }

    @RequestMapping("/findById")
    public Map<String,Object> findById(Integer arcTypeId){
        Map<String,Object> map = new HashMap<>();
        map.put("data",arcTypeService.getById(arcTypeId));
        map.put("errorNo",0);
        return map;
    }
}
