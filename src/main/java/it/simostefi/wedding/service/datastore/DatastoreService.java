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

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.googlecode.objectify.*;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.QueryKeys;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Created by Francesca on 09/05/2017.
 */
@Slf4j
public class DatastoreService  {

    private final String namespace;

    /**
     *
     * @param namespace
     */
    public DatastoreService(String namespace) {
        this.namespace = namespace;
    }

    /**
     *
     * @return
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     *
     * @return
     */
    public Objectify getClient() {
        return ObjectifyService.ofy();
    }

    /**
     * @param <T>
     */
    abstract class NamedOperation<T> {

        T get() {
            String oldNamespace = null;
            if (namespace != null) {
                oldNamespace = NamespaceManager.get();
                NamespaceManager.set(namespace);
            }
            T result = doGet();
            if (namespace != null) {
                NamespaceManager.set(oldNamespace);
            }
            getClient().clear();
            return result;
        }

        protected abstract T doGet();

    }

    /**
     *
     * @param <T>
     */
    public class QueryExecutor<T> {

        private Query<T> query;
        private Cursor cursor;

        private QueryExecutor(Query<T> query) {
            this.query = query;
        }

        /**
         *
         * @param limit
         * @return
         */
        public QueryExecutor<T> limit(final int limit) {
            new NamedOperation<Void>() {
                @Override
                protected Void doGet() {
                    query = query.limit(limit);
                    return null;
                }
            }.get();
            return this;
        }

        /**
         *
         * @param order
         * @return
         */
        public QueryExecutor<T> order(final String order) {
            new NamedOperation<Void>() {
                @Override
                protected Void doGet() {
                    query = query.order(order);
                    return null;
                }
            }.get();
            return this;
        }

        /**
         *
         * @param cursor
         * @return
         */
        public QueryExecutor<T> cursor(final Cursor cursor) {
            if(cursor != null) {
                new NamedOperation<Void>() {
                    @Override
                    protected Void doGet() {
                        query = query.startAt(cursor);
                        return null;
                    }
                }.get();
            }
            return this;
        }

        /**
         *
         * @param projects
         * @return
         */
        public QueryExecutor<T> project(final String... projects) {
            new NamedOperation<Void>() {
                @Override
                protected Void doGet() {
                    query = query.project(projects);
                    return null;
                }
            }.get();
            return this;
        }

        /**
         *
         * @param clazz
         * @param id
         * @param <V>
         * @return
         */
        public <V>QueryExecutor<T> ancestor(final Class<V> clazz, final Object id) {
            new NamedOperation<Void>() {
                @Override
                protected Void doGet() {
                    if(id instanceof String)
                        query = query.ancestor(Key.create(clazz, (String) id));
                    else if(id instanceof Long){
                        query = query.ancestor(Key.create(clazz, (Long)id));
                    }else{
                        throw new IllegalStateException("Only supported Long and String id");
                    }
                    return null;
                }
            }.get();
            return this;
        }

        /**
         *
         * @return
         */
        public Cursor getCursor() {
            return cursor;
        }

        /**
         *
         * @param consumer
         */
        public void execute(final Consumer<QueryResultIterator<T>> consumer) {
            execute(new Function<QueryResultIterator<T>, Void>() {
                @Override
                public Void apply(QueryResultIterator<T> input) {
                    //make sure the consumer is not called if no result
                    if (input.hasNext())
                        consumer.accept(input);
                    return null;
                }
            });
        }

        /**
         * Apply a function to the result iterator lazily.
         * If no result in the query, the emptyResult is returned
         *
         * @param function a function apllied to the result iterator
         * @param <R>
         * @return null if no result, else the function result
         */
        public <R> R execute(Function<QueryResultIterator<T>, R> function) {
            String oldNamespace = null;
            if (namespace != null) {
                oldNamespace = NamespaceManager.get();
                NamespaceManager.set(namespace);
            }
            QueryResultIterator<T> iterator = query.iterator();
            R result = function.apply(iterator);
            cursor = iterator.getCursor();
            if (namespace != null) {
                NamespaceManager.set(oldNamespace);
            }
            getClient().clear();
            return result;
        }

