package com.pairoo.business.services.impl.demo;

import java.util.List;

import com.datazuul.framework.domain.geo.Country;
import com.datazuul.framework.domain.geo.Subdivision;
import com.googlecode.genericdao.search.ISearch;
import com.pairoo.business.api.SubdivisionService;

public class SubdivisionServiceImpl implements SubdivisionService {

    @Override
    public long count(final ISearch search) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public Long countAll() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean delete(final Subdivision object) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<Subdivision> findAll(final int offset, final int max) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Subdivision get(final Long id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean save(final Subdivision object) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<Subdivision> search(final ISearch search) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(final Subdivision object) {
	// TODO Auto-generated method stub

    }

    @Override
    public List<Subdivision> getByCountry(final Country country) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Subdivision> getAllSmallestRecursively(final Subdivision subdivision) {
	// TODO Auto-generated method stub
	return null;
    }

}
