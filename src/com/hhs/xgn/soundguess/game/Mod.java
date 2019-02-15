package com.hhs.xgn.soundguess.game;

import java.net.URL;
import java.util.Map;

/**
 * The abstract class for all mods. <br/>
 * For mod developers please write an annotation containing the information <br/>
 * and extend this class <br/>
 * @author XGN
 *
 */
public abstract class Mod {
	
	/**
	 * The build version your mod is using. It should match the game's version.
	 */
	public abstract int getBuildVersion();
	
	public abstract String getModName();
	
	/**
	 * This function is called when the mod loader finds your mod.
	 */
	public abstract void init();
	
	/**
	 * This function is called when the Sound Guess feature is triggered. <br/>
	 * You need to return a URL to the sound of the creature by the given id.
	 * @param id
	 * @return
	 */
	public abstract URL getMusic(int id);
	
	
	/**
	 * This function is called when the Graph Guess feature is triggered. <br/>
	 * You need to return a URL to the picture of the creature by the given id.
	 * @param id
	 * @return
	 */
	public abstract URL getPicture(int id);
	
	/**
	 * This function is to check whether the given id is a valid id in your series.
	 * @param id
	 * @return
	 */
	public abstract boolean isIdOk(int id);
	
	/**
	 * This function is to return an integer to tell the game what is the r-boundary to generate next id <br/>
	 * That's, the number will be chosen from [1,getLimit()]. <br/>
	 * @return
	 */
	public abstract int getLimit();
	
	/**
	 * This function is called for a user successfully guessed the id creature <br/>
	 * You need to return rewards. The reward string is a identifier. <br/>
	 * Start with a "M" stands for a music. <br/>
	 * Start with a "P" stands for a picture. <br/>
	 * Start with a "O" stands for other stuff. <br/>
	 * For example, "Mpikachu" must correspond with a music URL that links to a sound somehow related to "pikachu" <br/>
	 * 
	 * @param id
	 * @return
	 */
	public abstract Map<String,URL> onAcquired(int id);
	
	/**
	 * The user guessed the name, return whether it is right or not. <br/>
	 * The first letter in the string is verdict, then followed by the comment <br/>
	 * The verdict could be "C" for correct, "W" for wrong answer, "U" for checker internal error <br/>
	 * For example, "CYou found my name!" will display a line of "You found my name!" and is considered a correct guess <br/>
	 * @param id
	 * @param name
	 * @return
	 */
	public abstract String isCorrect(int id,String name);
	
	public boolean supportVoice(){
		return true;
	}
	
	public boolean supportPicture(){
		return true;
	}
}
