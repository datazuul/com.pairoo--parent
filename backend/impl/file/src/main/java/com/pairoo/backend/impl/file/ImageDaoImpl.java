package com.pairoo.backend.impl.file;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pairoo.backend.dao.ImageDao;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.enums.ImageEntryType;

/**
 * WARNING: This is a simple implementation ONLY using UUID.randomUUID() as id.
 * Hopefully this generates an unique id in a cluster environment?
 * 
 * @author Ralf Eichinger
 */
public class ImageDaoImpl implements ImageDao {
    static final Logger LOGGER = LoggerFactory.getLogger(ImageDaoImpl.class);

    private File repository;
    private File tempDir;

    private ImageDaoImpl() {

    }

    /**
     * @param repositoryPath
     *            storage location / directory for images
     */
    private ImageDaoImpl(final String repositoryPath, final String temporaryRepositoryPath) {
	super();
	this.repository = new File(repositoryPath);
	this.tempDir = new File(temporaryRepositoryPath);

	if (this.repository.equals(tempDir)) {
	    throw new IllegalStateException("the repository directory " + repositoryPath
		    + " must not be the same directory than the temporary repository!");
	}
	init();
    }

    private String convertId2Path(final String id) {
	if (id.length() == 1) {
	    return "0" + id;
	}
	String result = "";
	// final int l = id.length();
	// final int steps = Math.round(l / 8);
	// for (int i = 0; i < steps; i++) {
	// result += id.substring((i * 8), (i * 8) + 8) + File.separator;
	// }
	// result += id;

	result = id.substring(0, 2);
	return result;
    }

    /**
     * @param imageEntry
     * @param imageEntryType
     * @param repositoryId
     * @return
     * @throws IOException
     */
    private String getFilePath(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final String repositoryId) throws IOException {
	final String relativeFilePath = getRelativeFilePath(imageEntry, imageEntryType, repositoryId);
	final String filePath = repository.getCanonicalPath() + File.separator + relativeFilePath;
	return filePath;
    }

