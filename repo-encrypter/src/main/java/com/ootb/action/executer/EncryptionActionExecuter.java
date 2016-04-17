package com.ootb.action.executer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import com.ootb.util.AES;

public class EncryptionActionExecuter extends BaseExecuter {
	public static final String NAME = "encryption-action";
    //private NodeService nodeService;
    
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		byte[] key = null;
		try {
			String inputPassword = (String) action.getParameterValue(PARAM_PASS);
			
			if(inputPassword.isEmpty()){
				inputPassword = "123";
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputPassword.getBytes(),0,inputPassword.length());
			
			String inputMD5Password = new BigInteger(1, md.digest()).toString(16);
			key = inputMD5Password.getBytes();
			
			byte[] data = AES.encrypt(super.getNodeContent(actionedUponNodeRef), key);
			super.write(actionedUponNodeRef, data);
			action.setParameterValue(BaseExecuter.PARAM_ACTIVE, true);
			action.setParameterValue(BaseExecuter.PARAM_PASS, inputMD5Password);
			super.executeImpl(action, actionedUponNodeRef);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		super.addParameterDefinitions(paramList);
	}
    
    
}
