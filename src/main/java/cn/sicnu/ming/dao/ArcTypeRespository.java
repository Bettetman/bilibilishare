package cn.sicnu.ming.dao;

import cn.sicnu.ming.entity.ArcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ArcTypeRespository extends JpaRepository<ArcType,Integer>,
        JpaSpecificationExecutor<ArcType> {

    @Query(value = "select * from arc_type where user_id=?1",nativeQuery = true)
    public ArcType findByArcTypeName(String name);
}
