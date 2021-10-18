package com.abapblog.classicOutline.api.rfc;

import com.abapblog.classicOutline.tree.ClassNode;
import com.sap.conn.jco.JCoStructure;

public class RfcClassNodeContentHandler {

	public static JCoStructure serialize(ClassNode classNode, JCoStructure rfcStructure) {

		rfcStructure.setValue(ClassNode.fieldNameId, classNode.getId());
		rfcStructure.setValue(ClassNode.fieldNameChild, classNode.getChild());
		rfcStructure.setValue(ClassNode.fieldNameParent, classNode.getParent());
		rfcStructure.setValue(ClassNode.fieldNameType, classNode.getType());
		rfcStructure.setValue(ClassNode.fieldNameName, classNode.getName());
		rfcStructure.setValue(ClassNode.fieldNameText1, classNode.getText1());
		rfcStructure.setValue(ClassNode.fieldNameText2, classNode.getText2());
		rfcStructure.setValue(ClassNode.fieldNameText8, classNode.getText8());
		rfcStructure.setValue(ClassNode.fieldNameText9, classNode.getText9());
		return rfcStructure;

	}

}
