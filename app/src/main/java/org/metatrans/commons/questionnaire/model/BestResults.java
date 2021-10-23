package org.metatrans.commons.questionnaire.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class BestResults implements Serializable {
	
	
	private static final long serialVersionUID = 5642029932644761762L;
	
	
	protected Map<Integer, GameResult> results;
	
	
	public BestResults() {
		results = new HashMap<Integer, GameResult>();
	}
	
	
	public void addResult(int modeID, GameResult new_result) {

		if (new_result == null) {

			throw new IllegalStateException("new_result=" + new_result);
		}

		GameResult old = getResult(modeID);

		if (old == null || old.isTheOtherBetter(new_result)) {

			results.put(modeID, new_result);
		}
	}
	
	
	public GameResult getResult(int modeID) {
		return results.get(modeID);
	}
}
