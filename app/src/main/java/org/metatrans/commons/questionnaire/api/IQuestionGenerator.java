package org.metatrans.commons.questionnaire.api;


import org.metatrans.commons.questionnaire.model.GameData;


public interface IQuestionGenerator {

	
	public IConfigurationQuestion nextQuestion(GameData gameData);
	
}
