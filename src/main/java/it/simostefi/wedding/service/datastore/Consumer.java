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

public interface Consumer<T> {
    
	void accept(T t);
    
}
