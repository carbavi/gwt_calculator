package com.carbavi.calculator.server;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.carbavi.calculator.client.services.BinaryTransformerService;
import com.carbavi.calculator.server.persistence.PMF;
import com.carbavi.calculator.server.services.OperationDaoService;
import com.carbavi.calculator.server.services.impl.OperationDaoServiceImpl;
import com.carbavi.calculator.shared.Operation;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BinaryTransformerServiceImpl extends RemoteServiceServlet implements BinaryTransformerService {

	//private static final Logger LOGGER = Logger.getLogger(BinaryTransformerServiceImpl.class);
	private static final String ERROR_MSG_PERSIST = "There was an error storing Operation"; 
	
	private OperationDaoService operationDaoService = new OperationDaoServiceImpl();
	
	@Override
	public String getBinaryFormat(long input) throws IllegalArgumentException {
		String binaryNumber = Long.toBinaryString(input);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Operation operation = new Operation();
		operation.setNumberDecimal(input);
		operation.setNumberBinary(binaryNumber);
		operationDaoService.save(operation);

		return binaryNumber;
	}

	@Override
	public List<Operation> list() throws IllegalArgumentException {
		return operationDaoService.list();
	}


}
