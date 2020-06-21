package cn.sicnu.ming.controller.admin;

import cn.sicnu.ming.entity.ArcType;
import cn.sicnu.ming.prepare.LoadData;
import cn.sicnu.ming.service.ArcTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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


    @Autowired
    private LoadData loadData;

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
    @RequiresPermissions(value = "根据id查询资源类型实体")
    public Map<String,Object> findById(Integer arcTypeId){
        Map<String,Object> map = new HashMap<>();
        map.put("data",arcTypeService.getById(arcTypeId));
        map.put("errorNo",0);
        return map;
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "添加或修改资源类型信息")
    public Map<String,Object> save(ArcType arcType){
        Map<String,Object> map = new HashMap<>();
        arcTypeService.save(arcType);
        map.put("errorNo",0);
        return map;
    }


//    @RequestMapping("/delete")
//    public Map<String,Object> delete(ArcType arcType){
//        Map<String,Object> map = new HashMap<>();
//        if (arcType == null && arcTypeService.getById(arcType.getArcTypeId())==null){
//            map.put("errorNo",1);
//        }else {
//        arcTypeService.delete(arcType.getArcTypeId());
//        map.put("errorNo",0);
//        }
//        return map;
//    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = "删除资源类型信息")
    public Map<String,Object> save(@RequestParam(value = "arcTypeId") String arcTypeId){
        Map<String,Object> map = new HashMap<>();
        String[] split = arcTypeId.split(",");
        for (int i = 0; i < split.length; i++) {
            arcTypeService.delete(Integer.parseInt(split[i]));
        }
        loadData.loadDate();
        map.put("errorNo",0);
        return map;
    }
}
