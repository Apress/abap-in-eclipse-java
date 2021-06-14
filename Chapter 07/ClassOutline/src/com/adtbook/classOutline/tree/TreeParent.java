package com.adtbook.classOutline.tree;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

public class TreeParent extends TreeNode {

	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();

	public TreeParent(String className, IProject project, ClassNode sourceNode) {
		super(className, project, sourceNode);
		children = new ArrayList<TreeNode>();
	}

	public void addChild(TreeNode child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(TreeNode child) {
		children.remove(child);
		child.setParent(null);
	}

	public TreeNode[] getChildren() {
		return children.toArray(new TreeNode[children.size()]);
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

}
