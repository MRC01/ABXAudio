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

import com.lamerman.*;

// Load a clip, test play it, and adjust volume
public class ScreenClip extends Activity implements SeekBar.OnSeekBarChangeListener {
	static final int	parentCode = 1;

	AudioClip	clip;
	EditText	etxtClipPath;
	SeekBar		sbarClipLevel;
	TextView	tvClipLevel;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screenclip);
		// Whoever sent us this Intent injected a string telling us which clip to present
		clip = getClip(getIntent().getExtras().getString("whichClip"));
		// Get the GUI elements
		etxtClipPath = (EditText)findViewById(R.id.etxt_clippath);
		tvClipLevel = (TextView)findViewById(R.id.tv_cliplevel);
		sbarClipLevel = (SeekBar)findViewById(R.id.sbar_cliplevel);
		sbarClipLevel.setOnSeekBarChangeListener(this);
	}

    protected void onResume() {
		super.onResume();
		setGui();
		// This prevents the keyboard from popping up when the dialog appears
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

    public void onBackPressed() {
		if(!init())
			warnClipNotSet();
		super.onBackPressed();
    }

    // Results screen uses this to tell me whether to quit or continue
	protected void onActivityResult(int pCode, int retCode, Intent data) {
		if(pCode == parentCode) {
            if (retCode == Activity.RESULT_OK) {
                clip.fName = data.getStringExtra(FileDialog.RESULT_PATH);
            }
            else
            	warnClipNotSet();
		}
	}

    // Gets settings and initializes the audio clip.
    // If returns true, it's successful and clip can be played
    protected boolean init() {
		getGui();
		try {
			clip.close();
			clip.init();
			clip.sound.stop();
			return true;
		}
		catch(Exception e) {
			return false;
		}
    }

    // button callback
	public void butPlay(View pView) {
		if(init())
			clip.sound.play();
		else {
			Util.dialogMessage(this, "Bad Clip", "Could not play clip: " + clip.fName);
		}
	}

    // button callback
	public void butSetPath(View pView) {
		// invoke the file selection activity
		Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, clip.fName);
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
        startActivityForResult(intent, parentCode);
	}

    // button callback
	public void butVolume(View pView) {
		sbarClipLevel.setProgress(Config.levelRange);
	}

    // button callback
	public void butSetDefaultPath(View pView) {
		clip.fName = Config.get().clipDir;
		setGui();
	}

	// set the GUI to match my values
	void setGui() {
		etxtClipPath.setText(clip.fName);
		sbarClipLevel.setProgress((int)(clip.vLevel * Config.levelRange));
	}

	// set my values from the GUI
	void getGui() {
		int		idx;

		clip.fName = etxtClipPath.getText().toString();
		idx = sbarClipLevel.getProgress();
		clip.vLevel = (float)((double)idx / (double)Config.levelRange);
	}
	
	// Get the audio clip object
	protected AudioClip getClip(String fieldName) {
		try {
			clip = (AudioClip)Config.class.getField(fieldName).get(Config.get());
		}
		catch(Exception e) {
			// This should never happen!
			clip = null;
		}
		return clip;
	}

	// Seekbar change listener methods
	public void onStartTrackingTouch(SeekBar sb) { }
	public void onStopTrackingTouch(SeekBar sb) { }
	public void onProgressChanged(SeekBar sb, int val, boolean fromUser) {
		// Compute and set the value text
		tvClipLevel.setText(levelStringFromVal(val));
	}

	protected String levelStringFromVal(int val) {
		double	valPct, valDb;

		valPct = (double)val / (double)Config.levelRange;
		valDb = Math.log10(valPct) * 20.0;
		return String.format("%.1f Db  %.0f %c", valDb, valPct * 100.0, '%');
	}

	protected void warnClipNotSet() {
		// Warn user this is an invalid clip, but let him exit anyway
		// Otherwise (if we don't let him exit), he could get stuck on this screen
		Toast.makeText(this, "Clip was not set!", Toast.LENGTH_SHORT).show();
	}
}
