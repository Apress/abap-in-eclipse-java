package com.abapblog.classicOutline.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IEditorPart;

import com.abapblog.classicOutline.api.ApiCallerFactory;
import com.abapblog.classicOutline.utils.ClassPageChangeListener;
import com.abapblog.classicOutline.utils.ProjectUtility;
import com.abapblog.classicOutline.views.LinkedObject;
import com.abapblog.classicOutline.views.View;
import com.sap.adt.oo.ui.classes.IMultiPageClassEditor;

@SuppressWarnings("restriction")
public class TreeContentProvider implements ITreeContentProvider {
	private TreeParent invisibleRoot;
	private LinkedObject linkedObject;
	private Object[] elements;
	private boolean refreshTree = false;
	private org.eclipse.jface.viewers.TreePath[] treePaths;
	private View view;

	public TreeContentProvider(LinkedObject linkedObject, View view) {
		super();
		this.setLinkedObject(linkedObject);
		addClassPageListener();
		setView(view);

	}

	private void addClassPageListener() {
		if (getLinkedObject() == null || getLinkedObject().isEmpty())
			return;
		IEditorPart activeEditor = ProjectUtility.getActiveEditor();
		if (activeEditor instanceof IMultiPageClassEditor) {
			IMultiPageClassEditor classEditor = (IMultiPageClassEditor) activeEditor;
			classEditor.addPageChangedListener(new ClassPageChangeListener(this));
		}
	}

	@Override
	public Object[] getElements(Object parent) {
		if (invisibleRoot == null)
			initialize();
		return getChildren(invisibleRoot);
	}

	@Override
	public Object getParent(Object child) {
		if (child instanceof TreeNode) {
			return ((TreeNode) child).getParent();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeParent) {
			return ((TreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof TreeParent)
			return ((TreeParent) parent).hasChildren();
		return false;
	}

	public void initialize() {
		Boolean refresh = isRefreshTree();
		setRefreshTree(false);

		try {
			invisibleRoot = new TreeParent(getLinkedObject(), null);
			if (getLinkedObject() == null || getLinkedObject().isEmpty())
				return;
			invisibleRoot = ApiCallerFactory.getCaller().getObjectTree(getLinkedObject(), refresh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object[] getElements() {
		return elements;
	}

	public void setElements(Object[] elements) {
		this.elements = elements;
	}

	public org.eclipse.jface.viewers.TreePath[] getTreePaths() {
		return treePaths;
	}

	public void setTreePaths(org.eclipse.jface.viewers.TreePath[] treePaths) {
		this.treePaths = treePaths;
	}

	public boolean isRefreshTree() {
		return refreshTree;
	}

	public void setRefreshTree(boolean refreshTree) {
		this.refreshTree = refreshTree;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public LinkedObject getLinkedObject() {
		return linkedObject;
	}

	public void setLinkedObject(LinkedObject linkedObject) {
		this.linkedObject = linkedObject;
	}

}
