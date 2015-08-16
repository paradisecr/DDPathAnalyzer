package com.paradise.file;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PathTest {

	@Test
	public void PathName(){
		Path path = Paths.get("d:/test/dowhiletest.txt");
		String name = path.getFileName().toString();
		System.out.println(name.substring(0, name.lastIndexOf(".")));
	}
	
}
