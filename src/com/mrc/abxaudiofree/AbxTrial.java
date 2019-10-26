/* Copyright 2014 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudiofree;

import com.mrc.util.SoundLong;

/* A single trial in an AbxTest
*/
public class AbxTrial {
	AudioClip	clipA, clipB, clipX, clipSelect;
	Boolean		isCorrect;
	boolean		isStarted;

	public AbxTrial(AbxTest parent) {
		clipSelect = null;
		isCorrect = null;
		isStarted = false;
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
			// Clips A and B don't change through the test
			clipA = parent.clipA;
			clipB = parent.clipB;
		}
		initSound(clipA.sound);
		initSound(clipB.sound);
	}

	// Stop all sounds and reset them to the start position
	protected void initSound(SoundLong s) {
		s.stop();
		if(Config.get().syncPlay)
			s.mute();
		else
			s.unMute();
	}

	// Start this trial; if already started, do nothing.
	// If synced, both clips will already be muted, so start them playing (while still muted).
	public void start() {
		if(isStarted)
			return;
		isStarted = true;
		if(Config.get().syncPlay) {
			clipA.sound.play();
			clipB.sound.play();
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

	public boolean isStarted() {
		return isStarted;
	}
}
