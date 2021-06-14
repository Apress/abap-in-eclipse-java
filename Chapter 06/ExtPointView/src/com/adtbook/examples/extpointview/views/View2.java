package com.adtbook.examples.extpointview.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class View2 extends ViewPart {
	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		setHelpContext(parent);
	}

	private void setHelpContext(Composite parent) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				"com.adtbook.examples.extpointview.View2");
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}
}
