/* Copyright 2014 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudio;

import java.util.*;

/* Everything we need to know about an audio A/B/X test
*/
public class AbxTest {
	static AbxTest	current;

	AudioClip		clipA, clipB;
	List<AbxTrial>	trials;
	Random			testRandomizer;

	public static AbxTest get() {
		return current;
	}

	public AbxTest() {
		// You can set a seed here, but you shouldn't need to.
		// The default seed is supposed to be probabilistically unique and hard to guess.
		testRandomizer = new Random();
		trials = new ArrayList<AbxTrial>();
		clipA = Config.get().clipA;
		clipB = Config.get().clipB;
		current = this;
	}

	public void close() {
		clipA.close();
		clipB.close();
	}

	public AbxTrial newTrial() {
		AbxTrial	trial = new AbxTrial(this);
		trials.add(trial);
		return trial;
	}

	public int getTrialsCompleted() {
		int	rc = 0;
		for(AbxTrial trial : trials)
			if(trial.isComplete())
				rc++;
		return rc;
	}

	public int getTrialsCorrect() {
		int	rc = 0;
		for(AbxTrial trial : trials)
			if(trial.isCorrect())
				rc++;
		return rc;
	}

	// Returns the confidence percentile
	// This is 1-probability of getting the trails right by random guessing
	public double getPercentile() {
		int			trialsTotal, trialsCorrect;
		double		pTile = 0.0;

		trialsTotal = getTrialsCompleted();
		trialsCorrect = getTrialsCorrect();
		if(trialsTotal <= 0)
			return 0.0;
		for(int i = 0; i < trialsCorrect; i++)
			pTile += computePtile(trialsTotal, i);
		return pTile;
	}

	// Returns the probability of getting exactly N of 'total' guesses right
	// When each guess is 50/50 (A or B)
	public double computePtile(double total, double n) {
		return factorial(total) / (factorial(n) * factorial(total - n))
				* Math.pow(0.5, n)
				* Math.pow(0.5, total - n);
	}

	// Naive implementation of factorial, which is OK to use
	// since we have only have a handful of trials
	public double factorial(double n) {
		if(n == 0 || n == 1)
			return 1;
		else
			return n * factorial(n - 1);
	}
}
