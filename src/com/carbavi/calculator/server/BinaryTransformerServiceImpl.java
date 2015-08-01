package com.carbavi.calculator.server;

import java.util.List;

import com.carbavi.calculator.client.services.BinaryTransformerService;
import com.carbavi.calculator.server.services.OperationDaoService;
import com.carbavi.calculator.server.services.impl.OperationDaoServiceImpl;
import com.carbavi.calculator.shared.Operation;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BinaryTransformerServiceImpl extends RemoteServiceServlet implements BinaryTransformerService {

	private OperationDaoService operationDaoService = new OperationDaoServiceImpl();
	
	@Override
	public String getBinaryFormat(long input) throws IllegalArgumentException {
		String binaryNumber = Long.toBinaryString(input);
		Operation operation = new Operation();
		operation.setNumberDecimal(input);
		operation.setNumberBinary(binaryNumber);
		operationDaoService.save(operation);
		return binaryNumber;
	}

	@Override
	public Operation getBinaryFormatOperation(long input) throws IllegalArgumentException {
		String binaryNumber = Long.toBinaryString(input);
		Operation operation = new Operation();
		operation.setNumberDecimal(input);
		operation.setNumberBinary(binaryNumber);
		return operationDaoService.saveOperation(operation);
	}

	
	@Override
	public List<Operation> list() throws IllegalArgumentException {
		return operationDaoService.list();
	}

	@Override
	public Boolean delete(Long id) throws IllegalArgumentException {
		return operationDaoService.delete(id);
	}

	@Override
	public Boolean delete(Operation op) throws IllegalArgumentException {
		return operationDaoService.delete(op);
	}


}
