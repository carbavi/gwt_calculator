package com.carbavi.calculator.client.services;

import java.util.List;

import com.carbavi.calculator.shared.Operation;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("binarytransformer")
public interface BinaryTransformerService extends RemoteService {
	
	Operation getBinaryFormatOperation(long input) throws IllegalArgumentException;
	
	List<Operation> list() throws IllegalArgumentException;
	
	Boolean delete(Long id) throws IllegalArgumentException;
	
}
