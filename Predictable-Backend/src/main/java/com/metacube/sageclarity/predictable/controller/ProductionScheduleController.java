package com.metacube.sageclarity.predictable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacube.sageclarity.predictable.entity.ProductionScheduleMaster;
import com.metacube.sageclarity.predictable.entity.User;
import com.metacube.sageclarity.predictable.enums.ExceptionType;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.exception.InvalidParamException;
import com.metacube.sageclarity.predictable.helper.RequestHelper;
import com.metacube.sageclarity.predictable.helper.ResponseHelper;
import com.metacube.sageclarity.predictable.service.ProductionScheduleDataService;
import com.metacube.sageclarity.predictable.service.ProductionScheduleMasterService;
import com.metacube.sageclarity.predictable.service.XLSDataReader;
import com.metacube.sageclarity.predictable.util.ApplicationUtil;
import com.metacube.sageclarity.predictable.vo.DataUploadRowVO;
import com.metacube.sageclarity.predictable.vo.ProductionScheduleMasterVO;
import com.metacube.sageclarity.predictable.vo.ResponseObject;
import com.metacube.sageclarity.predictable.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductionScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduleController.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ProductionScheduleMasterService scheduleService;

    @Autowired
    private ProductionScheduleDataService productionScheduleDataService;

    @Autowired
    private XLSDataReader dataReader;

    @RequestMapping(value = "/schedule/create", produces = "application/json",consumes="application/json"
            ,method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject createSchedule(@RequestBody String scheduleStr){
        if(RequestHelper.isEmptyRequestString(scheduleStr))
            return (ResponseObject.getResponse(ExceptionType.INVALID_METHOD_PARAM.getMessage()
                    , ExceptionType.INVALID_METHOD_PARAM.getCode()));
        try {
            ProductionScheduleMasterVO scheduleVO = mapper.readValue(scheduleStr,ProductionScheduleMasterVO.class);
            ProductionScheduleMaster obj = new ProductionScheduleMaster(scheduleVO);
            obj = scheduleService.save(obj);
            return ResponseObject.getResponse(new ProductionScheduleMasterVO(obj));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.GENERAL_ERROR.getMessage()
                    , ExceptionType.GENERAL_ERROR.getCode());
        }
    }

    @RequestMapping(value = "/schedule/update", produces = "application/json",consumes="application/json"
            ,method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject updateSchedule(@RequestBody String scheduleStr){
        if(RequestHelper.isEmptyRequestString(scheduleStr))
            return (ResponseObject.getResponse(ExceptionType.INVALID_METHOD_PARAM.getMessage()
                    , ExceptionType.INVALID_METHOD_PARAM.getCode()));
        try {
            ProductionScheduleMasterVO productScheduleVO = mapper.readValue(scheduleStr,ProductionScheduleMasterVO.class);
            Long scheduleId = productScheduleVO.getId();
            ProductionScheduleMaster schedule = scheduleService.getById(scheduleId);
            if(schedule == null){
                logger.error("No Schedule found for id: " + scheduleId);
                return ResponseObject.getResponse(ExceptionType.NO_DATA_FOUND.getMessage()
                        , ExceptionType.NO_DATA_FOUND.getCode());
            }

            schedule = new ProductionScheduleMaster(productScheduleVO,schedule);
            schedule = scheduleService.save(schedule);
            return ResponseObject.getResponse(new ProductionScheduleMasterVO(schedule));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.GENERAL_ERROR.getMessage()
                    , ExceptionType.GENERAL_ERROR.getCode());
        }
    }

    @RequestMapping(value = "/schedule/all", produces = "application/json",method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseObject getAllSchedules(@RequestParam(value = "companyId") String  companyIdStr){
        Long companyId = null;
        if(StringUtils.isNotBlank(companyIdStr)){
            companyId = Long.parseLong(companyIdStr);
        }
        try {
            List<ProductionScheduleMaster> schedules = null;
            if(companyId!=null){
                schedules = scheduleService.getAllByCompanyId(companyId);
            }else{
                schedules = scheduleService.getAll();
            }
            return ResponseObject.getResponse(ResponseHelper.getScheduleVOList(schedules));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.GENERAL_ERROR.getMessage()
                    , ExceptionType.GENERAL_ERROR.getCode());
        }
    }

    @RequestMapping(value = "/schedule" , method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    ResponseObject getScheduleById(@RequestParam(value = "id", required = true) String idStr) {
        Long entityId = Long.parseLong(idStr);
        try {
            ProductionScheduleMaster schedule= scheduleService.getById(entityId);
            if(schedule == null){
                logger.error("No schedule found for id: " + entityId);
                return ResponseObject.getResponse(ExceptionType.NO_DATA_FOUND.getMessage()
                        , ExceptionType.NO_DATA_FOUND.getCode());
            }
            return ResponseObject.getResponse(new ProductionScheduleMasterVO(schedule));
        }catch (InvalidParamException e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.INVALID_METHOD_PARAM.getMessage()
                    , ExceptionType.INVALID_METHOD_PARAM.getCode());
        } catch (ApplicationLevelException e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.GENERAL_ERROR.getMessage()
                    , ExceptionType.GENERAL_ERROR.getCode());
        }
    }

   /* @RequestMapping(value = "/schedule/delete" , method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    ResponseObject deleteSchedule(@RequestParam(value = "id", required = true) String idStr) {
        Long entityId = Long.parseLong(idStr);
        try {
            ProductionScheduleMaster entity = scheduleService.getById(entityId);
            if(entity == null){
                logger.error("No schedule found for id: " + entityId);
                return ResponseObject.getResponse(ExceptionType.NO_DATA_FOUND.getMessage()
                        , ExceptionType.NO_DATA_FOUND.getCode());
            }
            scheduleService.deleteSchedule(entity);
            return ResponseObject.getResponse(true);
        }catch (InvalidParamException e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.INVALID_METHOD_PARAM.getMessage()
                    , ExceptionType.INVALID_METHOD_PARAM.getCode());
        } catch (ApplicationLevelException e) {
            logger.error(e.getMessage(), e);
            return ResponseObject.getResponse(ExceptionType.GENERAL_ERROR.getMessage()
                    , ExceptionType.GENERAL_ERROR.getCode());
        }
    }*/

   @PostMapping("/schedule/upload")
   public ResponseEntity<ResponseObject> uploadSchedule(@RequestParam("file") MultipartFile file,HttpSession session) {
       String message = "";
       try {
           DataUploadRowVO dataVO = dataReader.readData(file.getInputStream());
           if(dataVO==null){
               message = "Failed to read file " + file.getOriginalFilename() + "!";
               return new ResponseEntity(ResponseObject.getResponse(message),HttpStatus.INTERNAL_SERVER_ERROR);
           }
           UserVO userVO = ApplicationUtil.getUser(session);
           if(userVO == null){
               return new ResponseEntity(ResponseObject.getResponse("Unable to find user."),HttpStatus.INTERNAL_SERVER_ERROR);
           }
           User user = new User(userVO);
           ProductionScheduleMaster master = scheduleService.createProductionScheduleFromExcelDataVO(dataVO,file.getOriginalFilename(),user);
           if(master==null || master.getId()==0){
               message = "Unable to create schedule master from file." + file.getOriginalFilename() + "!";
               return new ResponseEntity(ResponseObject.getResponse(message),HttpStatus.INTERNAL_SERVER_ERROR);
           }
           productionScheduleDataService.createScheduleDataListFromUploadedFileData(dataVO,master);
           message = "Schedule creation task has been initiated for file." + file.getOriginalFilename() + "!";
           return new ResponseEntity(ResponseObject.getResponse(message),HttpStatus.OK);
       } catch (Exception e) {
           message = "FAIL to upload " + file.getOriginalFilename() + "!";
           return new ResponseEntity(ResponseObject.getResponse(message),HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
}
