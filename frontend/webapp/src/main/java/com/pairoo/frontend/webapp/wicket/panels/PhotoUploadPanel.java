package com.pairoo.frontend.webapp.wicket.panels;

import java.io.File;
import java.io.IOException;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.business.api.ImageService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.MediaType;
import com.pairoo.frontend.webapp.wicket.WicketWebApplication;

/**
 * @author Ralf Eichinger
 */
public class PhotoUploadPanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    static final Logger LOGGER = LoggerFactory.getLogger(PhotoUploadPanel.class);
    private static final String[] VALID_CONTENT_TYPES = {"image/gif", "image/jpeg", "image/png"};
    @SpringBean(name = ImageEntryService.BEAN_ID)
    private ImageEntryService imageEntryService;
    @SpringBean(name = ImageService.BEAN_ID)
    private ImageService imageService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
//    private FileUploadField fileUploadField;
    private WebMarkupContainer step2Container;
    private Label step2Text;

    public PhotoUploadPanel(final String id, final IModel<ImageEntry> imageEntryModel, final IModel<User> userModel) {
        super(id, imageEntryModel, userModel);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PhotoUploadPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        FileUploadForm form = new FileUploadForm("ajax-simpleUpload");
//        final Form<ImageEntry> form = createForm((IModel<ImageEntry>) PhotoUploadPanel.this.getDefaultModel());
        add(form);

//        form.add(fileUploadField("fileInput"));
        form.add(step2Container("step2Container"));
        step2Container.add(step2Text("step2Text"));
//        form.add(new UploadProgressBar("progress", form));
    }

    private Component step2Text(String id) {
        step2Text = new Label(id, new StringResourceModel("step2.text", new Model<String>("")));
        step2Text.setEscapeModelStrings(false);
        return step2Text;
    }

    private WebMarkupContainer step2Container(String id) {
        step2Container = new WebMarkupContainer(id);
        // step2Container.setVisible(false);
        step2Container.setOutputMarkupId(true);
        step2Container.setOutputMarkupPlaceholderTag(true);
        return step2Container;
    }

