package com.pairoo.frontend.webapp.wicket.panels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.datazuul.framework.webapp.wicket.model.LoadableDetachableDomainObjectModel;
import com.pairoo.business.api.ImageEntryService;
import com.pairoo.business.api.ImageService;
import com.pairoo.business.api.UserService;
import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.User;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.ImageEntryType;
import com.pairoo.frontend.webapp.wicket.components.ContextImageConstants;
import com.pairoo.frontend.webapp.wicket.components.ProfileImage;
//import com.visural.wicket.component.fancybox.Fancybox;
//import com.visural.wicket.component.fancybox.FancyboxGroup;
//import com.visural.wicket.component.fancybox.TitlePosition;
//import com.visural.wicket.util.images.ImageReferenceFactory;

/**
 * see https://cwiki.apache.org/WICKET/uploaddownload.html
 *
 * @author Ralf Eichinger
 */
public class ProfileMyPhotosPanel extends BasePanel {

    private static final long serialVersionUID = 2L;
    @SpringBean(name = ImageEntryService.BEAN_ID)
    private ImageEntryService imageEntryService;
    @SpringBean(name = ImageService.BEAN_ID)
    private ImageService imageService;
    @SpringBean(name = UserService.BEAN_ID)
    private UserService userService;
    private int totalImages = 0;

