package it.simostefi.wedding.service.datastore;

public interface GenericConsumer<T> {
    
	void accept(T t);
    
}
