package com.adtbook.classOutline.tree;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;

public class ClassTree extends TreeParent {
	private ArrayList<TreeNode> treeNodes = new ArrayList<TreeNode>();

	public ClassTree(String className, IProject project) {
		super(className, project, null);
		this.setSourceNode(createRootNode());
		treeNodes.add(this);
	}

	private ClassNode createRootNode() {
		ClassNode classRoot = new ClassNode(0);
		setName("ROOT");
		setDescription("ROOT");
		setId(0);
		return classRoot;
	}

	public void addChild(ClassNode sourceNode) {
		TreeParent parentNode;
		TreeNode newNode;
		TreeParent newParentNode;
		if (sourceNode.getChild() == 0) {
			newNode = new TreeNode(getClassName(), getProject(), sourceNode);
			treeNodes.add(newNode);
			parentNode = (TreeParent) treeNodes.get(sourceNode.getParent());
			parentNode.addChild(newNode);
		} else {
			newParentNode = new TreeParent(getClassName(), getProject(), sourceNode);
			treeNodes.add(newParentNode);
			parentNode = (TreeParent) treeNodes.get(sourceNode.getParent());
			parentNode.addChild(newParentNode);
		}

	}

}
