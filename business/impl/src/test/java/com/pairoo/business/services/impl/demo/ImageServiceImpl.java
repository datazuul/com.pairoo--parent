package com.pairoo.business.services.impl.demo;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.pairoo.business.api.ImageService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.ImageEntryType;

public class ImageServiceImpl implements ImageService {

    @Override
    public void save(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final byte[] imageBytes)
	    throws IOException {
	// TODO Auto-generated method stub

    }

    @Override
    public void save(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final BufferedImage bufferedImage)
	    throws IOException {
	// TODO Auto-generated method stub

    }

    @Override
    public BufferedImage load(final ImageEntry imageEntry, final ImageEntryType imageEntryType) throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void delete(final ImageEntry imageEntry) {
	// TODO Auto-generated method stub

    }

    @Override
    public void saveTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final byte[] imageBytes)
	    throws IOException {
	// TODO Auto-generated method stub

    }

    @Override
    public void saveTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final BufferedImage bufferedImage) throws IOException {
	// TODO Auto-generated method stub

    }

    @Override
    public BufferedImage loadTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType)
	    throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void deleteTemporary(final ImageEntry imageEntry) {
	// TODO Auto-generated method stub

    }

    @Override
    public double getMaxFileSizeInMB() {
	return 5;
    }

}
