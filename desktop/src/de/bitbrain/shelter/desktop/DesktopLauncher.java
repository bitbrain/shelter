package de.bitbrain.shelter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.bitbrain.shelter.ShelterGame;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "shelter";
		config.forceExit = false;
		new LwjglApplication(new ShelterGame(args), config);
	}
}
