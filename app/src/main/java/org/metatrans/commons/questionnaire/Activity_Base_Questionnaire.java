package org.metatrans.commons.questionnaire;


import org.metatrans.commons.Activity_Base_Ads_Banner;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.questionnaire.api.IConfigurationQuestion;
import org.metatrans.commons.questionnaire.model.BestResults;
import org.metatrans.commons.questionnaire.model.GameData;
import org.metatrans.commons.questionnaire.model.UserSettings;
import org.metatrans.commons.storage.StorageUtils;


public abstract class Activity_Base_Questionnaire extends Activity_Base_Ads_Banner {


	public static final String FILE_NAME_BEST_RESULTS		= "best_results";
	
	
	@Override
	public void onPause() {
		
		storeData();
		
		super.onPause();
	}
	
	
	@Override
	public void onResume() {
		
		super.onResume();
		
		getGameData();
		getUserSettings();
		getBestResults();
	}


	protected IConfigurationQuestion getNextQuestion(GameData gameData) {

		//Must be overrided
		//TODO: make it abstract
		if (true) {

			throw new IllegalStateException("startNewGame");
		}

		return null;
	}


	protected void storeData() {

		Application_Base.getInstance().storeUserSettings();;
		Application_Base.getInstance().storeGameData();
		
		getBestResults();
		StorageUtils.writeStore(this, FILE_NAME_BEST_RESULTS);
	}
	

	public GameData getGameData() {

		GameData game_data = (GameData) Application_Base.getInstance().getGameData();

		if (game_data.current_question == null) {

			IConfigurationQuestion next_question = getNextQuestion(game_data);

			game_data.clearForNewQuestion(next_question);
		}

		return game_data;
	}
	
	
	public UserSettings getUserSettings() {

		return (UserSettings) Application_Base.getInstance().getUserSettings();
	}
	
	
	public BestResults getBestResults() {
		BestResults results = (BestResults) StorageUtils.readStorage(this, FILE_NAME_BEST_RESULTS);
		if (results == null) {
			results = new BestResults();
			StorageUtils.writeStore(this, FILE_NAME_BEST_RESULTS, results);
			results = (BestResults) StorageUtils.readStorage(this, FILE_NAME_BEST_RESULTS);
		}
		return results;
	}
}
