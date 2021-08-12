package com.adtbook.classOutline.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;

public class LinkWithEditorHandler implements IHandler {
	public static String ID = "com.adtbook.classOutline.LinkWithEditor";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Command command = event.getCommand();
		boolean oldValue = HandlerUtil.toggleCommandState(command);
		View view = (View) HandlerUtil.getActivePart(event);
		command.getState(RegistryToggleState.STATE_ID).setValue(!oldValue);
		view.callEditorActivationWhenNeeded(!oldValue);
		return !oldValue;
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
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
