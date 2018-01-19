package io.genetic.guess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

	static File dump;
	static BufferedWriter dumpOut;
	
	public static void probaility(float prob, Runnable run){
		if(Math.random() <= prob){
			run.run();
		}
	}
	
	public static void createdump(){
		try {
			File directory = new File(System.getProperty("user.dir"));
			dump = new File(directory, "dump.dmp");
			if(!dump.exists()) dump.createNewFile();
			dumpOut = new BufferedWriter(new FileWriter(dump));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void outputDump(String out){
		try {
			dumpOut.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeDump(){
		try {
			dumpOut.flush();
			dumpOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
