package org.metatransapps.commons.questionnaire.utils;


import java.util.Random;


public class NumbersUtils {
	
	
	public static void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	
	public static void shuffleArray(Object[] ar) {
		shuffleArray(ar, 0);
	}
	
	
	public static void shuffleArray(Object[] ar, int start) {
		
		Random rnd = new Random();
		
		for (int i = ar.length - 1; i > start; i--) {
			
			int index = start + rnd.nextInt(i + 1 - start);
			
			// Simple swap
			Object a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
}
