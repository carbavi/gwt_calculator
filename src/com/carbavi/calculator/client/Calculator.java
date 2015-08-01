package com.carbavi.calculator.client;

import com.carbavi.calculator.client.services.BinaryTransformerService;
import com.carbavi.calculator.client.services.BinaryTransformerServiceAsync;
import com.carbavi.calculator.shared.Operation;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.widget.client.TextButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Calculator implements EntryPoint {

	// Remote services
	private final BinaryTransformerServiceAsync binaryService = GWT.create(BinaryTransformerService.class);
	// Properties to create the Operations table
	private OperationTableWidget operationTable;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final Label errorLabel = new Label();
		final TextField numberField = new TextField();
		final Button sendNumberButton = new Button("Transform to binary");

		operationTable = new OperationTableWidget();

		// Binary test
		RootPanel.get("numberFieldContainer").add(numberField);
		RootPanel.get("sendNumberButtonContainer").add(sendNumberButton);
		// Error label container
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Delete button
		final TextButton refreshButton = new TextButton("Update table");
		RootPanel.get("operationsTableRefresh").add(refreshButton);
		RootPanel.get("operationsTable").add(operationTable);

		class BinaryButtonHandler implements ClickHandler, KeyUpHandler {

			public void onClick(ClickEvent event) {
				calculateBinaryNumber();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					calculateBinaryNumber();
				}
			}

			private void calculateBinaryNumber() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = numberField.getText();
				long number;
				try {
					number = Long.valueOf(textToServer);
				} catch (NumberFormatException ex) {
					errorLabel.setText("Wrong number");
					return;
				}

				binaryService.getBinaryFormatOperation(number, new AsyncCallback<Operation>() {
					@Override
					public void onFailure(Throwable caught) {
						String errorMsg = "Error connecting to server: " + caught.getLocalizedMessage();
						MessageBox messageBox = new MessageBox(errorMsg);
						messageBox.show();						
					}

					public void onSuccess(Operation result) {
						//operationTable.refreshTable();
						MessageBox messageBox = new MessageBox("Binary value is " + result.getNumberBinary());
						messageBox.show();						
						operationTable.addOperation(result);
					}
				});
			}
		}

		class RefreshButtonHandler implements ClickHandler, KeyUpHandler {

			public void onClick(ClickEvent event) {
				operationTable.refreshTable();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					operationTable.refreshTable();
				}
			}
			
		}

		BinaryButtonHandler binaryHandler = new BinaryButtonHandler();
		sendNumberButton.addClickHandler(binaryHandler);
		numberField.addKeyUpHandler(binaryHandler);

		RefreshButtonHandler refreshButtonHandler = new RefreshButtonHandler();
		refreshButton.addClickHandler(refreshButtonHandler);

		// Update operations table (read from DataStore)
		operationTable.refreshTable();
	}

}
