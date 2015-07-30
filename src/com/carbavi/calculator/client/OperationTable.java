package com.carbavi.calculator.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.carbavi.calculator.shared.Operation;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.GridStateHandler;
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

public class OperationTable implements IsWidget, EntryPoint  {

	private static final OperationProperties props = GWT.create(OperationProperties.class);
	private ContentPanel panel;
	private ListStore<Operation> store;
	private Grid<Operation> grid;

	@Override
	public Widget asWidget() {
		if (panel == null) {
			ColumnConfig<Operation, Date> timestampCol = new ColumnConfig<Operation, Date>(props.timestamp(), 100, SafeHtmlUtils.fromTrustedString("<b>Date</b>"));
			ColumnConfig<Operation, Long> decimalNumberCol = new ColumnConfig<Operation, Long>(props.numberDecimal(), 150, "Decimal Number");
			ColumnConfig<Operation, String> binaryNumberCol = new ColumnConfig<Operation, String>(props.numberBinary(), 150, "Binary Number");

			// Date cell format
			timestampCol.setCell(new DateCell(DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss")));

			List<ColumnConfig<Operation, ?>> columns = new ArrayList<ColumnConfig<Operation, ?>>();
			columns.add(timestampCol);
			columns.add(decimalNumberCol);
			columns.add(binaryNumberCol);

			ColumnModel<Operation> cm = new ColumnModel<Operation>(columns);

			store = new ListStore<Operation>(props.key());

			grid = new Grid<Operation>(store, cm);
			grid.setAllowTextSelection(true);
			grid.getView().setAutoExpandColumn(timestampCol);
			grid.getView().setStripeRows(true);
			grid.getView().setColumnLines(true);
			grid.setBorders(false);
			grid.setColumnReordering(true);

			// Stage manager, turn on state management
			grid.setStateful(true);
			grid.setStateId("operationTable");

			// Stage manager, load previous state
			GridStateHandler<Operation> state = new GridStateHandler<Operation>(grid);
			state.loadState();

			SimpleComboBox<String> typeCombo = new SimpleComboBox<String>(new StringLabelProvider<String>());
			typeCombo.setTriggerAction(TriggerAction.ALL);
			typeCombo.setEditable(false);
			typeCombo.setWidth(100);
			typeCombo.add("Row");
			typeCombo.add("Cell");
			typeCombo.setValue("Row");

			VerticalLayoutContainer con = new VerticalLayoutContainer();
			con.add(grid, new VerticalLayoutData(1, 1));

			panel = new ContentPanel();
			panel.setHeadingText("Binary format transformation history");
			panel.setPixelSize(600, 300);
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
			panel.setWidget(con);

			// Enables quicktips (qtitle for the heading and qtip for the
			// content) that are setup in the change GridCellRenderer
			new QuickTip(grid);
		}

		return panel;
	}

	@Override
	public void onModuleLoad() {
		// State manager, initialize the state options
		StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));
		RootPanel.get().add(asWidget());
	}	
	
	public void setStore(List<Operation> result) {
		this.store.clear();
		this.store.addAll(result);
		grid.getView().refresh(false);
	}

}
