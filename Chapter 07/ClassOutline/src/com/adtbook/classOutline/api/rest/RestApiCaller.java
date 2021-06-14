package com.adtbook.classOutline.api.rest;

import java.net.URI;
import java.nio.charset.Charset;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.adtbook.classOutline.api.IApiCaller;
import com.adtbook.classOutline.tree.ClassTree;
import com.adtbook.classOutline.tree.TreeNode;
import com.adtbook.classOutline.utils.ProjectUtility;
import com.sap.adt.communication.message.IResponse;
import com.sap.adt.communication.resources.AdtRestResourceFactory;
import com.sap.adt.communication.resources.IRestResource;
import com.sap.adt.communication.resources.IRestResourceFactory;
import com.sap.adt.communication.resources.ResourceNotFoundException;
import com.sap.adt.compatibility.discovery.AdtDiscoveryFactory;
import com.sap.adt.compatibility.discovery.IAdtDiscovery;
import com.sap.adt.compatibility.discovery.IAdtDiscoveryCollectionMember;
import com.sap.adt.compatibility.model.templatelink.IAdtTemplateLink;
import com.sap.adt.compatibility.uritemplate.IAdtUriTemplate;

public class RestApiCaller implements IApiCaller {

	private static final String PLUGIN_DISCOVERY = "/adtbook/discovery";
	private static final String URI_PARAMETER_CLASS_NAME = "class_name";
	private static final String RELATION = "http://abapblog.com/adt/relations";
	private static final String SCHEME = "http://abapblog.com/adt";
	private static final String ClASSTREE_TERM = "classtree";
	private static final String TREEURI_TERM = "treeuri";

	@Override
	public ClassTree getClassTree(String className, IProject project, boolean forceRefresh) {

		ClassTree restClassTree = getClassTree(className, project);

		if (forceRefresh == false & restClassTree != null) {
			return restClassTree;
		}
		return getNewClassTree(className, project);
	}

	@Override
	public ClassTree getNewClassTree(String className, IProject project) {
		String destinationId = ProjectUtility.getDestinationID(project);
		URI classTreeUri = getURI(destinationId, className, ClASSTREE_TERM);
		IRestResourceFactory restResourceFactory = AdtRestResourceFactory.createRestResourceFactory();
		IRestResource classTreeResource = restResourceFactory.createResourceWithStatelessSession(classTreeUri,
				destinationId);
		try {
			IResponse response = classTreeResource.get(null, IResponse.class);
			ClassTree classTree = new RestClassTreeContentHandler(className, project).deserialize(response.getBody(),
					ClassTree.class);
			return classTree;
		} catch (ResourceNotFoundException e) {
			System.out.println(e.toString());
			return null;
		} catch (RuntimeException e) {
			System.out.println(e.toString());
			return null;
		}
	}

	private URI getURI(String destination, String className, String categoryTerm) {
		IAdtDiscovery discovery = AdtDiscoveryFactory.createDiscovery(destination, URI.create(PLUGIN_DISCOVERY));
		IAdtDiscoveryCollectionMember collectionMember = discovery.getCollectionMember(SCHEME, categoryTerm,
				new NullProgressMonitor());
		IAdtTemplateLink templateLink = collectionMember.getTemplateLink(RELATION);
		IAdtUriTemplate uriTemplate = templateLink.getUriTemplate();
		String uri = uriTemplate.set(URI_PARAMETER_CLASS_NAME, className).expand();
		return URI.create(uri);
	}

	@Override
	public ClassTree getClassTree(String className, IProject project) {
		int count = 0;
		while (classList.size() > count) {
			ClassTree restClassTree = classList.get(count);
			if (restClassTree.getClassName() == className
					&& restClassTree.getProject().getName() == project.getName()) {
				return restClassTree;
			}
			count++;
		}
		return null;
	}

	@Override
	public String getUriForTreeNode(TreeNode treeNode) {
		String destinationId = ProjectUtility.getDestinationID(treeNode.getProject());
		URI classTreeUri = getURI(destinationId, treeNode.getClassName(), TREEURI_TERM);
		IRestResourceFactory restResourceFactory = AdtRestResourceFactory.createRestResourceFactory();
		IRestResource classTreeResource = restResourceFactory.createResourceWithStatelessSession(classTreeUri,
				destinationId);
		try {
			IResponse response = classTreeResource.post(null, IResponse.class,
					new RestClassNodeContentHandler().serialize(treeNode.getSourceNode(), Charset.forName("UTF-8")));
			return response.getBody().toString();
		} catch (ResourceNotFoundException e) {
			System.out.println(e.toString());
			return null;
		} catch (RuntimeException e) {
			System.out.println(e.toString());
			return null;
		}
	}

}
