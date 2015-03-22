package com.pairoo.business.services.impl;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pairoo.backend.dao.ImageDao;
import com.pairoo.business.api.ImageService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.MediaType;

/**
 * @see https://cwiki.apache.org/WICKET/uploaddownload.html
 * @author Ralf Eichinger
 */
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);
    private static final float COMPRESSION_QUALITY_SAVE = 0.9f;
    private static final float COMPRESSION_QUALITY_UPDATE = 1.0f;
    private static final int MAX_WIDTH_NORMAL = 740;
    private static final int MAX_HEIGHT_THUMBNAIL = 50;
    private static final int MAX_WIDTH_MIDDLE = 183;
    private final ImageDao dao;

    public ImageServiceImpl(final ImageDao dao) {
	this.dao = dao;
    }

    @Override
    public BufferedImage load(final ImageEntry imageEntry, final ImageEntryType imageEntryType) throws IOException {
	BufferedImage bi = null;
	try {
	    bi = dao.load(imageEntry, imageEntryType);
	} catch (final IOException e) {
	    LOGGER.warn("error loading image {} of type {}", imageEntry.getRepositoryId(), imageEntryType.name());
	    if (imageEntryType != ImageEntryType.RAW_ORIGINAL) {
		// that is business logic: try to recreate if loading fails
		recreateResizedImages(imageEntry);
	    }
	}
	return bi;
    }

    @Override
    public BufferedImage loadTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType)
	    throws IOException {
	BufferedImage bi = null;
	try {
	    bi = dao.loadTemporary(imageEntry, imageEntryType);
	} catch (final IOException e) {
	    LOGGER.warn("error loading image {} of type {}", imageEntry.getRepositoryId(), imageEntryType.name());
	}
	return bi;
    }

    @Override
    public void delete(final ImageEntry imageEntry) {
	dao.delete(imageEntry);
    }

    @Override
    public void deleteTemporary(final ImageEntry imageEntry) {
	dao.deleteTemporary(imageEntry);
    }

    @Override
    public void save(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final BufferedImage bufferedImage)
	    throws IOException {
	// only save as JPEG
	imageEntry.setMediaType(MediaType.IMAGE_JPEG);

	if (imageEntryType.equals(ImageEntryType.RAW_ORIGINAL)) {
	    // first save after upload
	    dao.save(imageEntry, ImageEntryType.NORMAL, scaleToMaxWidth(bufferedImage, MAX_WIDTH_NORMAL),
		    COMPRESSION_QUALITY_SAVE);
	} else if (imageEntryType.equals(ImageEntryType.NORMAL)) {
	    // update after editing image
	    dao.save(imageEntry, ImageEntryType.NORMAL, bufferedImage, COMPRESSION_QUALITY_UPDATE);
	}
	dao.save(imageEntry, ImageEntryType.MIDDLE, scaleToMaxWidth(bufferedImage, MAX_WIDTH_MIDDLE),
		COMPRESSION_QUALITY_SAVE);
	dao.save(imageEntry, ImageEntryType.THUMBNAIL, scaleToMaxHeight(bufferedImage, MAX_HEIGHT_THUMBNAIL),
		COMPRESSION_QUALITY_SAVE);
	imageEntry.setLastModified(new Date());
    }

    @Override
    public void save(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final byte[] imageBytes)
	    throws IOException {
	final BufferedImage bufferedImage = byteArrayToBufferedImage(imageBytes);
	save(imageEntry, imageEntryType, bufferedImage);
    }

    @Override
    public void saveTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final byte[] imageBytes)
	    throws IOException {
	final BufferedImage bufferedImage = byteArrayToBufferedImage(imageBytes);
	saveTemporary(imageEntry, imageEntryType, bufferedImage);
    }

    @Override
    public void saveTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final BufferedImage bufferedImage) throws IOException {
	// only save as JPEG
	imageEntry.setMediaType(MediaType.IMAGE_JPEG);

	if (imageEntryType.equals(ImageEntryType.RAW_ORIGINAL)) {
	    // save also raw original to make it possible to redo cropping
	    dao.saveTemporary(imageEntry, ImageEntryType.RAW_ORIGINAL,
		    scaleToMaxWidth(bufferedImage, MAX_WIDTH_NORMAL), COMPRESSION_QUALITY_SAVE);
	    // first save after upload
	    dao.saveTemporary(imageEntry, ImageEntryType.NORMAL, scaleToMaxWidth(bufferedImage, MAX_WIDTH_NORMAL),
		    COMPRESSION_QUALITY_SAVE);
	} else if (imageEntryType.equals(ImageEntryType.NORMAL)) {
	    // update after editing image (no scaling needed as it is within max
	    // size from first resizing)
	    dao.saveTemporary(imageEntry, ImageEntryType.NORMAL, bufferedImage, COMPRESSION_QUALITY_UPDATE);
	}
	dao.saveTemporary(imageEntry, ImageEntryType.MIDDLE, scaleToMaxWidth(bufferedImage, MAX_WIDTH_MIDDLE),
		COMPRESSION_QUALITY_SAVE);
	dao.saveTemporary(imageEntry, ImageEntryType.THUMBNAIL, scaleToMaxHeight(bufferedImage, MAX_HEIGHT_THUMBNAIL),
		COMPRESSION_QUALITY_SAVE);
	imageEntry.setLastModified(new Date());
    }

    private BufferedImage byteArrayToBufferedImage(final byte[] imageBytes) throws IOException {
	// convert byte array back to BufferedImage
	final InputStream in = new ByteArrayInputStream(imageBytes);
	final BufferedImage bufferedImage = ImageIO.read(in);
	return bufferedImage;
    }

    private byte[] scaleImage(final byte[] imageData, final int maxSize) throws IOException {
	// if (logger.isDebugEnabled()) {
	// logger.debug("Scaling image...");
	// }
	// Get the image from a file.
	final Image inImage = new ImageIcon(imageData).getImage();

	// Determine the scale.
	double scale = (double) maxSize / (double) inImage.getHeight(null);
	if (inImage.getWidth(null) > inImage.getHeight(null)) {
	    scale = (double) maxSize / (double) inImage.getWidth(null);
	}

	// Determine size of new image.
	// One of the dimensions should equal maxSize.
	final int scaledW = (int) (scale * inImage.getWidth(null));
	final int scaledH = (int) (scale * inImage.getHeight(null));

	// Create an image buffer in
	// which to paint on.
	final BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);

	// Set the scale.
	final AffineTransform tx = new AffineTransform();

	// If the image is smaller than
	// the desired image size,
	// don't bother scaling.
	if (scale < 1.0d) {
	    tx.scale(scale, scale);
	}

	// Paint image.
	final Graphics2D g2d = outImage.createGraphics();
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.drawImage(inImage, tx, null);
	g2d.dispose();

	// JPEG-encode the image
	// and write to file.
	final ByteArrayOutputStream os = new ByteArrayOutputStream();
	ImageIO.write(outImage, "jpeg", os);
	// final JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
	// encoder.encode(outImage);
	os.close();
	// if (logger.isDebugEnabled()) {
	// logger.debug("Scaling done.");
	// }
	return os.toByteArray();
    }

    private void recreateResizedImages(final ImageEntry imageEntry) throws IOException {
	// self healing system... (if this fails then really something
	// is broken...)
	LOGGER.info("recreating images");
	BufferedImage bufferedImage;
	try {
	    bufferedImage = load(imageEntry, ImageEntryType.RAW_ORIGINAL);
	    save(imageEntry, ImageEntryType.RAW_ORIGINAL, bufferedImage);
	    LOGGER.info("images {} successfully recreated", imageEntry.getRepositoryId());
	} catch (final IOException e1) {
	    LOGGER.error("ERROR recreating images for {}", imageEntry.getRepositoryId(), e1);
	    throw e1;
	}
    }

    private BufferedImage scaleToMaxHeight(final BufferedImage bufferedImage, final int maxHeight) {
	BufferedImage resizedImage = bufferedImage;
	if (bufferedImage.getHeight() > maxHeight) {
	    resizedImage = Scalr.resize(bufferedImage, maxHeight);
	}
	return resizedImage;
    }

    private BufferedImage scaleToMaxWidth(final BufferedImage bufferedImage, final int maxWidth) {
	BufferedImage resizedImage = bufferedImage;
	if (bufferedImage.getWidth() > maxWidth) {
	    resizedImage = Scalr.resize(bufferedImage, maxWidth);
	}
	return resizedImage;
    }

    @Override
    public double getMaxFileSizeInMB() {
	// Set maximum size to 5MB, cause DLSR cameras produce mostly less than
	// 5 MB
	return 5;
    }
}
