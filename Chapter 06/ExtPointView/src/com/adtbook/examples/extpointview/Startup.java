package com.adtbook.examples.extpointview;

import org.eclipse.ui.IStartup;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		System.err.println("ADT Book plugin startup");
		System.out.println("Standard Console text");
		System.out.println();	}

}
