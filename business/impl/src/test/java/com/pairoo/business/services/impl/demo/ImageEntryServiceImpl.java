package com.pairoo.business.services.impl.demo;

import java.util.List;

import com.googlecode.genericdao.search.ISearch;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.domain.ImageEntry;

public class ImageEntryServiceImpl implements ImageEntryService {

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
    public boolean delete(final ImageEntry object) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<ImageEntry> findAll(final int offset, final int max) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ImageEntry get(final Long id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean save(final ImageEntry object) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<ImageEntry> search(final ISearch search) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(final ImageEntry object) {
	// TODO Auto-generated method stub

    }

    @Override
    public int getMaxCount() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int getVisibleCount(final List<ImageEntry> imageEntries) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public ImageEntry getByRepositoryId(String repositoryId) {
	// TODO Auto-generated method stub
	return null;
    }

}
