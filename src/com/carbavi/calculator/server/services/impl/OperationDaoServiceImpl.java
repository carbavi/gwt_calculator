package com.carbavi.calculator.server.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

import org.apache.log4j.Logger;

import com.carbavi.calculator.server.persistence.PMF;
import com.carbavi.calculator.server.services.OperationDaoService;
import com.carbavi.calculator.shared.Operation;
import com.google.appengine.tools.admin.AppAdminFactory.PasswordPrompt;
import com.ibm.icu.util.BytesTrie.Iterator;

public class OperationDaoServiceImpl implements OperationDaoService {
	
	private static final Logger LOGGER = Logger.getLogger(OperationDaoServiceImpl.class);
	private static final String ERROR_MSG_PERSIST = "Error saving Operation in DataStore";
	private static final String ERROR_MSG_LIST = "Error listing Operations from DataStore";
	private static final String ERROR_MSG_DELETE = "Error deleting Operation from DataStore";
	
	/**
	 * See {@link com.carbavi.calculator.server.services.OperationDaoService.save(Operation)}
	 */
	@Override
	public void save(Operation operation) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(operation);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.warn(ERROR_MSG_PERSIST, ex);
		} finally {
			pm.close();
		}
	}

	/**
	 * See {@link com.carbavi.calculator.server.services.OperationDaoService.delete(Operation)}
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
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.warn(ERROR_MSG_PERSIST, ex);
		} finally {
			pm.close();
		}
		return list;
	}

	/**
	 * See {@link com.carbavi.calculator.server.services.OperationDaoService.list()}
	 */
	@Override
	public boolean delete(Operation operation) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(operation);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.warn(ERROR_MSG_PERSIST, ex);
		} finally {
			pm.close();
		}
		return false;
	}

}
