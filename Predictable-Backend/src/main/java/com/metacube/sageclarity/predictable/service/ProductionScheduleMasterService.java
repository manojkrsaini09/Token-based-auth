package com.metacube.sageclarity.predictable.service;

import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;
import com.metacube.sageclarity.predictable.entity.User;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;

import java.util.List;

public interface ProductionScheduleMasterService {
    public ProductionScheduleMaster getById(Long id) throws ApplicationLevelException;
    public ProductionScheduleMaster save(ProductionScheduleMaster schedule) throws ApplicationLevelException;
    public List<ProductionScheduleMaster> getAll() throws ApplicationLevelException;
    public Boolean deleteSchedule(ProductionScheduleMaster schedule) throws ApplicationLevelException;
    public List<ProductionScheduleMaster> getAllByCompanyId(Long companyId) throws ApplicationLevelException;
    public ProductionScheduleMaster createProductionScheduleFromExcelDataVO(DataUploadRowVO dataVO, String fileName, User user);
}
