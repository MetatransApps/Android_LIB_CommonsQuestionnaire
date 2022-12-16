package org.metatrans.commons.questionnaire.api;


import android.graphics.Bitmap;

public interface IConfigurationQuestion_ImageQuestion extends IConfigurationQuestion {
	public int getResID_Question();
	public Bitmap getQuestion();
	public int getColor_AreaAndText();
}
