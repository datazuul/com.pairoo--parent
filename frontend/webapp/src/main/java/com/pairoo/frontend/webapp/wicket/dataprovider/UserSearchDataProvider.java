package com.pairoo.frontend.webapp.wicket.dataprovider;

import com.pairoo.business.api.UserService;
import com.pairoo.domain.SearchProfile;
import com.pairoo.domain.User;
import com.pairoo.domain.search.UserSearchResult;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Data provider for basic search results.
 *
 * @author ralf
 */
public class UserSearchDataProvider<T extends UserSearchResult, S extends Object> extends SortableDataProvider<T, S> {

    private final UserService service;
    private final SearchProfile searchProfile;
    private final User user;

    public UserSearchDataProvider(final UserService service, final User user, final SearchProfile searchProfile) {
        super();
        this.service = service;
        this.searchProfile = searchProfile;
        this.user = user;

        // set the default sort
        //		List<Sort> sorts = search.getSorts();
        //		if (sorts != null) {
        //			Sort sort = sorts.get(0);
        //			setSort(sort.getProperty(), !sort.isDesc());
        //		}
    }

    @Override
    public Iterator<? extends T> iterator(final long first, final long count) {
        final List<UserSearchResult> list = service.search(user, searchProfile, first, count);
        return (Iterator<? extends T>) list.iterator();
    }

    @Override
    public long size() {
        return service.count(user, searchProfile);
    }

    @Override
    public IModel<T> model(T t) {
        return new Model(t);
    }
}
