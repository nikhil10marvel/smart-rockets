package io.smart.rockets.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.csv.BIn;
import io.smart.genetic.RocketPopulation;
import io.smart.rockets.Game;
import io.smart.rockets.Rocket;

import javax.swing.*;
import java.util.HashMap;

public class DesktopLauncher {
	public static void main (String[] arg) {
		if(arg.length  > 0){
			HashMap<Float, Integer> floatHashMap = (HashMap<Float, Integer>) (BIn.getSerializer(Gdx.files.local("fitness.map.bin"), false).getValue());
			System.out.println(floatHashMap);
		}else {
			Game.file_name_local = JOptionPane.showInputDialog(null, "Level file", "Level Selection", JOptionPane.QUESTION_MESSAGE);
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			Rocket.max_flow_size += Integer.parseInt(JOptionPane.showInputDialog(null,"Step Size or Max Flow Size"));
			config.height = 800;
			config.width = 608;
			config.resizable = false;
			//Game.file_name_local = JOptionPane.showInputDialog(null, "Stage File name") + ".png";
			new LwjglApplication(new Game(), config);
		}
	}
}
