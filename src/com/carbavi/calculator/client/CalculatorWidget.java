package com.carbavi.calculator.client;

import java.util.ArrayList;
import java.util.List;

import com.carbavi.calculator.client.services.BinaryTransformerService;
import com.carbavi.calculator.client.services.BinaryTransformerServiceAsync;
import com.carbavi.calculator.shared.Operation;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

public class CalculatorWidget implements IsWidget, EntryPoint  {

	private static final String ZERO = "0";
	private static final String C = "C";
	private static final String CE = "CE";
	private static final String PLUS_MINUS = "+/-";
	private static final String PERCENTAGE = "%";
	private static final String PLUS = "+";
	private static final String MINUS = "-";
	private static final String MULT = "*";
	private static final String DIV = "/";
	private static final String EQUALS = "=";
	private static final String DOT = ".";
	private static final String BINARY = "Binary";

	private static final String[] symbols = {C,CE,PLUS_MINUS,PERCENTAGE,PLUS,MINUS,MULT,DIV,EQUALS,DOT};
	private static final String[] operations = {PLUS,MINUS,MULT,DIV,EQUALS};

	private static final int buttonWidth = 50;
	private static final int marginWidth = 5;

	// Remote services
	private final BinaryTransformerServiceAsync binaryService = GWT.create(BinaryTransformerService.class);
	private OperationTableWidget operationTableWidget;

	private final TextField display = new TextField();
	private ContentPanel panel;
	private List<TextButton> numericButtons;
	private List<TextButton> symbolButtons;
	private List<String> valueList = new ArrayList<String>();
	private boolean isNewNumber = false;

	public CalculatorWidget(OperationTableWidget operationTableWidget) {
		this.operationTableWidget = operationTableWidget;
	}

	@Override
	public Widget asWidget() {

		if (panel == null) {

			numericButtons = generateNumericButtons();
			symbolButtons = generateSymbolButtons();

			List<HBoxLayoutContainer> list = createBoxLayouts();

			VBoxLayoutContainer c = new VBoxLayoutContainer();
			c.setPadding(new Padding(5));
			c.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
			c.add(list.get(0));
			c.add(list.get(1));
			c.add(list.get(2));
			c.add(list.get(3));
			c.add(list.get(4));

			panel = new ContentPanel();
			panel.setHeadingText("Calculator");
			panel.addStyleName("margin-10");

			final Resizable resizable = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
			panel.addExpandHandler(new ExpandHandler() {
				@Override
				public void onExpand(ExpandEvent event) {
					resizable.setEnabled(true);
				}
			});
			panel.addCollapseHandler(new CollapseHandler() {
				@Override
				public void onCollapse(CollapseEvent event) {
					resizable.setEnabled(false);
				}
			});
			panel.setWidget(c);
		}

		display.setText("0");
		return panel;
	}

	private List<HBoxLayoutContainer> createBoxLayouts() {
		List<HBoxLayoutContainer> list = new ArrayList<HBoxLayoutContainer>();
		list.add(getLine01());
		list.add(getLine02());
		list.add(getLine03());
		list.add(getLine04());
		list.add(getLine05());
		return list;
	}

	private HBoxLayoutContainer getLine01() {
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		display.setWidth(buttonWidth*3 + marginWidth*2);
		c.add(display, new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(0), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(1), new BoxLayoutData(createDefaultMargins()));
		return c;
	}

	private HBoxLayoutContainer getLine02() {
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		c.add(numericButtons.get(7), new BoxLayoutData(createDefaultMargins()));
		c.add(numericButtons.get(8), new BoxLayoutData(createDefaultMargins()));
		c.add(numericButtons.get(9), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(2), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(3), new BoxLayoutData(createDefaultMargins()));
		return c;
	}

	private HBoxLayoutContainer getLine03() {
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		c.add(numericButtons.get(4), new BoxLayoutData(createDefaultMargins()));
		c.add(numericButtons.get(5), new BoxLayoutData(createDefaultMargins()));
		c.add(numericButtons.get(6), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(4), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(5), new BoxLayoutData(createDefaultMargins()));
		return c;
	}	

	private HBoxLayoutContainer getLine04() {
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		c.add(numericButtons.get(1), new BoxLayoutData(createDefaultMargins()));
		c.add(numericButtons.get(2), new BoxLayoutData(createDefaultMargins()));
		c.add(numericButtons.get(3), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(6), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(7), new BoxLayoutData(createDefaultMargins()));
		return c;
	}

