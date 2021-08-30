package org.metatransapps.commons.questionnaire.api;


import java.io.Serializable;


public interface IConfigurationQuestion extends Serializable {
	
	public long getID();
	public int getName();
	
	public int getAnswersCount();
	public int getIndexCorrect();
	public Object[] getAnswers();
	
	public void shuffle();
}
