package com.carbavi.calculator.client.services;

import java.util.List;

import com.carbavi.calculator.shared.Operation;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BinaryTransformerServiceAsync {
	
	void getBinaryFormat(long input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void list(AsyncCallback<List<Operation>> callback);
}
