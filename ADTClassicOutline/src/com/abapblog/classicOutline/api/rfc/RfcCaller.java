package com.abapblog.classicOutline.api.rfc;

import org.eclipse.core.resources.IProject;

import com.abapblog.classicOutline.api.IApiCaller;
import com.abapblog.classicOutline.tree.ClassTree;
import com.abapblog.classicOutline.tree.TreeNode;
import com.abapblog.classicOutline.utils.ProjectUtility;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class RfcCaller implements IApiCaller {

	@Override
	public ClassTree getClassTree(String className, IProject project, boolean forceRefresh) {

		ClassTree rfcClassTree = getClassTree(className, project);

		if (forceRefresh == false & rfcClassTree != null) {
			return rfcClassTree;
		}
		return getNewClassTree(className, project);

	}

	@Override
	public ClassTree getNewClassTree(String className, IProject project) {
		try {
			String destinationId = ProjectUtility.getDestinationID(project);
			JCoDestination destination = JCoDestinationManager.getDestination(destinationId);
			JCoFunction function = destination.getRepository().getFunction("Z_ADT_BOOK_GET_TREE_FOR_CLASS");
			if (function == null)
				throw new RuntimeException("Z_ADT_BOOK_GET_TREE_FOR_CLASS not found.");
			function.getImportParameterList().getField("CLASS_NAME").setValue(className.toUpperCase());
			try {
				function.execute(destination);
				JCoTable classTree = function.getExportParameterList().getTable("TREE");
				return RfcClassTreeContentHandler.deserialize(className, project, classTree);
			} catch (AbapException e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (

		JCoException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ClassTree getClassTree(String className, IProject project) {
		int count = 0;
		while (classList.size() > count) {
			ClassTree rfcClassTree = classList.get(count);
			if (rfcClassTree.getClassName() == className && rfcClassTree.getProject().getName() == project.getName()) {
				return rfcClassTree;
			}
			count++;
		}
		return null;
	}

	@Override
	public String getUriForTreeNode(TreeNode treeNode) {
		IProject project = treeNode.getProject();
		String className = treeNode.getClassName();
		String destinationId = ProjectUtility.getDestinationID(project);
		try {
			JCoDestination destination = JCoDestinationManager.getDestination(destinationId);
			JCoFunction function = destination.getRepository().getFunction("Z_ADT_BOOK_GET_URI_FOR_NODE");
			if (function == null)
				throw new RuntimeException("Z_ADT_BOOK_GET_URI_FOR_NODE not found.");

			function.getImportParameterList().getField("CLASS_NAME").setValue(className.toUpperCase());
			RfcClassNodeContentHandler.serialize(treeNode.getSourceNode(),
					function.getImportParameterList().getStructure("NODE"));

			try {
				function.execute(destination);
				return function.getExportParameterList().getString("URI");

			} catch (AbapException e) {
				System.out.println(e.toString());
				return "";
			}
		} catch (JCoException e) {
			e.printStackTrace();
		}
		return "";
	}

}
