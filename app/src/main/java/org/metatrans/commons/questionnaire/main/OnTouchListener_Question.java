package org.metatrans.commons.questionnaire.main;


import org.metatrans.commons.Alerts_Base;
import org.metatrans.commons.questionnaire.R;
import org.metatrans.commons.questionnaire.model.GameData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class OnTouchListener_Question implements OnTouchListener {
	
	
	private View_Question view;
	
	private MediaPlayer audio_player_correct;
	private MediaPlayer audio_player_incorrect;
	
	
	public OnTouchListener_Question(View_Question _view) {
		
		view = _view;
		
		audio_player_correct = MediaPlayer.create(view.getContext(), R.raw.sound_correct);
		audio_player_correct.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		audio_player_incorrect = MediaPlayer.create(view.getContext(), R.raw.sound_incorrect);
		audio_player_incorrect.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}
	
	
	private GameData getGameData() {
		return (GameData) ((Activity_Question)view.getContext()).getGameData();
	}
	
	
	@Override
	public boolean onTouch(View _view, MotionEvent event) {
		
		synchronized (view) {
			
			int action = event.getAction();
			
			if (action == MotionEvent.ACTION_DOWN) {

				processEvent_DOWN(event);

			} else if (action == MotionEvent.ACTION_MOVE) {
				
				processEvent_MOVE(event);
				
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {

				processEvent_UP(event);

			}
		}
		
		view.invalidate();
		
		return true;
	}
	
	
	private void processEvent_DOWN(MotionEvent event) {
		
		
		float x = event.getX();
		float y = event.getY();
		
		
		if (getGameData().isCountedAsCompleted()) {
			if (view.isOverLeaderBoards(x, y)) {
				if (view.getLeaderboard() != null) {
					view.getLeaderboard().onTouch(view, event);
					return;
				}
			}
		}
		
		
		if (view.isOverButton_New(x, y)) {
			
			view.selectButton_New();
			
		} else if (view.isOverButton_Menu(x, y)) {
			
			view.selectButton_Menu();
		}
		
		
		int index = view.isOverButtons_Answers(x, y);
		
		if (index > -1) {
			
			if (!getGameData().buttons_clicked[index]) { 
			
				view.selectButton(index);
				
				((Activity_Question)view.getContext()).answer(index);
				
				if (getGameData().isCorrectAnswer(index)) {

					view.setColourOfRectangleQuestionToValid();
					
					audio_player_correct.start();
					
				} else {

					view.setColourOfRectangleQuestionToInValid();
					
					audio_player_incorrect.start();
				}
			}
		}
		
		if (getGameData().isCountedAsCompleted()) {

			if (view.isOverCentralButton(x, y)) {

				view.selectCentralButton();
			}
		}
	}
	
	
	private void processEvent_MOVE(MotionEvent event) {
		
		
		float x = event.getX();
		float y = event.getY();
		
		
		if (getGameData().isCountedAsCompleted()) {

			if (view.isOverLeaderBoards(x, y)) {

				if (view.getLeaderboard() != null) {

					view.getLeaderboard().onTouch(view, event);

					return;
				}
			}
		}
		
		
		if (view.isOverButton_New(x, y)) {

			view.selectButton_New();

		} else {

			view.deselectButton_New();
		}
		
		if (view.isOverButton_Menu(x, y)) {

			view.selectButton_Menu();

		} else {
			view.deselectButton_Menu();
		}
		
			
		int index = view.isOverButtons_Answers(x, y);

		if (index > -1) {

			//Do nothing

		} else {

			if (!getGameData().isCountedAsCompleted()) {

				if (((Activity_Question)view.getContext()).allowedProcceedingWithWrongAnswer()) {

					if (getGameData().buttons_clicked[0] || getGameData().buttons_clicked[1] || getGameData().buttons_clicked[2] || getGameData().buttons_clicked[3]) {

						((Activity_Question)view.getContext()).nextQuestion();
					}

				} else {

					if (getGameData().buttons_clicked[getGameData().current_question.getIndexCorrect()]) {

						((Activity_Question)view.getContext()).nextQuestion();
					}
				}
			}
		}
		
		
		if (getGameData().isCountedAsCompleted()) {

			if (view.isOverCentralButton(x, y)) {

				view.selectCentralButton();

			} else {

				view.deselectCentralButton();

			}
		}
	}
	
	
	private void processEvent_UP(MotionEvent event) {
		
		
		float x = event.getX();
		float y = event.getY();
		
		
		if (getGameData().isCountedAsCompleted()) {

			if (view.isOverLeaderBoards(x, y)) {

				if (view.getLeaderboard() != null) {

					view.getLeaderboard().onTouch(view, event);

					return;
				}
			}
		}
		
		
		if (view.isOverButton_New(x, y)) {
			
			view.deselectButton_New();
			
			AlertDialog.Builder adb = Alerts_Base.createAlertDialog_LoseGame(view.getContext(),

					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							((Activity_Question)view.getContext()).startNewGame();
							
						}
					});
			
			adb.show();
			
		} else if (view.isOverButton_Menu(x, y)) {
			
			view.deselectButton_Menu();
			
			Intent i = new Intent(view.getContext(), ((Activity_Question)view.getContext()).getActivityClass_Menu());

			view.getContext().startActivity(i);
		}
		
		
		int index = view.isOverButtons_Answers(x, y);

		if (index > -1) {

			if (((Activity_Question)view.getContext()).allowedProcceedingWithWrongAnswer()) {

				((Activity_Question)view.getContext()).nextQuestion();

			} else {

				if (getGameData().isCorrectAnswer(index)) {

					if (getGameData().buttons_clicked[getGameData().current_question.getIndexCorrect()]) {

						((Activity_Question)view.getContext()).nextQuestion();
					}
				}
			}
		}
		
		
		view.deselectCentralButton();
		
		if (getGameData().isCountedAsCompleted() && view.isOverCentralButton(x, y)) {
			
			if (getGameData().count_correct == getGameData().count_answered) {

				//Increase level
				((Activity_Question)view.getContext()).setNextLevel();
			}
			
			((Activity_Question)view.getContext()).startNewGame();
		}
	}
}
