package com.abapblog.classicOutline.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.abapblog.classicOutline.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final IPreferenceStore store;

	public PreferencePage() {
		super(GRID);
		store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("Classic Outline Settings");
	}

	@Override
	protected void createFieldEditors() {
		RadioGroupFieldEditor apiCallPreference = new RadioGroupFieldEditor(PreferenceInitilizer.APICallType,
				"Please choose a type of call to backend", 1,
				new String[][] { { "RFC Call (JCO)", PreferenceInitilizer.APIJCoCall },
						{ "REST API", PreferenceInitilizer.APIRestCall } },
				getFieldEditorParent(), true);
		addField(apiCallPreference);

	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

}
