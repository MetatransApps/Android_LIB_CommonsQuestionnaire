package org.metatrans.commons.questionnaire.logic.questions;

import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.questionnaire.api.IConfigurationQuestion_TextButtons;



public abstract class CfgQuestion_Base_TextButtons extends CfgQuestion_Base implements IConfigurationQuestion_TextButtons {
	
	
	private static final long serialVersionUID = -4428528108418647396L;


	private String question;
	private int colourText;
	private int resid;


	protected CfgQuestion_Base_TextButtons(int _index_correct, Integer[] _array, String _question, int _colourText, int _resid) {

		super(_index_correct, _array);

		question = _question;
		colourText = _colourText;
		resid = _resid;
	}


	@Override
	public Object getQuestion() {

		return question;
	}


	@Override
	public int getColor_Text() {

		return colourText;
	}


	public int getResID_Question() {

		return resid;
	}


	@Override
	public int getColor_Button() {

		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(
				Application_Base.getInstance().getUserSettings().uiColoursID
		);

		return coloursCfg.getColour_Square_White();
	}


	@Override
	public long getID() {

		throw new IllegalStateException();
	}
}
