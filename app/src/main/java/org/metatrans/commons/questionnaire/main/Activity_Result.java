package org.metatrans.commons.questionnaire.main;


import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.engagement.social.View_Social_InviteFriends;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.questionnaire.Activity_Base_Questionnaire;
import org.metatrans.commons.questionnaire.R;

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
		
		super.onResume();

		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);

		View view_invite_friends = new View_Social_InviteFriends(this, view.getRectangle_InviteFriends(),
				((Application_Base_Ads)getApplication()).getEngagementProvider().getSocialProvider(), coloursCfg);

		frame.addView(view_invite_friends);
	}
	

	@Override
	protected FrameLayout getFrame() {

		return findViewById(R.id.layout_result_vertical);
	}
	
	
	private View_Result createView() {
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);

		View_Result view = new View_Result(this, coloursCfg, getUserSettings(), getModeName());

		view.setOnTouchListener(new OnTouchListener_Result(view));

		return view;
	}
	
	
	public void startNewGame() {

		if (true) {

			throw new IllegalStateException("startNewGame");
		}

		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		
		eventsManager.handleGameEvents_OnExit(this, getGameData(), getUserSettings());
		
		Application_Base.getInstance().recreateGameDataObject();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

	    super.onConfigurationChanged(newConfig);
	}
}
