package com.apps.mobile.android.commons.questionnaire.logic.questions;


import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion;
import com.apps.mobile.android.commons.questionnaire.utils.NumbersUtils;


public abstract class CfgQuestion_Base implements IConfigurationQuestion {
	
	
	private static final long serialVersionUID = -3697955793379663195L;
	
	
	private int index_correct;
	protected int[] array;
	protected Object[] array_o;
	
	
	protected CfgQuestion_Base(int _index_correct, Object[] _array) {
		index_correct = _index_correct;
		array_o = _array;
	}
	
	
	@Override
	public int getIndexCorrect() {
		return index_correct;
	}
	
	
	@Override
	public Object[] getAnswers() {
		
		if (array_o == null) {
			array_o = new Object[array.length];
			for(int i=0; i<array.length; i++) {
				array_o[i] = array[i];
			}
		}
		
		return array_o;
	}
	
	
	@Override
	public void shuffle() {
		
		int _index_correct = getIndexCorrect();
		
		Object value_correct = getAnswers()[_index_correct];
		
		NumbersUtils.shuffleArray(getAnswers());
		
		for (int i=0; i<getAnswers().length; i++) {
			if (getAnswers()[i].equals(value_correct)) {
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
