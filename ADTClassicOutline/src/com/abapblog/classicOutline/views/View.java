package com.abapblog.classicOutline.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import com.abapblog.classicOutline.tree.OutlineFilteredTree;
import com.abapblog.classicOutline.tree.TreeCellLabelProvider;
import com.abapblog.classicOutline.tree.TreeContentProvider;
import com.abapblog.classicOutline.tree.TreeDoubleClickListener;
import com.abapblog.classicOutline.tree.TreeParent;
import com.abapblog.classicOutline.tree.TreePatternFilter;
import com.abapblog.classicOutline.tree.listeners.ITreeContentRefreshListener;
import com.abapblog.classicOutline.utils.ProjectUtility;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;

@SuppressWarnings("restriction")
public class View extends ViewPart implements ILinkedWithEditorView, ITreeContentRefreshListener {
	private Composite parent;
	private static OutlineFilteredTree currentTree;
	public static List<LinkedObject> linkedObjects = new ArrayList<>();
	protected IPartListener2 linkWithEditorPartListener = new LinkWithEditorPartListener(this);
	public LinkedObject linkedObject = new LinkedObject(null, null);
	public static ArrayList<OutlineFilteredTree> filteredTrees = new ArrayList<OutlineFilteredTree>();
	private static Composite container;
	private static StackLayout layout;
	public static View view;
	private SaveCommandListener saveCommandListener = new SaveCommandListener();

	@Override

	public void createPartControl(Composite parent) {
		addSaveCommandListener();
		this.parent = parent;
		view = this;
		if (container == null) {
			container = new Composite(parent, SWT.NONE);
			layout = new StackLayout();
			container.setLayout(layout);
		}
		setLinkingWithEditor();
		linkedObject = ProjectUtility.getObjectFromEditor();
		if (linkedObject != null)
			reloadOutlineContent(false, false, true);
	}

	private void addSaveCommandListener() {
		ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
		commandService.addExecutionListener(saveCommandListener);
	}

	private void getViewerForLinkedObject(Composite parent, LinkedObject linkedObject, boolean refresh) {

		currentTree = getFilteredTree(parent, linkedObject, refresh);
		layout.topControl = currentTree;
		parent.layout();
	}

	private OutlineFilteredTree getFilteredTree(Composite parent, LinkedObject linkedObject, boolean refresh) {

		int count = 0;
		while (filteredTrees.size() > count) {
			OutlineFilteredTree filteredTree = filteredTrees.get(count);
			if (linkedObject == null && filteredTree.containsObject(null))
				return filteredTree;
			if (linkedObject != null && filteredTree.containsObject(linkedObject)) {
				if (refresh) {
					TreeViewer viewer = filteredTree.getViewer();
					Object[] expandedNodes = viewer.getExpandedElements();
					TreeContentProvider contentProvider = new TreeContentProvider(linkedObject, filteredTree,
							expandedNodes);
					contentProvider.addContentRefreshListener(this);
					contentProvider.setRefreshTree(true);
					contentProvider.initialize();
					viewer.setContentProvider(contentProvider);
				}
				return filteredTree;
			}
			count++;
		}

		return getNewFilteredTree(parent, linkedObject);

	}

	private Boolean setExpandedElements(Object[] elementToExpand, TreeContentProvider contentProvider,
			TreeViewer viewer) {
		Boolean found = false;
		if (elementToExpand == null)
			return found;
		List<Object> toExpand = new ArrayList<Object>();
		Object[] currentNodes = contentProvider.getAllElements();
		for (int i = 0; i < elementToExpand.length; i++) {
			try {
				TreeParent nodeToExpand = (TreeParent) elementToExpand[i];
				for (int count = 0; count < currentNodes.length; count++) {
					try {
						TreeParent currentNode = (TreeParent) currentNodes[count];
						if (currentNode.hashCode() == nodeToExpand.hashCode()) {
							toExpand.add(currentNode);
						}
					} catch (Exception e) {

					}
				}
			} catch (Exception e) {

			}
		}
		if (toExpand.size() > 0) {
			viewer.setExpandedElements(toExpand.toArray());
			found = true;
		}
		return found;
	}

