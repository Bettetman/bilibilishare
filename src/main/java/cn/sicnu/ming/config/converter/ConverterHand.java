package cn.sicnu.ming.config.converter;

import cn.sicnu.ming.entity.ArcType;
import cn.sicnu.ming.service.ArcTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author frank ming
 * @createTime 20200619 13:49
 * @description 解决表单提交出现转换的问题
 */
@Component
public class ConverterHand implements Converter<String, ArcType> {
    @Autowired
    private ArcTypeService arcTypeService;
    @Override
    public ArcType convert(String s) {
        if(s!=null){
            ArcType byId = arcTypeService.getById(Integer.parseInt(s));
            return byId;
        }
        return  null;
    }
}