        /**
         *
         * @param consumer
         */
        public void forEach(final Consumer<T> consumer) {
            execute(new Consumer<QueryResultIterator<T>>() {
                @Override
                public void accept(QueryResultIterator<T> iterator) {
                    while (iterator.hasNext()) {
                        consumer.accept(iterator.next());
                    }
                }
            });
        }

        /**
         *
         * @param function a function applied to each item
         * @param <R>
         * @return null if no result, else the function result
         */
        public <R> Iterator<R> transform(final Function<T, R> function) {
            return execute(new Function<QueryResultIterator<T>, Iterator<R>>() {
                @Override
                public Iterator<R> apply(QueryResultIterator<T> input) {
                    if (input.hasNext())
                        return Iterators.transform(input, function);
                    return Collections.emptyIterator();
                }
            });
        }

        /**
         * Careful with that !! Might be memory intensive
         * @return the results as a list
         */
        public List<T> asList() {
            return new NamedOperation<List<T>>() {
                @Override
                protected List<T> doGet() {
                    //query.list() returns a proxy, we want to have the list
                    //dereferenced before leaving the namespaced context
                    //TODO check that limit is set?
                    return Lists.newArrayList(query.list());
                }
            }.get();
        }

        /**
         *
         * @return
         */
        public QueryKeys<T> keys() {
            return new NamedOperation<QueryKeys<T>>() {
                @Override
                protected QueryKeys<T> doGet() {
                    return query.keys();
                }
            }.get();
        }
    }

    /**
     * Create a key for a child entity with id String
     *
     * @param parentKey
     * @param entityClass
     * @param id
     * @param <T>
     * @param <V>
     * @param <P>
     * @return
     */
    public <T,V, P> Key<T> createKey(final Key<V> parentKey, final Class<T> entityClass, final String id){
        return new NamedOperation<Key<T>>() {
            @Override
            protected Key<T> doGet() {
                return Key.create(parentKey, entityClass, id);
            }
        }.get();
    }

    /**
     * Create a key for a child entity with id Long
     *
     * @param parentKey
     * @param entityClass
     * @param id
     * @param <T>
     * @param <V>
     * @param <P>
     * @return
     */
    public <T,V, P> Key<T> createKey(final Key<V> parentKey, final Class<T> entityClass, final long id){
        return new NamedOperation<Key<T>>() {
            @Override
            protected Key<T> doGet() {
                return Key.create(parentKey, entityClass, id);
            }
        }.get();
    }

    /**
     * Create a key for an entity with id Long
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public <T> Key<T> createKey(final Class<T> entityClass, final long id){
        return new NamedOperation<Key<T>>() {
            @Override
            protected Key<T> doGet() {
                return Key.create(entityClass, id);
            }
        }.get();
    }

    /**
     * Create a key for an entity with id String
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @param <P>
     * @return
     */
    public <T,P> Key<T> createKey(final Class<T> entityClass, final String id){
        return new NamedOperation<Key<T>>() {
            @Override
            protected Key<T> doGet() {
                return Key.create(entityClass, id);
            }
        }.get();
    }

    /* GET OPERATIONS */

    /**
     * Load a child entity with id String
     *
     * @param parentKey
     * @param entityClass
     * @param name
     * @param <T>
     * @param <V>
     * @return
     */
    public <T,V> T loadEntity(final Key<V> parentKey, final Class<T> entityClass, final String name) {
        return new NamedOperation<T>() {
            @Override
            protected T doGet() {
                return ObjectifyService.ofy().load().key(createKey(parentKey, entityClass, name)).now();
            }
        }.get();
    }

    /**
     * Load a child entity with id Long
     *
     * @param parentKey
     * @param entityClass
     * @param id
     * @param <T>
     * @param <V>
     * @return
     */
    public <T,V> T loadEntity(final Key<V> parentKey, final Class<T> entityClass, final long id) {
        return new NamedOperation<T>() {
            @Override
            protected T doGet() {
                return ObjectifyService.ofy().load().key(createKey(parentKey, entityClass, id)).now();
            }
        }.get();
    }

    /**
     * Load an entity with id String
     *
     * @param entityClass
     * @param name
     * @param <T>
     * @return
     */
    public <T> T loadEntity(final Class<T> entityClass, final String name) {
        return new NamedOperation<T>() {
            @Override
            protected T doGet() {
                return ObjectifyService.ofy().load().type(entityClass).id(name).now();
            }
        }.get();
    }

