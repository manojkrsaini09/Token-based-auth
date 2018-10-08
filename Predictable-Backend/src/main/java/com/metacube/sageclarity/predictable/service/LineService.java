package com.metacube.sageclarity.predictable.service;

import com.metacube.sageclarity.predictable.entity.Line;
import com.metacube.sageclarity.predictable.exception.ApplicationLevelException;

import java.util.List;

public interface LineService {
    public Line getById(Long id) throws ApplicationLevelException;
    public Line save(Line Line) throws ApplicationLevelException;
    public List<Line> getAll() throws ApplicationLevelException;
    public Boolean deleteLine(Line Line) throws ApplicationLevelException;
    public List<Line> getAllByCompanyId(Long companyId) throws ApplicationLevelException;
}
