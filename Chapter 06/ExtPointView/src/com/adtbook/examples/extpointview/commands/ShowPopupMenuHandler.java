package com.adtbook.examples.extpointview.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;

import com.adtbook.examples.extpointview.views.View1;

public class ShowPopupMenuHandler extends AbstractHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event)
			throws ExecutionException {
		IStructuredSelection structuredSelection = HandlerUtil
				.getCurrentStructuredSelection(event);
		if (structuredSelection != null) {
			for (Iterator<Object> iterator = structuredSelection
					.iterator(); iterator.hasNext();) {
				View1.TableRow row = (View1.TableRow) iterator.next();

				showMessageBox(event, row);

			}
		}
		return null;
	}

	private void showMessageBox(ExecutionEvent event,
			View1.TableRow row) throws ExecutionException {
		MessageBox popup = new MessageBox(HandlerUtil
				.getActiveShellChecked(event), SWT.OK);
		popup.setMessage("Row " + row.id + " " + row.name);
		popup.open();
	}
}
