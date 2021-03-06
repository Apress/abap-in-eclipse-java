package com.abapblog.classicOutline.tree;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.abapblog.classicOutline.api.ApiCallerFactory;
import com.abapblog.classicOutline.views.View;
import com.sap.adt.tools.abapsource.ui.internal.sources.editors.CompoundTextSelection;
import com.sap.adt.tools.abapsource.ui.sources.editors.AbapSourcePage;
import com.sap.adt.tools.core.IAdtObjectReference;
import com.sap.adt.tools.core.internal.navigation.AbapNavigationServicesFactory;
import com.sap.adt.tools.core.navigation.AbapNavigationException;
import com.sap.adt.tools.core.navigation.IAbapNavigationServices;
import com.sap.adt.tools.core.navigation.IAbapNavigationServices.FilterValue;
import com.sap.adt.tools.core.ui.editors.IAdtFormEditor;
import com.sap.adt.tools.core.ui.navigation.AdtNavigationServiceFactory;

@SuppressWarnings("restriction")
public class TreeDoubleClickListener implements IDoubleClickListener {

	@Override
	public void doubleClick(DoubleClickEvent event) {
		IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
		TreeNode selectedNode = (TreeNode) thisSelection.getFirstElement();
		String uri = "";
		IProject project = selectedNode.getLinkedObject().getProject();
		uri = getDefinitionURI(selectedNode, uri, project);
		if (uri.equals(""))
			uri = ApiCallerFactory.getCaller().getUriForTreeNode(selectedNode);

		if (uri != null && !uri.isEmpty() && !uri.equals("")) {
			uri = "adt://" + project.getName() + uri;
			uri = correctUriForNamespaces(uri);
			AdtNavigationServiceFactory.createNavigationService().navigateWithExternalLink(uri, project);

		}
	}

	private String correctUriForNamespaces(String uri) {
		if (uri.contains("/progpc/") || uri.contains("/progpz/") || uri.contains("/fugrpc/")
				|| uri.contains("/fugrpz/")) {
			try {
				uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.toString());
				uri = URLEncoder.encode(uri, StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		return uri;
	}

	private String getDefinitionURI(TreeNode selectedNode, String uri, IProject project) {
		if ((selectedNode.getType().equals("OOLD") && !selectedNode.getLinkedObject().getType().contains("CLAS"))) {
			TreeContentProvider contentProvider = (TreeContentProvider) View.getCurrentTree().getViewer()
					.getContentProvider();
			ObjectTree objectTree = (ObjectTree) contentProvider.getInvisibleRoot();
			TreeParent implementationParent = (TreeParent) objectTree
					.getParentByID(selectedNode.getDefinitionStartId());
			TreeNode implementationNode = selectedNode;
			for (TreeNode node : implementationParent.getChildren()) {
				if (node.getName().equals(selectedNode.getName())) {
					implementationNode = node;
					uri = ApiCallerFactory.getCaller().getUriForTreeNode(implementationNode);
					break;
				}
			}
			IAbapNavigationServices navigationService = AbapNavigationServicesFactory.getInstance()
					.createNavigationService(project.getName());
			URI navigationServiceUri = URI.create(uri);
			AbapSourcePage sourcePage = null;
			IAdtFormEditor editor = null;
			try {

				if (!implementationNode.getSourceNode().getText8().substring(0, 39)
						.equals(implementationNode.getLinkedObject().getName())) {
					String includeUri = "adt://" + project.getName() + uri;

					AdtNavigationServiceFactory.createNavigationService().navigateWithExternalLink(includeUri, project);
					editor = (IAdtFormEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.getActiveEditor();
					navigationServiceUri = getURIWithStartAndEnd(navigationServiceUri, editor);

				}
				if (editor == null)
					editor = (IAdtFormEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.getActiveEditor();
				sourcePage = editor.getAdapter(AbapSourcePage.class);
				AdtNavigationServiceFactory.createNavigationService().canHandleExternalLink(uri);
				IAdtObjectReference adtObject = navigationService.getNavigationTarget(navigationServiceUri,
						sourcePage.getSource(), null, prepareFilters());
				if (adtObject != null)
					uri = adtObject.getUri().toString();

			} catch (AbapNavigationException e) {
				e.printStackTrace();
			}
		}
		return uri;
	}

	private FilterValue[] prepareFilters() {
		FilterValue[] filterValue = new FilterValue[1];
		LinkedHashSet filters = new LinkedHashSet(2);
		filters.add(FilterValue.DEFINITION);
		filterValue = (FilterValue[]) filters.toArray(new FilterValue[filters.size()]);
		return filterValue;
	}

	private URI getURIWithStartAndEnd(URI navigationServiceUri, IAdtFormEditor editor) {
		String includeUri;
		try {
			CompoundTextSelection selection = (CompoundTextSelection) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().getSelection();

			IDocument document = editor.getAdapter(AbapSourcePage.class).getDocument();
			int startLine = selection.getStartLine();
			int beginningOfName = selection.getOffset() - document.getLineOffset(startLine);
			startLine = startLine + 1;
			includeUri = "/sap/bc/adt/" + getPath(editor).toString() + "/source/main#start%3D" + startLine + "%2C"
					+ beginningOfName + "%3Bend%3D" + startLine + "%2C" + beginningOfName;
			navigationServiceUri = URI.create(includeUri);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return navigationServiceUri;
	}

	private IPath getPath(IAdtFormEditor editor) {
		IPath path = editor.getModelFile().getFullPath();
		path = path.removeFirstSegments(2);
		path = path.removeLastSegments(1);
		return path;
	}
}
