package org.metatransapps.commons.questionnaire.main;


import java.util.Arrays;

import org.metatransapps.commons.Activity_Base;
import org.metatransapps.commons.ads.api.IAdsConfiguration;
import org.metatransapps.commons.ads.impl.flow.IAdLoadFlow;
import org.metatransapps.commons.app.Application_Base;
import org.metatransapps.commons.app.Application_Base_Ads;
import org.metatransapps.commons.app.IActivityInterstitial;
import org.metatransapps.commons.events.api.IEventsManager;
import org.metatransapps.commons.questionnaire.Activity_Base_Questionnaire;
import org.metatransapps.commons.questionnaire.R;
import org.metatransapps.commons.questionnaire.api.IConfigurationQuestion;
import org.metatransapps.commons.questionnaire.model.GameResult;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;


public abstract class Activity_Question extends Activity_Base_Questionnaire implements IActivityInterstitial {
	
	
	protected static final int VIEW_ID = 123;
	
	
	private long timestamp_resume;
	
	private IAdLoadFlow current_adLoadFlow_Interstitial;
	
	
	protected abstract View createView();
	protected abstract IConfigurationQuestion getNextQuestion();
	protected abstract Class<? extends Activity_Base> getActivityClass_Menu();
	protected abstract Class<? extends Activity_Base> getActivityClass_Result();
	public abstract void setNextLevel();
	
	
	public boolean allowedProcceedingWithWrongAnswer() {
		return false;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Override
	public void onResume() {

		//Toast_Base.showToast_InCenter(this, "onResume: timestamp_resume=" + timestamp_resume);
		
		timestamp_resume = System.currentTimeMillis();
		
		if (getGameData().current_question == null) {
			getGameData().current_question = getNextQuestion();
		}
		
		setContentView(R.layout.activity_question);
		
		FrameLayout frame = getFrame();
		
		Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(frame);

		View view = createView();
		frame.addView(view, 0);
		
		/*View_Question view_question = (View_Question) frame.findViewById(MAIN_VIEW_ID);
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(getUserSettings().uiColoursID);
		View _view_leaderboards = Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().getLeaderboardView(coloursCfg, view_question.getRectangle_LeaderBoards());
		View _view_achievements = Application_Base.getInstance().getEngagementProvider().getAchievementsProvider().getAchievementsView(coloursCfg, view_question.getRectangle_LeaderBoards());
		
		if (_view_leaderboards != null && _view_achievements != null) {
			if (_view_leaderboards != _view_achievements) {
				throw new IllegalStateException("_view_leaderboards != _view_achievements");
			}
		}
		
		frame.addView(_view_leaderboards);
		
		*/
		
		setUpLeaderboard(getGameData().isCountedAsCompleted());

		super.onResume();
		
		if (getInterstitialName() != null) {
			
			current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().getCachedFlow(getInterstitialName());
			
			if (current_adLoadFlow_Interstitial == null) {
				
				System.out.println("Activity_Question create Interstitial");
				
				current_adLoadFlow_Interstitial = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Interstitial_Mixed(getInterstitialName());
				((Application_Base_Ads)getApplication()).getAdsManager().putCachedFlow(getInterstitialName(), current_adLoadFlow_Interstitial);
			} else {
				
				System.out.println("Activity_Question Interstitial EXISTS");
				
				//current_adLoadFlow_Interstitial.cleanCurrent();
				current_adLoadFlow_Interstitial.pause();
			}
		}
	}
	
	
	@Override
	public void onPause() {
		
		//Toast_Base.showToast_InCenter(this, "onPause: timestamp_resume=" + timestamp_resume);
		
		if (!getGameData().isCountedAsCompleted()) {
		//if (getGameData().count_answered < getUserSettings().countQuestions) {
			long lastPeriodInsideTheMainScreen = System.currentTimeMillis() - timestamp_resume;
			getGameData().addAccumulated_time_inmainscreen(lastPeriodInsideTheMainScreen);
		}
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		eventsManager.updateLastMainScreenInteraction(this, System.currentTimeMillis());
		
		super.onPause();
	}
	
	
	private String getInterstitialName() {
		return IAdsConfiguration.AD_ID_INTERSTITIAL1;
	}
	
	
	@Override
	protected FrameLayout getFrame() {
		return (FrameLayout) findViewById(R.id.layout_main_vertical);
	}
	
	
	public void startNewGame() {
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		
		eventsManager.handleGameEvents_OnExit(this, getGameData(), getUserSettings());
		
		timestamp_resume = System.currentTimeMillis();
		
		Application_Base.getInstance().recreateGameDataObject();
		
		nextQuestion();
	}
	
	
	public boolean isAnswered() {
		return getGameData().current_answered;
	}
	
	
	public boolean isAnsweredCorrect() {
		if (!getGameData().current_answered) {
			throw new IllegalStateException("!getGameData().current_answered");
		}
		return getGameData().current_answered_correct;
	}
	
	
	public void answer(int _index_button) {
		
		if (!getGameData().current_answered) {

			getGameData().current_answered = true;
			getGameData().current_answered_correct = getGameData().isCorrectAnswer(_index_button);
			
			getGameData().count_answered++;
			if (getGameData().current_answered_correct) {
				getGameData().count_correct++;
			}
			
			if (getGameData().answers_history == null) {
				getGameData().answers_history = new boolean[0];
			}
			getGameData().answers_history = Arrays.copyOf(getGameData().answers_history, getGameData().answers_history.length + 1);
			getGameData().answers_history[getGameData().answers_history.length - 1] = getGameData().current_answered_correct;
		}
		
		getGameData().buttons_clicked[_index_button] = true;
		
	}
	
	
	private void openQuestion(IConfigurationQuestion question) {
		
		
		getGameData().clearForNewQuestion(question);

		
		FrameLayout frame = getFrame();
		View view = findViewById(VIEW_ID);
		view.destroyDrawingCache();
		frame.removeView(view);
		
		view = createView();
		
		frame.addView(view, 0);
	}
	
	
	public void setUpLeaderboard(boolean gameCompleted) { 
		if (gameCompleted) {
			((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().setEnabled(true);
		} else {
			((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().setEnabled(false);
		}
	}
	
	
	public void nextQuestion() {
		
		IConfigurationQuestion next_question = getNextQuestion();
		
		boolean gameCompleted = next_question == null || getGameData().count_answered >= getUserSettings().countQuestions;
		
		setUpLeaderboard(gameCompleted);
		
		if (gameCompleted) {
			
			if (!getGameData().isCountedAsCompleted()) {
				
				//INFO: Is set in handleGameEvents_OnFinish
				//getGameData().setCountedAsCompleted();
				
				long lastPeriodInsideTheMainScreen = System.currentTimeMillis() - timestamp_resume;
				getGameData().addAccumulated_time_inmainscreen(lastPeriodInsideTheMainScreen);
				
				GameResult gameResult = getGameData().getGameResult();
				getBestResults().addResult(getUserSettings().modeID, gameResult);
				
				IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
				eventsManager.handleGameEvents_OnFinish(this, getGameData(), getUserSettings(), -1);
				
				storeData();
				
				Application_Base_Ads.getInstance().openInterstitial();
			}
			
			
			((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().openLeaderboard_LocalOnly(getUserSettings().modeID);
			
			
			//Submit score and open Leaderboards (or connect, if not)
			if (((Application_Base_Ads)getApplication()).getEngagementProvider().getSocialProvider().isConnected()) {
				
				long time_to2submit = 0;
				if (getGameData().getGameResult().count_incorrect == 0) {
					time_to2submit = getGameData().getGameResult().time;
				}
				
				GameResult best = getBestResults().getResult(getUserSettings().modeID);
				if (best != null) {
					if (best.count_incorrect == 0) {
						if (best.time < time_to2submit) { //Attention: less is better
							time_to2submit = best.time;
						}
					}
				}
				
				if (time_to2submit != 0) {
					((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().submitLeaderboardScore(getUserSettings().modeID, time_to2submit);
				}
				
				((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().openLeaderboard(getUserSettings().modeID);
				
			} else {
				
				((Application_Base_Ads)getApplication()).getEngagementProvider().getSocialProvider().connect();
			}
			
		} else {
			openQuestion(next_question);
		}
	}
	
	
	/*protected void showLeaderBoards() {

	}*/
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		Intent i = new Intent(getApplicationContext(), getActivityClass_Menu());
		startActivity(i);
		
		return false;
	}
	
	
	/*@Override
	public void onBackPressed() {
		AlertDialog.Builder adb = Alerts_Base.createAlertDialog_Exit(Activity_Question.this,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
						finish();
					}
				});
				
		adb.show();
	}*/
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	
	@Override
	public void openInterstitial() {
		try {
			
			System.out.println("Activity_Question openInterstitial called");
			
			if (current_adLoadFlow_Interstitial != null) {
				System.out.println("Activity_Question openInterstitial RESUMED");
				current_adLoadFlow_Interstitial.resume();
			}
			
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}