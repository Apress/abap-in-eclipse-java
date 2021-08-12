package com.abapblog.classicOutline.tree;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.abapblog.classicOutline.api.ApiCallerFactory;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

public class TreeDoubleClickListener implements IDoubleClickListener {

	@Override
	public void doubleClick(DoubleClickEvent event) {
		IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
		TreeNode selectedNode = (TreeNode) thisSelection.getFirstElement();
		String uri = ApiCallerFactory.getCaller().getUriForTreeNode(selectedNode);
		if (uri != null && !uri.isEmpty()) {
			IProject project = selectedNode.getProject();
			uri = "adt://" + project.getName() + uri;
			AdtNavigationServiceFactory.createNavigationService().navigateWithExternalLink(uri, project);

		}

	}
}
