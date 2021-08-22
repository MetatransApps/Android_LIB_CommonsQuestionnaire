package com.apps.mobile.android.commons.questionnaire.main;


import com.apps.mobile.android.commons.app.Application_Base;
import com.apps.mobile.android.commons.cfg.colours.ConfigurationUtils_Colours;
import com.apps.mobile.android.commons.cfg.colours.IConfigurationColours;
import com.apps.mobile.android.commons.engagement.leaderboards.View_Achievements_And_Leaderboards_Base;
import com.apps.mobile.android.commons.questionnaire.R;
import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion_ImageButtons;
import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion_ImageQuestion;
import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion_TextButtons;
import com.apps.mobile.android.commons.questionnaire.api.IConfigurationQuestion_TextQuestion;
import com.apps.mobile.android.commons.questionnaire.model.GameData;
import com.apps.mobile.android.commons.ui.ButtonAreaClick;
import com.apps.mobile.android.commons.ui.ButtonAreaClick_Image;
import com.apps.mobile.android.commons.ui.IButtonArea;
import com.apps.mobile.android.commons.ui.TextArea;
import com.apps.mobile.android.commons.ui.utils.BitmapUtils;
import com.apps.mobile.android.commons.ui.utils.DrawingUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;;


public class View_Question extends View {
	
	
	private boolean initialized = false;
	
	private static final int MARGIN1 = 10;
	
	private RectF rectf_main;
	
	private RectF rectf_menu;
	private RectF rectf_menu_scores_all;
	private RectF rectf_menu_scores_correct;
	private RectF rectf_menu_scores_incorrect;
	private RectF rectf_menu_new;
	private RectF rectf_menu_button_menu;
	private RectF rectf_button_center;
	
	private TextArea textarea_menu_scores_all;
	private TextArea textarea_menu_scores_correct;
	private TextArea textarea_menu_scores_incorrect;
	private IButtonArea button_menu_new;
	private IButtonArea button_menu_menu;
	private IButtonArea button_center_new;
	private IButtonArea button_center_next_level;
	
	
	private RectF rectf_question;
	private RectF rectf_question_image;
	private RectF rectf_question_text;
	private TextArea textarea_question_text;
	
	
	private RectF rectf_buttons;
	private RectF[] rectangles_buttons;
	private IButtonArea[] buttons;
	
	private RectF rectf_leaderboards;
	private View_Achievements_And_Leaderboards_Base view_leaderboards;
	
	protected Paint paint_background;
	
	private Bitmap bitmap_question;
	private Rect rect_bitmap_question;
	private Bitmap[] bitmaps_buttons;
	
	private static Bitmap bitmap_replay;
	private static Bitmap bitmap_next;
	
	private IConfigurationColours coloursCfg;
	private GameData gameData;
	
	private int colour_of_rectangle_question;
	private int colour_of_question_and_buttons;
	
	
	public View_Question(Context context, IConfigurationColours _coloursCfg, GameData _gameData) {
		
		super(context);
		
		coloursCfg = _coloursCfg;
		
		gameData = _gameData;
		
		rectf_main 	= new RectF();
		
		rectf_menu = new RectF();
		
		rectf_menu_scores_all = new RectF();
		rectf_menu_scores_correct = new RectF();
		rectf_menu_scores_incorrect = new RectF();
		rectf_menu_new = new RectF();
		rectf_menu_button_menu = new RectF();
		
		rectf_buttons = new RectF();
		
		rectf_question = new RectF();
		rectf_question_image = new RectF();
		rectf_question_text = new RectF();
		
		rectf_button_center 		= new RectF();
		
		rectangles_buttons = new RectF[gameData.current_question.getAnswersCount()];
		for (int i=0; i<rectangles_buttons.length; i++) {
			rectangles_buttons[i] = new RectF();
		}
		
		bitmaps_buttons = new Bitmap[gameData.current_question.getAnswersCount()];
		for (int i=0; i<bitmaps_buttons.length; i++) {
			if (gameData.current_question instanceof IConfigurationQuestion_ImageButtons){
				bitmaps_buttons[i] = BitmapUtils.fromResource(getContext(), ((IConfigurationQuestion_ImageButtons)gameData.current_question).getResID_Answers()[i]);
				//bitmaps_buttons[i] = BitmapFactory.decodeResource(getResources(), ((IConfigurationQuestion_ImageButtons)gameData.current_question).getResID_Answers()[i]);
				//System.out.println("BUTTON IMAGE: " + ((IConfigurationQuestion_ImageButtons)gameData.current_question).getResID_Answers()[i]);
			} else {
				bitmaps_buttons[i] = BitmapUtils.textAsBitmap(((IConfigurationQuestion_TextButtons)gameData.current_question).getAnswers()[i] + "", 10, coloursCfg.getColour_Square_White());
				//System.out.println("BUTTON TEXT: " + ((IConfigurationQuestion_TextButtons)gameData.current_question).getAnswers()[i]);
			}

			//System.out.println("BUTTON: " + bitmaps_buttons[i] );
		}
		
		if (bitmap_replay == null) {
			bitmap_replay				= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_replay);
			bitmap_next					= BitmapUtils.fromResource(getContext(), R.drawable.ic_action_next);
		}
		
