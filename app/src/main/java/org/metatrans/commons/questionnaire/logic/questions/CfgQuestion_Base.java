package org.metatrans.commons.questionnaire.logic.questions;


import org.metatrans.commons.questionnaire.api.IConfigurationQuestion;
import org.metatrans.commons.questionnaire.utils.NumbersUtils;

import java.util.Arrays;


public abstract class CfgQuestion_Base implements IConfigurationQuestion {
	
	
	private static final long serialVersionUID = -3697955793379663195L;
	
	
	private int index_correct;
	protected Integer[] buttons;
	
	
	protected CfgQuestion_Base(int _index_correct, Integer[] _buttons) {
		index_correct = _index_correct;
		buttons = _buttons;
	}
	
	
	@Override
	public int getIndexCorrect() {
		return index_correct;
	}
	
	
	@Override
	public Object[] getAnswers() {

		if (buttons == null) {

			buttons = new Integer[4];
		}

		return Arrays.copyOf(buttons, buttons.length);
	}


	@Override
	public int getAnswersCount() {

		return getAnswers().length;
	}


	@Override
	public void shuffle() {


		int _index_correct = index_correct;


		Object value_correct = buttons[_index_correct];


		NumbersUtils.shuffleArray(buttons);


		for (int i=0; i<buttons.length; i++) {

			if (buttons[i].equals(value_correct)) {

				_index_correct = i;

				break;
			}
		}


		index_correct = _index_correct;
	}
	
	
	@Override
	public int getName() {

		throw new IllegalStateException("CfgQuestion_Base: int getName() is not supported. this=" + this.toString());
	}
}
