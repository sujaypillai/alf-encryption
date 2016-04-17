package com.ootb.action.evaluator;

import java.util.List;

import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.evaluator.ActionConditionEvaluatorAbstractBase;
import org.alfresco.service.cmr.action.ActionCondition;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;

public class EncryptionEnableEvaluator extends ActionConditionEvaluatorAbstractBase{
	public final static String PARAM_ACTIVE = "active";
	public static final String PARAM_PASS = "pass";
	@Override
	protected boolean evaluateImpl(ActionCondition actionCondition,
			NodeRef actionedUponNodeRef) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {
		paramList.add(new ParameterDefinitionImpl(PARAM_ACTIVE, DataTypeDefinition.BOOLEAN, false, getParamDisplayLabel(PARAM_ACTIVE)));
		
	}

}
