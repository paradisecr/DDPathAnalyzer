package com.paradise.ddpath.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.colt.function.DoubleDoubleFunction;

import com.paradise.ddpath.graphviz.GraphViz;

public class DDpathGraph {
	
	private List<DDpathNode> ddPathList;

	
	private List<DDpathLink> linkList;
	
	private Map<String, String> contentMap = null;
	
	private DDpathNode root = null;
	
	public DDpathGraph(){
		ddPathList = new ArrayList<DDpathNode>();
		linkList = new ArrayList<DDpathLink>();
	}
	
	public DDpathNode newNode(String id){
		DDpathNode ddNode = new DDpathNode(id);
		addDdpathNode(ddNode);
		return ddNode;
	}
	
	public void addDdpathNode(DDpathNode ddPathNode){
		if(null == root){
			root = ddPathNode;
		}
		ddPathList.add(ddPathNode);
	}
	
	public List<DDpathNode> getDdPathList() {
		return ddPathList;
	}
	public void setDdPathList(List<DDpathNode> ddPathList) {
		this.ddPathList = ddPathList;
	}
	public DDpathNode getRoot() {
		return root;
	}
	public void setRoot(DDpathNode root) {
		this.root = root;
	}
	
	public List<DDpathLink> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<DDpathLink> linkList) {
		this.linkList = linkList;
	}
	
	public void initNodeContent(){
		for(DDpathNode node : ddPathList){
			node.initContent();
		}
	}
	
	public String getContentById(String id){
		if(null == contentMap){
			initContentMap();
		}
		return contentMap.get(id);
	}
	
	protected void initContentMap(){
		contentMap = new HashMap<String, String>();
		for(DDpathNode ddNode : ddPathList){
			contentMap.put(ddNode.getId(), ddNode.getContent());
		}
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

	public void link(DDpathNode preNode, DDpathNode sucNode){
		if(!preNode.getSucList().contains(sucNode)){
			preNode.getSucList().add(sucNode);
			DDpathLink link = new DDpathLink(preNode, sucNode);
			linkList.add(link);
		}
		if(!sucNode.getPreList().contains(preNode)){
			sucNode.getPreList().add(preNode);
		}
		
	}
	
	protected GraphViz  parse2Dot(){
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for(DDpathNode node : ddPathList){
			gv.addln(node.getId() + " [label=\"" + node.getId() + "\"];");
		}
		for(DDpathLink link : linkList){
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