		if (gameData.current_question instanceof IConfigurationQuestion_ImageButtons){
			buttons = new ButtonAreaClick_Image[gameData.current_question.getAnswersCount()];
		} else {
			buttons = new ButtonAreaClick[gameData.current_question.getAnswersCount()];
		}
		
		if (gameData.current_question instanceof IConfigurationQuestion_ImageQuestion){
			bitmap_question = BitmapUtils.fromResource(getContext(), ((IConfigurationQuestion_ImageQuestion)gameData.current_question).getResID_Question());
			//bitmap_question = BitmapFactory.decodeResource(getResources(), ((IConfigurationQuestion_ImageQuestion)gameData.current_question).getResID_Question());
		} else {

			int textColour = ((IConfigurationQuestion_TextQuestion) gameData.current_question).getQuestionColour();
			if (textColour == -1) {
				textColour = coloursCfg.getColour_Square_White();	
			}
			bitmap_question = BitmapUtils.textAsBitmap(((IConfigurationQuestion_TextQuestion)gameData.current_question).getQuestion() + "", 10, textColour);
		}
		
		rect_bitmap_question = new Rect(0, 0, bitmap_question.getWidth(), bitmap_question.getHeight());
		
		
		rectf_leaderboards = new RectF();
		
		paint_background = new Paint();
		
		colour_of_question_and_buttons = Color.WHITE;//coloursCfg.getColour_Square_MarkingSelection();
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (!initialized) {
			
			initializeDimensions();
			
			initialized = true;
		}
		
