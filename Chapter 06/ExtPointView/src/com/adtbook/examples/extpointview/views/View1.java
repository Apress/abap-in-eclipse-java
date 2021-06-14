package com.adtbook.examples.extpointview.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;

public class View1 extends ViewPart {
	private TableViewer tableViewer;
	private Composite parent;

	public class TableRow {
		public int id;
		public String name;

		public TableRow(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		createTreeViewerAndItsMenu(parent);
		setHelpContext(parent);
		setKeyBindingContext();
	}

	private void createTreeViewerAndItsMenu(Composite parent) {
	this.parent = parent;
		createTreeViewer(parent);
	createAndRegisterMenu(getTable());
	}

	private void setKeyBindingContext() {
		String id = "com.adtbook.examples.extpointview.context";
		IContextService contextService = (IContextService) getSite()
				.getService(IContextService.class);
		contextService.activateContext(id);
	}

	private void setHelpContext(Composite parent) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				"com.adtbook.examples.extpointview.View1");
	}

	private void createAndRegisterMenu(Table table) {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(table);

		table.setMenu(menu);

		getSite().registerContextMenu(menuManager, tableViewer);
		getSite().setSelectionProvider(tableViewer);
	}

	private void createColumns() {
		createIdColumn();
		createNameColumn();
	}

	private Table getTable() {
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		return table;
	}

	private void createTreeViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.MULTI
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		tableViewer.setContentProvider(ArrayContentProvider
				.getInstance());
		createColumns();
		tableViewer.setInput(getContent());
	}

	private void createNameColumn() {
		TableViewerColumn nameColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		nameColumn.getColumn().setText("Name");
		nameColumn.getColumn().setWidth(200);
		nameColumn.getColumn().setMoveable(true);
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((TableRow) element).name;
			}
		});
	}

	private void createIdColumn() {
		TableViewerColumn idColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		idColumn.getColumn().setText("ID");
		idColumn.getColumn().setWidth(50);
		idColumn.getColumn().setMoveable(true);
		idColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return String.valueOf(((TableRow) element).id);
			}
		});
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}

	private TableRow[] getContent() {
		return new TableRow[] { new TableRow(1, "First row"),
				new TableRow(2, "Second row"), new TableRow(3,
						"Third row"), };
	}
}
