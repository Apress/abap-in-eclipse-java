package com.abapblog.classicOutline.api;

import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.classicOutline.Activator;
import com.abapblog.classicOutline.api.rfc.RfcCaller;
import com.abapblog.classicOutline.preferences.PreferenceInitilizer;

public class ApiCallerFactory {

	public static IApiCaller getCaller() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String apiCallType = store.getString(PreferenceInitilizer.APICallType);
		switch (apiCallType) {
		case PreferenceInitilizer.APIJCoCall: {
			return new RfcCaller();
		}
		}
		return null;
	}

}
