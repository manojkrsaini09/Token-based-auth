package com.metacube.sageclarity.predictable.service.impl;

import com.metacube.sageclarity.predictable.dao.LineDao;
import com.metacube.sageclarity.predictable.entity.Company;
import com.metacube.sageclarity.predictable.entity.Line;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;
import com.metacube.sageclarity.predictable.exception.InvalidParamException;
import com.metacube.sageclarity.predictable.service.LineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineServiceImpl implements LineService {

    private static final Logger logger = LoggerFactory.getLogger(LineServiceImpl.class);
    @Autowired
    private LineDao LineDao;

    @Override
    public Line getById(Long id) throws ApplicationLevelException {
        if(id == null){
            String message= "Line id is null";
            logger.error(message);
            throw new InvalidParamException(message);
        }
        try {
            Line Line = LineDao.getById(id);
            return Line;
        }catch (Exception e){
            String message = "Db exception while finding Line by id: "+ id + ". " + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public Line save(Line Line) throws ApplicationLevelException {
        if(Line == null){
            String message= "Null object";
            logger.error(message);
            throw new InvalidParamException(message);
        }
        try{
            Line = LineDao.save(Line);
            return Line;
        }catch (Exception e){
            String message = "Db exception while saving Line with name "+ Line.getLineName() + ". " + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public List<Line> getAll() throws ApplicationLevelException {
        try{
            List<Line> Lines = LineDao.getAll();
            return Lines;
        }catch(Exception e){
            String message = "Db exception while fetching all Lines :"+ e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public Boolean deleteLine(Line Line) throws ApplicationLevelException {
        if(Line == null){
            String message= "Null object";
            logger.error(message);
            throw new InvalidParamException(message);
        }
        try{
            Line.setDeleted(true);
            Line = LineDao.save(Line);
            return true;
        }catch (Exception e){
            String message = "Db exception while deleting Line with id "+ Line.getId() + ". " + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }

    @Override
    public List<Line> getAllByCompanyId(Long companyId) throws ApplicationLevelException {
        try{
            return LineDao.getByCompany(new Company(companyId));
        }catch(Exception e) {
            String message = "Db exception while fetching  Line for company id " + companyId + ". " + e.getMessage();
            logger.error(message, e);
            throw new ApplicationLevelException(message, e);
        }
    }
}
