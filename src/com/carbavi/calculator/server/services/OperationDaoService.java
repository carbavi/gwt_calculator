package com.carbavi.calculator.server.services;

import java.util.List;

import com.carbavi.calculator.shared.Operation;

public interface OperationDaoService {
	
	/**
	 * Saves an {@code Operation} in the DataStore
	 * 
	 * @param operation to be saved
	 */
	void save(Operation operation);
	Operation saveOperation(Operation operation);
	
	/**
	 * Deletes an {@code Operation} from the DataStore
	 * 
	 * @param id identifier of the Operation to be deleted
	 * @return {@literal true} if deleted successfully, {@literal false} otherwise
	 */
	Boolean delete(Long id);
	Boolean delete(Operation op);
	
	/**
	 * Returns the full list of {@code Operations} stored in the DataStore
	 * 
	 * @return the full list of {@code Operations} stored in the DataStore
	 */
	List<Operation> list();

}
