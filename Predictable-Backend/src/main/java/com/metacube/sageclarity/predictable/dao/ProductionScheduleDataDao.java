package com.metacube.sageclarity.predictable.dao;

import com.metacube.sageclarity.predictable.entity.ProductionScheduleData;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;

import java.util.List;

public interface ProductionScheduleDataDao {
    public ProductionScheduleData save(ProductionScheduleData product);
    public List<ProductionScheduleData> getAll();
    public ProductionScheduleData getById(Long id);
    public List<ProductionScheduleData> getByProductionScheduleMaster(ProductionScheduleMaster master);
}
