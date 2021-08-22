package com.apps.mobile.android.commons.questionnaire.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.apps.mobile.android.commons.cfg.colours.IConfigurationColours;
import com.apps.mobile.android.commons.model.UserSettings_Base;


public class UserSettings extends UserSettings_Base {
	
	
	private static final long serialVersionUID = -177320591967476112L;
	
	
	public int countQuestions;
	
	
	public UserSettings() {
		
		super();
		
		fixFields("constructor");
	}
	
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		
	    fixFields("writeObject");
	    
	    // default serialization 
	    oos.defaultWriteObject();
	}
	

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	    
	    // default deserialization
	    ois.defaultReadObject();
	    
	    fixFields("readObject");
	}
	
	
	private void fixFields(String op) {
		if (uiColoursID == 0) {
	    	uiColoursID 		= IConfigurationColours.CFG_COLOUR_BLUE_PETROL;
	    	System.out.println(this.getClass().getName() + " : " + op + " - updating colour id");
	    }
		if (modeID == 0) {
			modeID 		= 5;
	    	System.out.println(this.getClass().getName() + " : " + op + " - updating mode id");
	    }
		if (countQuestions == 0){
			countQuestions = 30;
			System.out.println(this.getClass().getName() + " : " + op + " - updating countQuestions");
		}
	}
}
