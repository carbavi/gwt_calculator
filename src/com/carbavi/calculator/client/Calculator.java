package com.carbavi.calculator.client;

import java.util.List;

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
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Calculator implements EntryPoint {

	// Remote services
	private final BinaryTransformerServiceAsync binaryService = GWT.create(BinaryTransformerService.class);
	// Properties to create the Operations table
	private static final OperationProperties props = GWT.create(OperationProperties.class);
	// Where Operations table metadata is stored
	private ListStore<Operation> store;
	private OperationTable operationTable;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final Label errorLabel = new Label();
		final TextField numberField = new TextField();
		final Button sendNumberButton = new Button("Transform to binary");
		
		operationTable = new OperationTable();

		// Binary test
		RootPanel.get("numberFieldContainer").add(numberField);
		RootPanel.get("sendNumberButtonContainer").add(sendNumberButton);
		// Error label container
		RootPanel.get("errorLabelContainer").add(errorLabel);
		
		// Operations Table content 
		store = new ListStore<Operation>(props.key());
		final TextButton refreshButton = new TextButton("Update table");
		RootPanel.get("operationsTableRefresh").add(refreshButton);
		RootPanel.get("operationsTable").add(operationTable);
		
		class BinaryButtonHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
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

				// Then, we send the input to the server.
				binaryService.getBinaryFormat(number, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						String errorMsg = "Error connecting to server: " + caught.getLocalizedMessage();
					     MessageBox messageBox = new MessageBox(errorMsg);
					     messageBox.show();						
					}

					public void onSuccess(String result) {
						refreshOptionsTable();
					     MessageBox messageBox = new MessageBox("Binary value is " + result);
					     messageBox.show();						
						// Refresh table
//						refreshButton.click();
					     
					}
				});
			}
		}
		
		class RefreshButtonHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				refreshOptionsTable();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					refreshOptionsTable();
				}
			}

//			/**
//			 * Send the name from the nameField to the server and wait for a response.
//			 */
//			private void refreshOptionsTable() {
//				
//				binaryService.list(new AsyncCallback<List<Operation>>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						String errorMsg = "Error retrieving stored data from server: " + caught.getLocalizedMessage();
//					     MessageBox messageBox = new MessageBox(errorMsg);
//					     messageBox.show();						
//					}
//
//					@Override
//					public void onSuccess(List<Operation> result) {
//						try {
//							if (store.size() > 0) {
//								store.clear();	
//							}
//							store.addAll(result);
//							operationTable.setStore(result);
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//					}
//				});
//			}
		}

		BinaryButtonHandler binaryHandler = new BinaryButtonHandler();
		sendNumberButton.addClickHandler(binaryHandler);
		numberField.addKeyUpHandler(binaryHandler);
		
		RefreshButtonHandler refreshButtonHandler = new RefreshButtonHandler();
		refreshButton.addClickHandler(refreshButtonHandler);
		
		// Update operations table
		refreshOptionsTable();
	}
	
	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void refreshOptionsTable() {
		
		binaryService.list(new AsyncCallback<List<Operation>>() {

			@Override
			public void onFailure(Throwable caught) {
				String errorMsg = "Error retrieving stored data from server: " + caught.getLocalizedMessage();
			     MessageBox messageBox = new MessageBox(errorMsg);
			     messageBox.show();						
			}

			@Override
			public void onSuccess(List<Operation> result) {
				try {
					if (store.size() > 0) {
						store.clear();	
					}
					store.addAll(result);
					operationTable.setStore(result);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}	
}
