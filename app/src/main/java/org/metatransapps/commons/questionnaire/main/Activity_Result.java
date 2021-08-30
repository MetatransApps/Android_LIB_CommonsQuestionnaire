package org.metatransapps.commons.questionnaire.main;


import org.metatransapps.commons.app.Application_Base;
import org.metatransapps.commons.app.Application_Base_Ads;
import org.metatransapps.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatransapps.commons.cfg.colours.IConfigurationColours;
import org.metatransapps.commons.engagement.social.View_Social_InviteFriends;
import org.metatransapps.commons.events.api.IEventsManager;
import org.metatransapps.commons.questionnaire.Activity_Base_Questionnaire;
import org.metatransapps.commons.questionnaire.R;
import org.metatransapps.commons.questionnaire.model.GameResult;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;


public abstract class Activity_Result extends Activity_Base_Questionnaire {
	
	
	protected int VIEW_ID = 12345678;
	
	
	protected abstract String getModeName();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Override
	public void onResume() {
		
		setContentView(R.layout.activity_result);
		
		FrameLayout frame = getFrame();
		View_Result view = createView();
		view.setId(VIEW_ID);
		frame.addView(view);
		
		/*IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);
		View view_leaderboards = new View_Social_Leaderboards_GoogleImpl(this, view.getRectangle_LeaderBoards(),
				((Application_Base_Ads)getApplication()).getSocialProvider(), coloursCfg);
		frame.addView(view_leaderboards);
		*/
		
		super.onResume();
		
		
		
		//FrameLayout frame = getFrame();
		//View_Result view = (View_Result) findViewById(MAIN_VIEW_ID);
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);		
		View view_invite_friends = new View_Social_InviteFriends(this, view.getRectangle_InviteFriends(),
				((Application_Base_Ads)getApplication()).getEngagementProvider().getSocialProvider(), coloursCfg);
		frame.addView(view_invite_friends);
	}
	

	@Override
	protected FrameLayout getFrame() {
		return (FrameLayout) findViewById(R.id.layout_result_vertical);
	}
	
	
	private View_Result createView() {
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);
		GameResult bestResult = getBestResults().getResult(getUserSettings().modeID);
		
		if (bestResult == null) {
			bestResult = getGameData().getGameResult();
			getBestResults().addResult(getUserSettings().modeID, bestResult);
		}
		
		View_Result view = new View_Result(this, coloursCfg, getUserSettings(), bestResult, getModeName());
		view.setOnTouchListener(new OnTouchListener_Result(view));
		return view;
	}
	
	
	public void startNewGame() {
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		
		eventsManager.handleGameEvents_OnExit(this, getGameData(), getUserSettings());
		
		Application_Base.getInstance().recreateGameDataObject();
	}
	
	
	/*@Override
	public void onBackPressed() {
		View view = findViewById(R.id.layout_result_vertical);
		Bitmap screen = ScreenShare.getScreen(this, view);
		((Application_MFK)getApplication()).shareResultScreen(this, screen);
		ScreenShare.cleanup(this, view);
	}*/
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
