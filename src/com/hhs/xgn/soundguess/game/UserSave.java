package com.hhs.xgn.soundguess.game;

import java.util.*;

import java.net.URL;

public class UserSave {
	public String username;
	public Map<String,Map<String,URL>> acquired=new HashMap<>();
	public int correctGuessByVoice;
	public int correctGuessByPicture;
	public Map<String,HashSet<String>> named=new HashMap<>();
	
	
}