		this.setMeasuredDimension( (int) (rectf_main.right - rectf_main.left), (int) (rectf_main.bottom - rectf_main.top) );
	}
	
	
	private void initializeDimensions() {
		
		int main_width = getMeasuredWidth();
		int main_height = getMeasuredHeight();
		
		int segments = 8;
		
		rectf_button_center.left = main_width / 2 - main_height / 11;//- main_width / 9;
		rectf_button_center.top = main_height / 2 - main_height / 11;
		rectf_button_center.right = main_width / 2 + main_height / 11;//+ main_width / 9;
		rectf_button_center.bottom = main_height / 2 + main_height / 11;
		
		rectf_main.left = 0;
		rectf_main.top = 0;
		rectf_main.right = main_width;
		rectf_main.bottom = main_height;
		
		rectf_menu.left = 0 + MARGIN1;
		rectf_menu.top = 0 + MARGIN1;
		rectf_menu.right = main_width - MARGIN1;
		rectf_menu.bottom = rectf_menu.top + main_height / segments - MARGIN1;
		
		int count_menu_buttons = 6;
		int count_spaces = count_menu_buttons;// + 1;
		int menu_button_width = (int) (((rectf_menu.right - rectf_menu.left) - count_spaces * MARGIN1 ) / count_menu_buttons);
		
		rectf_menu_new.left = rectf_menu.left + MARGIN1;
		rectf_menu_new.top = rectf_menu.top + MARGIN1;
		rectf_menu_new.right = rectf_menu_new.left + menu_button_width;
		rectf_menu_new.bottom = rectf_menu.bottom - MARGIN1;
		
		rectf_menu_scores_correct.left = rectf_menu_new.right + MARGIN1;
		rectf_menu_scores_correct.top = rectf_menu.top + MARGIN1;
		rectf_menu_scores_correct.right = rectf_menu_scores_correct.left + menu_button_width;
		rectf_menu_scores_correct.bottom = rectf_menu.bottom - MARGIN1;
		
		rectf_menu_scores_all.left = rectf_menu_scores_correct.right + MARGIN1;
		rectf_menu_scores_all.top = rectf_menu.top + MARGIN1;
		rectf_menu_scores_all.right = rectf_menu_scores_all.left + 2 * menu_button_width;
		rectf_menu_scores_all.bottom = rectf_menu.bottom - MARGIN1;
		
		rectf_menu_scores_incorrect.left = rectf_menu_scores_all.right + MARGIN1;
		rectf_menu_scores_incorrect.top = rectf_menu.top + MARGIN1;
		rectf_menu_scores_incorrect.right = rectf_menu_scores_incorrect.left + menu_button_width;
		rectf_menu_scores_incorrect.bottom = rectf_menu.bottom - MARGIN1;
		
		rectf_menu_button_menu.left = rectf_menu_scores_incorrect.right + MARGIN1;
		rectf_menu_button_menu.top = rectf_menu.top + MARGIN1;
		rectf_menu_button_menu.right = rectf_menu_button_menu.left + menu_button_width;
		rectf_menu_button_menu.bottom = rectf_menu.bottom - MARGIN1;
		
		
		rectf_question.left = 0 + MARGIN1;
		rectf_question.top = rectf_menu.bottom + MARGIN1;
		rectf_question.right = main_width - MARGIN1;
		rectf_question.bottom = rectf_question.top + 4 * (main_height / segments);//rectf_question.top + 4 * (main_height / 9) - MARGIN1;
		
		rectf_question_image.left = rectf_question.left + MARGIN1;
		rectf_question_image.top = rectf_question.top + MARGIN1;
		rectf_question_image.right = rectf_question.right - MARGIN1;
		rectf_question_image.bottom = rectf_question.bottom - MARGIN1;
		
		//Handle image case
		if (gameData.current_question instanceof IConfigurationQuestion_ImageQuestion){
			
			float x_image = bitmap_question.getWidth();
			float y_image = bitmap_question.getHeight();
			float x_area = rectf_question_image.width();
			float y_area = rectf_question_image.height();
			float x_rate = x_image / x_area;
			float y_rate = y_image / y_area;
			
			if (x_rate > y_rate) {
				x_image /= x_rate;
				y_image /= x_rate;
			} else {
				x_image /= y_rate;
				y_image /= y_rate;				
			}
			
			rectf_question_image.left = rectf_question_image.left + ((rectf_question_image.right - rectf_question_image.left) - x_image) / 2 ;
			rectf_question_image.top = rectf_question_image.top + ((rectf_question_image.bottom - rectf_question_image.top) - y_image) / 2 ;
			rectf_question_image.right = rectf_question_image.left + x_image;
			rectf_question_image.bottom = rectf_question_image.top + y_image;
		}
		
		rectf_question_text.left = 0 + MARGIN1 + MARGIN1;
		rectf_question_text.top = rectf_question.bottom - (rectf_question.bottom - rectf_question.top) / 2 - (rectf_question.bottom - rectf_question.top) / 4;
		rectf_question_text.right = main_width - MARGIN1 - MARGIN1;
		rectf_question_text.bottom = rectf_question.bottom - (rectf_question.bottom - rectf_question.top) / 2 + (rectf_question.bottom - rectf_question.top) / 4;
		
		rectf_buttons.left = 0 + MARGIN1;
		rectf_buttons.top = rectf_question.bottom + MARGIN1;
		rectf_buttons.right = main_width - MARGIN1;
		rectf_buttons.bottom = rectf_buttons.top + 1 * (main_height / segments);
		
		rectf_leaderboards.bottom = rectf_button_center.top - MARGIN1;
		rectf_leaderboards.top = rectf_leaderboards.bottom - menu_button_width;
		float height_leaderboards = rectf_leaderboards.bottom - rectf_leaderboards.top;
		rectf_leaderboards.left = (float) (main_width / 2 - 1.5 * height_leaderboards);
		rectf_leaderboards.right = (float) (main_width / 2 + 1.5 * height_leaderboards);
		//float width = rectf_leaderboards.right - rectf_leaderboards.left;
		//rectf_leaderboards.left = rectf_leaderboards.left - width / 3;
		//rectf_leaderboards.top = rectf_leaderboards.top;
		//rectf_leaderboards.right = rectf_leaderboards.right + width / 3;
		//rectf_leaderboards.bottom = rectf_leaderboards.bottom + (rectf_leaderboards.bottom - rectf_leaderboards.top) / 5;
		
		textarea_menu_scores_all = new TextArea(rectf_menu_scores_all, " " + getTextToDisplayOnMenuScoresAll() + " ",
				coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_MarkingSelection());
		
		textarea_menu_scores_correct = new TextArea(rectf_menu_scores_correct, " " + toDoubleDigit(gameData.count_correct) + " ",
				coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_ValidSelection());
		
		textarea_menu_scores_incorrect = new TextArea(rectf_menu_scores_incorrect, " " + toDoubleDigit(gameData.count_answered - gameData.count_correct)  + " ",
				coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_InvalidSelection());
		
		button_menu_new= new ButtonAreaClick(rectf_menu_new, "  " + getContext().getString(R.string.new_game)+ "  ",
				//coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_White(), coloursCfg.getColour_Square_ValidSelection()
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_MarkingSelection()
				);
		/*= new ButtonAreaClick_Image(rectf_menu_new, BitmapUtils.fromResource(getContext(), R.drawable.ic_new),
				coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_ValidSelection()
				);*/
		
		button_menu_menu = new ButtonAreaClick(rectf_menu_button_menu, " " + getContext().getString(R.string.button_menu)+ " ",
				//coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_White(), coloursCfg.getColour_Square_ValidSelection()
				coloursCfg.getColour_Square_ValidSelection(), coloursCfg.getColour_Square_Black(), coloursCfg.getColour_Square_MarkingSelection()
				);
		
		
		button_center_new = new ButtonAreaClick_Image(rectf_button_center, bitmap_replay,
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_MarkingSelection(), true);
		
		button_center_next_level = new ButtonAreaClick_Image(rectf_button_center, bitmap_next,
				coloursCfg.getColour_Square_ValidSelection(),
				coloursCfg.getColour_Square_MarkingSelection(), true);
		
		
		if (gameData.current_question instanceof IConfigurationQuestion_TextQuestion) {
			
			int textColour = ((IConfigurationQuestion_TextQuestion) gameData.current_question).getQuestionColour();
			int areaColour;
			if (textColour == -1) {
				textColour = Color.WHITE;//coloursCfg.getColour_Square_White();
				areaColour = coloursCfg.getColour_Square_Black();
			} else {
				if (gameData.current_question instanceof IConfigurationQuestion_TextButtons) {
					areaColour = coloursCfg.getColour_Delimiter();
				} else {
					areaColour = Color.rgb(Color.red(textColour) / 2, Color.green(textColour) / 2, Color.blue(textColour) / 2);
				}
			}
			
			textarea_question_text = new TextArea(rectf_question_text,
					((IConfigurationQuestion_TextQuestion) gameData.current_question).getQuestion(),
					areaColour,
					textColour
					);
		}
		
		
		int count_buttons = gameData.current_question.getAnswersCount();
		int width_delim = count_buttons + 1;
		
		if (count_buttons == 4) {
			
			for (int i=0; i<rectangles_buttons.length; i++) {
				
				RectF rectangle = rectangles_buttons[i];
				
				rectangle.left = (i == 0) ? (main_width / width_delim  - (count_buttons - 1) * MARGIN1) / 2 : rectangles_buttons[i - 1].right + MARGIN1;
				//rectangle.top = main_height - (rectf_main.bottom - rectf_main.top) / 3;
				rectangle.top = rectf_buttons.top + MARGIN1;
				rectangle.right = rectangle.left + main_width / width_delim;
				rectangle.bottom = rectf_buttons.bottom - MARGIN1;
			}
		} else {
			throw new IllegalStateException();
		}
		
		
		if (gameData.current_question instanceof IConfigurationQuestion_ImageButtons) {
			for (int i=0; i<buttons.length; i++) {
				int colour_mark = (i == gameData.current_question.getIndexCorrect()) ? coloursCfg.getColour_Square_ValidSelection() : coloursCfg.getColour_Square_InvalidSelection();
				buttons[i] = new ButtonAreaClick_Image(rectangles_buttons[i], bitmaps_buttons[i], coloursCfg.getColour_Square_Black(), colour_mark);
			}
		} else {
			for (int i=0; i<buttons.length; i++) {
				int colour_mark = (i == gameData.current_question.getIndexCorrect()) ? coloursCfg.getColour_Square_ValidSelection() : coloursCfg.getColour_Square_InvalidSelection();
				buttons[i] = new ButtonAreaClick(rectangles_buttons[i], "" + ((IConfigurationQuestion_TextButtons) gameData.current_question).getAnswers()[i],
						coloursCfg.getColour_Square_Black(),
						colour_of_question_and_buttons, colour_mark);
			}
		}
		
		
		if (gameData.buttons_clicked != null) {
			for (int i=0; i<gameData.buttons_clicked.length; i++) {
				if (gameData.buttons_clicked[i]) {
					buttons[i].select();
				}
			}
		}
		
		if (gameData.current_answered) {
			
			if (gameData.current_answered_correct) {
				setColourOfRectangleQuestionToValid();
			} else {
				setColourOfRectangleQuestionToInValid();
			}
			
		} else {
			setColourOfRectangleQuestionToNeutral();
		}
	}
	
	
	protected String getTextToDisplayOnMenuScoresAll() {
		return toDoubleDigit(gameData.count_answered) + " / " + ((Activity_Question)getContext()).getUserSettings().countQuestions;
	}


	private void createLeaderBoardsView() {
		
		Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(null);
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(Application_Base.getInstance().getUserSettings().uiColoursID);	
		final View _view_leaderboards = Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().getLeaderboardView(coloursCfg, getRectangle_LeaderBoards());
		
		View _view_achievements = Application_Base.getInstance().getEngagementProvider().getAchievementsProvider().getAchievementsView(coloursCfg, getRectangle_LeaderBoards());
		
		if (_view_leaderboards != null && _view_achievements != null) {
			if (_view_leaderboards != _view_achievements) {
				throw new IllegalStateException("_view_leaderboards != _view_achievements");
			}
		}
		
		((View_Achievements_And_Leaderboards_Base)_view_leaderboards).measure(0, 0);
		
		view_leaderboards = (View_Achievements_And_Leaderboards_Base) _view_leaderboards;
		
		Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().setEnabled(true);
	}
	
	
	public View_Achievements_And_Leaderboards_Base getLeaderboard() {
		return view_leaderboards;
	}
	
	
	public boolean isOverLeaderBoards(float x, float y) {
		return rectf_leaderboards.contains(x, y);
	}
	
	
	private RectF getRectangle_LeaderBoards() {
		return rectf_leaderboards;
	}
	
	
	private String toDoubleDigit(int number) {
		String result = "";
		if (number < 10) {
			result = "0" + number;
		} else {
			result += number;
		}
		return result;
	}
	
	
	public int isOverButtons_Answers(float x, float y) {
		
		for (int i=0; i<rectangles_buttons.length; i++) {
			if (rectangles_buttons[i] != null
					&& rectangles_buttons[i].contains(x, y)) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	public boolean isOverCorrectButton(float x, float y) {
		
		return rectangles_buttons[gameData.current_question.getIndexCorrect()].contains(x, y);
	}
	
	
	public void selectButton(int index) {
		buttons[index].select();
		invalidate();
	}
	

	public void deselectButton(int index) {
		buttons[index].deselect();
		invalidate();
	}
	
	
	/*public void deselectButtons() {
		for (int i=0; i<buttons.length; i++) {
			if (buttons[i] != null) {
				buttons[i].deselect();
			}
		}
		invalidate();
	}
	
	
	public void deselectButtons_NotClicked() {
		for (int i=0; i<buttons.length; i++) {
			if (buttons[i] != null) {
				if (!gameData.buttons_clicked[i]) {
					buttons[i].deselect();
				}
			}
		}
		invalidate();
	}*/

	
	public boolean isOverButton_New(float x, float y) {
		return rectf_menu_new.contains(x, y);
	}
	
	
	public void selectButton_New() {
		button_menu_new.select();
		invalidate();
	}
	

	public void deselectButton_New() {
		button_menu_new.deselect();
		invalidate();
	}

	
	public boolean isOverButton_Menu(float x, float y) {
		return rectf_menu_button_menu.contains(x, y);
	}
	
	
	public void selectButton_Menu() {
		button_menu_menu.select();
		invalidate();
	}
	

	public void deselectButton_Menu() {
		button_menu_menu.deselect();
		invalidate();
	}
	
	
	public boolean isOverCentralButton(float x, float y) {
		return rectf_button_center.contains(x, y);
	}
	
	
	public void selectCentralButton() {
		button_center_new.select();
		button_center_next_level.select();
		invalidate();
	}
	

	public void deselectCentralButton() {
		button_center_new.deselect();
		button_center_next_level.deselect();
		invalidate();
	}
	
	
	public void setColourOfRectangleQuestionToNeutral() {
		
		if (gameData.current_question instanceof IConfigurationQuestion_TextQuestion) {
			
			int textColour = ((IConfigurationQuestion_TextQuestion) gameData.current_question).getQuestionColour();
			if (textColour == -1) {
				colour_of_rectangle_question = coloursCfg.getColour_Delimiter();
			} else {
				if (gameData.current_question instanceof IConfigurationQuestion_TextButtons) {
					colour_of_rectangle_question = coloursCfg.getColour_Delimiter();
				} else {
					colour_of_rectangle_question = Color.rgb(Color.red(textColour) / 4, Color.green(textColour) / 4, Color.blue(textColour) / 4);
				}
				
			}
		} else {
			colour_of_rectangle_question = coloursCfg.getColour_Delimiter();
		}
		
		invalidate();
	}
	
	
	public void setColourOfRectangleQuestionToValid() {
		colour_of_rectangle_question = coloursCfg.getColour_Square_ValidSelection();
		if (gameData.current_question instanceof IConfigurationQuestion_TextQuestion) {
			textarea_question_text.setColour_Area(colour_of_rectangle_question);
		}
		invalidate();
	}
	

	public void setColourOfRectangleQuestionToInValid() {
		colour_of_rectangle_question = coloursCfg.getColour_Square_InvalidSelection();
		if (gameData.current_question instanceof IConfigurationQuestion_TextQuestion) {
			textarea_question_text.setColour_Area(colour_of_rectangle_question);
		}
		invalidate();
	}
	
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		paint_background.setColor(coloursCfg.getColour_Background());
		//DrawingUtils.drawRoundRectangle(canvas, paint_background, rectf_main);
		canvas.drawRect(0, 0, rectf_main.width(), rectf_main.height(), paint_background);
		
		paint_background.setColor(coloursCfg.getColour_Delimiter());
		DrawingUtils.drawRoundRectangle(canvas, paint_background, rectf_menu);
		DrawingUtils.drawRoundRectangle(canvas, paint_background, rectf_buttons);
		
		paint_background.setColor(colour_of_rectangle_question);
		DrawingUtils.drawRoundRectangle(canvas, paint_background, rectf_question);
		
		textarea_menu_scores_all.draw(canvas);
		textarea_menu_scores_correct.draw(canvas);
		textarea_menu_scores_incorrect.draw(canvas);
		
		button_menu_new.draw(canvas);
		button_menu_menu.draw(canvas);
		
		if (gameData.current_question instanceof IConfigurationQuestion_ImageQuestion) {
			
			canvas.drawBitmap(bitmap_question, rect_bitmap_question, rectf_question_image, paint_background);
			
		} else {
			
			textarea_question_text.draw(canvas);
		}
		
		for (int i=0; i<buttons.length; i++) {
			if (buttons[i] != null) {
				buttons[i].draw(canvas);
			}
		}
		
		if (gameData.isCountedAsCompleted()) {
			
			if (gameData.count_correct == gameData.count_answered) {
				button_center_next_level.draw(canvas);
			} else {
				button_center_new.draw(canvas);
			}
			
			if (view_leaderboards == null) {
				createLeaderBoardsView();
			}
			view_leaderboards.draw(canvas);
				
		} else {
			
			if (view_leaderboards != null) {
				Application_Base.getInstance().getEngagementProvider().getLeaderboardsProvider().detachLeaderboardView(null);
				view_leaderboards = null;
			}
		}
	}
}
