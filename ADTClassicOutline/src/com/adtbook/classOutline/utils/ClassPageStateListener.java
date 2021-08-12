package com.adtbook.classOutline.utils;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IElementStateListener;

import com.adtbook.classOutline.tree.TreeContentProvider;

public class ClassPageStateListener implements IElementStateListener {

	private IEditorInput input;
	private TreeContentProvider contentProvider;

	public ClassPageStateListener(IEditorInput input, TreeContentProvider contentProvider) {
		setInput(input);
		setContentProvider(contentProvider);
	}

	@Override
	public void elementDirtyStateChanged(Object element, boolean isDirty) {
		if (!isDirty && getInput().equals(element)) {
			contentProvider.setRefreshTree(true);
			contentProvider.initialize();
			contentProvider.getView().treeViewerRefresh();
			contentProvider.getView().getViewer().expandToLevel(2);
		}
	}

	@Override
	public void elementContentAboutToBeReplaced(Object element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void elementContentReplaced(Object element) {
		if (getInput().equals(element))
			System.out.println("Activated");

	}

	@Override
	public void elementDeleted(Object element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void elementMoved(Object originalElement, Object movedElement) {
		// TODO Auto-generated method stub

	}

	public IEditorInput getInput() {
		return input;
	}

	public void setInput(IEditorInput input) {
		this.input = input;
	}

	public TreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(TreeContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

}
