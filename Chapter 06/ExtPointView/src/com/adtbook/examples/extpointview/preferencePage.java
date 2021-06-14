package com.adtbook.examples.extpointview;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class preferencePage extends
		FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public preferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault()
				.getPreferenceStore());
		setDescription("Settings for ADT Book plugin");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(
				preferenceInitializer.ShowPopupWhenOpened,
				"Show Popup at Start", getFieldEditorParent()));
	}
}
