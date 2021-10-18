package com.abapblog.classicOutline.views;

import java.util.ArrayList;
import java.util.List;

import com.sap.adt.tools.abapsource.ui.sources.editors.AbstractAbapSourcePageExtensionProcessor;
import com.sap.adt.tools.abapsource.ui.sources.editors.IAbapSourcePage;

public class AbapPageLoadListener extends AbstractAbapSourcePageExtensionProcessor {
	private static final List<IAbapPageLoadListener> listeners = new ArrayList<>();

	public static void addListener(IAbapPageLoadListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(IAbapPageLoadListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void processOnDocumentLoaded(IAbapSourcePage sourcePage) {
		listeners.forEach(listener -> {
			listener.pageLoaded(sourcePage);
			removeListener(listener);
		});
	}
}