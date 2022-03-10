package com.abapblog.classicOutline.api.rfc;

import com.abapblog.classicOutline.tree.SourceNode;
import com.sap.conn.jco.JCoStructure;

public class RfcObjectNodeContentHandler {

	public static JCoStructure serialize(SourceNode sourceNode, JCoStructure rfcStructure) {

		rfcStructure.setValue(SourceNode.fieldNameId, sourceNode.getId());
		rfcStructure.setValue(SourceNode.fieldNameChild, sourceNode.getChild());
		rfcStructure.setValue(SourceNode.fieldNameParent, sourceNode.getParent());
		rfcStructure.setValue(SourceNode.fieldNameType, sourceNode.getType());
		rfcStructure.setValue(SourceNode.fieldNameName, sourceNode.getName());
		rfcStructure.setValue(SourceNode.fieldNameText1, sourceNode.getText1());
		rfcStructure.setValue(SourceNode.fieldNameText2, sourceNode.getText2());
		rfcStructure.setValue(SourceNode.fieldNameText8, sourceNode.getText8());
		rfcStructure.setValue(SourceNode.fieldNameText9, sourceNode.getText9());
		return rfcStructure;

	}

}
