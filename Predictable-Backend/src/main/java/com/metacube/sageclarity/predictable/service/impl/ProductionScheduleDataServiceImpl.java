package com.metacube.sageclarity.predictable.service.impl;

import com.metacube.sageclarity.predictable.dao.LineDao;
import com.metacube.sageclarity.predictable.dao.ProductDao;
import com.metacube.sageclarity.predictable.dao.ProductionScheduleDataDao;
import com.metacube.sageclarity.predictable.entity.*;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.exception.InvalidParamException;
import com.metacube.sageclarity.predictable.service.ProductionScheduleDataService;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductionScheduleDataServiceImpl implements ProductionScheduleDataService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduleDataServiceImpl.class);
    @Autowired
    private ProductionScheduleDataDao productionScheduleDataDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private DemoEntityServiceImpl dmoService;

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
    @Async("threadPoolTaskExecutor")
    public void createScheduleDataListFromUploadedFileData(DataUploadRowVO dataVO, ProductionScheduleMaster master) {
        String masterError = master.getErrors();
        Boolean errorInMaster = false;
        List<ProductionScheduleData> scheduleData = new ArrayList<>();
        Map<String,Product> productMap = new HashMap<>();
        List<Product> products =  productDao.getAll();
        List<Line> lines =  lineDao.getAll();
        Map<String,Line> lineMap = new HashMap<>();
        if(ObjectUtils.isEmpty(products)){
            errorInMaster = true;
            masterError += "Products are not created. ";
        }else{
            products.stream().forEach(element-> productMap.put(element.getName(),element));
        }
        if(ObjectUtils.isEmpty(lines)){
            errorInMaster = true;
            masterError += "Lines are not created. ";
        }else{
            lines.stream().forEach(element -> lineMap.put(element.getLineName(),element));
        }

         if(errorInMaster)
             return;

        dataVO.getRowOfDataList().stream().forEach(
                element -> scheduleData.add(this.getProductScheduleDataFromDataRow(element,master,productMap,lineMap)));
        productionScheduleDataDao.saveAll(scheduleData);
      /*  DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("test");
        try {
            dmoService.saveDemoEntity(demoEntity);
        } catch (ApplicationLevelException e) {
            e.printStackTrace();
        }*/
    }

    public ProductionScheduleData getProductScheduleDataFromDataRow(List<Object> dataRow, ProductionScheduleMaster master,
                                                                    Map<String,Product> prodcutMap, Map<String,Line> lineMap){
        ProductionScheduleData scheduleData = new ProductionScheduleData();
        String error = "";
        Line line = null;
        Product product = null;
        scheduleData.setScheduleMaster(master);
        String lineName =  dataRow.get(0) != null ? String.valueOf(dataRow.get(0)) : "";
        if(StringUtils.isEmpty(lineName))
            error += "Line name isn't provided.";
        else {
             line = lineMap.get(lineName);
             if (ObjectUtils.isEmpty(line))
                 error += "Unable to find line by name "+ lineName+". ";
        }

        String productName = dataRow.get(0) != null ? String.valueOf(dataRow.get(0)) : "";
        if(StringUtils.isEmpty(productName))
            error += "Product name isn't provided.";
        else {
            product = prodcutMap.get(productName);
            if (ObjectUtils.isEmpty(product))
                error += "Unable to find product by name "+ productName+". ";
        }


        scheduleData.setErrors(error);
        return scheduleData;
    }
}
