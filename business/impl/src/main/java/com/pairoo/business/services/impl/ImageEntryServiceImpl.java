package com.pairoo.business.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.business.services.persistence.AbstractDomainObjectServiceImpl;
import com.googlecode.genericdao.search.Search;
import com.pairoo.backend.dao.ImageEntryDao;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.domain.ImageEntry;

/**
 * @author Ralf Eichinger
 */
public class ImageEntryServiceImpl extends AbstractDomainObjectServiceImpl<Long, ImageEntry> implements
	ImageEntryService {

    static final Logger LOGGER = LoggerFactory.getLogger(ImageEntryServiceImpl.class);

    /**
     * Constructor needed for test handing over a mock dao.
     * 
     * @param dao
     *            the data access interface
     */
    public ImageEntryServiceImpl(final ImageEntryDao dao) {
	super(dao);
    }

    @Override
    public int getMaxCount() {
	// TODO make it configurable
	return 5;
    }

    @Override
    public int getVisibleCount(final List<ImageEntry> imageEntries) {
	int count = 0;
	for (final ImageEntry imageEntry : imageEntries) {
	    if (imageEntry != null && imageEntry.isVisible()) {
		count++;
	    }
	}
	return count;
    }

    @Override
    public ImageEntry getByRepositoryId(String repositoryId) {
	final Search search = new Search(ImageEntry.class);
	search.addFilterEqual("repositoryId", repositoryId);
	return dao.searchUnique(search);
    }
}
