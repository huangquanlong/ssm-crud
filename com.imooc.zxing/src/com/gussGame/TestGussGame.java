package com.gussGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Title: TestGussGame 
* @Description:  测试猜数字游戏
* @date 2017年4月16日 
 */
public class TestGussGame {
	List<Song> list=new ArrayList<Song>();
	class TitleCompare implements Comparator<Song>{
		public int compare(Song one,Song two){
			return one.getTitle().compareTo(two.getTitle());
		}
	}
	public static void main(String[] args){
		Class song=Song.class;
		System.out.println(song.getFields().toString());
		/**
		 * 
		 */
/*		GussGame game=new GussGame();
		game.StartGame();*/
/*		Player player=new Player();
		Player player2=new Player();
		int x=0;
		while(x<4){
			player.getCount();
			player.count=player.count+1;
			if(x==3){
				player2.count=player2.count+1;
			}
			if(x>0){
				player2.count=player.count+player2.count;
			}
			x=x+1;
		}
		System.out.println(player2.count);*/
		//Long sum=0L;
	/*	List<String> list=new ArrayList<>();
		System.out.println(list.hashCode());
		for(int i=1;i<=15;i++){
			String string="string"+i;
			System.out.println(string);
			list.add(string);
		//	sum+=i;
		}
		list.add(null);
		System.out.println(list.size());
		for (int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}
		System.out.println(list.get(5));*/
		//System.out.println(sum);
		//System.out.println(sum.TYPE.getName());
		//new TestGussGame().goSong();
	}
	void goSong(){
		getSong();
		System.out.println(list);
		TitleCompare titleCompare=new TitleCompare();
		Collections.sort(list, titleCompare);
		System.out.println(list.get(0));
	}
	void getSong(){
		File file =new File("C:/Users/hp/Desktop/新建文本文档 (2).txt");
		try {
			BufferedReader bd=new BufferedReader(new FileReader(file));
			String line=null;
			while((line=bd.readLine())!=null){
				addSong(line);
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void addSong(String line){
		String[] token=line.split("/");
		Song song=new Song(token[0], token[1]);
		list.add(song);
	}
	
	
}


