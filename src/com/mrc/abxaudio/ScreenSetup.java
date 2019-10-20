/* Copyright 2014 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudio;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.app.*;

public class ScreenSetup extends Activity  implements SeekBar.OnSeekBarChangeListener {
	CheckBox	cboxShowResults, cboxSyncPlay, cboxRandomAB;
	SeekBar		sbarSwitchDelay;
	EditText	etxtClipDir;
	TextView	tvSwitchDelay;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screensetup);
		// Get the GUI elements
		cboxShowResults = (CheckBox)findViewById(R.id.cbox_showresults);
		cboxSyncPlay = (CheckBox)findViewById(R.id.cbox_syncplay);
		cboxRandomAB = (CheckBox)findViewById(R.id.cbox_randomab);
		etxtClipDir = (EditText)findViewById(R.id.etxt_clipdir);
		tvSwitchDelay = (TextView)findViewById(R.id.tv_switchdelay);
		sbarSwitchDelay = (SeekBar)findViewById(R.id.sbar_switchdelay);
		sbarSwitchDelay.setOnSeekBarChangeListener(this);
	}

	protected void onResume() {
		super.onResume();
		setGui();
		// This prevents the keyboard from popping up when the dialog appears
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

    protected void onPause() {
		super.onPause();
		if(isFinishing())
			getGui();
	}

	// set my values from the GUI
	void getGui() {
		Config	cfg = Config.get();
		
		cfg.clipDir = etxtClipDir.getText().toString();
		cfg.showResults = cboxShowResults.isChecked();
		cfg.switchDelay = sbarSwitchDelay.getProgress();
		cfg.syncPlay = cboxSyncPlay.isChecked();
		cfg.randomAB = cboxRandomAB.isChecked();
	}

	// set the GUI from my values
	void setGui() {
		Config	cfg = Config.get();

		etxtClipDir.setText(cfg.clipDir);
		cboxShowResults.setChecked(cfg.showResults);
		sbarSwitchDelay.setProgress(cfg.switchDelay);
		tvSwitchDelay.setText(String.format("%d msecs", cfg.switchDelay));
		cboxSyncPlay.setChecked(cfg.syncPlay);
		cboxRandomAB.setChecked(cfg.randomAB);
	}

	// Seekbar change listener methods
	public void onStartTrackingTouch(SeekBar sb) { }
	public void onStopTrackingTouch(SeekBar sb) { }
	public void onProgressChanged(SeekBar sb, int val, boolean fromUser) {
		// Compute and set the value text
		tvSwitchDelay.setText(String.format("%d msecs", val));
	}
}
