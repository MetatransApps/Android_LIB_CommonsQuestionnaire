package com.apps.mobile.android.commons.questionnaire.api;


import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion;
import com.apps.mobile.android.commons.questionnaire.model.GameData;


public interface IQuestionGenerator {

	
	public IConfigurationQuestion nextQuestion(GameData gameData);
	
}
