package org.metatrans.commons.questionnaire.logic.questions;


import org.metatrans.commons.questionnaire.api.IConfigurationQuestion_ImageButtons;


public abstract class CfgQuestion_Base_ImageButtons extends CfgQuestion_Base implements IConfigurationQuestion_ImageButtons {
	
	
	private static final long serialVersionUID = -4428528108418647396L;
	
	
	protected CfgQuestion_Base_ImageButtons(int _index_correct, Integer[] _array) {
		super(_index_correct, _array);
	}


	@Override
	public int[] getResID_Answers() {
		
		int[] array = new int[getAnswersCount()];

		if (buttons != null) {

			for (int i = 0; i < array.length; i++) {

				if (buttons[i] != null) {

					array[i] = buttons[i];
				}
			}
		}
		
		return array;
	}
}
