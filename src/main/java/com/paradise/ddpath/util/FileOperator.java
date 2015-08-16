package com.paradise.ddpath.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperator {
	
	public static void writeFile(String path,String content){
		try {
			FileWriter fileWriter = new FileWriter(path,false);
			fileWriter.write(content);
//			fileWriter.flush();
			fileWriter.close();
//			PrintWriter printWriter = new PrintWriter(file);
//			printWriter.println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件读写失败");
			return;
		}
	}
	
	public static void main(String[] args) {
		String filePath = "F:\\大四\\毕业设计\\java绘图\\graphviz-2.38\\release\\bin\\1test.dot";
		String commandPath = "F:\\大四\\毕业设计\\java绘图\\graphviz-2.38\\release\\bin\\1command.bat";
		String command = "cmd.exe /c start  " + commandPath;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//writeFile(path, "test");
	}
	
}
