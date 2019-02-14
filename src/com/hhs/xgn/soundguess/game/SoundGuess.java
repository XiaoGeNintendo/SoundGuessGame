package com.hhs.xgn.soundguess.game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.gson.Gson;
import com.hhs.xiaomao.modloader.MainLoader;

import javazoom.jl.player.Player;

public class SoundGuess {
	
	public final static int build=2;
	
	MenuBar menu;
	Menu guess,inventory,setting,about;
	MenuItem soundguess,graphguess;
	MenuItem openinv;
	MenuItem setspe;
	MenuItem aboutdia,exit;
	JLabel text;
	
	JFrame win,loading;
	
	UserSave save;
	
	public static void main(String[] args) {
		SoundGuess x=new SoundGuess();
		x.start();
	}


	public void drawLoading(){
		loading=new JFrame("Loading mods");
		JLabel jl=new JLabel("Loading mods, please wait...");
		loading.add(jl);
		loading.pack();
		loading.setVisible(true);
		loading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	public void start(){
		
		File f=new File("mods");
		if(!f.exists()){
			f.mkdirs();
		}
		
		File savef=new File("save.json");
		if(!savef.exists()){
			try {
				savef.createNewFile();
			} catch (IOException e) {
				msgBox("Could not create save file!","Error");
				return;
			}
			
			String inp=JOptionPane.showInputDialog(null, "Welcome to Sound Guess Game!\nWhat's your name?", "Welcome", JOptionPane.QUESTION_MESSAGE);
			save=new UserSave();
			save.username=inp;
			
			saveData();
			
		}
		
		loadData();
		
		drawLoading();
		
		MainLoader.load();
		loading.dispose();
		
		if(MainLoader.mods.size()==0){
			msgBox("No mods loaded!","Error");
			System.exit(1);
		}
		
		drawMainWindow();	
	}

	
	public void loadData(){
		try{
			Gson gs=new Gson();
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File("save.json"))));
			String tot="",lne;
			while((lne=br.readLine())!=null){
				tot+=lne+"\n";
			}
			br.close();
			