    /**
     * Load an entity
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public <T> T loadEntity(final Class<T> entityClass, final Long id) {
        return new NamedOperation<T>() {
            @Override
            protected T doGet() {
                return ObjectifyService.ofy().load().type(entityClass).id(id).now();
            }
        }.get();
    }

    /**
     * Load an entity
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T loadEntity(final Key<T> key) {
        return new NamedOperation<T>() {
            @Override
            protected T doGet() {
                return ObjectifyService.ofy().load().key(key).now();
            }
        }.get();
    }

    /**
     * Reload an entity
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T reloadEntity(final T entity) {
        return new NamedOperation<T>() {
            @Override
            protected T doGet() {
                return ObjectifyService.ofy().load().entity(entity).now();
            }
        }.get();
    }

    /**
     * Load entities by ids
     *
     * @param entityClass
     * @param names
     * @param <K>
     * @param <T>
     * @return
     */
    public <K, T> Map<K, T> loadEntities(final Class<T> entityClass, final Iterable<K> names) {
        return new NamedOperation<Map<K, T>>() {
            @Override
            protected Map<K, T> doGet() {
                return ObjectifyService.ofy().load().type(entityClass).ids(names);
            }
        }.get();
    }

    /**
     * Load entities by keys
     *
     * @param keys
     * @param <T>
     * @return
     */
    public <T> Map<Key<T>, T> loadEntities(final Iterable<Key<T>> keys) {
        return new NamedOperation<Map<Key<T>, T>>() {
            @Override
            protected Map<Key<T>, T> doGet() {
                return ObjectifyService.ofy().load().keys(keys);
            }
        }.get();
    }

    /**
     * Entity exists, type String
     *
     * @param entityClass
     * @param name
     * @return
     */
    public boolean entityExists(final Class<?> entityClass, final String name) {
        return new NamedOperation<Boolean>() {
            @Override
            protected Boolean doGet() {
                return entityExists(createKey(entityClass, name));
            }
        }.get();
    }

    /**
     * Entity exists with id, type Long
     *
     * @param entityClass
     * @param id
     * @return
     */
    public boolean entityExists(final Class<?> entityClass, final long id) {
        return new NamedOperation<Boolean>() {
            @Override
            protected Boolean doGet() {
                return entityExists(createKey(entityClass, id));
            }
        }.get();
    }

    /**
     * Entities exists by their id, type String
     *
     * @param entityClass
     * @param ids
     * @return
     */
    public Set<?> entitiesExistStr(final Class<?> entityClass, final List<String> ids) {
        return new NamedOperation<Set<?>>() {
            @Override
            protected Set<?> doGet() {
                List<Key> keys = new ArrayList<>();
                final boolean str;
                if(ids.isEmpty())
                    return new HashSet<>();
                for(String id : ids)
                    keys.add(createKey(entityClass, id));

                return entitiesExist(keys, true);
            }
        }.get();
    }

    /**
     * Entities exists by their id, type Long
     *
     * @param entityClass
     * @param ids
     * @return
     */
    public Set<?> entitiesExistLong(final Class<?> entityClass, final List<Long> ids) {
        return new NamedOperation<Set<?>>() {
            @Override
            protected Set<?> doGet() {
                List<Key> keys = new ArrayList<>();
                final boolean str;
                if(ids.isEmpty())
                    return new HashSet<>();

                for(Long id : ids)
                    keys.add(createKey(entityClass, id));

                return entitiesExist(keys, false);
            }
        }.get();
    }

    /**
     * Check if an entity exists
     *
     * @param key
     * @return
     */
    public boolean entityExists(final Key key) {
        return new NamedOperation<Boolean>() {
            @Override
            protected Boolean doGet() {
                return ObjectifyService.ofy().load().filterKey(key).keys().first().now() != null;
            }
        }.get();
    }

