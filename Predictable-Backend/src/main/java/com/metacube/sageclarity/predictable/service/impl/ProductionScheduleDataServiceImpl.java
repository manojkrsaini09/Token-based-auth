package com.metacube.sageclarity.predictable.service.impl;

import com.metacube.sageclarity.predictable.dao.LineDao;
import com.metacube.sageclarity.predictable.dao.ProductDao;
import com.metacube.sageclarity.predictable.dao.ProductionScheduleDataDao;
import com.metacube.sageclarity.predictable.entity.*;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.exception.InvalidParamException;
import com.metacube.sageclarity.predictable.service.ProductionScheduleDataService;
import com.metacube.sageclarity.predictable.util.LocalizationUtil;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
        List<Product> products =  productDao.getByCompany(master.getCompany());
        List<Line> lines =  lineDao.getByCompany(master.getCompany());
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
       // String workOrderNumber = dataRow.get(0) != null ? String.valueOf((int)dataRow.get(0)) : "";
        if(StringUtils.isEmpty(dataRow.get(0)))
            error += "WorkOrder Number isn't provided.";
        else{
            String workOrderNumber = String.valueOf(dataRow.get(0));
            scheduleData.setWorkOrderNumber(workOrderNumber);
        }

        String lineName =  dataRow.get(1) != null ? String.valueOf(dataRow.get(1)) : "";
        if(StringUtils.isEmpty(lineName))
            error += "Line name isn't provided.";
        else {
             line = lineMap.get(lineName);
             if (ObjectUtils.isEmpty(line))
                 error += "Unable to find line by name "+ lineName+". ";
             else
                 scheduleData.setLine(line);
        }

        String productName = dataRow.get(2) != null ? String.valueOf(dataRow.get(2)) : "";
        if(StringUtils.isEmpty(productName))
            error += "Product name isn't provided.";
        else {
            product = prodcutMap.get(productName);
            if (ObjectUtils.isEmpty(product))
                error += "Unable to find product by name "+ productName+". ";
            else
                scheduleData.setProduct(product);
        }

        String orderQuantity = dataRow.get(3) != null ? String.valueOf(dataRow.get(3)) : "";
        if(StringUtils.isEmpty(orderQuantity))
            error += "Order quantity isn't provided.";
        else {
           scheduleData.setOrderQuantity(Long.valueOf(orderQuantity));
        }
        String unit = dataRow.get(4) != null ? String.valueOf(dataRow.get(4)) : "";
        if(StringUtils.isEmpty(unit))
            error += "Order unit isn't provided.";
        else {
            scheduleData.setUnit(unit);
        }
        String machineHour = dataRow.get(5) != null ? String.valueOf(dataRow.get(5)) : "";
        String machineMinut = dataRow.get(6) != null ? String.valueOf(dataRow.get(6)): "";
        String machineSecond = dataRow.get(7) != null ? String.valueOf(dataRow.get(7)): "";
        Long machineTime = 0L;
        if(!StringUtils.isEmpty(machineHour))
            machineTime += Long.valueOf(machineHour)* 3600;
        if(!StringUtils.isEmpty(machineMinut))
            machineTime += Long.valueOf(machineMinut)* 60;
        if(!StringUtils.isEmpty(machineSecond))
            machineTime += Long.valueOf(machineSecond);

        String setUpHour = dataRow.get(8) != null ? String.valueOf(dataRow.get(8)) : "";
        String setUpMinut = dataRow.get(9) != null ? String.valueOf(dataRow.get(9)): "";
        String setUpSecond = dataRow.get(10) != null ? String.valueOf(dataRow.get(10)): "";
        Long setUpTime = 0L;
        if(!StringUtils.isEmpty(setUpHour))
            setUpTime += Long.valueOf(setUpHour)* 3600;
        if(!StringUtils.isEmpty(setUpMinut))
            setUpTime +=Long.valueOf(setUpMinut)* 60;
        if(!StringUtils.isEmpty(setUpSecond))
            setUpTime += Long.valueOf(setUpSecond);

        String scheduleStart = dataRow.get(11) != null ? String.valueOf(dataRow.get(11)) : "";
        LocalDateTime scheduleStartTime = null;
        if(!StringUtils.isEmpty(scheduleStart)){
            scheduleStartTime = LocalizationUtil.getLocalDateTimeFromStringDate(scheduleStart,"MM/dd/yyyy hh:mm:ss");
        }
        scheduleData.setMachineTime(machineTime);
        scheduleData.setSetupTime(setUpTime);
        scheduleData.setScheduledStart(scheduleStartTime);
        scheduleData.setErrors(error);
        return scheduleData;
    }
}
