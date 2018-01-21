package com.gussGame;

public class Song  {
	String title;
	String artist;

	public Song(String title, String artist) {
		super();
		this.title = title;
		this.artist = artist;
	}
	
	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String toString(){
		return title+""+artist;
	}
}
