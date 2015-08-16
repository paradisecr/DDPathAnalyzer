package com.paradise.ddpath.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class DDpathNode {
	/**
	 * cfgNodelist
	 */
	private List<CFGNode> cfgNodeList;
	
	private DDPathType type;
	
	private String content = null;
	
	private Map<Integer, String> contentMap;
	
	private String id = "";
	
	public DDPathType getType() {
		return type;
	}

	public void setType(DDPathType type) {
		this.type = type;
	}

	private List<DDpathNode> preList;
	
	private List<DDpathNode> sucList;
	
	public DDpathNode(){
		cfgNodeList = new ArrayList<CFGNode>();
		preList = new ArrayList<DDpathNode>();
		sucList = new ArrayList<DDpathNode>();
		contentMap = new TreeMap<Integer, String>();
	}
	
	public DDpathNode(String id){
		cfgNodeList = new ArrayList<CFGNode>();
		preList = new ArrayList<DDpathNode>();
		sucList = new ArrayList<DDpathNode>();
		contentMap = new TreeMap<Integer, String>();
		this.id = id;
	}
	
	public void addCFGNode(CFGNode node){
		cfgNodeList.add(node);
//		contentMap.put(node.getMinLine(),node.toString());
		contentMap.put(node.getId(),node.getContent());
	}
	
	
	public String toString(){
		return content;
	}
	
	public  void link(DDpathNode preNode, DDpathNode sucNode){
		preNode.getSucList().add(sucNode);
		sucNode.getPreList().add(preNode);
	}
	
	public void initContent(){
		content = new String();
		for(CFGNode node : cfgNodeList){
			if(null != content && StringUtils.isNotBlank(content)){
				content += "\n";
			}
			content += node.getContent();
		}
	}
	
	public void initContent2(){
		content = new String();
		for(Integer line : contentMap.keySet()){
			content += contentMap.get(line);
		}
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<CFGNode> getCfgNodeList() {
		return cfgNodeList;
	}

	public void setCfgNodeList(List<CFGNode> cfgNodeList) {
		this.cfgNodeList = cfgNodeList;
	}

	public List<DDpathNode> getPreList() {
		return preList;
	}

	public void setPreList(List<DDpathNode> preList) {
		this.preList = preList;
	}

	public List<DDpathNode> getSucList() {
		return sucList;
	}

	public void setSucList(List<DDpathNode> sucList) {
		this.sucList = sucList;
	}
	
	public Map<Integer, String> getContentMap() {
		return contentMap;
	}

	public void setContentMap(Map<Integer, String> contentMap) {
		this.contentMap = contentMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int size(){
		return cfgNodeList.size();
	}
	
	public CFGNode getNode(int index){
		if(index > cfgNodeList.size()-1) return null;
		return cfgNodeList.get(index);
	}
	
	
}
