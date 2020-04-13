package de.bitbrain.shelter.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import de.bitbrain.shelter.ShelterGame;

import javax.swing.*;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "shelter";
		config.forceExit = false;
		config.audioDeviceBufferSize = 1024;
		config.audioDeviceSimultaneousSources = 32;
		setApplicationIcon(config);
		try {
			new LwjglApplication(new ShelterGame(args), config);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
	}

	private static void setApplicationIcon(LwjglApplicationConfiguration config) {
		try {
			config.addIcon("icon-16.png", Files.FileType.Internal);
			config.addIcon("icon-32.png", Files.FileType.Internal);
			config.addIcon("icon-64.png", Files.FileType.Internal);
			Class<?> cls = Class.forName("com.apple.eawt.Application");
			Object application = cls.newInstance().getClass().getMethod("getApplication").invoke(null);
			FileHandle icon = Gdx.files.internal("icon-128.png");
			application.getClass().getMethod("setDockIconImage", java.awt.Image.class)
					.invoke(application, new ImageIcon(icon.file().getAbsolutePath()).getImage());
		} catch (Exception e) {

		}
	}
}
