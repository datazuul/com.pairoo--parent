package com.pairoo.frontend.webapp.wicket.panels;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jcrop.Coordinates;
import org.jcrop.CroppableSettings;
import org.jcrop.JcropImage;
import org.jcrop.PreviewSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datazuul.framework.webapp.wicket.components.form.ShinyForm;
import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.business.api.ImageService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.domain.enums.MediaType;

/**
 * @author Ralf Eichinger
 */
public abstract class PhotoEditPanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoEditPanel.class);
    private final Rectangle cropArea = new Rectangle();
    protected final IModel<ImageEntry> originalImageEntryModel;
    @SpringBean(name = ImageEntryService.BEAN_ID)
    private ImageEntryService imageEntryService;
    @SpringBean(name = ImageService.BEAN_ID)
    private ImageService imageService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;

    public PhotoEditPanel(final String id, final IModel<ImageEntry> uploadedImageEntry,
            final IModel<User> userModel, final IModel<ImageEntry> originalImageEntryModel) {
        super(id, uploadedImageEntry, userModel);

        this.originalImageEntryModel = originalImageEntryModel;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(PhotoEditPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // info label
        final Label label = selectionInfoLabel("info");
        add(label);

        // croppable image
        final JcropImage jcropImage = croppableImage("jcropImage");
        add(jcropImage);

        // form
        final ShinyForm form = new ShinyForm("form");
        add(form);

        // buttons
        final Button crop = createCropButton("crop", jcropImage);
        form.add(crop);

        final Button discard = createDiscardButton("discard");
        form.add(discard);
    }

    private Button createDiscardButton(String id) {
        final Button discard = new Button(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                ImageEntry uploadedImageEntry = (ImageEntry) PhotoEditPanel.this.getDefaultModelObject();
                imageService.deleteTemporary(uploadedImageEntry);
                onGoBack();
            }
        };
        discard.setDefaultFormProcessing(false);
        return discard;
    }

    private Button createCropButton(String id, final JcropImage jcropImage) {
        final Button crop = new Button(id) {
            private static final long serialVersionUID = 2L;

            @Override
            public void onSubmit() {
                if (cropArea.width > 0 && cropArea.height > 0) {
                    BufferedImage bi = null;
                    try {
                        ImageEntry uploadedImageEntry = (ImageEntry) PhotoEditPanel.this.getDefaultModelObject();
                        // load original
                        bi = imageService.loadTemporary(uploadedImageEntry, ImageEntryType.RAW_ORIGINAL);

                        LOGGER.info(
                                "trying to crop image from original width={}, height={} to width={}, height={} starting at top point x={}, y={}",
                                new Object[]{bi.getWidth(), bi.getHeight(), cropArea.width, cropArea.height,
                            cropArea.x, cropArea.y});

                        // just crop if crop area smaller than original image
                        if (cropArea.width < bi.getWidth() || cropArea.height < bi.getHeight()) {
                            if (cropArea.width > bi.getWidth()) {
                                cropArea.width = bi.getWidth();
                            }
                            if (cropArea.height > bi.getHeight()) {
                                cropArea.height = bi.getHeight();
                            }
                            
                            // (y + height) or (x + width) is outside of Raster
                            if ((cropArea.y + cropArea.height) > bi.getHeight() || (cropArea.x + cropArea.width) > bi.getWidth()) {
                        	info("error.image.selected.area.invalid");
                        	return;
                            }
                            // crop
                            final BufferedImage bic = bi.getSubimage(cropArea.x, cropArea.y, cropArea.width,
                                    cropArea.height);
                            // save cropped image
                            uploadedImageEntry.setMediaType(MediaType.IMAGE_JPEG);
                            imageService.saveTemporary(uploadedImageEntry, ImageEntryType.NORMAL, bic);

                            // call next panel/page
                            // next panel: preview/save image
                            User user = PhotoEditPanel.this.getUserModel().getObject();
                            final ImageEntry originalImageEntry = originalImageEntryModel.getObject();
                            IModel<ImageEntry> originalImageEntryModel;
                            if (originalImageEntry == null) {
                                originalImageEntryModel = Model.of(new ImageEntry());
                            } else {
                                originalImageEntryModel = new LoadableDetachableDomainObjectModel<ImageEntry>(originalImageEntry, imageEntryService);
                            }

                            final PhotoPreviewPanel photoPreviewPanel = new PhotoPreviewPanel(
                                    PhotoEditPanel.this.getId(), (IModel<ImageEntry>) PhotoEditPanel.this.getDefaultModel(),
                                    new LoadableDetachableDomainObjectModel<Long>(user, userService),
                                    originalImageEntryModel) {
                                private static final long serialVersionUID = 1L;

                                @Override
                                protected void onGoBack() {
                                    replaceWith(PhotoEditPanel.this);
                                    logEnter(PhotoEditPanel.class);
                                }
                            };
                            PhotoEditPanel.this.replaceWith(photoPreviewPanel);
                        } else {
                            // TODO add warning that selection has to be smaller
                            // than original?
                        }
                        // set start selection newly
                        // cropArea.setBounds(0, 0, cropArea.width,
                        // cropArea.height);
//                        jcropImage.getApiController().setSelection(new Coordinates(cropArea.x, cropArea.y, cropArea.width, cropArea.height), null);
                    } catch (final IOException e) {
                        LOGGER.error("error saving temporary file", e);
                    }
                }
            }
        };
        crop.setDefaultFormProcessing(false);
        return crop;
    }

    private JcropImage croppableImage(String id) {
        final IResource resource = new DynamicImageResource("jpeg") {
            private static final long serialVersionUID = 1L;

            @Override
            protected byte[] getImageData(final Attributes attributes) {
                BufferedImage bufferedImage = null;
                try {
                    ImageEntry uploadedImageEntry = (ImageEntry) getDefaultModelObject();
                    bufferedImage = imageService.loadTemporary(uploadedImageEntry, ImageEntryType.NORMAL);
                } catch (final IOException e) {
                    LOGGER.error("error loading temporary file", e);
                }
                return toImageData(bufferedImage);
            }
        };

        final Coordinates coordinates;
        if (!cropArea.isEmpty()) {
            coordinates = convertToCoordinates(cropArea);
        } else {
            coordinates = new Coordinates(10, 10, 10 + 150, 10 + 200);
            updateSelectedRectangle(coordinates);
        }

        CroppableSettings croppableSettings = new CroppableSettings()
                .setAspectRatio(0.75)
                .setProvideApiController(true)
                .setPreview(new PreviewSettings("preview", 150, 200))
                //                .setBgColor("green")
                .setSelect(coordinates);
        final JcropImage jcropImage = new JcropImage(id, resource, croppableSettings) {
            @Override
            protected void onCoordinatesChange(Coordinates coordinates) {
                updateSelectedRectangle(coordinates);
            }
        };
        return jcropImage;

//        final JCropPanel jcropPanel = new JCropPanel(id, new PropertyModel<Rectangle>(this, "cropArea"), resource,
//                false) {
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void onCropAreaUpdate(final AjaxRequestTarget target) {
//                if (label.isVisible()) {
//                    target.add(label);
//                }
//            }
//        };
//        jcropPanel.setAspectRatio(3, 4);
//        jcropPanel.setPreviewMaxSize(150);
//        jcropPanel.setSelection(0, 0, 100, 100);
//        return jcropPanel;
    }

    private void updateSelectedRectangle(Coordinates coordinates) {
        // super.onCoordinatesChange(coordinates);
        cropArea.setRect(coordinates.getX(), coordinates.getY(), coordinates.getX2() - coordinates.getX(), coordinates.getY2() - coordinates.getY());
    }

    private Label selectionInfoLabel(String id) {
        final Label label = new Label(id, new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                return "Size: " + cropArea.width + "&times;" + cropArea.height + " px";
            }
        });
        label.setEscapeModelStrings(false);
        label.setOutputMarkupId(true);
        label.setVisible(false); // not visible in production, but stay as good
        // example ... ;-)
        return label;
    }

    /**
     * Hook for implementing action after finishing panel
     */
    protected abstract void onGoBack();

    private Coordinates convertToCoordinates(Rectangle rectangle) {
        Coordinates coordinates = new Coordinates((rectangle.x), rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        return coordinates;
    }
}
