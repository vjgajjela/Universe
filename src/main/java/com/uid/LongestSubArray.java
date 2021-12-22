package com.uid;

import java.util.ArrayList;

public class LongestSubArray {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] array = { 10, 1, 2, 5, 0, 5, 7, 9, 9, 5, 6 };
		System.out.println(longestSubArray(array));
	}

	/*
	 * 
	 * (10,1,2,5,0,5,7,9,9,10,6) = 11
	 * 
	 * 
	 * - half - find how many element left before moving ahead - a variable to
	 * save current sub array size
	 * 
	 * 
	 * 
	 */

	public static ArrayList<ArrayList<Integer>> longestSubArray(int[] array) {
		ArrayList<ArrayList<Integer>> listOfSubArray = new ArrayList<>();
		ArrayList<Integer> currentSubArray = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			if (i == array.length - 1) {
				currentSubArray.add(array[i]);
				listOfSubArray.add(new ArrayList<>(currentSubArray));
				break;
			}
			if (array[i] < array[i + 1]) {
				currentSubArray.add(array[i]);
			} else {
				currentSubArray.add(array[i]);
				listOfSubArray.add(new ArrayList<>(currentSubArray));
				currentSubArray = new ArrayList<>();
			}
		}
		return listOfSubArray;
	}

}
