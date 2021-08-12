package com.abapblog.classicOutline.api.rest;

import java.nio.charset.Charset;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.core.resources.IProject;

import com.abapblog.classicOutline.tree.ClassNode;
import com.abapblog.classicOutline.tree.ClassTree;
import com.sap.adt.communication.content.AdtMediaType;
import com.sap.adt.communication.content.ContentHandlerException;
import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.message.IMessageBody;
import com.sap.adt.tools.core.content.AdtStaxContentHandlerUtility;

public class RestClassTreeContentHandler implements IContentHandler<ClassTree> {
	private static final String CLASS_NODE = "CLASSTREE";
	protected final AdtStaxContentHandlerUtility utility = new AdtStaxContentHandlerUtility();

	private String className;
	private IProject project;

	public RestClassTreeContentHandler(String className, IProject project) {
		this.className = className;
		this.project = project;
	}

	@Override
	public ClassTree deserialize(IMessageBody body, Class<? extends ClassTree> dataType) {
		ClassNode classNode = null;
		ClassTree classTree = null;
		String lastNode = "";
		XMLStreamReader xsr = null;
		try {
			xsr = this.utility.getXMLStreamReader(body);

			for (int event = xsr.next(); event != XMLStreamReader.END_DOCUMENT; event = xsr.next()) {
				switch (event) {
				case XMLStreamReader.START_ELEMENT:
					lastNode = xsr.getLocalName();
					if (CLASS_NODE.equals(xsr.getLocalName())) {
						classTree = new ClassTree(className, project);
					}
					break;
				case XMLStreamReader.CHARACTERS: {
					if (lastNode.equals(ClassNode.fieldNameId)) {
						if (classNode != null) {
							classTree.addChild(classNode);
						}
						classNode = new ClassNode(Integer.parseInt(xsr.getText()));

						break;
					}
					if (lastNode.equals(ClassNode.fieldNameChild)) {
						classNode.setChild(Integer.parseInt(xsr.getText()));
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameParent)) {
						classNode.setParent(Integer.parseInt(xsr.getText()));
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameName)) {
						classNode.setName(xsr.getText());
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameType)) {
						classNode.setType(xsr.getText());
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameText1)) {
						classNode.setText1(xsr.getText());
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameText2)) {
						classNode.setText2(xsr.getText());
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameText8)) {
						classNode.setText8(xsr.getText());
						break;
					}
					if (lastNode.equals(ClassNode.fieldNameText9)) {
						classNode.setText9(xsr.getText());
						break;
					}

					break;
				}
				}
			}
			if (classNode != null) {
				classTree.addChild(classNode);
			}
			return classTree;
		} catch (XMLStreamException e) {
			throw new ContentHandlerException(e.getMessage(), e);
		} catch (NumberFormatException e) {
			throw new ContentHandlerException(e.getMessage(), e);
		} finally {
			if (xsr != null) {
				this.utility.closeXMLStreamReader(xsr);
			}
		}
	}

	@Override
	public String getSupportedContentType() {
		return AdtMediaType.APPLICATION_XML;
	}

	@Override
	public Class<ClassTree> getSupportedDataType() {
		return ClassTree.class;
	}

	@Override
	public IMessageBody serialize(ClassTree var1, Charset var2) {
		// TODO Auto-generated method stub
		return null;
	}

}
