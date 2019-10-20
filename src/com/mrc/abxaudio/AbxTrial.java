/* Copyright 2014 by Michael R. Clements
 * This source code is the intellectual property of the author.
 * This is not open source. All rights reserved.
*/
package com.mrc.abxaudio;

/* A single trial in an AbxTest
*/
public class AbxTrial {
	AudioClip	clipA, clipB, clipX, clipSelect;
	Boolean		isCorrect;

	public AbxTrial(AbxTest parent) {
		clipSelect = null;
		isCorrect = null;
		if(parent.testRandomizer.nextInt(2) == 0)
			clipX = parent.clipA;
		else
			clipX = parent.clipB;
		if(Config.get().randomAB) {
			// Randomize A & B (in addition to X) for each trial
			if(parent.testRandomizer.nextInt(2) == 0) {
				clipA = parent.clipA;
				clipB = parent.clipB;
			}
			else {
				clipA = parent.clipB;
				clipB = parent.clipA;
			}
		}
		else {
			clipA = parent.clipA;
			clipB = parent.clipB;
		}
	}

	public void pickX(AudioClip c) {
		clipSelect = c;
		isCorrect = (clipSelect == clipX ? Boolean.TRUE : Boolean.FALSE);
	}

	public boolean isComplete() {
		return (isCorrect != null);
	}

	public boolean isCorrect() {
		return (isCorrect == Boolean.TRUE);
	}
}
