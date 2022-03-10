package com.abapblog.classicOutline.utils;

import org.eclipse.core.runtime.IProgressMonitor;

import com.sap.adt.destinations.logon.notification.ILoggedOnEvent;
import com.sap.adt.destinations.logon.notification.ILogonListener;

public class LogonListener implements ILogonListener {

	@Override
	public void loggedOn(ILoggedOnEvent loggedOnEvent, IProgressMonitor progressMonitor) {

		String destId = loggedOnEvent.getDestinationData().getId();
		System.out.println("LoggedOnEvent: " + destId);

	}

}
