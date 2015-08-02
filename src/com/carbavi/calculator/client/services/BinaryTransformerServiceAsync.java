package com.carbavi.calculator.client.services;

import java.util.List;

import com.carbavi.calculator.shared.Operation;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BinaryTransformerServiceAsync {
	
	void getBinaryFormatOperation(long input, AsyncCallback<Operation> callback) throws IllegalArgumentException;
	
	void list(AsyncCallback<List<Operation>> callback);
	
	void delete(Long id, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	
}
