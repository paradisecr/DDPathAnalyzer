package com.paradise.ddpath.util;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {
	
	public static void writeToFile(byte[] img, File to){
		try {
		      FileOutputStream fos = new FileOutputStream(to);
		      fos.write(img);
		      fos.close();
		   } catch (java.io.IOException ioe) { return;}
	    return;
	}
}