    /**
     * Check if entities exit
     *
     * @param keys
     * @param str
     * @return
     */
    public Set<Object> entitiesExist(final Collection<Key> keys, final boolean str) {
        return new NamedOperation<Set<Object>>() {
            @Override
            protected Set<Object> doGet() {
                List<Key<Object>> ids = ObjectifyService.ofy().load().filterKey("in",keys).keys().list();
                Set<Object> output = new HashSet<>();
                for(Key<Object> k : ids){
                    if(keys.contains(k)){
                        if(str)
                            output.add(k.getName());
                        else
                            output.add(k.getId());
                    }
                }
                return output;
            }
        }.get();
    }

    /* SAVE OPERATIONS */

    /**
     * Store entity
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Key<T> storeEntity(final T entity) {
        return new NamedOperation<Key<T>>() {
            @Override
            protected Key<T> doGet() {
                return ObjectifyService.ofy().save().entity(entity).now();
            }
        }.get();
    }

    /**
     * Store entities
     *
     * @param entities
     * @param <T>
     * @return
     */
    public <T> Map<Key<T>, T> storeEntities(final Iterable<T> entities) {
        return new NamedOperation<Map<Key<T>,T>>() {
            @Override
            protected Map<Key<T>, T> doGet() {
                return ObjectifyService.ofy().save().entities(entities).now();
            }
        }.get();
    }

    /**
     * Store entities with check if it already exists
     *
     * @param clazz
     * @param entities
     * @param force
     * @param <T>
     * @return
     */
    public <T> Map<Key<T>, T> storeEntities(final Class<T> clazz,
                                            final Map<String, T> entities,
                                            final boolean force) {
        return new NamedOperation<Map<Key<T>,T>>() {
            @Override
            protected Map<Key<T>, T> doGet() {
                Collection<T> toBeSaved = new ArrayList<>();
                if(force) {
                    toBeSaved = entities.values();
                }else {
                    List<String> ids = new ArrayList<>(entities.keySet());
                    Set<?> existing = entitiesExistStr(clazz, ids);
                    for(String k : ids){
                        if(!existing.contains(k)){
                            toBeSaved.add(entities.get(k));
                        }
                    }
                }
                return ObjectifyService.ofy().save().entities(toBeSaved).now();
            }
        }.get();
    }

    /**
     * Store entities with the type index Long
     *
     * @param clazz
     * @param entities
     * @param force
     * @param <T>
     * @return
     */
    public <T> Map<Key<T>, T> storeEntitiesLong(final Class<T> clazz,
                                                final Map<Long, T> entities,
                                                final boolean force) {
        return new NamedOperation<Map<Key<T>,T>>() {
            @Override
            protected Map<Key<T>, T> doGet() {
                Collection<T> toBeSaved = new ArrayList<>();
                if(force) {
                    toBeSaved = entities.values();
                }else {
                    List<Long> ids = new ArrayList<>(entities.keySet());
                    Set<?> existing = entitiesExistLong(clazz, ids);
                    for(Long k : ids){
                        if(!existing.contains(k)){
                            toBeSaved.add(entities.get(k));
                        }
                    }
                }
                return ObjectifyService.ofy().save().entities(toBeSaved).now();
            }
        }.get();
    }

    /**
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Result<Key<T>> storeEntityAsync(final T entity) {
        return new NamedOperation<Result<Key<T>>>() {
            @Override
            protected Result<Key<T>> doGet() {
                return ObjectifyService.ofy().save().entity(entity);
            }
        }.get();
    }

    /**
     *
     * @param entities
     * @param <T>
     * @return
     */
    public <T> Result<Map<Key<T>, T>> storeEntitiesAsync(final Iterable<T> entities) {
        return new NamedOperation<Result<Map<Key<T>,T>>>() {
            @Override
            protected Result<Map<Key<T>, T>> doGet() {
                return ObjectifyService.ofy().save().entities(entities);
            }
        }.get();
    }

    /* LIST OPERATIONS */

    /**
     * List entities
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T> QueryExecutor<T> listEntities(Class<T> entityClass) {
        return listEntities(entityClass, Collections.<String, Object>emptyMap());
    }

    /**
     * List entities with one filter
     *
     * @param entityClass
     * @param field
     * @param value
     * @param <T>
     * @return
     */
    public <T> QueryExecutor<T> listEntities(Class<T> entityClass, String field, Object value) {
        return listEntities(entityClass, Collections.singletonMap(field, value));
    }

