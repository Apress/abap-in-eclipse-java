package com.adtbook.examples.extpointview;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;
import org.osgi.framework.BundleContext;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.adtbook.examples.extpointview"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		if (shouldShowPopup())
			executeCommmand();
	}

	private boolean shouldShowPopup() {
		IPreferenceStore store = getDefault()
				.getPreferenceStore();
		Boolean shouldShow = store.getBoolean(
				preferenceInitializer.ShowPopupWhenOpened);
		return shouldShow;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private void executeCommmand() {
		IHandlerService handlerService = (IHandlerService) ((IServiceLocator) PlatformUI
				.getWorkbench()).getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"com.adtbook.examples.extpointview.commands.ShowPopup",
					null);
		} catch (Exception e) {
			// TODO error handling
		}
	}

}
