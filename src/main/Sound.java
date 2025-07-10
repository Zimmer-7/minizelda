package main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	public static Sound musicBackGround = new Sound("/background.mp3");
	public static Sound hurt = new Sound("/Hurt.wav");
	public static Sound shoot = new Sound("/Shoot.wav");
	public static Sound pickup = new Sound("/Pickup.wav");
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch(Throwable e) {
			//error
		}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch(Throwable e) {
			//error
		}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		} catch(Throwable e) {
			//error
		}
	}
	
}
