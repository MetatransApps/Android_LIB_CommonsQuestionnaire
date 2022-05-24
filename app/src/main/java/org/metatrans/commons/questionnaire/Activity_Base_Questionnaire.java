package org.metatrans.commons.questionnaire;


import org.metatrans.commons.Activity_Base_Ads_Banner;
import org.metatrans.commons.app.Application_Base;
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
	
	
	protected void storeData() {

		Application_Base.getInstance().storeUserSettings();;
		Application_Base.getInstance().storeGameData();
		
		getBestResults();
		StorageUtils.writeStore(this, FILE_NAME_BEST_RESULTS);
	}
	

	public GameData getGameData() {

		try {

			return (GameData) Application_Base.getInstance().getGameData();

		} catch (Exception e) {

			e.printStackTrace();

			Application_Base.getInstance().recreateGameDataObject();

			return (GameData) Application_Base.getInstance().getGameData();
		}
	}
	
	
	public UserSettings getUserSettings() {
		return (UserSettings) ((Application_Base)getApplication()).getUserSettings();
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
