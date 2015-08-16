package com.paradise.ddpath.graph;

public class CFGLink {
	
	private CFGNode preNode;
	private CFGNode sucNode;
	
	public CFGLink(CFGNode preNode,  CFGNode sucNode){
		this.preNode = preNode;
		this.sucNode = sucNode;
	}

	public CFGNode getPreNode() {
		return preNode;
	}

	public void setPreNode(CFGNode preNode) {
		this.preNode = preNode;
	}

	public CFGNode getSucNode() {
		return sucNode;
	}

	public void setSucNode(CFGNode sucNode) {
		this.sucNode = sucNode;
	}
	
	
	
}
