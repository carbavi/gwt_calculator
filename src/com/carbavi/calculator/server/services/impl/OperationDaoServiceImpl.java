package com.carbavi.calculator.server.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.carbavi.calculator.server.persistence.PMF;
import com.carbavi.calculator.server.services.OperationDaoService;
import com.carbavi.calculator.shared.Operation;

public class OperationDaoServiceImpl implements OperationDaoService {
	
	/**
	 * See {@link com.carbavi.calculator.server.services.OperationDaoService.saveOperation(Operation)}
	 */
	@Override
	public Operation saveOperation(Operation operation) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Operation newOp = (Operation) pm.makePersistent(operation);
			return pm.detachCopy(newOp);
		} finally {
			pm.close();
		}
	}	

	/**
	 * See {@link com.carbavi.calculator.server.services.OperationDaoService.delete(Long)}
	 */
	@Override
	public Boolean delete(Long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Operation op = pm.getObjectById(Operation.class, id);
			pm.deletePersistent(op);
			return Boolean.TRUE;
		} finally {
			pm.close();
		}
	}

	/**
	 * See {@link com.carbavi.calculator.server.services.OperationDaoService.list()}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Operation> list() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Operation.class);
		List<Operation> list = new ArrayList<Operation>();
		try {
			List<Operation> list_tmp = (List<Operation>) q.execute();
			// Objects detached to avoid serialization problems with Dates
			list.addAll(pm.detachCopyAll(list_tmp));
		} finally {
			pm.close();
		}
		return list;
	}
	
}
