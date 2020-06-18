package cn.sicnu.ming.dao;

import cn.sicnu.ming.entity.ArcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArcTypeRespository extends JpaRepository<ArcType,Integer>,
        JpaSpecificationExecutor<ArcType> {
}
