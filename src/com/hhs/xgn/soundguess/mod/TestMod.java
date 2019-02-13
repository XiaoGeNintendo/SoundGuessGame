package com.hhs.xgn.soundguess.mod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.hhs.xgn.soundguess.game.Mod;

@com.hhs.xiaomao.modloader.Mod(modid = "test", name = "Test", version = "0")
public class TestMod extends Mod {

	
	@Override
	public void init() {
		System.out.println("Test Init Ok!");
		
	}

	@Override
	public URL getMusic(int id) {
		// TODO Auto-generated method stub
		try {
			return new URL("https://raw.githubusercontent.com/XiaoGeNintendo/public-resource-hut/master/pokemon/cries/mp3/1.ogg.mp3");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public URL getPicture(int id) {
		try {
			return new URL("http://codeforces.com/predownloaded/e4/3d/e43db219bcdb65a5cdee025665c30d55207ba2b3.png");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isIdOk(int id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getLimit() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Map<String, URL> onAcquired(int id) {
		// TODO Auto-generated method stub
		
		try{
			Map<String,URL> r=new HashMap<>();
			r.put("Osome stuff...", new URL("https://veekun.com/"));
			for(int i=0;i<10;i++){
				r.put("O"+i, new URL("https://veekun.com/"));
			}
			
			return r;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isCorrect(int id, String name) {
		// TODO Auto-generated method stub
		return name.length()==id;
	}

	@Override
	public String getModName() {
		// TODO Auto-generated method stub
		return "Test Mod";
	}

}
