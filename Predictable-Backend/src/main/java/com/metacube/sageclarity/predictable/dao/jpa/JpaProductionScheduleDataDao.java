package com.metacube.sageclarity.predictable.dao.jpa;

import com.metacube.sageclarity.predictable.entity.ProductionScheduleData;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface JpaProductionScheduleDataDao extends JpaRepository<ProductionScheduleData,Long> {
    public List<ProductionScheduleData> getByScheduleMaster(ProductionScheduleMaster master);
}
