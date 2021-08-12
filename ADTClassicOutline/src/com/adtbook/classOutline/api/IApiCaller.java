package com.adtbook.classOutline.api;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

import com.adtbook.classOutline.tree.ClassTree;
import com.adtbook.classOutline.tree.TreeNode;

public interface IApiCaller {
	static ArrayList<ClassTree> classList = new ArrayList<ClassTree>();

	public ClassTree getClassTree(String className, IProject project, boolean forceRefresh);

	public ClassTree getNewClassTree(String className, IProject project);

	public ClassTree getClassTree(String className, IProject project);

	public String getUriForTreeNode(TreeNode treeNode);

}
