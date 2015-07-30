package com.carbavi.calculator.client;

import java.util.Date;

import com.carbavi.calculator.shared.Operation;
import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface OperationProperties extends PropertyAccess<Operation>{
	
	@Path("id")
    ModelKeyProvider<Operation> key();
	
	// Date
	ValueProvider<Operation, Date> timestamp();
	// Decimal Number
	ValueProvider<Operation, Long> numberDecimal();
	// Binary Number
	ValueProvider<Operation, String> numberBinary();
	
}

