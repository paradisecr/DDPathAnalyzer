package com.paradise.ddpath.graph;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.paradise.ddpath.parser.Token;
import com.paradise.ddpath.parser.TokenType;

public class CFGNode {
	/**
	 * 行号
	 */
	private int minLine = Integer.MAX_VALUE;
	
	private int maxLine = 0;
	/**
	 * 编号
	 */
	private int id;
	
	/**
	 * 前驱节点链表
	 */
	private List<CFGNode> preList ;
	/**
	 * 后继节点链表
	 */
	private List<CFGNode> sucList ;
	
	private List<Token> tokenList;
	
	private String content = null;
	
	public CFGNode(){
		preList = new ArrayList<CFGNode>();
		sucList = new ArrayList<CFGNode>();
		tokenList = new ArrayList<Token>();
	}
	
	public CFGNode(int id){
		preList = new ArrayList<CFGNode>();
		sucList = new ArrayList<CFGNode>();
		tokenList = new ArrayList<Token>();
		this.id = id;
	}
	
	public  void linkNode(CFGNode sucNode){
		sucList.add(sucNode);
		sucNode.preList.add(this);
	}
	
	public void addToken(Token token){
		if(token.getLine() > maxLine) maxLine = token.getLine();
		if(token.getLine() < minLine) minLine = token.getLine();
		tokenList.add(token);
	}
	
	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<CFGNode> getPreList() {
		return preList;
	}

	public void setPreList(List<CFGNode> preList) {
		this.preList = preList;
	}

	public List<CFGNode> getSucList() {
		return sucList;
	}

	public void setSucList(List<CFGNode> sucList) {
		this.sucList = sucList;
	}
	
	public int inDegree(){
		return preList.size();
	}
	
	public int outDegree(){
		return sucList.size();
	}
	
	public String toString2(){
		if (content == null) {
			content = minLine + "行---->" + maxLine + "行";
		}
		return content;
	}
	
	public String toString(){
		if(content == null){
			content = "";
			for(Token token : tokenList){
				content += token.getName();
				//str += " ";
			}
		}
		return content;
	}

	public int getMinLine() {
		return minLine;
	}

	public void setMinLine(int minLine) {
		this.minLine = minLine;
	}

	public int getMaxLine() {
		return maxLine;
	}

	public void setMaxLine(int maxLine) {
		this.maxLine = maxLine;
	}

	public String getContent() {
		if(content == null){
			content = "";
			for(Token token : tokenList){
				if(StringUtils.isNoneBlank(content) && token.getTokenType() != TokenType.SEMI){
					content += " ";
				}
				content += token.getName();
				//str += " ";
			}
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
