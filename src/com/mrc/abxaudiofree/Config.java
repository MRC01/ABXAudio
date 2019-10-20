/* Copyright 2012 by Michael R. Clements
 * This software is open source and free to distribute and create derivative works.
*/
package com.mrc.abxaudiofree;

import java.io.*;

import com.mrc.util.Util;

/* Config settings for this app.
*/
public class Config {
	static final int		minTextSize = 8,
							maxTextSize = 40,
							levelRange = 1000;
	static final String		clipNameA = "clipA",	// field name for clip A (accessed via reflection)
							clipNameB = "clipB";	// field name for clip B (accessed via reflection)
	static Config			self;
	static {
		Util.APP_DIR = "com.mrc.abxaudio";
		self = new Config();
	}

	static Config get() {
		return self;
	}

	// prevent clients from instantiating; they must use get()
	protected Config() {
		// Can't do stuff in constructor,
		// because stuff I create might get data from me in their constructors
		// See init() below
	}

	// config settings
	public boolean		showResults,				// whether to show results during test
						syncPlay,					// whether to synchronize clips during playback
						randomAB;					// whether to randomize A & B for each trial
	public int			switchDelay;				// switch delay in msecs
	public String		clipDir;					// default file path to clip files
	public AudioClip	clipA, clipB;				// audio clips

	public void init() {
		showResults = false;
		syncPlay = true;
		randomAB = false;
		switchDelay = 0;
		clipDir = Util.getDefaultDownloadPath() + "/";
		clipA = new AudioClip();
		clipB = new AudioClip();
	}

	// persist config settings to disk
	public void save() throws IOException {
		BufferedWriter	stream;
		
		stream = Util.writeFile("config.txt");
		try {
			Util.writeBoolean(stream, showResults);
			Util.writeInt(stream, switchDelay);
			Util.writeString(stream, clipDir);
			clipA.write(stream);
			clipB.write(stream);
			Util.writeBoolean(stream, syncPlay);
			Util.writeBoolean(stream, randomAB);
		}
		finally {
			Util.closeFile(stream);
		}
	}

	// load config settings from disk
	public void load() throws IOException {
		BufferedReader	stream;

		init();
		stream = Util.readFile("config.txt");
		try {
			showResults = Util.readBoolean(stream);
			switchDelay = Util.readInt(stream);
			clipDir = Util.readString(stream);
			clipA = AudioClip.read(stream);
			clipB = AudioClip.read(stream);
			syncPlay = Util.readBoolean(stream);
			randomAB = Util.readBoolean(stream);
		}
		finally {
			Util.closeFile(stream);
		}
	}
}
