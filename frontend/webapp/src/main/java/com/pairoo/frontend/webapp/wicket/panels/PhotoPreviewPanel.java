package com.pairoo.frontend.webapp.wicket.panels;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.business.api.ImageService;
import com.pairoo.business.api.UserProfileService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.ImageEntryType;

/**
 * @author Ralf Eichinger
 */
public abstract class PhotoPreviewPanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    static final Logger LOGGER = LoggerFactory.getLogger(PhotoPreviewPanel.class);
    @SpringBean(name = ImageEntryService.BEAN_ID)
    private ImageEntryService imageEntryService;
    @SpringBean(name = ImageService.BEAN_ID)
    private ImageService imageService;
    @SpringBean(name = UserProfileService.BEAN_ID)
    private UserProfileService userProfileService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private final IModel<ImageEntry> originalImageEntryModel;

    public PhotoPreviewPanel(final String id, final IModel<ImageEntry> uploadedImageEntryModel,
            final IModel<User> userModel, final IModel<ImageEntry> originalImageEntryModel) {
        super(id, uploadedImageEntryModel, userModel);
        this.originalImageEntryModel = originalImageEntryModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        IModel<ImageEntry> defaultModel = (IModel<ImageEntry>) getDefaultModel();
        add(createPreviewImage("previewImage", defaultModel));
        add(createBackComponent("back", defaultModel));
        add(createSaveComponent("save", defaultModel, originalImageEntryModel));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PhotoPreviewPanel.class);
    }

    private Component createSaveComponent(final String id, final IModel<ImageEntry> uploadedImageEntryModel,
            final IModel<ImageEntry> originalImageEntryModel) {
        final Link component = new Link(id) {
            private static final long serialVersionUID = 2L;

            @Override
            public void onClick() {
                ImageEntry uploadedImageEntry = uploadedImageEntryModel.getObject();
                ImageEntry originalImageEntry = originalImageEntryModel.getObject();

                // move temporary saved files of uploaded image entry in
                // repository
                BufferedImage temporaryNormalImage = null;
                try {
                    temporaryNormalImage = imageService.loadTemporary(uploadedImageEntry, ImageEntryType.NORMAL);
                } catch (final IOException e) {
                    LOGGER.error("error loading temporary file with repository id '{}'",
                            uploadedImageEntry.getRepositoryId(), e);
                    throw new WicketRuntimeException();
                }
                try {
                    imageService.save(uploadedImageEntry, ImageEntryType.NORMAL, temporaryNormalImage);
                } catch (final IOException e) {
                    LOGGER.error("error saving file with repository id '{}'", uploadedImageEntry.getRepositoryId(), e);
                    throw new WicketRuntimeException();
                }

                if (originalImageEntry != null && originalImageEntry.getRepositoryId() != null) {
                    // delete original images
                    imageService.delete(originalImageEntry);
                    // update original image entry (e.g. to point to repository
                    // id of uploaded image entry)
                    originalImageEntry.setClientFileName(uploadedImageEntry.getClientFileName());
                    originalImageEntry.setHeight(uploadedImageEntry.getHeight());
                    originalImageEntry.setRepositoryId(uploadedImageEntry.getRepositoryId());
                    originalImageEntry.setVisible(false);
                    originalImageEntry.setWidth(uploadedImageEntry.getWidth());
                    imageEntryService.save(originalImageEntry);
                } else {
                    // add image to user profile and save it
                    User user = getUserModel().getObject();
                    final UserProfile userProfile = user.getUserProfile();
                    userProfile.getImageEntries().add(uploadedImageEntry);
                    userProfileService.save(userProfile);
                }
                // delete temporary images
                imageService.deleteTemporary(uploadedImageEntry);

                // back to gallery
                User user = getUserModel().getObject();
                PhotoPreviewPanel.this.replaceWith(new ProfileMyPhotosPanel(PhotoPreviewPanel.this.getId(),
                        new LoadableDetachableDomainObjectModel<Long>(user, userService)));
            }
        };
        return component;
    }

    private Component createBackComponent(final String id, final IModel<ImageEntry> model) {
        final Link<ImageEntry> component = new Link<ImageEntry>(id, model) {
            private static final long serialVersionUID = 2L;

            @Override
            public void onClick() {
                final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                // restore from raw original
                BufferedImage bufferedImage;
                try {
                    bufferedImage = imageService.loadTemporary(imageEntry, ImageEntryType.RAW_ORIGINAL);
                    imageService.saveTemporary(imageEntry, ImageEntryType.RAW_ORIGINAL, bufferedImage);
                } catch (final IOException e) {
                    LOGGER.error("error loading/saving temporary file with repository id '{}'",
                            imageEntry.getRepositoryId(), e);
                    throw new WicketRuntimeException();
                }
                onGoBack();
            }
        };
        return component;
    }

    private Component createPreviewImage(final String id, final IModel<ImageEntry> imageEntryModel) {
        final IResource resource = new DynamicImageResource("jpeg") {
            private static final long serialVersionUID = 2L;

            @Override
            protected byte[] getImageData(final Attributes attributes) {
                BufferedImage bufferedImage = null;
                ImageEntry imageEntry = imageEntryModel.getObject();
                try {
                    bufferedImage = imageService.loadTemporary(imageEntry, ImageEntryType.NORMAL);
                } catch (final IOException e) {
                    LOGGER.error("error loading temporary file with repository id '{}'", imageEntry.getRepositoryId(), e);
                    throw new WicketRuntimeException();
                }
                return toImageData(bufferedImage);
            }
        };
        return new NonCachingImage(id, resource);
    }

    /**
     * Hook for implementing action after finishing panel
     */
    protected abstract void onGoBack();
}
