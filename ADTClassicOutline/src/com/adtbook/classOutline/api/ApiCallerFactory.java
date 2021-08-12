package com.adtbook.classOutline.api;

import org.eclipse.jface.preference.IPreferenceStore;

import com.adtbook.classOutline.Activator;
import com.adtbook.classOutline.api.rest.RestApiCaller;
import com.adtbook.classOutline.api.rfc.RfcCaller;
import com.adtbook.classOutline.preferences.PreferenceInitilizer;

public class ApiCallerFactory {

	public static IApiCaller getCaller() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String apiCallType = store.getString(PreferenceInitilizer.APICallType);
		switch (apiCallType) {
		case PreferenceInitilizer.APIJCoCall: {
			return new RfcCaller();

		}
		case PreferenceInitilizer.APIRestCall: {
			return new RestApiCaller();
		}
		}
		return null;
	}

}
