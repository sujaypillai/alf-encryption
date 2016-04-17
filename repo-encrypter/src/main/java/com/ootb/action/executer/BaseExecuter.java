package com.ootb.action.executer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;

import com.ootb.model.EncryptionModel;

public class BaseExecuter extends ActionExecuterAbstractBase {
	public final static String NAME = "new-base-executer";
    public final static String PARAM_ACTIVE = "active";
    public static final String PARAM_PASS = "pass";
    
    public NodeService nodeService;
    public ContentService contentService;
    public ContentReader actionedUponContentReader;
    public static int bytesize = 16384;
    protected ServiceRegistry serviceRegistry;
    protected String newPassword;
    
	public BaseExecuter() {
		super();
	}

	
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}


	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}


	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public byte[] converToByteArray(InputStream is) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[bytesize];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

	public byte[] getNodeContent(NodeRef nodeRef) {
        byte[] data = new byte[bytesize];

        // Reading the node content
        ContentReader contentReader = serviceRegistry.getContentService().getReader(
                nodeRef, ContentModel.PROP_CONTENT);

        actionedUponContentReader = contentReader;
        InputStream is = contentReader.getContentInputStream();

        // Convert input stream to bytes
        try {
            data = this.converToByteArray(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return data;
    }
	
    public void write(NodeRef nodeRefer, byte[] data) {
        // Getting Mimetype of node        
        QName PROP_QNAME_CONTENT = QName.createQName(
                "http://www.alfresco.org/model/content/1.0", "content");
        ContentData contentData = (ContentData) serviceRegistry.getNodeService()
                .getProperty(nodeRefer, PROP_QNAME_CONTENT);
        String originalMimeType = contentData.getMimetype();

        ContentService contentService = serviceRegistry.getContentService();
        ContentWriter contentWriter = contentService.getWriter(nodeRefer,
                ContentModel.PROP_CONTENT, true);

        contentWriter.setMimetype(originalMimeType);
        contentWriter.setEncoding(actionedUponContentReader.getEncoding());

        contentWriter.putContent(new ByteArrayInputStream(data));
    }

    public String getPassword(NodeRef nodeRef) {
        return (String) nodeService.getProperty(nodeRef,EncryptionModel.PROP_PASS);
    }
	
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		Boolean activeFlag = (Boolean) action.getParameterValue(PARAM_ACTIVE);
		String MD5password = (String) action.getParameterValue(PARAM_PASS);
		
		if(activeFlag == null){
			activeFlag = true;
		}
		
		Map<QName, Serializable> properties = nodeService.getProperties(actionedUponNodeRef);
		properties.put(EncryptionModel.PROP_IS_ACTIVE,activeFlag);
		properties.put(EncryptionModel.PROP_PASS, MD5password);
		
		if(nodeService.hasAspect(actionedUponNodeRef, EncryptionModel.ASPECT_MYC_ENCRYPT)){
			nodeService.setProperties(actionedUponNodeRef, properties);
		}
		else{
			nodeService.addAspect(actionedUponNodeRef, EncryptionModel.ASPECT_MYC_ENCRYPT, properties);
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_ACTIVE, DataTypeDefinition.BOOLEAN, false, getParamDisplayLabel(PARAM_ACTIVE)));
		paramList.add(new ParameterDefinitionImpl(PARAM_PASS, DataTypeDefinition.TEXT, true, getParamDisplayLabel(PARAM_PASS)));
	}

}
