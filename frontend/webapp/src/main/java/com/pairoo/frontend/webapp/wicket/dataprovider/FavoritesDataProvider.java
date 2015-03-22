package com.pairoo.frontend.webapp.wicket.dataprovider;

import com.pairoo.business.api.FavoriteService;
import com.pairoo.domain.User;
import com.pairoo.domain.search.FavoriteSearchResult;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Data provider for favorites list.
 *
 * @author ralf
 */
public class FavoritesDataProvider<T extends FavoriteSearchResult, S extends Object> extends SortableDataProvider<T, S> {

    private final FavoriteService service;
    private final User user;

    public FavoritesDataProvider(final FavoriteService service, final User user) {
        super();
        this.service = service;
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
        final List<FavoriteSearchResult> list = service.search(user, first, count);
        return (Iterator<? extends T>) list.iterator();
    }

    @Override
    public long size() {
        return service.count(user);
    }

    @Override
    public IModel<T> model(T t) {
        return new Model(t);
    }
}
