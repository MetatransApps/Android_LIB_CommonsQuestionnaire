package org.metatrans.commons.questionnaire.main;


import java.util.Arrays;

import org.metatrans.commons.Activity_Base;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.impl.flow.IAdLoadFlow;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;
import org.metatrans.commons.IActivityInterstitial;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.questionnaire.Activity_Base_Questionnaire;
import org.metatrans.commons.questionnaire.R;
import org.metatrans.commons.questionnaire.api.IConfigurationQuestion;
import org.metatrans.commons.questionnaire.model.GameResult;

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
	
	
	protected abstract View createView();


	protected abstract IConfigurationQuestion getNextQuestion();


	protected abstract Class<? extends Activity_Base> getActivityClass_Menu();


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
		
		IConfigurationSound coloursCfg = ConfigurationUtils_Sound.getConfigByID(getUserSettings().uiColoursID);
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
	}
	
	
	@Override
	public void onPause() {
		
		//Toast_Base.showToast_InCenter(this, "onPause: timestamp_resume=" + timestamp_resume);
		
		if (!getGameData().isCountedAsCompleted()) {

			long lastPeriodInsideTheMainScreen = System.currentTimeMillis() - timestamp_resume;

			getGameData().addAccumulated_time_inmainscreen(lastPeriodInsideTheMainScreen);
		}
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();

		eventsManager.updateLastMainScreenInteraction(this, System.currentTimeMillis());
		
		super.onPause();
	}


	@Override
	protected String getInterstitialName() {
		return IAdsConfiguration.AD_ID_INTERSTITIAL1;
	}


	@Override
	protected FrameLayout getFrame() {
		return findViewById(R.id.layout_main_vertical);
	}


	public void startNewGame() {
		
		IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		
		eventsManager.handleGameEvents_OnExit(this, getGameData(), getUserSettings());
		
		timestamp_resume = System.currentTimeMillis();
		
		Application_Base.getInstance().recreateGameDataObject();
		
		nextQuestion();

		Application_Base_Ads.getInstance().openInterstitial();
	}

	
	public void answer(int _index_button) {

		System.out.println("answer._index_button=" + _index_button);

		if (!getGameData().current_answered) {

			System.out.println("answer.getGameData().current_answered=" + getGameData().current_answered);

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

		System.out.println("answer.getGameData().count_answered=" + getGameData().count_answered);

		getGameData().buttons_clicked[_index_button] = true;
	}
	
	
	private void openQuestion(IConfigurationQuestion question) {
		
		getGameData().clearForNewQuestion(question);
	}


	public void setUpLeaderboard(boolean gameCompleted) { 
		/*if (gameCompleted) {
			((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().setEnabled(true);
		} else {
			((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().setEnabled(false);
		}*/
	}
	
	
	public void nextQuestion() {
		
		IConfigurationQuestion next_question = getNextQuestion();
		
		boolean gameCompleted = next_question == null || getGameData().count_answered >= getUserSettings().countQuestions;
		
		setUpLeaderboard(gameCompleted);

		System.out.println("Activity_Question.nextQuestion: gameCompleted=" + gameCompleted);

		if (gameCompleted) {

			System.out.println("Activity_Question.nextQuestion: getGameData().isCountedAsCompleted()=" + getGameData().isCountedAsCompleted());

			if (!getGameData().isCountedAsCompleted()) {

				long lastPeriodInsideTheMainScreen = System.currentTimeMillis() - timestamp_resume;
				getGameData().addAccumulated_time_inmainscreen(lastPeriodInsideTheMainScreen);
				
				GameResult gameResult = getGameData().getGameResult();
				getBestResults().addResult(getUserSettings().modeID, gameResult);

				System.out.println("Activity_Question.nextQuestion: added gameResult=" + gameResult + ", gameResult.count_correct=" + gameResult.count_correct);

				IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
				eventsManager.handleGameEvents_OnFinish(this, getGameData(), getUserSettings(), -1);

				storeData();
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

				GameResult best_result_for_this_mode = getBestResults().getResult(getUserSettings().modeID);

				if (best_result_for_this_mode.isTheOtherBetter(getGameData().getGameResult())) {

					((Application_Base_Ads)getApplication()).getEngagementProvider().getLeaderboardsProvider().openLeaderboard(getUserSettings().modeID);
				}
				
			} else {
				
				((Application_Base_Ads)getApplication()).getEngagementProvider().getSocialProvider().connect();
			}
			
		} else {

			openQuestion(next_question);
		}

		recreateView();
	}


	private void recreateView() {

		View view = findViewById(VIEW_ID);

		FrameLayout frame = getFrame();

		frame.removeView(view);

		view.destroyDrawingCache();

		view = createView();

		frame.addView(view, 0);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		Intent i = new Intent(getApplicationContext(), getActivityClass_Menu());

		startActivity(i);
		
		return false;
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

	    super.onConfigurationChanged(newConfig);

	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
