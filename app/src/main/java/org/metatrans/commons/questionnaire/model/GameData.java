package org.metatrans.commons.questionnaire.model;


import org.metatrans.commons.model.GameData_Base;
import org.metatrans.commons.questionnaire.api.IConfigurationQuestion;


public class GameData extends GameData_Base {
	
	
	private static final long serialVersionUID = 1921682220442758971L;
	
	
	public int count_answered;
	public int count_correct;
	
	public IConfigurationQuestion current_question;
	public boolean current_answered;
	public boolean current_answered_correct;
	public boolean[] buttons_clicked;
	
	public boolean[] answers_history;
			
	public GameResult result;
	
	
	public GameData() {
		clear();
	}
	
	
	protected void clear() {

		count_answered 			= 0;
		count_correct 			= 0;

		clearForNewQuestion(null);
	}
	
	
	public void clearForNewQuestion(IConfigurationQuestion new_question) {
		current_question 			= new_question;
		current_answered 			= false;
		current_answered_correct 	= false;
		buttons_clicked 			= new boolean[4];
	}
	
	
	public boolean isCorrectAnswer(int index) {
		return index == current_question.getIndexCorrect();
	}
	
	
	public GameResult getGameResult() {
		//if (result == null) {
			result = createGameResult();
			result.count_correct = count_correct;
			result.count_incorrect = count_answered - count_correct;
			result.time = getAccumulated_time_inmainscreen();			
		//}
		return result;
	}
	
	
	protected GameResult createGameResult() {
		return new GameResult();
	}
}
