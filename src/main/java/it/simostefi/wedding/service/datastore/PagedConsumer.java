/*
 * COPYRIGHT Revevol
 *
 * The copyright to the computer program herein is the property of
 * Revevol, Italy. The program may be used and/or
 * copied only with the written permission from Revevol
 * or in the accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program has been supplied.
 */
package it.simostefi.wedding.service.datastore;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.base.Function;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class PagedConsumer. It is a generic class which PagedConsumerAbstract
 *
 * @param <T>
 */
@Data
public class PagedConsumer<T> extends PagedConsumerAbstract<QueryResultIterator<T>> {

    private List<T> results;
    private Function<T, Boolean> filter;
    private Function<T, T> transform;
    private Function<T, Boolean> skip;
    private Integer batchSize;

    /**
     * Constructor - it allows to set the batch size
     * @param batchSize
     */
    public PagedConsumer(int batchSize){
        this.batchSize = batchSize;
        this.results = new ArrayList<>();
    }

    /**
     * Generic constructor
     */
    public PagedConsumer(){
        this.batchSize = null;
        this.results = new ArrayList<>();
    }

    /**
     * It gets the data from an entity using an iterator
     *
     * @param it iterator
     */
    @Override
    public void accept(QueryResultIterator<T> it) {
        while (it.hasNext()) {
            if(batchSize != null
                    && results.size() >= batchSize)
                break;

            T res = it.next();

            if(filter != null && !filter.apply(res)){
                return;
            }

            if(transform != null)
                res = transform.apply(res);

            if(skip != null) {
                if (!skip.apply(res)) {
                    results.add(res);
                }
            }else {
                results.add(res);
            }
        }
        if(it.hasNext())
            setCursor(it.getCursor());
    }
}