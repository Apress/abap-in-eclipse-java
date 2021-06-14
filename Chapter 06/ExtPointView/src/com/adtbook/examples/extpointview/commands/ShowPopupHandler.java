package com.adtbook.examples.extpointview.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowPopupHandler implements IHandler {

	@Override
	public void addHandlerListener(
			IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event)
			throws ExecutionException {
		MessageBox popup = new MessageBox(
				HandlerUtil.getActiveShellChecked(event),
				SWT.OK);
		popup.setText("This is a title");
		popup.setMessage(
				"Show popup command was triggered");
		popup.open();
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(
			IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
