package com.paradise.ddpath.graph;

public class DDpathLink {
	
	private DDpathNode preNode;
	
	private DDpathNode sucNode;
	
	public DDpathLink(DDpathNode preNode, DDpathNode sucNode){
		this.preNode = preNode;
		this.sucNode = sucNode;
	}

	public DDpathNode getPreNode() {
		return preNode;
	}

	public void setPreNode(DDpathNode preNode) {
		this.preNode = preNode;
	}

	public DDpathNode getSucNode() {
		return sucNode;
	}

	public void setSucNode(DDpathNode sucNode) {
		this.sucNode = sucNode;
	}
	
}
