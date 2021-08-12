package com.adtbook.classOutline.tree;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.adtbook.classOutline.api.ApiCallerFactory;
import com.sap.adt.tools.core.ui.AbapCoreUi;
import com.sap.adt.tools.core.ui.IAdtObjectTypeInfoUi;

@SuppressWarnings("restriction")
public class TreeNode implements IAdaptable {

	private int id;
	private String type;
	private String name;
	private String description;
	private TreeParent parent;
	private String adtUri;
	private String visibility = "";
	private String className = "";
	private IProject project;
	private ClassNode sourceNode;

	public TreeNode(String className, IProject project, ClassNode sourceNode) {
		try {
			setId(sourceNode.getId());
			setType(sourceNode.getType());
			setName(sourceNode.getText1());
			setDescription(sourceNode.getText2());
			setVisibility(sourceNode.getName());
			setSourceNode(sourceNode);

		} catch (Exception e) {
			// TODO
		}
		setClassName(className);
		setProject(project);

	}

	private void setVisibility(String visibility) {
		switch (visibility) {
		case "@5B@": {
			this.visibility = "public";
			break;
		}
		case "@5C@": {
			this.visibility = "private";
			break;
		}
		case "@5D@": {
			this.visibility = "protected";
			break;
		}
		default: {
		}
		}
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TreeParent getParent() {
		return parent;
	}

	public void setParent(TreeParent parent) {
		this.parent = parent;
	}

	public String getAdtUri() {
		if (adtUri.isEmpty() && isClassElementType(getType())) {
			setAdtUri(ApiCallerFactory.getCaller().getUriForTreeNode(this));
		}
		return adtUri;
	}

	private boolean isClassElementType(String type) {
		if (type.charAt(0) == 'C') {
			return false;
		} else {
			return true;
		}
	}

	private void setAdtUri(String adtUri) {
		this.adtUri = adtUri;
	}

	public Image getImage() {
		IAdtObjectTypeInfoUi typeInfo = AbapCoreUi.getObjectTypeRegistry()
				.getObjectTypeByGlobalWorkbenchType(buildWorkbenchType(getType(), getVisibility()));
		if (typeInfo != null) {
			return typeInfo.getImage();
		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		}
	}

	private String getVisibility() {
		return visibility;
	}

	private String buildWorkbenchType(String treeNodeType, String visibility) {
		String wbType;
		switch (treeNodeType) {
		case "COI": {
			wbType = "INTF/" + treeNodeType.substring(1, treeNodeType.length());
			return wbType;
		}
		case "OOI": {
			wbType = "INTF/" + treeNodeType.substring(1, treeNodeType.length());
			return wbType;
		}

		case "OOL": {
			wbType = "CLAS/OC";
			return wbType;
		}
		case "OOLD": {
			wbType = "CLAS/OM";
			break;
		}

		case "OOND": {
			wbType = "CLAS/OM";
			break;
		}
		case "OOLI": {
			wbType = "CLAS/OM";
			break;
		}
		case "OOY": {
			wbType = "CLAS/OT";
			break;
		}

		case "OOLT": {
			wbType = "CLAS/OT";
			break;
		}

		case "OOK": {
			return "CLAS/OK";
		}

		case "OOLN": {
			return "INTF/OI";
		}

		case "OON": {
			return "INTF/OI";
		}

		case "OOLA": {
			wbType = "CLAS/OA";
			break;
		}

		case "OONA": {
			wbType = "CLAS/OA";
			break;
		}

		case "OONT": {
			wbType = "CLAS/OT";
			break;
		}

		default: {
			wbType = "CLAS/" + treeNodeType.substring(1, treeNodeType.length());
		}
		}
		if (visibility.isEmpty()) {
			return wbType;
		} else {
			return wbType + "/" + visibility;
		}
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public ClassNode getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(ClassNode sourceNode) {
		this.sourceNode = sourceNode;
	}

}