			save=gs.fromJson(tot, UserSave.class);
		}catch(Exception e){
			msgBox("Fatal Error: Could not load save data!\n"+e,"Error");
			System.exit(1);
		}
	}
	public void saveData(){
		try{
			Gson gs=new Gson();
			String json=gs.toJson(save);
			PrintWriter pw=new PrintWriter("save.json");
			pw.println(json);
			pw.close();
		}catch(Exception e){
			msgBox("Could not save file!","Error");
		}
	}
	
	public void msgBox(String msg,String title){
		JOptionPane.showMessageDialog(null, msg,title,JOptionPane.INFORMATION_MESSAGE);
	}

	public void drawMainWindow() {
		win=new JFrame("Sound Guess!");
		
		text=new JLabel("Welcome to Sound Guess Game!");
		
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setVerticalAlignment(SwingConstants.CENTER);
		
		win.add(text);
		
		menu=new MenuBar();
		
		guess=new Menu("Guess");
		inventory=new Menu("Inventory");
		setting=new Menu("Mods");
		about=new Menu("About");
		
		soundguess=new MenuItem("Guess by voice!");
		graphguess=new MenuItem("Guess by picture!");
		
		openinv=new MenuItem(save.username+"'s inventory");
		
		
		setspe=new MenuItem("Mods");
		
		aboutdia=new MenuItem("About");
		exit=new MenuItem("Exit");
		
		guess.add(soundguess);
		guess.add(graphguess);
		
		inventory.add(openinv);
		
		setting.add(setspe);
		
		about.add(aboutdia);
		about.add(exit);
		
		menu.add(guess);
		menu.add(inventory);
		menu.add(setting);
		menu.add(about);
		
		
		win.setMenuBar(menu);
		
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setSize(600, 150);
		win.setVisible(true);
		
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Guessing Game By XGN from HHS 2019");
			}
		});
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
		setspe.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				drawSetting();
			}
		});
		
		soundguess.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				soundGuess();
			}
		});
		
		graphguess.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphGuess();
			}
		});
		
		openinv.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawInventory();
			}
			
		});
	}
	
	public void drawInventory(){
		JFrame inv=new JFrame(save.username+"'s Inventory");
		inv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inv.setLayout(new GridLayout(1,4));
		
		JList<String> mods=new JList<>();
		mods.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		mods.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane jsp1=new JScrollPane(mods);
		
		JList<String> creatures=new JList<>();
		creatures.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		creatures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane jsp2=new JScrollPane(creatures);
		
		JList<String> resources=new JList<>();
		resources.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		resources.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane jsp3=new JScrollPane(resources);
		
		JTextArea display=new JTextArea();
		display.setEditable(false);
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		display.setText("Welcome back,"+save.username+"!\nSuccessful Guess By Voice:"+save.correctGuessByVoice+"\nSuccessful Guess By Picture:"+save.correctGuessByPicture);
		
		JScrollPane jsp4=new JScrollPane(display);
		
		inv.add(jsp1);
		inv.add(jsp2);
		inv.add(jsp3);
		inv.add(jsp4);

		inv.setSize(700, 300);
		inv.setVisible(true);
		
		mods.setListData(getLayer(0,""));
		
		mods.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()){
					return;
				}
				if(mods.isSelectionEmpty()){
					return;
				}
				creatures.setListData(getLayer(1,mods.getSelectedValue()));
				resources.setListData(new Vector<>());
			}
		});
		
		creatures.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()){
					return;
				}
				if(creatures.isSelectionEmpty()){
					return;
				}
				resources.setListData(getLayer(2,creatures.getSelectedValue()));
				
				String peek=creatures.getSelectedValue();
				
				display.setText("Creature "+peek+"\n"+
						"Loot Count:"+save.acquired.get(peek).size()+"\n"+
						"All known names:\n"+
						save.named.getOrDefault(peek, new HashSet<>()));
				
				
			}
		});
		
		
		resources.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()){
					return;
				}
				if(resources.isSelectionEmpty()){
					return;
				}
				
				String v=resources.getSelectedValue();
				
				for(Entry<String,URL> entry:save.acquired.get(creatures.getSelectedValue()).entrySet()){
					if(entry.getKey().equals("M"+v)){
						//Music
						drawMusicOpen(entry.getValue());
						continue;
					}
					if(entry.getKey().equals("P"+v)){
						//Picture
						drawPictureOpen(entry.getValue());
						continue;
					}
					if(entry.getKey().equals("O"+v)){
						//Normal URL
						drawURLOpen(entry.getValue());
						continue;
					}
					//??????
//					System.out.println("[WARNING]Unknown key:"+entry.getKey());
				}
			}
		});
	}
	
	public void drawPictureOpen(URL target){
		
		System.out.println("Loading frame for "+target);;
		
		JFrame url=new JFrame("Picture Viewer");
		url.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		url.setLayout(new BorderLayout());
		
		JLabel text=new JLabel("Picture Link:"+target);
		url.add("North",text);
		
		JLabel picture=new JLabel();
		picture.setHorizontalAlignment(SwingConstants.CENTER);
		picture.setText("Loading picture view...");
		
		JScrollPane jsp=new JScrollPane(picture);
		url.add("Center",jsp);
		
		JButton download=new JButton("Download");
		url.add("South",download);
		
		download.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				downloadFile(target);
			}
		});
		url.setSize(300,300);
		url.setVisible(true);
		
		Thread load=new Thread(){
			public void run(){
				picture.setIcon(new ImageIcon(target));
				picture.setText("");
			}
		};
		
		load.start();
	}
	
	public void drawMusicOpen(URL target){
		JFrame url=new JFrame("Music Viewer");
		url.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		url.setLayout(new GridLayout(3, 1));
		
		
		JLabel text=new JLabel("Music Link:"+target);
		url.add(text);
		
		JButton open=new JButton("Play");
		url.add(open);
		
		class MyActionListener implements ActionListener{
			Player player;
			boolean playing=false;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(!target.toString().endsWith(".mp3")){
						int res=JOptionPane.showConfirmDialog(null, "We support only mp3 playing\nBut this file doesn't look like a mp3 file.\nWill you continue to play it?\nYou can download and enjoy at any time.", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						
						if(res!=JOptionPane.YES_OPTION){
							return;
						}
					}
					if(player!=null && !player.isComplete()){
						player.close();
						player=null;
					}else{
						
						open.setText("Loading...");
						
						Thread t=new Thread(){
							public void run(){
								
								try{
									
									InputStream is=target.openStream();
									player=new Player(is);
									
									open.setText("Play Sound");
									
									player.play();
								}catch(Exception e){
									e.printStackTrace();
									msgBox("Error playing the sound :(","Error");
								}
							}
						};
						
						t.start();
					}
					playing=!playing;
					
				}catch(Exception er){
					er.printStackTrace();
					msgBox("Error playing the sound :(","Error");
				}
			}
		}
		
		MyActionListener al=new MyActionListener();
		open.addActionListener(al);
		
		url.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(al.player!=null){
					al.player.close();
				}
				super.windowClosing(e);
			}
		});
		
		
		JButton download=new JButton("Download");
		url.add(download);
		
		download.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				downloadFile(target);
			}
		});
		url.pack();
		url.setVisible(true);
	}
	
	
	public void drawURLOpen(URL target){
		JFrame url=new JFrame("URL Viewer");
		url.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		url.setLayout(new GridLayout(3, 1));
		
		JLabel text=new JLabel("Link:"+target);
		url.add(text);
		
		JButton open=new JButton("Open");
		url.add(open);
		
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!Desktop.isDesktopSupported()){
					msgBox("Sorry. Your computer doesn't support opening :(","Sad");
					return;
				}
				
				try{
					Desktop.getDesktop().browse(target.toURI());
				}catch(Exception ex){
					msgBox("Error occurred during opening :(", "Error");
				}
			}
		});
		
		JButton download=new JButton("Download");
		url.add(download);
		
		download.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				downloadFile(target);
			}
		});
		url.pack();
		url.setVisible(true);
	}
	
	public void downloadFile(URL target){
		JFileChooser jfc=new JFileChooser(new File("save.json").getParentFile());
		jfc.setDialogTitle("Position to save");
		int state=jfc.showSaveDialog(null);
		if(state==JFileChooser.APPROVE_OPTION){
			
			try{
				File f=jfc.getSelectedFile();
			
				JFrame download=new JFrame("Download");
				
				download.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				JLabel jl=new JLabel("Waiting..");
				
				download.add(jl);
				
				download.setSize(500, 100);
				download.setVisible(true);
				
				
				
				Thread t=new Thread(){
					
					public void run(){
						try{
							BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(f));
							BufferedInputStream bis=new BufferedInputStream(target.openStream());
							
							byte[] data=new byte[16384];
							
							long sum=0,tmpsum=0;
							
							long start=System.currentTimeMillis();
							
							long bck=start;
							
							while(true){
								
								
								int read=bis.read(data);
	
								if(read==-1){
									break;
								}
								
								bos.write(data,0,read);
								
								sum+=read;
								
								tmpsum+=read;
								
								if(System.currentTimeMillis()-bck>=1000){
									jl.setText("Downloaded:"+sum+"bytes. Speed:"+getSpeed(tmpsum));
									tmpsum=0;
									bck=System.currentTimeMillis();
								}
								
								if(!download.isVisible()){
									System.out.println("Download stopped because window closed");
									
									break;
								}
							}
							
							bis.close();
							bos.close();
							long delta=System.currentTimeMillis()-start;
							jl.setText("Done:"+sum+"bytes in total.Cost "+delta+" ms. Speed:"+getSpeed(sum*1000/delta));
							
							System.out.println("Done downloading");
						}catch(Exception e){
							e.printStackTrace();
							msgBox("Failed to download:"+e,"Error");
							
							download.dispose();
						}
					}
				};
				
				t.start();
			}catch(Exception e){
				msgBox("Download Error:"+e,"Error");
			}
		}else{
			msgBox("Cancelled or error dismissed","End download");
		}
	}
	
	public String getSpeed(long inbyte){
		if(inbyte<1024){
			return inbyte+"b/s";
		}
		if(inbyte/1024<1024){
			return inbyte/1024f+"KB/s";
		}
		if(inbyte/1024/1024<1024){
			return inbyte/1048576f+"MB/s";
		}
		return inbyte/1073741824f+"GB/s";
	}
	@Deprecated
	public String getInformation(int layer,String peek){
		switch(layer){
			case 0:
				return "Select a mod you have";
			case 1:
				for(Mod md:MainLoader.mods){
					if(md.getModName().equals(peek)){
						return "Module name:"+md.getModName()+"\n"+(md.supportVoice()?"Support Voice Guessing":"")+"\n"+(md.supportPicture()?"Support Picture Guessing":"");
					}
				}
				return "Unknown Module Name "+peek;
			case 2:
				for(Entry<String,Map<String,URL>> entry:save.acquired.entrySet()){
					if(entry.getKey().equals(peek)){
						return "Creature "+peek+"\n"+
								"Loot Count:"+entry.getValue().size()+"\n"+
								"All known names:\n"+
								save.named.getOrDefault(peek, new HashSet<>());
								
					}
				}
				
				return "Unknown Creature Name "+peek;
			case 3:
				return peek;
		}
		
		return "Unknown Layer";
	}
	
	public Vector<String> getLayer(int layer,String lastSelection){
		Vector<String> vs=new Vector<String>();
		
		switch(layer){
			case 0:
				for(Mod x:MainLoader.mods){
					vs.add(x.getModName());
				}
				break;
			case 1:
				for(Entry<String,Map<String,URL>> entry:save.acquired.entrySet()){
					if(entry.getKey().startsWith(lastSelection+":")){
						vs.addElement(entry.getKey());
					}
				}
				break;
			case 2:
				for(Entry<String,Map<String,URL>> entry:save.acquired.entrySet()){
					if(entry.getKey().equals(lastSelection)){
						for(Entry<String,URL> ert:entry.getValue().entrySet()){
							vs.add(ert.getKey().substring(1));
						}
					}
				}
				break;
		}
		
		return vs;
	}
	
	public void soundGuess(){
		ArrayList<Mod> ok=new ArrayList<>();
		for(Mod md:MainLoader.mods){
			if(md.supportVoice()){
				ok.add(md);
			}
		}
		
		if(ok.size()==0){
			msgBox("No mods support guessing by voice!","Error");
			return;
		}
		
		Random r=new Random(System.currentTimeMillis());
		
		Mod choose=ok.get(r.nextInt(ok.size())); //The chosen mod
		
		int toGuessId=0;
		int guessTime=0;
		while(true){
			toGuessId=r.nextInt(choose.getLimit())+1;
			if(choose.isIdOk(toGuessId)){
				break;
			}else{
				guessTime++;
				if(guessTime>=1000){
					msgBox("No wild creatures passed by :(. Try again later","Oops...");
					return;
				}
			}
		}
		
		drawGuessVoiceWindow(choose,toGuessId);
	}
	
	public void graphGuess(){
		ArrayList<Mod> ok=new ArrayList<>();
		for(Mod md:MainLoader.mods){
			if(md.supportPicture()){
				ok.add(md);
			}
		}
		
		if(ok.size()==0){
			msgBox("No mods support guessing by picture!","Error");
			return;
		}
		
		Random r=new Random(System.currentTimeMillis());
		
		Mod choose=ok.get(r.nextInt(ok.size())); //The chosen mod
		
		int toGuessId=0;
		int guessTime=0;
		while(true){
			toGuessId=r.nextInt(choose.getLimit())+1;
			if(choose.isIdOk(toGuessId)){
				break;
			}else{
				guessTime++;
				if(guessTime>=1000){
					msgBox("No wild creatures passed by :(. Try again later","Oops...");
					return;
				}
			}
		}
		
		drawGuessPictureWindow(choose,toGuessId);
	}
	
	public void drawGuessPictureWindow(Mod mod,int guess){
		System.out.println("GuessId="+mod.getModName()+":"+guess);
		JFrame voice=new JFrame("Picture Guess");
		
		voice.setLayout(new GridLayout(5, 1));
		voice.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel text=new JLabel("Watch and write its name!Resize the window if it isn't fully shown!");
		voice.add(text);
		
		JLabel play=new JLabel("Loading...");
		play.setHorizontalAlignment(SwingConstants.CENTER);
		play.setVerticalAlignment(SwingConstants.TOP);
		
		Thread t=new Thread(){
			public void run() {
				play.setIcon(new ImageIcon(mod.getPicture(guess)));
				play.setText("");
			};
		};
		t.start();
		
		JScrollPane jsp=new JScrollPane(play);
		voice.add(jsp);
		
		JTextField write=new JTextField("");
		voice.add(write);
		
		JButton yes=new JButton("Confirm!");
		voice.add(yes);
		
		JButton no=new JButton("Cancel");
		voice.add(no);
		
		no.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				voice.dispose();
			}
		});
		
		yes.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String ret=mod.isCorrect(guess, write.getText().trim());
				
				if(ret.startsWith("C")){
					
					if(save.acquired.containsKey(mod.getModName()+":"+guess)){
						msgBox("You caught a "+write.getText()+" again!\nComment:"+ret.substring(1),"Wow!");
						save.named.get(mod.getModName()+":"+guess).add(write.getText());
						
					}else{
						String msg="The first time,you caught a "+write.getText()+"!\nYou get:";
						Map<String,URL> get=mod.onAcquired(guess);
						
						int lines=0;
						
						for(Entry<String,URL> entry:get.entrySet()){
							if(lines<10){
								msg+=entry.getKey().substring(1)+"!\n";
							}
							lines++;
							
						}
						if(lines>=5){
							msg+="...And more";
						}
						
						msg+="\nComment:"+ret.substring(1);
						msgBox(msg,"Wow!");
						
						
						save.acquired.put(mod.getModName()+":"+guess,get);
						
						save.named.put(mod.getModName()+":"+guess, new HashSet<String>());
						save.named.get(mod.getModName()+":"+guess).add(write.getText());
						
					}
					
					save.correctGuessByPicture++;
					saveData();
					
					voice.dispose();
				}else{
					if(ret.startsWith("W")){
						msgBox("Wrong Answer:\n"+ret.substring(1),"Try again");
					}else{
						msgBox("Oops.. Something bad happened:\n"+ret.substring(1),"Sorry");
					}
				}
			}
			
		});
		voice.setSize(400,400);
		voice.setVisible(true);
	}

	public void drawGuessVoiceWindow(Mod mod,int guess){
		
		System.out.println("GuessId="+mod.getModName()+":"+guess);
		JFrame voice=new JFrame("Sound Guess");
		
		voice.setLayout(new GridLayout(5, 1));
		voice.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel text=new JLabel("Listen and write its name!");
		voice.add(text);
		
		JButton play=new JButton("Play Sound");
		voice.add(play);
		
		class MyActionListener implements ActionListener{
			Player player;
			boolean playing=false;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(player!=null && !player.isComplete()){
						player.close();
						player=null;
					}else{
						
						play.setText("Loading...");
						
						Thread t=new Thread(){
							public void run(){
								
								try{
									
									
									InputStream is=mod.getMusic(guess).openStream();
									player=new Player(is);
									
									play.setText("Play Sound");
									
									player.play();
								}catch(Exception e){
									e.printStackTrace();
									msgBox("Error playing the sound :(","Error");
								}
							}
						};
						
						t.start();
					}
					playing=!playing;
					
				}catch(Exception er){
					er.printStackTrace();
					msgBox("Error playing the sound :(","Error");
				}
			}
			
		}
		
		MyActionListener al=new MyActionListener();
		
		play.addActionListener(al);
		
		voice.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(al.player!=null){
					al.player.close();
				}
				super.windowClosing(e);
			}
		});
		
		JTextField write=new JTextField("");
		voice.add(write);
		
		JButton yes=new JButton("Confirm!");
		voice.add(yes);
		
		JButton no=new JButton("Cancel");
		voice.add(no);
		
		no.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				voice.dispose();
			}
		});
		
		yes.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String ret=mod.isCorrect(guess, write.getText().trim());
				
				if(ret.startsWith("C")){
					
					if(save.acquired.containsKey(mod.getModName()+":"+guess)){
						msgBox("You caught a "+write.getText()+" again!\nComment:"+ret.substring(1),"Wow!");
						save.named.get(mod.getModName()+":"+guess).add(write.getText());
						
					}else{
						String msg="The first time,you caught a "+write.getText()+"!\nYou get:";
						Map<String,URL> get=mod.onAcquired(guess);
						
						int lines=0;
						
						for(Entry<String,URL> entry:get.entrySet()){
							if(lines<10){
								msg+=entry.getKey().substring(1)+"!\n";
							}
							lines++;
							
						}
						if(lines>=5){
							msg+="...And more";
						}
						
						msg+="\nComment:"+ret.substring(1);
						msgBox(msg,"Wow!");
						
						
						save.acquired.put(mod.getModName()+":"+guess,get);
						
						save.named.put(mod.getModName()+":"+guess, new HashSet<String>());
						save.named.get(mod.getModName()+":"+guess).add(write.getText());
						
					}
					
					save.correctGuessByVoice++;
					saveData();
					
					voice.dispose();
				}else{
					if(ret.startsWith("W")){
						msgBox("Wrong Answer:\n"+ret.substring(1),"Try again");
					}else{
						msgBox("Oops.. Something bad happened:\n"+ret.substring(1),"Sorry");
					}
				}
			}
			
		});
		voice.pack();
		voice.setVisible(true);
	}
	
	public void drawSetting(){
		JFrame settings=new JFrame("Mods");
		
		JList<String> jl=new JList<>();
		Vector<String> vec=new Vector<>();
		
		for(Mod md:MainLoader.mods){
			vec.add(md.getModName());
		}
		
		jl.setListData(vec);
		
		JScrollPane jsp=new JScrollPane(jl);
		settings.add(jsp);
		
		settings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settings.setSize(250, 500);
		settings.setVisible(true);
	}
}