	private HBoxLayoutContainer getLine05() {
		// Binary button
		TextButton binaryButton = new TextButton(BINARY); 
		binaryButton.setWidth(buttonWidth*2 + marginWidth);
		binaryButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				// First, we validate the input.
				String decimalNumber = display.getText();
				Long number;
				try {
					number = Long.valueOf(decimalNumber);
				} catch (NumberFormatException ex) {
					String errorMsg = "Incorrect number: " + decimalNumber;
					MessageBox messageBox = new MessageBox(errorMsg);
					messageBox.show();						
					return;
				}

				// Remote call
				binaryService.getBinaryFormatOperation(number, new AsyncCallback<Operation>() {
					@Override
					public void onFailure(Throwable caught) {
						String errorMsg = "Error connecting to server: " + caught.getLocalizedMessage();
						MessageBox messageBox = new MessageBox(errorMsg);
						messageBox.show();						
					}

					public void onSuccess(Operation result) {
						display.setText(result.getNumberBinary());
						operationTableWidget.addOperation(result);
						isNewNumber = true;
					}
				});
			}

		});
		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		c.add(numericButtons.get(0), new BoxLayoutData(createDefaultMargins()));
		c.add(binaryButton, new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(8), new BoxLayoutData(createDefaultMargins()));
		c.add(symbolButtons.get(9), new BoxLayoutData(createDefaultMargins()));
		return c;
	}	

	@Override
	public void onModuleLoad() {
		// State manager, initialize the state options
		StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));
		RootPanel.get().add(asWidget());
	}

	/**
	 * Creates the list of numeric buttons (0 .. 9)
	 */
	private List<TextButton> generateNumericButtons() {
		List<TextButton> list = new ArrayList<TextButton>();
		for (int i=0; i<10; i++) {
			final TextButton button = new TextButton(Integer.toString(i));
			button.setWidth(buttonWidth);
			button.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					buttonClicked(button.getValue());
				}

			});
			list.add(button);
		}
		return list;
	}

	/**
	 * Creates the list of Operation buttons
	 */
	private List<TextButton> generateSymbolButtons() {
		List<TextButton> list = new ArrayList<TextButton>();
		for (String value : symbols) {
			final TextButton button = new TextButton(value);
			button.setWidth(buttonWidth);
			button.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					buttonClicked(button.getValue());
				}

			});
			list.add(button);
		}
		return list;
	}


	private void buttonClicked(String value) {
		if (containedInArray(symbols, value)) {
			processSymbol(value);
		} else {
			String displayText = display.getText();
			if (display.getText().equals(ZERO) || isNewNumber) {
				display.setText(value);
				isNewNumber = false;
			} else {
				display.setText(displayText + value);
				isNewNumber = false;
			}

		}
	}	

	private void processSymbol(String symbol) {
		String displayValue = display.getText();

		// Any kind of Operation
		if (containedInArray(operations, symbol)) {
			isNewNumber = true;
			if (symbol.equals(EQUALS)) {
				valueList.add(displayValue);
				calculate();
				valueList.clear();
				return;
			} else {
				if (displayValue != null && !displayValue.equals("")) {
					valueList.add(displayValue);
				}
				calculate();
				valueList.add(symbol);
				return;
			}
		}

		// Another kind of symbol
		switch (symbol)
		{
		case PLUS_MINUS:
			if (displayValue != null && !displayValue.equals("") && !displayValue.equals("0") && displayValue.contains("-")) {
				display.setText(displayValue.substring(1));
			} else {
				display.setText(MINUS  + displayValue);
			}
			break;

		case C:
			display.setText(ZERO);
			isNewNumber = true;
			break;

		case CE:
			display.setText(ZERO);
			valueList.clear();
			isNewNumber = true;
			break;

		case PERCENTAGE:
			Double percentage = Double.parseDouble(displayValue) / 100;
			display.setText(percentage.toString());
			break;

		case DOT:
			if (!displayValue.contains(DOT)) {
				display.setText(displayValue + DOT);
				isNewNumber = false;
			}
			break;

		default:
			String displayText = display.getText();
			display.setText(displayText + symbol);	

		}
	}

	private void calculate() {
		Double frstOp = null;
		Double scndOp = null;
		String operation = null;

		for (int i = 0; i < valueList.size(); i++) {
			String tmpValue = valueList.get(i);
			if (frstOp == null) {
				// First Number				
				frstOp = Double.parseDouble(tmpValue);
			} else if (containedInArray(operations, tmpValue)) {
				// Operation
				operation = tmpValue;
			} else if (scndOp == null) {
				// Second Operator: can be calculated
				scndOp = Double.parseDouble(tmpValue);

				// Total value is calculated
				Double result = calculate(frstOp, scndOp, operation);
				valueList.clear();
				valueList.add(result.toString());
				display.setText(isLongOrDouble(result));
				frstOp = result;
				scndOp = null;
			}
		}

	}
	
	private String isLongOrDouble(Double result) {
		String str = result.toString();
		if (str.contains(".")) {
			String decimalStr = str.substring(str.lastIndexOf(".")+1);
			Long decimalValue = Long.valueOf(decimalStr);
			if (decimalValue == 0) {
				return str.substring(0, str.lastIndexOf("."));
			}
		}
		return str;
	}
	
	private Double calculate(Double frstOp, Double scndOp, String operation) {
		switch (operation) {
		case PLUS:
			return frstOp + scndOp;
		case MINUS:
			return frstOp - scndOp;
		case MULT:
			return frstOp * scndOp;
		case DIV:
			return frstOp / scndOp;
		default:
			return Double.valueOf(0);	
		}
	}

	private boolean containedInArray(String[] array, String valueToFind) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(valueToFind)) {
				return true;
			}
		}
		return false;
	}

	private Margins createDefaultMargins() {
		return new Margins(0, marginWidth, 0, 0);
	}

}
