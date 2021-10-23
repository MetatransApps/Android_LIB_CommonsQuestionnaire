package org.metatrans.commons.questionnaire.model;


import java.io.Serializable;


public class GameResult implements Serializable {
	
	
	private static final long serialVersionUID = 2371327761714677356L;
	
	
	public int count_correct;
	public int count_incorrect;
	public long time;
	
	
	public boolean isTheOtherBetter(GameResult other) {
		
		if (other.count_correct > count_correct) {
			return true;
		}
		
		if (other.count_correct == count_correct) {
			if (other.time <= time) {
				return true;
			}
		}
		
		return false;
	}
}
