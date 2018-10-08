package com.metacube.sageclarity.predictable.dao.impl;

import com.metacube.sageclarity.predictable.dao.LineDao;
import com.metacube.sageclarity.predictable.dao.jpa.JpaLineDao;
import com.metacube.sageclarity.predictable.entity.Company;
import com.metacube.sageclarity.predictable.entity.Line;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LineDaoImpl implements LineDao {
    @Autowired
    private JpaLineDao jpaLineDao;

    @Override
    public Line save(Line Line) {
        return jpaLineDao.save(Line);
    }

    @Override
    public List<Line> getAll() {
        return jpaLineDao.findAll();
    }

    @Override
    public Line getById(Long id) {
        return jpaLineDao.getOne(id);
    }

    @Override
    public List<Line> getByCompany(Company company) {
        return jpaLineDao.findByCompany(company);
    }
}
