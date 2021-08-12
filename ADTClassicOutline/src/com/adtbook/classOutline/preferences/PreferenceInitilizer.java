package com.adtbook.classOutline.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.adtbook.classOutline.Activator;

public class PreferenceInitilizer extends AbstractPreferenceInitializer {
	public static final String APICallType = "APICallType";
	public static final String APIRestCall = "REST";
	public static final String APIJCoCall = "JCO";

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(APICallType, APIJCoCall);
	}

}
