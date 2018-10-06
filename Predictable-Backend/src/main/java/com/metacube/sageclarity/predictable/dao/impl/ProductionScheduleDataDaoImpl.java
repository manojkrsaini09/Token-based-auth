package com.metacube.sageclarity.predictable.dao.impl;

import com.metacube.sageclarity.predictable.dao.ProductionScheduleDataDao;
import com.metacube.sageclarity.predictable.dao.jpa.JpaProductionScheduleDataDao;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleData;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductionScheduleDataDaoImpl implements ProductionScheduleDataDao {
    @Autowired
    private JpaProductionScheduleDataDao jpaProductionScheduleDataDao;

    @Override
    public ProductionScheduleData save(ProductionScheduleData productScheduleMaster) {
        return jpaProductionScheduleDataDao.save(productScheduleMaster);
    }

    @Override
    public List<ProductionScheduleData> getAll() {
        return jpaProductionScheduleDataDao.findAll();
    }

    @Override
    public ProductionScheduleData getById(Long id) {
        return jpaProductionScheduleDataDao.getOne(id);
    }

    @Override
    public List<ProductionScheduleData> getByProductionScheduleMaster(ProductionScheduleMaster master) {
        return jpaProductionScheduleDataDao.getByScheduleMaster(master);
    }
}
