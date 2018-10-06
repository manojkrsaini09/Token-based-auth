package com.metacube.sageclarity.predictable.service;

import com.metacube.sageclarity.predictable.entity.ProductionScheduleData;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;

import java.util.List;

public interface ProductionScheduleDataService {
    public ProductionScheduleData getById(Long id) throws ApplicationLevelException;
    public ProductionScheduleData save(ProductionScheduleData schedule) throws ApplicationLevelException;
    public List<ProductionScheduleData> getAll() throws ApplicationLevelException;
    public List<ProductionScheduleData> getByProductionScheduleMaster(Long masterId) throws ApplicationLevelException;
    public List<ProductionScheduleData> getDataListFromUploadedFileData(DataUploadRowVO dataVO, ProductionScheduleMaster master);
}
