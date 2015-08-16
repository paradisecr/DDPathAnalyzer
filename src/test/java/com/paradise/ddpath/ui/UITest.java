package com.paradise.ddpath.ui;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class UITest {
	public static void borderTest(){
		JFrame frame = new JFrame();
		//关联关闭按钮
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextPane textPane = new JTextPane();
		textPane.setBorder(new LineNumberBorder());
		textPane.setText("测试/n换行");
		frame.add(textPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		borderTest();
	}
}
