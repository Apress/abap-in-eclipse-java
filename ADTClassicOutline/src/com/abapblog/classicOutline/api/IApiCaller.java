package com.abapblog.classicOutline.api;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import com.abapblog.classicOutline.tree.ClassTree;
import com.abapblog.classicOutline.tree.TreeNode;

public interface IApiCaller {
	static ArrayList<ClassTree> classList = new ArrayList<ClassTree>();

	public ClassTree getClassTree(String className, IProject project, boolean forceRefresh);

	public ClassTree getNewClassTree(String className, IProject project);

	public ClassTree getClassTree(String className, IProject project);

	public String getUriForTreeNode(TreeNode treeNode);

}
