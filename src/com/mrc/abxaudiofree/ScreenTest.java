/* Copyright 2014 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudiofree;

import com.mrc.util.*;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.app.*;
import android.content.*;
import android.graphics.Color;

public class ScreenTest extends Activity {
	static final int	parentCode = 1;

	static enum WhichClip { A, B, X };

	TextView	tvTrial;
	Button		butPlayA, butPlayB, butPlayX;

	AbxTest		test;
	AbxTrial	trial;
	int			posA, posB, posX;
	WhichClip	isPlaying;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screentest);
		tvTrial = (TextView)findViewById(R.id.tv_trial);
		butPlayA = (Button)findViewById(R.id.but_play_a);
		butPlayB = (Button)findViewById(R.id.but_play_b);
		butPlayX = (Button)findViewById(R.id.but_play_x);
		test = new AbxTest();
		newTrial();
	}

    protected void onDestroy() {
		super.onDestroy();
		test.close();
	}

    // Results screen uses this to tell me whether to quit or continue
	protected void onActivityResult(int pCode, int retCode, Intent data) {
		if(pCode == parentCode) {
			// call came from our child (results screen)
			if(retCode == Activity.RESULT_CANCELED) {
				// user hit "quit" - instead of back
				// so finish myself (return to main screen)
				finish();
			}
		}
	}

	// button callback: play clip A
	public void butPlayA(View pView) {
		trial.start();
		unfocusAll();
		savePos();
		colorButtons();
		if(Config.get().switchDelay > 0)
			Util.sleep(Config.get().switchDelay);
		focus(WhichClip.A);
		butPlayA.setTextColor(Color.GREEN);
	}

	// button callback: play clip B
	public void butPlayB(View pView) {
		trial.start();
		unfocusAll();
		savePos();
		colorButtons();
		if(Config.get().switchDelay > 0)
			Util.sleep(Config.get().switchDelay);
		focus(WhichClip.B);
		butPlayB.setTextColor(Color.GREEN);
	}

	// button callback: play clip X
	public void butPlayX(View pView) {
		trial.start();
		unfocusAll();
		savePos();
		colorButtons();
		if(Config.get().switchDelay > 0)
			Util.sleep(Config.get().switchDelay);
		focus(WhichClip.X);
		butPlayX.setTextColor(Color.GREEN);
	}

	// button callback: pick X=A and advance to next trial
	public void butXisA(View pView) {
		trial.pickX(trial.clipA);
		newTrial();
	}

	// button callback: pick X=B and advance to next trial
	public void butXisB(View pView) {
		trial.pickX(trial.clipB);
		newTrial();
	}

	// button callback: check rest results (so far)
	public void butResults(View pView) {
		// The result screen tells us whether to quit to continue
		startActivityForResult(new Intent(this, ScreenResults.class), parentCode);
	}

	// Save the pos of the currently playing clip (A, B or X)
	// If play is synced, do nothing.
	protected void savePos() {
		if(!Config.get().syncPlay) {
			if(isPlaying != null) {
				switch(isPlaying) {
				case A:
					posA = trial.clipA.sound.getPos();
					break;
				case B:
					posB = trial.clipB.sound.getPos();
					break;
				case X:
					if(trial.clipX == trial.clipA)
						posX = trial.clipA.sound.getPos();
					else
						posX = trial.clipB.sound.getPos();
					break;
				}
			}
		}
	}

	protected void colorButtons() {
		butPlayA.setTextColor(Color.BLACK);
		butPlayB.setTextColor(Color.BLACK);
		butPlayX.setTextColor(Color.BLUE);
	}

	protected void newTrial() {
		int		trialsComplete, trialsCorrect;
		String	msg;

		trialsComplete = test.getTrialsCompleted();
		trialsCorrect = test.getTrialsCorrect();

		trial = test.newTrial();
		isPlaying = null;
		posA = posB = posX = 0;
		if(Config.get().showResults)
			msg = "Trials: " + trialsCorrect + " for " + trialsComplete;
		else
			msg = "Trial " + (trialsComplete + 1);
		tvTrial.setText(msg);
		colorButtons();
	}

	// shift focus to (play) the given clip
	protected void focus(WhichClip wc) {
		SoundLong	s = null;
		int			pos = 0;
		switch(wc) {
		case A:	s = trial.clipA.sound;
				pos = posA;
			break;
		case B: s = trial.clipB.sound;
				pos = posB;
			break;
		case X: s = trial.clipX.sound;
				pos = posX;
			break;
		}
		if(Config.get().syncPlay)
			s.unMute();
		else {
			s.setPos(pos);
			s.play();
		}
		isPlaying = wc;
	}

	protected void unfocusAll() {
		if(Config.get().syncPlay) {
			trial.clipA.sound.mute();
			trial.clipB.sound.mute();
		}
		else {
			trial.clipA.sound.pause();
			trial.clipB.sound.pause();
		}
	}
}
