package org.metatrans.commons.questionnaire.api;


import java.io.Serializable;


public interface IConfigurationQuestion extends Serializable {

	long getID();

	int getName();

	int getAnswersCount();

	int getIndexCorrect();

	Object getQuestion();

	Object[] getAnswers();

	void shuffle();
}
