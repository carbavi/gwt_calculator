package com.carbavi.calculator.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Calculator implements EntryPoint {

	private OperationTableWidget operationTableWidget;
	private CalculatorWidget calculatorWidget;

	public void onModuleLoad() {

		operationTableWidget = new OperationTableWidget();
		calculatorWidget = new CalculatorWidget(operationTableWidget);

		RootPanel.get("calculatorWidget").add(calculatorWidget);
		RootPanel.get("operationsTable").add(operationTableWidget);

		// Update operations table (read from DataStore)
		operationTableWidget.refreshTable();
	}

}
