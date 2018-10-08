package com.metacube.sageclarity.predictable.dao;

import com.metacube.sageclarity.predictable.entity.Company;
import com.metacube.sageclarity.predictable.entity.Line;

import java.util.List;

public interface LineDao {
    public Line save(Line Line);
    public List<Line> getAll();
    public Line getById(Long id);
    public List<Line> getByCompany(Company company);
}
