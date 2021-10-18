package com.abapblog.classicOutline.tree;

import java.util.ArrayList;

import com.abapblog.classicOutline.views.LinkedObject;

public class TreeParent extends TreeNode {

	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();

	public TreeParent(LinkedObject linkedObject, SourceNode sourceNode) {
		super(linkedObject, sourceNode);
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
