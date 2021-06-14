package com.adtbook.classOutline.api.rfc;

import org.eclipse.core.resources.IProject;

import com.adtbook.classOutline.tree.ClassNode;
import com.adtbook.classOutline.tree.ClassTree;
import com.sap.conn.jco.JCoTable;

public class RfcClassTreeContentHandler {

	public static ClassTree deserialize(String className, IProject project, JCoTable rfcTable) {
		ClassTree newClassTree = new ClassTree(className, project);
		if (rfcTable == null)
			return null;
		for (int i = 0; i < rfcTable.getNumRows(); i++) {
			rfcTable.setRow(i);
			ClassNode classNode = new ClassNode(rfcTable.getInt(ClassNode.fieldNameId));
			classNode.setChild(rfcTable.getInt(ClassNode.fieldNameChild));
			classNode.setParent(rfcTable.getInt(ClassNode.fieldNameParent));
			classNode.setType(rfcTable.getString(ClassNode.fieldNameType));
			classNode.setName(rfcTable.getString(ClassNode.fieldNameName));
			classNode.setText1(rfcTable.getString(ClassNode.fieldNameText1));
			classNode.setText2(rfcTable.getString(ClassNode.fieldNameText2));
			classNode.setText8(rfcTable.getString(ClassNode.fieldNameText8));
			classNode.setText9(rfcTable.getString(ClassNode.fieldNameText9));
			newClassTree.addChild(classNode);
		}
		return newClassTree;
	}

}