	private OutlineFilteredTree getNewFilteredTree(Composite parent, LinkedObject linkedObject) {

		OutlineFilteredTree filteredTree = new OutlineFilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL,
				new TreePatternFilter(), true, true, linkedObject);
		filteredTrees.add(filteredTree);
		prepareTree(linkedObject, filteredTree);
		return filteredTree;
	}

	private void prepareTree(LinkedObject linkedObject, OutlineFilteredTree filteredTree) {
		TreeViewer viewer = filteredTree.getViewer();
		TreeContentProvider contentProvider = new TreeContentProvider(linkedObject, filteredTree, null);
		contentProvider.addContentRefreshListener(this);
		contentProvider.initialize();
		viewer.setContentProvider(contentProvider);
		setTreeProperties(viewer.getTree());
		createColumns(viewer);
		viewer.setLabelProvider(new TreeCellLabelProvider());
		createGridData(viewer);
		viewer.addDoubleClickListener(new TreeDoubleClickListener());
		viewer.expandToLevel(2);
		createAndRegisterMenu(viewer);
	}

	private void createAndRegisterMenu(TreeViewer viewer) {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
		getSite().setSelectionProvider(viewer);
	}

	private void setTreeProperties(Tree tree) {
		tree.setHeaderVisible(false);
		tree.setLinesVisible(false);
	}

	private void createColumns(TreeViewer viewer) {
		TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		viewerColumn.getColumn().setWidth(700);
		viewerColumn.getColumn().setText("Names");
	}

	private void createGridData(TreeViewer viewer) {
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		viewer.getControl().setLayoutData(data);
		try {
			viewer.setInput(getViewSite());
		} catch (Exception e) {

		}
	}

	@Override
	public void setFocus() {
		parent.setFocus();
		refreshObjectTree();

	}

	private void refreshObjectTree() {
		if ((linkedObject != null && linkedObject.isEmpty() && (isLinkingActive()))) {
			IProject LinkedProject = ProjectUtility.getActiveAdtProject();
			if (LinkedProject == null)
				return;
			linkedObject = ProjectUtility.getObjectFromEditor();
			if (linkedObject == null || linkedObject.isEmpty())
				return;
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					getViewerForLinkedObject(container, linkedObject, false);
				}
			});
		}
	}

	@Override
	public void editorActivated(IEditorPart activeEditor) {
		if (isLinkingActive()) {

//			IAdtFormEditor formEditor = (IAdtFormEditor) activeEditor;
//			IAbapSourcePage test = (IAbapSourcePage) formEditor.getSelectedPage();
//			test.getDocumentProvider().addElementStateListener(new AbapStateChangeListener());
//			// formEditor.getDocumentProvider().addElementStateListener(new
//			// AbapStateChangeListener());
			IProject LinkedProject = ProjectUtility.getActiveAdtProject();
			if (LinkedProject == null)
				return;
			linkedObject = ProjectUtility.getObjectFromEditor(activeEditor);
			if (linkedObject == null || linkedObject.isEmpty())
				return;
			TreeContentProvider contentProvider = null;
			try {
				contentProvider = (TreeContentProvider) getCurrentTree().getViewer().getContentProvider();
			} catch (Exception e) {
			}

			if (contentProvider == null || !getCurrentTree().containsObject(linkedObject)) {

				getViewerForLinkedObject(container, linkedObject, false);
			}
			;
		}
	}

	@Override
	public void dispose() {
		getSite().getPage().removePartListener(this.linkWithEditorPartListener);
		removeSaveCommandListener();
		currentTree = null;
		container = null;
		filteredTrees = new ArrayList<OutlineFilteredTree>();
		super.dispose();
	}

	private void removeSaveCommandListener() {
		ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
		commandService.removeExecutionListener(saveCommandListener);
	}

	public boolean isLinkingActive() {
		return true;
	}

	public void callEditorActivationWhenNeeded(final boolean linkingActive) {
		if (linkingActive)
			editorActivated(getSite().getPage().getActiveEditor());
	}

	private void setLinkingWithEditor() {
		getSite().getPage().addPartListener(this.linkWithEditorPartListener);
	}

	public void reloadOutlineContent(boolean refresh, boolean async, boolean direct) {
		if (async == true) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					linkedObject = ProjectUtility.getObjectFromEditor();
					getViewerForLinkedObject(container, linkedObject, refresh);
				}

			});
		} else if (direct == true) {
			linkedObject = ProjectUtility.getObjectFromEditor();
			getViewerForLinkedObject(container, linkedObject, refresh);
		} else {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					linkedObject = ProjectUtility.getObjectFromEditor();
					getViewerForLinkedObject(container, linkedObject, refresh);
				}

			});
		}
	}

	public static void destroyLinkedObject(IAdtFormEditor formEditor) {
		if (formEditor.getModel() != null) {
			IProject project = ProjectUtility.getActiveAdtProject();
			int count = 0;
			while (linkedObjects.size() > count) {
				LinkedObject currentlyLinkedObject = linkedObjects.get(count);

				if (currentlyLinkedObject.equals(formEditor.getModel(), project)) {
					linkedObjects.remove(currentlyLinkedObject);

					int countTrees = 0;
					while (filteredTrees.size() > countTrees) {
						OutlineFilteredTree currentFilteredTree = filteredTrees.get(countTrees);
						if (currentFilteredTree.containsObject(currentlyLinkedObject)) {
							currentFilteredTree.getLinkedObjects().remove(currentlyLinkedObject);
							if (currentFilteredTree.getLinkedObjects().size() == 0)
								filteredTrees.remove(currentFilteredTree);
							currentFilteredTree = null;
							break;
						}
						countTrees++;
					}
					currentlyLinkedObject = null;

				}

				count++;
			}
		}

	}

	public static OutlineFilteredTree getCurrentTree() {
		return currentTree;
	}

	@Override
	public void ContentRefreshed(TreeContentProvider contentProvider) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				OutlineFilteredTree filteredTree = contentProvider.getFilteredTree();
				TreeViewer viewer = contentProvider.getFilteredTree().getViewer();
				Object[] expandedNodes = contentProvider.getExpandedNodes();
				viewer.setContentProvider(contentProvider);
				if (filteredTree.getFilterControl().getText() != "") {
					viewer.expandAll();
				} else {
					if (expandedNodes != null && expandedNodes.length > 0) {
						if (Boolean.FALSE.equals(setExpandedElements(expandedNodes, contentProvider, viewer)))
							viewer.expandToLevel(2);
					} else {
						viewer.expandToLevel(2);
					}
				}

			}
		});
	}

}
