package com.paradise.ddpath.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.paradise.ddpath.graph.CFGGraph;
import com.paradise.ddpath.graph.CFGNode;
import com.paradise.ddpath.util.FileOperator;

public class CFGGraphPainter {
	public static String str = "";
	public static String label = "";
	public static Map<CFGNode, Integer>  map = null;
	public static Set<CFGNode> set = null;
	public static int count = 0;
	
	public static void paintGraph(CFGGraph tree,String graphFilePath, String commandPath){
		String dot = "digraph G{\r\n";
		dot += traversal(tree);
		dot += "\r\n}";
		FileOperator.writeFile(graphFilePath, dot);
		String command = "cmd.exe /c start " + commandPath;
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String traversal(CFGGraph tree){
		CFGNode node = tree.getRoot();
		map = new HashMap<CFGNode, Integer>();
		set = new HashSet<CFGNode>();
		str = "";
		label = "";
		count = 0;
		scan(node);
		return label + str;
	}
	
	private static void scan(CFGNode node){
		String nodeStr = node.toString();
		int index1 = getIndex(node);
		if(set.contains(node)){
			return;
		}else{
			set.add(node);
		}
		for(CFGNode tmp : node.getSucList()){
			int index2 = getIndex(tmp);
			str += index1;
			str += " -> ";
			str += index2;
			str += ";\r\n";
			scan(tmp);
		}
	}
	
	private static int getIndex(CFGNode node){
		if(map.get(node) == null){
			map.put(node, count);
			label += count;
			label += " [label=\"";
			label += node.toString();
			label += "\"];\r\n";
			count++;
		}
		return map.get(node);
	}
	
}
