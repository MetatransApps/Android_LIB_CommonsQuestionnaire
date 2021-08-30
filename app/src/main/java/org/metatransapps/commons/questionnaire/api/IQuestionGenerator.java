package org.metatransapps.commons.questionnaire.api;


import org.metatransapps.commons.questionnaire.model.GameData;


public interface IQuestionGenerator {

	
	public IConfigurationQuestion nextQuestion(GameData gameData);
	
}
