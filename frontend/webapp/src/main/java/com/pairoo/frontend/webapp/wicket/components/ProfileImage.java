package com.pairoo.frontend.webapp.wicket.components;

import java.util.List;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

import com.pairoo.domain.ImageEntry;
import com.pairoo.domain.UserProfile;
import com.pairoo.domain.enums.ImageEntryType;

/**
 * @author Ralf Eichinger
 */
public class ProfileImage extends Image {

    private static final long serialVersionUID = 1L;
    private PageParameters params;

    public ProfileImage(final String id, final IModel<UserProfile> model, final ImageEntryType imageEntryType) {
        super(id, model);

        params = null;

        final UserProfile userProfile = model.getObject();

        // images
        final List<ImageEntry> imageEntries = userProfile.getImageEntries();
        // final int maxCount = imageEntryService.getMaxCount();
        for (final ImageEntry imageEntry : imageEntries) {
            if (imageEntry != null && imageEntry.isProfileImage()) {
                setImageResourceReference(imageEntry, imageEntryType);
                setVisible(imageEntry.isVisible());
                break;
            }
        }
    }

    public ProfileImage(final String id, final ImageEntry imageEntry, final ImageEntryType imageEntryType) {
        super(id);
        params = null;
        setImageResourceReference(imageEntry, imageEntryType);
    }

    private void setImageResourceReference(final ImageEntry imageEntry, final ImageEntryType imageEntryType) {
        if (imageEntry != null && imageEntryType != null) {
            final ResourceReference profileImageResource = new ImageResourceReference();
            params = getPageParameters(imageEntry, imageEntryType);
            setImageResourceReference(profileImageResource, params);
        }
    }

    private PageParameters getPageParameters(final ImageEntry imageEntry, final ImageEntryType imageEntryType) {
        final PageParameters params = new PageParameters();
        params.set("id", imageEntry.getRepositoryId());
        params.set("size", imageEntryType.name());
        return params;
    }

    public String getImageUrl(final ImageEntryType imageEntryType) {
        String result = null;
        final ResourceReference imagesResourceReference = getImageResourceReference();
        if (imagesResourceReference != null) {
            final PageParameters paramsTemp = new PageParameters(params);
            if (imageEntryType != null) {
                paramsTemp.set("size", imageEntryType.name());
            }
            final CharSequence url = getRequestCycle().urlFor(imagesResourceReference, paramsTemp);
            result = url.toString();
        }
        return result;
    }

    public boolean exists() {
        // no profile image found
        return (getImageResourceReference() != null);
    }
}
