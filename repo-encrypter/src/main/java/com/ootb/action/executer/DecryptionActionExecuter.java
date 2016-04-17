package com.ootb.action.executer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

import com.ootb.util.AES;

public class DecryptionActionExecuter extends BaseExecuter {
	public static final String NAME = "decryption-action";
    private NodeService nodeService;
    
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		byte[] key = null;
		try {
	        String pass = super.getPassword(actionedUponNodeRef);
	        String inputPassword = (String) action.getParameterValue(BaseExecuter.PARAM_PASS);
	        MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputPassword.getBytes(),0,inputPassword.length());
			String inputMD5Password = new BigInteger(1, md.digest()).toString(16);
	        
			if(!pass.equals(inputMD5Password)){
				int i = 0/0;
				return;
			}
			
			key = inputMD5Password.getBytes();
			
			byte[] data = AES.decrypt(super.getNodeContent(actionedUponNodeRef), key);
			super.write(actionedUponNodeRef, data);
			action.setParameterValue(BaseExecuter.PARAM_ACTIVE, false);
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