    /**
     * List entities with filters
     *
     * @param entityClass
     * @param fieldsMap
     * @param <T>
     * @param <V>
     * @return
     */
    public <T,V> QueryExecutor<T> listEntities(Class<T> entityClass, Map<String, ?> fieldsMap) {
        Query<T> query = ObjectifyService.ofy().load().type(entityClass);
        for (String key : fieldsMap.keySet()) {
            query = query.filter(key, fieldsMap.get(key));
        }
        return new QueryExecutor<>(query);
    }

    /**
     * List of children by parent id
     *
     * @param entityClass
     * @param parentClass
     * @param id
     * @param <T>
     * @param <V>
     * @return
     */
    public <T,V> QueryExecutor<T> listChildEntities(final Class<T> entityClass, final Class<V> parentClass, final String id) {
        return new NamedOperation<QueryExecutor<T>>() {
            @Override
            protected QueryExecutor<T> doGet() {
                Query<T> query = ObjectifyService.ofy().load().type(entityClass).ancestor(createKey(parentClass, id));
                return new QueryExecutor<>(query);
            }
        }.get();
    }

    /**
     * List child entities by ancestor id when the parent is the same type of the child
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public <T> QueryExecutor<T> listChildEntities(Class<T> entityClass, final long id) {
        Query<T> query = ObjectifyService.ofy().load().type(entityClass).ancestor(createKey(entityClass, id));
        return new QueryExecutor<>(query);
    }

    /* DELETE OPERATIONS */

    /**
     * Delete entity by id
     *
     * @param entityClass
     * @param entityId
     */
    public void deleteEntityById(final Class<?> entityClass, final String entityId) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().type(entityClass).id(entityId).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete child entity by id
     *
     * @param parentClass
     * @param parentId
     * @param entityClass
     * @param entityId
     */
    public void deleteChildEntityById(final Class<?> parentClass,
                                      final String parentId,
                                      final Class<?> entityClass, final String entityId) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().type(entityClass)
                        .parent(createKey(parentClass, parentId)).id(entityId).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete the children of an entity by their ids
     *
     * @param parentClass
     * @param parentId
     * @param entityClass
     * @param ids
     * @param <T>
     */
    public <T>void deleteChildEntityByIds(final Class<?> parentClass,
                                          final String parentId,
                                          final Class<?> entityClass, final Iterable<T> ids) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().type(entityClass)
                        .parent(createKey(parentClass, parentId)).ids(ids).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete a single entity by id
     *
     * @param entityClass
     * @param entityId
     */
    public void deleteEntityById(final Class<?> entityClass, final Long entityId) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().type(entityClass).id(entityId).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete the entities by id
     *
     * @param entityClass
     * @param ids
     * @param <S>
     */
    public <S> void deleteEntityByIds(final Class<?> entityClass, final Iterable<S> ids) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().type(entityClass).ids(ids).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete the entities given as argument
     *
     * @param entities
     */
    public void deleteEntities(final Object ... entities) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().entities(entities).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete the entities given as argument
     *
     * @param entitiesList
     */
    public void deleteEntities(final Iterable<?> entitiesList) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                ObjectifyService.ofy().delete().entities(entitiesList).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete all the entities stored for the argument class
     *
     * @param entityClass
     * @param <T>
     */
    public <T> void deleteEntitiesOfClass(final Class<T> entityClass) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                List<Key<T>> keys = ObjectifyService.ofy().load().type(entityClass).keys().list();
                ObjectifyService.ofy().delete().keys(keys).now();
                return null;
            }
        }.get();
    }

    /**
     * Delete all the entities stored for the argument class
     *
     * @param classes
     */
    public void deleteEntitiesOfClasses(final Set<Class<?>> classes) {
        new NamedOperation<Void>() {
            @Override
            protected Void doGet() {
                for (Class<?> c : classes) {
                    ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(c).keys().list()).now();
                }
                return null;
            }
        }.get();
    }

    /* TRANSACTION */

    /**
     *
     * @param transaction
     * @param <R>
     * @return
     */
    public <R> R executeTransaction(final Work<R> transaction) {
        return new NamedOperation<R>() {
            @Override
            protected R doGet() {
                return ObjectifyService.ofy().transact(transaction);
            }
        }.get();
    }

}
