package com.apps.mobile.android.commons.questionnaire.logic.questions;

import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion_ImageButtons;



public abstract class CfgQuestion_Base_ImageButtons extends CfgQuestion_Base implements IConfigurationQuestion_ImageButtons {
	
	
	private static final long serialVersionUID = -4428528108418647396L;
	
	
	protected CfgQuestion_Base_ImageButtons(int _index_correct, Object[] _array) {
		super(_index_correct, _array);
	}
	
	
	public int[] getResID_Answers() {
		
		if (array != null) {
			return array;
		}
		
		array = new int[array_o.length];
		for(int i=0; i<array.length; i++) {
			array[i] = (Integer) array_o[i];
		}
		
		return array;
	}
}