//    private FileUploadField fileUploadField(String id) {
////        fileUploadField = new FileUploadField(id);
//        // behavior
////        fileUploadField.add(new AjaxFormComponentUpdatingBehavior("onchange") {
////            private static final long serialVersionUID = 1L;
////
////            @Override
////            protected void onUpdate(final AjaxRequestTarget target) {
////                // FIXME
////                // org.apache.wicket.util.upload.FileUploadBase$IOFileUploadException:
////                // Processing of multipart/form-data request failed. Socket read
////                // failed
////                // java.io.IOException: Socket read failed
////                final Request request = RequestCycle.get().getRequest();
////                final String filename = request.getQueryParameters().getParameterValue("filename").toString();
////                LOGGER.info("filepath changed to {}", filename);
////                step2Text.setDefaultModel(new StringResourceModel("step2.text", new Model<String>(filename)));
////                // step2Container.setVisible(true);
////                // does not work with Apple upload field...
////                target.add(step2Container);
////            }
////
////            @Override
////            public CharSequence getCallbackUrl() {
////                final CharSequence callBackUrl = super.getCallbackUrl();
////                return callBackUrl + "&filename=' + this.value + '";
////            }
////        });
//        return fileUploadField;
//    }
//    private Form<ImageEntry> createForm(final IModel<ImageEntry> model) {
//
//        @SuppressWarnings("unchecked")
//        final Form<ImageEntry> form = new ShinyForm("ajax-simpleUpload", model) {
//            private static final long serialVersionUID = 1L;
//
//            /**
//             * @see org.apache.wicket.markup.html.form.Form#onSubmit()
//             */
//            @Override
//            protected void onSubmit() {
//                final FileUpload upload = fileUploadField.getFileUpload();
//                if (upload != null) {
//                    // validation
//                    if (upload.getSize() == 0) {
//                        error(getString("error.upload.empty"));
//                        return;
//                    } else if (!checkContentType(upload.getContentType())) {
//                        error(getString("error.upload.onlyImages"));
//                        return;
//                    }
//
//                    final ImageEntry originalImageEntry = (ImageEntry) getDefaultModelObject();
//
//                    final ImageEntry uploadedImageEntry = new ImageEntry();
//                    // uploadedImageEntry.setMediaType(MediaType.parseMediaType(upload.getContentType()));
//                    final String contentType = upload.getContentType();
//                    if (MediaType.IMAGE_GIF.getContentType().equals(contentType)) {
//                        uploadedImageEntry.setMediaType(MediaType.IMAGE_GIF);
//                    } else if (MediaType.IMAGE_JPEG.getContentType().equals(contentType)) {
//                        uploadedImageEntry.setMediaType(MediaType.IMAGE_JPEG);
//                    } else if (MediaType.IMAGE_PNG.getContentType().equals(contentType)) {
//                        uploadedImageEntry.setMediaType(MediaType.IMAGE_PNG);
//                    }
//                    final String clientFileName = upload.getClientFileName();
//                    uploadedImageEntry.setClientFileName(clientFileName);
//
//                    try {
//                        // save raw original image
//                        final byte[] imageBytes = upload.getBytes();
//                        imageService.saveTemporary(uploadedImageEntry, ImageEntryType.RAW_ORIGINAL, imageBytes);
//                        upload.delete();
//                    } catch (final IOException ioe) {
//                        LOGGER.error(ioe.getMessage() + "\n[content-type: '" + contentType + "', clientside filename '"
//                                + clientFileName + "']");
//                        ioe.printStackTrace();
//
//                        error(getString("error.upload.imageSave"));
//                    }
//
//                    // call next panel/page
//                    // next panel: edit image
//                    final User user = userModel.getObject();
//                    final PhotoEditPanel photoEditPanel = new PhotoEditPanel(PhotoUploadPanel.this.getId(),
//                            new LoadableDetachableDomainObjectModel<Long>(user, userService), originalImageEntry, uploadedImageEntry) {
//                        private static final long serialVersionUID = 1L;
//
//                        @Override
//                        protected void onGoBack() {
//                            replaceWith(new PhotoUploadPanel(getId(), new LoadableDetachableDomainObjectModel<Long>(user, userService),
//                                    (IModel<ImageEntry>) getModel()));
//                            logEnter(PhotoUploadPanel.class);
//                        }
//                    };
//                    PhotoUploadPanel.this.replaceWith(photoEditPanel);
//                } else {
//                    // The FileUpload is null.
//                    error(getString("error.upload.noImage"));
//                    return;
//                }
//            }
//        };
//        // set this form to multipart mode (always needed for uploads!)
//        form.setMultiPart(true);
//        // DiskFileItemFactory itemFactory = new DiskFileItemFactory(600 *
//        // 1024, getUploadFolder());
//        // FileItem fileItem = itemFactory.createItem("fileInput", null,
//        // false, getUploadFolder().getAbsolutePath());
//        // FileUpload fu = new FileUpload(fileItem);
//        // Add one file input field
//        // add(fileUploadField = new FileUploadField("fileInput", new
//        // Model<FileUpload>(fu)));
//        form.setMaxSize(Bytes.megabytes(imageService.getMaxFileSizeInMB()));
//        // commented because we get a serializable exception here because of the
//        // contained DeferredFileOutputStream in DiskFileItem (which is
//        // null)
//
//        return form;
//    }
    private boolean checkContentType(final String contentType) {
        for (int i = 0; i < VALID_CONTENT_TYPES.length; i++) {
            if (VALID_CONTENT_TYPES[i].equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Form for uploads.
     */
    private class FileUploadForm extends Form<Void> {

        FileUploadField fileUploadField;

        /**
         * Construct.
         *
         * @param id Component name
         */
        public FileUploadForm(String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize(); //To change body of generated methods, choose Tools | Templates.
            // set this form to multipart mode (allways needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            add(fileUploadField = new FileUploadField("fileUploadField"));

            // Set maximum size
            setMaxSize(Bytes.megabytes(imageService.getMaxFileSizeInMB()));
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        protected void onSubmit() {
            final FileUpload upload = fileUploadField.getFileUpload();
            if (upload != null) {
                // validation
                if (upload.getSize() == 0) {
                    error(getString("error.upload.empty"));
                    return;
                } else if (!checkContentType(upload.getContentType())) {
                    error(getString("error.upload.onlyImages"));
                    return;
                }

                final ImageEntry uploadedImageEntry = new ImageEntry();
                // uploadedImageEntry.setMediaType(MediaType.parseMediaType(upload.getContentType()));
                final String contentType = upload.getContentType();
                if (MediaType.IMAGE_GIF.getContentType().equals(contentType)) {
                    uploadedImageEntry.setMediaType(MediaType.IMAGE_GIF);
                } else if (MediaType.IMAGE_JPEG.getContentType().equals(contentType)) {
                    uploadedImageEntry.setMediaType(MediaType.IMAGE_JPEG);
                } else if (MediaType.IMAGE_PNG.getContentType().equals(contentType)) {
                    uploadedImageEntry.setMediaType(MediaType.IMAGE_PNG);
                }
                final String clientFileName = upload.getClientFileName();
                uploadedImageEntry.setClientFileName(clientFileName);

                try {
                    // save raw original image
                    final byte[] imageBytes = upload.getBytes();
                    imageService.saveTemporary(uploadedImageEntry, ImageEntryType.RAW_ORIGINAL, imageBytes);
                    upload.delete();
                } catch (final IOException ioe) {
                    LOGGER.error(ioe.getMessage() + "\n[content-type: '" + contentType + "', clientside filename '"
                            + clientFileName + "']");
                    ioe.printStackTrace();

                    error(getString("error.upload.imageSave"));
                }

                // call next panel/page
                // next panel: edit image
                final User user = userModel.getObject();
                final ImageEntry originalImageEntry = (ImageEntry) PhotoUploadPanel.this.getDefaultModelObject();
                IModel<ImageEntry> originalImageEntryModel;
                if (originalImageEntry == null) {
                    originalImageEntryModel = Model.of(new ImageEntry());
                } else {
                    originalImageEntryModel = new LoadableDetachableDomainObjectModel<ImageEntry>(originalImageEntry, imageEntryService);
                }

                final PhotoEditPanel photoEditPanel = new PhotoEditPanel(PhotoUploadPanel.this.getId(), Model.of(uploadedImageEntry),
                        new LoadableDetachableDomainObjectModel<Long>(user, userService),
                        originalImageEntryModel) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onGoBack() {
                        replaceWith(new PhotoUploadPanel(getId(), originalImageEntryModel,
                                new LoadableDetachableDomainObjectModel<Long>(user, userService)));
                    }
                };
                PhotoUploadPanel.this.replaceWith(photoEditPanel);
            } else {
                // The FileUpload is null.
                error(getString("error.upload.noImage"));
                return;
            }

//            final List<FileUpload> uploads = fileUploadField.getFileUploads();
//            if (uploads != null) {
//                for (FileUpload upload : uploads) {
//                    // Create a new file
//                    File newFile = new File(getUploadFolder(), upload.getClientFileName());
//
//                    // Check new file, delete if it already existed
//                    checkFileExists(newFile);
//                    try {
//                        // Save to new file
//                        newFile.createNewFile();
//                        upload.writeTo(newFile);
//
//                        getPage().info("saved file: " + upload.getClientFileName());
//                    } catch (Exception e) {
//                        throw new IllegalStateException("Unable to write file", e);
//                    }
//                }
//            }
        }
    }

    /**
     * Check whether the file allready exists, and if so, try to delete it.
     *
     * @param newFile the file to check
     */
    private void checkFileExists(File newFile) {
        if (newFile.exists()) {
            // Try to delete the file
            if (!Files.remove(newFile)) {
                throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
            }
        }
    }

    private Folder getUploadFolder() {
        return ((WicketWebApplication) Application.get()).getUploadFolder();
    }
}
