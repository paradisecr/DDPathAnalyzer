package com.paradise.ddpath.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paradise.ddpath.graphviz.GraphViz;

public class CFGGraph {
	/**
	 * 头节点
	 */
	private CFGNode root = null;
	/**
	 * 节点数
	 */
	private int num;
	
	private List<CFGNode> nodeList;
	
	private List<CFGLink> linkList;
	private Map<String, String> contentMap = null;
	
	public CFGGraph(){
		nodeList = new ArrayList<CFGNode>();
		linkList = new ArrayList<CFGLink>();
		num = 0;
	}

	public CFGNode newCFGNode(){
		CFGNode cfgNode = new CFGNode(++num);
		nodeList.add(cfgNode);
		if(root == null){
			root = cfgNode;
		}
		return cfgNode;
	}
	
	public CFGNode getRoot() {
		return root;
	}

	public List<CFGNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<CFGNode> nodeList) {
		this.nodeList = nodeList;
	}

	public void setRoot(CFGNode root) {
		this.root = root;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
	
	public List<CFGLink> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<CFGLink> linkList) {
		this.linkList = linkList;
	}

	protected void initContentMap(){
		contentMap = new HashMap<String, String>();
		for(CFGNode node : nodeList){
			contentMap.put(String.valueOf(node.getId()), node.getContent());
		}
	}

	public void link(CFGNode preNode,  CFGNode sucNode){
		CFGLink link = new CFGLink(preNode, sucNode);
		linkList.add(link);
		preNode.linkNode(sucNode);
	}
	
	public Map<String, String> getContentMap() {
		if(null == contentMap){
			initContentMap();
		}
		return contentMap;
	}

	public void setContentMap(Map<String, String> contentMap) {
		this.contentMap = contentMap;
	}
	
	protected GraphViz  parse2Dot(){
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for(CFGNode node : nodeList){
			gv.addln(node.getId() + " [label=\"" + node.getId() + "\"];");
		}
		for(CFGLink link : linkList){
			gv.add(link.getPreNode().getId() + " -> " + link.getSucNode().getId() + ";");
		}
	    gv.addln(gv.end_graph());
		return gv;
	}
	public void parse2SVG(String prefixName,String filePath){
		GraphViz gv = parse2Dot();
		gv.setPrefixName(prefixName);
		File out = new File(filePath);
	    gv.generateSVG(gv.getDotSource(), "svg" );
	}
	
}