    private String getFilePathTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final String repositoryId) throws IOException {
	final String relativeFilePath = getRelativeFilePath(imageEntry, imageEntryType, repositoryId);
	final String filePath = tempDir.getCanonicalPath() + File.separator + relativeFilePath;
	return filePath;
    }

    private String getRelativeFilePath(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final String repositoryId) {
	final String idPath = convertId2Path(repositoryId);
	// save with original size
	// {id}.{mimetype}
	final StringBuffer filename = new StringBuffer(repositoryId);
	if (imageEntryType.equals(ImageEntryType.NORMAL)) {
	    filename.append("_").append(ImageEntryType.NORMAL.name());
	} else if (imageEntryType.equals(ImageEntryType.MIDDLE)) {
	    filename.append("_").append(ImageEntryType.MIDDLE.name());
	} else if (imageEntryType.equals(ImageEntryType.THUMBNAIL)) {
	    filename.append("_").append(ImageEntryType.THUMBNAIL.name());
	}
	filename.append(".").append(imageEntry.getMediaType().getFileSuffix());

	final String relativeFilePath = idPath + File.separator + filename.toString();
	return relativeFilePath;
    }

    public void init() {
	// create repository
	if (!repository.exists()) {
	    if (!repository.mkdirs()) {
		throw new RuntimeException("Repository path " + repository.getAbsolutePath()
			+ " can not be initialised");
	    }
	}
	// create temp dir
	if (!tempDir.exists()) {
	    if (!tempDir.mkdirs()) {
		throw new RuntimeException("Temporary directory " + tempDir.getAbsolutePath()
			+ " can not be initialised");
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.pairoo.backend.dao.ImageDao#load(com.pairoo.domain.ImageEntry,
     * com.pairoo.domain.enums.ImageEntryType)
     */
    @Override
    public BufferedImage load(final ImageEntry imageEntry, final ImageEntryType imageEntryType) throws IOException {
	final String repositoryId = imageEntry.getRepositoryId();
	if (repositoryId != null) {
	    final String filePath = getFilePath(imageEntry, imageEntryType, repositoryId);
	    return load(imageEntry, filePath);
	}
	return null;
    }

    private BufferedImage load(final ImageEntry imageEntry, final String filePath) throws IOException {
	final File file = new File(filePath);
	LOGGER.debug("loading image id '{}' from '{}'", new Object[] { imageEntry.getId(), filePath });
	final BufferedImage bi = ImageIO.read(file);
	// // Create output stream
	// final ByteArrayOutputStream out = new
	// ByteArrayOutputStream();
	// // Write image using any matching ImageWriter
	// ImageIO.write(bi, imageEntry.getMediaType().getSubType(),
	// out);
	// // Return the image data
	// return out.toByteArray();
	return bi;
    }

    @Override
    public BufferedImage loadTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType)
	    throws IOException {
	final String repositoryId = imageEntry.getRepositoryId();
	if (repositoryId != null) {
	    final String filePath = getFilePathTemporary(imageEntry, imageEntryType, repositoryId);
	    return load(imageEntry, filePath);
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.pairoo.backend.dao.ImageDao#save(com.pairoo.domain.ImageEntry,
     * com.pairoo.domain.enums.ImageEntryType, byte[])
     */
    @Override
    public void save(final ImageEntry imageEntry, final ImageEntryType imageEntryType, final byte[] imageBytes,
	    final float quality) {
	// convert byte array back to BufferedImage
	final InputStream in = new ByteArrayInputStream(imageBytes);
	BufferedImage bufferedImage;
	try {
	    bufferedImage = ImageIO.read(in);
	    save(imageEntry, imageEntryType, bufferedImage, quality);
	} catch (final IOException e) {
	    LOGGER.error("ERROR converting bytes to buffered image.", e);
	}
    }

    @Override
    public void save(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final BufferedImage bufferedImage, final float quality) {
	final String repositoryId = getRepositoryId(imageEntry);
	try {
	    final String filePath = getFilePath(imageEntry, imageEntryType, repositoryId);
	    final String format = imageEntry.getMediaType().getSubType();
	    writeImage(bufferedImage, filePath, format, quality);
	} catch (final IOException e) {
	    LOGGER.error("ERROR saving file.", e);
	}
	imageEntry.setHeight(bufferedImage.getHeight());
	imageEntry.setWidth(bufferedImage.getWidth());
	imageEntry.setRepositoryId(repositoryId);
    }

    @Override
    public void saveTemporary(final ImageEntry imageEntry, final ImageEntryType imageEntryType,
	    final BufferedImage bufferedImage, final float quality) {
	final String repositoryId = getRepositoryId(imageEntry);
	try {
	    final String filePath = getFilePathTemporary(imageEntry, imageEntryType, repositoryId);
	    final String format = imageEntry.getMediaType().getSubType();
	    writeImage(bufferedImage, filePath, format, quality);
	} catch (final IOException e) {
	    LOGGER.error("ERROR saving file.", e);
	}
	imageEntry.setHeight(bufferedImage.getHeight());
	imageEntry.setWidth(bufferedImage.getWidth());
	imageEntry.setRepositoryId(repositoryId);
    }

    /**
     * Write a image file setting the compression quality.
     * 
     * @param image
     *            a BufferedImage to be saved
     * @param destFile
     *            destination file (absolute or relative path)
     * @param format
     *            format of image
     * @param quality
     *            a float between 0 and 1, where 1 means uncompressed.
     * @throws IOException
     *             in case of problems writing the file
     */
    private void writeImage(final BufferedImage image, final String destFile, final String format, final float quality)
	    throws IOException {
	LOGGER.info("saving image '{}'", destFile);
	final ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
	final ImageWriteParam param = writer.getDefaultWriteParam();
	param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	param.setCompressionQuality(quality);
	final File file = new File(destFile);
	if (!file.exists()) {
	    final File directory = file.getParentFile();
	    directory.mkdirs();
	    file.createNewFile();
	}
	final FileImageOutputStream output = new FileImageOutputStream(file);
	writer.setOutput(output);
	final IIOImage iioImage = new IIOImage(image, null, null);
	writer.write(null, iioImage, param);
	writer.dispose();
    }

    private String getRepositoryId(final ImageEntry imageEntry) {
	String repositoryId = imageEntry.getRepositoryId();
	if (repositoryId == null) {
	    final UUID uuid = UUID.randomUUID();
	    repositoryId = uuid.toString();
	}
	return repositoryId;
    }

    @Override
    public void delete(final ImageEntry imageEntry) {
	final String repositoryId = imageEntry.getRepositoryId();
	if (repositoryId != null) {
	    final ImageEntryType[] values = ImageEntryType.values();
	    for (int i = 0; i < values.length; i++) {
		final ImageEntryType imageEntryType = values[i];
		try {
		    final String filePath = getFilePath(imageEntry, imageEntryType, repositoryId);
		    final File file = new File(filePath);
		    if (file.exists()) {
			LOGGER.info("deleting image '{}'", filePath);
			file.delete();
		    }
		} catch (final IOException e) {
		    LOGGER.error("ERROR deleting file.", e);
		}
	    }
	}
    }

    @Override
    public void deleteTemporary(final ImageEntry imageEntry) {
	final String repositoryId = imageEntry.getRepositoryId();
	if (repositoryId != null) {
	    final ImageEntryType[] values = ImageEntryType.values();
	    for (int i = 0; i < values.length; i++) {
		final ImageEntryType imageEntryType = values[i];
		try {
		    final String filePath = getFilePathTemporary(imageEntry, imageEntryType, repositoryId);
		    final File file = new File(filePath);
		    if (file.exists()) {
			LOGGER.info("deleting image '{}'", filePath);
			file.delete();
		    }
		} catch (final IOException e) {
		    LOGGER.error("ERROR deleting file.", e);
		}
	    }
	}
    }
}
