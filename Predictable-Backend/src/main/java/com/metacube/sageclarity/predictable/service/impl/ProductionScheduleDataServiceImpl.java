package com.metacube.sageclarity.predictable.service.impl;

import com.metacube.sageclarity.predictable.dao.ProductionScheduleDataDao;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleData;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.exception.InvalidParamException;
import com.metacube.sageclarity.predictable.service.ProductionScheduleDataService;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionScheduleDataServiceImpl implements ProductionScheduleDataService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduleDataServiceImpl.class);
    @Autowired
    private ProductionScheduleDataDao productionScheduleDataDao;

    @Override
    public ProductionScheduleData getById(Long id) throws ApplicationLevelException {
        if(id == null){
            String message= "schedule id is null";
            logger.error(message);
            throw new InvalidParamException(message);
        }
        try {
            ProductionScheduleData productionSchedule = productionScheduleDataDao.getById(id);
            return productionSchedule;
        }catch (Exception e){
            String message = "Db exception while finding schedule by id: "+ id + ". " + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public ProductionScheduleData save(ProductionScheduleData productionSchedule) throws ApplicationLevelException {
        if(productionSchedule == null){
            String message= "Null object";
            logger.error(message);
            throw new InvalidParamException(message);
        }
        try{
            productionSchedule = productionScheduleDataDao.save(productionSchedule);
            return productionSchedule;
        }catch (Exception e){
            String message = "Db exception while saving schedule with name ." + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public List<ProductionScheduleData> getAll() throws ApplicationLevelException {
        try{
            List<ProductionScheduleData> productionSchedule = productionScheduleDataDao.getAll();
            return productionSchedule;
        }catch(Exception e){
            String message = "Db exception while fetching all schedule :"+ e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public List<ProductionScheduleData> getByProductionScheduleMaster(Long masterId) throws ApplicationLevelException {
        try{
            return productionScheduleDataDao.getByProductionScheduleMaster(new ProductionScheduleMaster((masterId)));
        }catch(Exception e) {
            String message = "Db exception while fetching  Schedule for master id " + masterId + ". " + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public List<ProductionScheduleData> getDataListFromUploadedFileData(DataUploadRowVO dataVO, ProductionScheduleMaster master) {
        return null;
    }

    public ProductionScheduleData getProductScheduleDataFromDataRow(List<Object> dataRow, ProductionScheduleMaster master){
        return null;
    }
}
