package com.adtbook.classOutline.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Display;
import com.adtbook.classOutline.tree.TreeCellLabelProvider;
import com.adtbook.classOutline.tree.TreeContentProvider;
import com.adtbook.classOutline.tree.TreeDoubleClickListener;
import com.adtbook.classOutline.utils.ProjectUtility;
import com.sap.adt.destinations.logon.notification.ILoggedOnEvent;
import com.sap.adt.destinations.logon.notification.ILogonListener;
import com.sap.adt.destinations.model.IDestinationData;
import com.sap.adt.tools.core.project.AdtProjectServiceFactory;

public class View extends ViewPart implements ILinkedWithEditorView {
	private TreeViewer viewer;
	private TreeContentProvider contentProvider;
	private Composite parent;
	protected IPartListener2 linkWithEditorPartListener = new LinkWithEditorPartListener(this);
	private IProject LinkedProject;
	private String LinkedClass;
	private ArrayList<TreeContentProvider> contentProviders = new ArrayList<TreeContentProvider>();

	@Override
	public void createPartControl(Composite parent) {

		this.parent = parent;
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		contentProvider = getContentProvider(LinkedClass, LinkedProject);
		viewer.setContentProvider(contentProvider);
		setTreeProperties();
		createColumns();
		viewer.setLabelProvider(new TreeCellLabelProvider());
		createGridData();
		setLinkingWithEditor();
		viewer.addDoubleClickListener(new TreeDoubleClickListener());

	}

	private void setTreeProperties() {
		getViewer().getTree().setHeaderVisible(false);
		getViewer().getTree().setLinesVisible(false);
	}

	private void createColumns() {
		TreeViewerColumn viewerColumn = new TreeViewerColumn(getViewer(), SWT.NONE);
		viewerColumn.getColumn().setWidth(500);
		viewerColumn.getColumn().setText("Names");
	}

	private void createGridData() {
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		getViewer().getControl().setLayoutData(data);
		try {
			getViewer().setInput(getViewSite());
		} catch (Exception e) {

		}
	}

	@Override
	public void setFocus() {
		parent.setFocus();
		if ((LinkedClass.isEmpty() && (isLinkingActive()))) {
			LinkedProject = ProjectUtility.getActiveAdtProject();
			if (LinkedProject == null)
				return;
	//		ProjectUtility.ensureLoggedOn(LinkedProject);
			LinkedClass = ProjectUtility.getClassNameFromEditor();
			if (LinkedClass.isEmpty())
				return;
			contentProvider = getContentProvider(LinkedClass, LinkedProject);
			getViewer().setContentProvider(contentProvider);
			treeViewerRefresh();
			getViewer().expandToLevel(2);
		}

	}

	public void treeViewerRefresh() {
		getViewer().refresh();
	}

	@Override
	public void editorActivated(IEditorPart activeEditor) {
		if (isLinkingActive()) {
			LinkedProject = ProjectUtility.getActiveAdtProject();
			if (LinkedProject == null)
				return;
			LinkedClass = ProjectUtility.getClassNameFromEditor(activeEditor);
			if (LinkedClass.isEmpty())
				return;
			if (!LinkedClass.equals(contentProvider.getClassName())
					|| (contentProvider.getProject() != null && LinkedProject != contentProvider.getProject())) {
				contentProvider.setElements(getViewer().getExpandedElements());
				contentProvider.setTreePaths(getViewer().getExpandedTreePaths());
				contentProvider = getContentProvider(LinkedClass, LinkedProject);
				getViewer().setContentProvider(contentProvider);
				expandTree();
				treeViewerRefresh();
			}
		}
	}

	public void expandTree() {
		getViewer().expandToLevel(2);
		Object[] expandedElements = contentProvider.getElements();
		if (expandedElements != null) {
			getViewer().setExpandedElements(expandedElements);
		}
		TreePath[] expandedTreePaths = contentProvider.getTreePaths();
		if (expandedTreePaths != null) {
			getViewer().setExpandedTreePaths(expandedTreePaths);
		}
	}

	@Override
	public void dispose() {
		getSite().getPage().removePartListener(this.linkWithEditorPartListener);
		super.dispose();
	}

	public boolean isLinkingActive() {
		ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
		Command toggleCommand = commandService.getCommand(LinkWithEditorHandler.ID);
		State state = toggleCommand.getState(RegistryToggleState.STATE_ID);
		return (Boolean) state.getValue();
	}

	public void callEditorActivationWhenNeeded(final boolean linkingActive) {
		if (linkingActive)
			editorActivated(getSite().getPage().getActiveEditor());
	}

	private void setLinkingWithEditor() {
		getSite().getPage().addPartListener(this.linkWithEditorPartListener);
	}

	private TreeContentProvider getContentProvider(String className, IProject project) {
		int count = 0;
		while (getContentProviders().size() > count) {
			TreeContentProvider contentProvider = getContentProviders().get(count);
			if (className.equals(contentProvider.getClassName())
					&& contentProvider.getProject().getName() == project.getName()) {
				return contentProvider;
			}
			count++;
		}

		TreeContentProvider contentProvider = new TreeContentProvider(className, project, this);
		contentProvider.initialize();
		getContentProviders().add(contentProvider);
		return contentProvider;
	}

	public ArrayList<TreeContentProvider> getContentProviders() {
		return contentProviders;
	}

	public void setContentProviders(ArrayList<TreeContentProvider> contentProviders) {
		this.contentProviders = contentProviders;
	}

	public TreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public TreeViewer getViewer() {
		return viewer;
	}
}
