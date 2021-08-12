package com.abapblog.classicOutline.api.rest;

import java.nio.charset.Charset;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.abapblog.classicOutline.tree.ClassNode;
import com.sap.adt.communication.content.AdtMediaType;
import com.sap.adt.communication.content.ContentHandlerException;
import com.sap.adt.communication.content.IContentHandler;
import com.sap.adt.communication.message.IMessageBody;
import com.sap.adt.tools.core.content.AdtStaxContentHandlerUtility;

@SuppressWarnings("restriction")
public class RestClassNodeContentHandler implements IContentHandler<ClassNode> {
	private static final String CLASS_NODE = "CLASSNODE";
	protected final AdtStaxContentHandlerUtility utility = new AdtStaxContentHandlerUtility();

	@Override
	public ClassNode deserialize(IMessageBody var1, Class<? extends ClassNode> var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSupportedContentType() {
		return AdtMediaType.APPLICATION_XML;
	}

	@Override
	public Class<ClassNode> getSupportedDataType() {
		return ClassNode.class;
	}

	@SuppressWarnings("static-access")
	@Override
	public IMessageBody serialize(ClassNode classNode, Charset charset) {
		XMLStreamWriter xsw = null;
		try {
			xsw = this.utility.getXMLStreamWriterAndStartDocument(charset,
					AdtStaxContentHandlerUtility.XML_VERSION_1_0);
			xsw.writeStartElement(CLASS_NODE);
			addElement(xsw, classNode.fieldNameId, String.valueOf(classNode.getId()));
			addElement(xsw, classNode.fieldNameType, classNode.getType());
			addElement(xsw, classNode.fieldNameName, classNode.getName());
			addElement(xsw, classNode.fieldNameParent, String.valueOf(classNode.getParent()));
			addElement(xsw, classNode.fieldNameChild, String.valueOf(classNode.getChild()));
			addElement(xsw, classNode.fieldNameText1, classNode.getText1());
			addElement(xsw, classNode.fieldNameText2, classNode.getText2());
			addElement(xsw, classNode.fieldNameText8, classNode.getText8());
			addElement(xsw, classNode.fieldNameText9, classNode.getText9());
			xsw.writeEndElement();
			xsw.writeEndDocument();
		} catch (XMLStreamException e) {
			throw new ContentHandlerException(null, e);
		} finally {
			this.utility.closeXMLStreamWriter(xsw);
		}
		return this.utility.createMessageBody(this.getSupportedContentType());
	}

	private void addElement(XMLStreamWriter xsw, String elementName, String elementValue) throws XMLStreamException {
		xsw.writeStartElement(elementName);
		xsw.writeCharacters(elementValue);
		xsw.writeEndElement();
	}

}