    public ProfileMyPhotosPanel(final String id, final IModel<User> model) {
        super(id, model);
        setOutputMarkupId(true);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        logEnter(ProfileMyPhotosPanel.class);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // images
        final int maxCount = imageEntryService.getMaxCount();

        final RefreshingView<ImageEntry> refreshingView = new RefreshingView<ImageEntry>("images") {
            private static final long serialVersionUID = 2L;
//			private FancyboxGroup group = null;

            @Override
            protected Iterator<IModel<ImageEntry>> getItemModels() {
//				group = FancyboxGroup.get();
                final ArrayList<ImageEntry> imageEntries = new ArrayList<ImageEntry>(maxCount);

                final UserProfile userProfile = ((User) getPage().getDefaultModelObject()).getUserProfile();
                final List<ImageEntry> imageEntries2 = userProfile.getImageEntries();

                totalImages = 0;

                // add it sorted (with profile image in first position)
                for (final Iterator<ImageEntry> iterator = imageEntries2.iterator(); iterator.hasNext();) {
                    final ImageEntry imageEntry = iterator.next();
                    if (imageEntry != null) {
                        if (imageEntry.isProfileImage()) {
                            imageEntries.add(0, imageEntry);
                        } else {
                            imageEntries.add(imageEntry);
                        }
                        totalImages++;
                    }
                }
                for (int a = 0; a < (maxCount - totalImages); a++) {
                    imageEntries.add(null);
                }
                final Iterator<ImageEntry> iterator = imageEntries.iterator();
                // the iterator returns ImageEntry objects, but we need it to
                // return models, we use this handy adapter class to perform
                // on-the-fly conversion.
                return new ModelIteratorAdapter<ImageEntry>(iterator) {
                    @Override
                    protected IModel<ImageEntry> model(final ImageEntry imageEntry) {
                        if (imageEntry == null) {
                            return null;
                        }
                        return new LoadableDetachableDomainObjectModel<ImageEntry>(imageEntry, imageEntryService);
                    }
                };
            }

            @Override
            protected void populateItem(final Item<ImageEntry> item) {
                // populate the row of the repeater
                IModel<ImageEntry> imageEntryModel = item.getModel();
                ImageEntry imageEntry = null;
                if (imageEntryModel != null) {
                    imageEntry = imageEntryModel.getObject();
                }

                // image title
                final Label imageTitle = createImageTitleComponent(item, imageEntry);
                item.add(imageTitle);

                // link to normal sized image
//                String urlNormal = null;
                ProfileImage thumbnailImage = null;

                thumbnailImage = new ProfileImage("image", imageEntry, ImageEntryType.MIDDLE);
                item.add(thumbnailImage);
//                urlNormal = thumbnailImage.getImageUrl(ImageEntryType.NORMAL);
//				final Fancybox fancyboxLink = new Fancybox("fancyboxLink", ImageReferenceFactory.fromURL(urlNormal));
//				item.add(fancyboxLink);

                final ContextImage contextImage = new ContextImage("noImage", ContextImageConstants.NO_PROFILE_IMAGE);
//                contextImage.add(new AttributeModifier("width", "100"));
                item.add(contextImage);

                if (imageEntry != null) {
//					fancyboxLink.setGroup(group);
//					fancyboxLink.setTitlePosition(TitlePosition.InsideOver);
//					// TODO extend FancyBox to use a ResourceModel as title text
//					// (because of warning...)
//					fancyboxLink.setBoxTitle(String.format(getString("image_of_total_images"), item.getIndex() + 1,
//							totalImages));
//					fancyboxLink.

                    contextImage.setVisible(false);
                } else {

                    thumbnailImage.setVisible(false);
//					fancyboxLink.setVisible(false);
                }

                // locked label
                final Label lblPublished = createPublishedComponent(imageEntry);
                item.add(lblPublished);

                // actions
                // =======
                if (imageEntry != null) {
                    imageEntryModel = new LoadableDetachableDomainObjectModel<ImageEntry>(imageEntry, imageEntryService);
                }
                // choose image / upload action
                final Link<ImageEntry> lnkUpload = createUploadLink("lnkUpload", imageEntryModel);
                item.add(lnkUpload);

                // unlock image (publish)
                final Link<ImageEntry> lnkPublish = createPublishLink(imageEntryModel);
                item.add(lnkPublish);

                // lock image (unpublish)
                final Link<ImageEntry> lnkUnpublish = createUnpublishLink(imageEntryModel);
                item.add(lnkUnpublish);

                // delete image
                final Link<ImageEntry> lnkDelete = createDeleteLink(imageEntryModel);
                item.add(lnkDelete);

                // set as profile image
                final Link<ImageEntry> lnkAsProfile = createAsProfileLink(imageEntryModel);
                item.add(lnkAsProfile);
            }

            private Link<ImageEntry> createAsProfileLink(final IModel<ImageEntry> imageEntryModel) {
                final Link<ImageEntry> lnkAsProfile = new Link<ImageEntry>("lnkAsProfile", imageEntryModel) {
                    private static final long serialVersionUID = 2L;

                    @Override
                    public boolean isEnabled() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        return (imageEntry != null && !imageEntry.isProfileImage());
                    }

                    @Override
                    public void onClick() {
                        final UserProfile userProfile = ((User) getPage().getDefaultModelObject()).getUserProfile();
                        final List<ImageEntry> imageEntries3 = userProfile.getImageEntries();
                        for (final Iterator<ImageEntry> iterator = imageEntries3.iterator(); iterator.hasNext();) {
                            final ImageEntry imageEntry = iterator.next();
                            if (imageEntry != null) {
                                if (imageEntry.isProfileImage()) {
                                    imageEntry.setProfileImage(false);
                                    imageEntryService.save(imageEntry);
                                }
                            }
                        }

                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        imageEntry.setProfileImage(true);
                        imageEntry.setVisible(true);
                        /*
                         * Caused by: org.hibernate.NonUniqueObjectException: a
                         * different object with the same identifier value was
                         * already associated with the session:
                         * [com.pairoo.domain.ImageEntry#64] at
                         * org.hibernate.engine
                         * .StatefulPersistenceContext.checkUniqueness
                         * (StatefulPersistenceContext.java:638) at
                         * org.hibernate
                         * .event.def.DefaultSaveOrUpdateEventListener
                         * .performUpdate
                         * (DefaultSaveOrUpdateEventListener.java:305) at
                         * org.hibernate
                         * .event.def.DefaultSaveOrUpdateEventListener
                         * .entityIsDetached
                         * (DefaultSaveOrUpdateEventListener.java:246) at
                         * org.hibernate
                         * .event.def.DefaultUpdateEventListener.performSaveOrUpdate
                         * (DefaultUpdateEventListener.java:57) at
                         * org.hibernate.
                         * event.def.DefaultSaveOrUpdateEventListener
                         * .onSaveOrUpdate
                         * (DefaultSaveOrUpdateEventListener.java:93) at
                         * org.hibernate
                         * .impl.SessionImpl.fireUpdate(SessionImpl.java:742) at
                         * org
                         * .hibernate.impl.SessionImpl.update(SessionImpl.java
                         * :730) at
                         * org.hibernate.impl.SessionImpl.update(SessionImpl
                         * .java:722) at
                         * com.googlecode.genericdao.dao.hibernate.
                         * HibernateBaseDAO._update(HibernateBaseDAO.java:439)
                         * at
                         * com.googlecode.genericdao.dao.hibernate.HibernateBaseDAO
                         * ._saveOrUpdateIsNew(HibernateBaseDAO.java:166) at
                         * com.
                         * googlecode.genericdao.dao.hibernate.GenericDAOImpl
                         * .save(GenericDAOImpl.java:101) at
                         * com.datazuul.framework.business.services.persistence.
                         * AbstractDomainObjectServiceImpl
                         * .save(AbstractDomainObjectServiceImpl.java:71) at
                         * sun.reflect.GeneratedMethodAccessor147.invoke(Unknown
                         * Source) at
                         * sun.reflect.DelegatingMethodAccessorImpl.invoke
                         * (DelegatingMethodAccessorImpl.java:43) at
                         * java.lang.reflect.Method.invoke(Method.java:601) at
                         * org.springframework.aop.support.AopUtils.
                         * invokeJoinpointUsingReflection(AopUtils.java:318) at
                         * org
                         * .springframework.aop.framework.ReflectiveMethodInvocation
                         * .invokeJoinpoint(ReflectiveMethodInvocation.java:183)
                         * at org.springframework.aop.framework.
                         * ReflectiveMethodInvocation
                         * .proceed(ReflectiveMethodInvocation.java:150) at
                         * org.springframework
                         * .transaction.interceptor.TransactionInterceptor
                         * .invoke(TransactionInterceptor.java:110) at
                         * org.springframework
                         * .aop.framework.ReflectiveMethodInvocation
                         * .proceed(ReflectiveMethodInvocation.java:172) at
                         * org.springframework
                         * .aop.interceptor.ExposeInvocationInterceptor
                         * .invoke(ExposeInvocationInterceptor.java:90) at
                         * org.springframework
                         * .aop.framework.ReflectiveMethodInvocation
                         * .proceed(ReflectiveMethodInvocation.java:172) at
                         * org.springframework
                         * .aop.framework.JdkDynamicAopProxy.invoke
                         * (JdkDynamicAopProxy.java:202) at
                         * $Proxy25.save(Unknown Source) at
                         * sun.reflect.GeneratedMethodAccessor147.invoke(Unknown
                         * Source) at
                         * sun.reflect.DelegatingMethodAccessorImpl.invoke
                         * (DelegatingMethodAccessorImpl.java:43) at
                         * java.lang.reflect.Method.invoke(Method.java:601) at
                         * org
                         * .apache.wicket.proxy.LazyInitProxyFactory$JdkHandler
                         * .invoke(LazyInitProxyFactory.java:416) at
                         * org.apache.wicket.proxy.$Proxy41.save(Unknown Source)
                         * at com.pairoo.frontend.webapp.wicket.panels.
                         * ProfileMyPhotosPanel$1$2
                         * .onClick(ProfileMyPhotosPanel.java:213)
                         */
                        imageEntryService.save(imageEntry);
                    }
                };
                return lnkAsProfile;
            }

            private Link<ImageEntry> createDeleteLink(final IModel<ImageEntry> imageEntryModel) {
                final Link<ImageEntry> lnkDelete = new Link<ImageEntry>("lnkDelete", imageEntryModel) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isEnabled() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        return (imageEntry != null);
                    }

                    @Override
                    public void onClick() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        final User user = (User) ProfileMyPhotosPanel.this.getDefaultModelObject();
                        final List<ImageEntry> imageEntries = user.getUserProfile().getImageEntries();
                        imageEntries.remove(imageEntry);
                        userService.save(user);
                        imageEntryService.delete(imageEntry);
                        imageService.delete(imageEntry);
                        ProfileMyPhotosPanel.this.replaceWith(new ProfileMyPhotosPanel(ProfileMyPhotosPanel.this
                                .getId(), new LoadableDetachableDomainObjectModel<Long>(user, userService)));
                    }
                };
                return lnkDelete;
            }

            private Link<ImageEntry> createUnpublishLink(final IModel<ImageEntry> imageEntryModel) {
                final Link<ImageEntry> lnkUnpublish = new Link<ImageEntry>("lnkUnpublish", imageEntryModel) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isEnabled() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        return (imageEntry != null && imageEntry.isVisible());
                    }

                    @Override
                    public void onClick() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        imageEntry.setVisible(false);
                        imageEntryService.save(imageEntry);
                    }
                };
                return lnkUnpublish;
            }

            private Link<ImageEntry> createPublishLink(final IModel<ImageEntry> imageEntryModel) {
                final Link<ImageEntry> lnkPublish = new Link<ImageEntry>("lnkPublish", imageEntryModel) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isEnabled() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        return (imageEntry != null && !imageEntry.isVisible());
                    }

                    @Override
                    public void onClick() {
                        final ImageEntry imageEntry = (ImageEntry) getDefaultModelObject();
                        imageEntry.setVisible(true);
                        imageEntryService.save(imageEntry);
                    }
                };
                return lnkPublish;
            }

            private Link<ImageEntry> createUploadLink(String id, IModel<ImageEntry> model) {
                final Link<ImageEntry> lnkUpload = new Link<ImageEntry>(id, model) {
                    private static final long serialVersionUID = 2L;

                    @Override
                    public void onClick() {
                        User user = (User) ProfileMyPhotosPanel.this.getDefaultModelObject();
                        final PhotoUploadPanel uploadPanel = new PhotoUploadPanel(ProfileMyPhotosPanel.this.getId(),
                                (IModel<ImageEntry>) getDefaultModel(),
                                new LoadableDetachableDomainObjectModel<Long>(user, userService));
                        ProfileMyPhotosPanel.this.replaceWith(uploadPanel);
                    }
                };
                return lnkUpload;
            }

            private Label createPublishedComponent(final ImageEntry imageEntry) {
                final Label lblPublished = new Label("lblPublished");
                if (imageEntry != null) {
                    if (!imageEntry.isVisible()) {
                        lblPublished.setDefaultModel(new ResourceModel("not.published"));
                        lblPublished.add(new AttributeModifier("class", "label label-important"));
                    } else {
                        lblPublished.setDefaultModel(new ResourceModel("published"));
                        lblPublished.add(new AttributeModifier("class", "label label-success"));
                    }
                } else {
                    lblPublished.setDefaultModel(new Model<String>("."));
                }
                return lblPublished;
            }

            private Label createImageTitleComponent(final Item<ImageEntry> item, final ImageEntry imageEntry) {
                IModel<String> imageTitleText = new StringResourceModel("photo", ProfileMyPhotosPanel.this,
                        new Model<Integer>(item.getIndex() + 1));
                if (imageEntry != null) {
                    if (imageEntry.isProfileImage()) {
                        imageTitleText = new ResourceModel("your.profile.photo");
                    }
                }
                final Label imageTitle = new Label("imageTitle", imageTitleText);
                if (imageEntry != null && imageEntry.isProfileImage()) {
                    imageTitle.add(new AttributeModifier("style", "background-color: lightblue;"));
                }
                return imageTitle;
            }
        };
        refreshingView.setItemReuseStrategy(DefaultItemReuseStrategy.getInstance());
        add(refreshingView);
    }
}
