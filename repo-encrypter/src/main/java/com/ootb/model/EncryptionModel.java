package com.ootb.model;

import org.alfresco.service.namespace.QName;

public interface EncryptionModel {
	public static final String MYCOMPANY_MODEL_URI    = "http://www.mycompany.com/model/content/1.0";
    public static final String MYCOMPANY_MODEL_PREFIX = "myc";
    
    static final QName ASPECT_MYC_ENCRYPT = QName.createQName(MYCOMPANY_MODEL_URI, "encrypt");
    static final QName PROP_IS_ACTIVE = QName.createQName(MYCOMPANY_MODEL_URI, "isActive");
    static final QName PROP_PASS = QName.createQName(MYCOMPANY_MODEL_URI, "pass");
}
