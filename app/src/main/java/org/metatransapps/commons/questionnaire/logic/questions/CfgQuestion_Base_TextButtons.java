package org.metatransapps.commons.questionnaire.logic.questions;

import org.metatransapps.commons.questionnaire.api.IConfigurationQuestion_TextButtons;



public abstract class CfgQuestion_Base_TextButtons extends CfgQuestion_Base implements IConfigurationQuestion_TextButtons {
	
	
	private static final long serialVersionUID = -4428528108418647396L;
	
	
	protected CfgQuestion_Base_TextButtons(int _index_correct, Object[] _array) {
		super(_index_correct, _array);
	}
}
