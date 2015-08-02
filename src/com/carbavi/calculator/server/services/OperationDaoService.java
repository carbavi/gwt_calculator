package com.carbavi.calculator.server.services;

import java.util.List;

import com.carbavi.calculator.shared.Operation;

public interface OperationDaoService {
	
	/**
	 * Saves an {@code Operation} in the DataStore and returns the persisted object
	 * 
	 * @param operation
	 * @return
	 */
	Operation saveOperation(Operation operation);
	
	/**
	 * Deletes an {@code Operation} from the DataStore
	 * 
	 * @param id identifier of the Operation to be deleted
	 * @return {@literal true} if deleted successfully, {@literal false} otherwise
	 */
	Boolean delete(Long id);
	
	/**
	 * Returns the full list of {@code Operation} stored in the DataStore
	 * 
	 * @return the full list of {@code Operation} stored in the DataStore
	 */
	List<Operation> list();

}
