package com.adtbook.examples.extpointview;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


public class preferenceInitializer extends
		AbstractPreferenceInitializer {

	public static final String ShowPopupWhenOpened = "ShowPopupWhenOpened";

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = Activator.getDefault()
				.getPreferenceStore();
		store.setDefault(ShowPopupWhenOpened, true);
	}

}
