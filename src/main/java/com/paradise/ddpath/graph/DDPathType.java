package com.paradise.ddpath.graph;

public enum DDPathType {
	ONE(1,"情况一","由一个节点组成，内度=0."),
	TWO(2,"情况二","由一个节点组成，外度=0."),
	THREE(3,"情况三","由一个节点组成，内度>=2或外度>=2."),
	FOUR(4,"情况四","由一个节点组成，内度==1并且外度=1."),
	FIVE(5,"情况五","长度>=1的最大链。");
	
	final int id;
	final String name;
	final String desc;
	
	private DDPathType(int id,String name, String desc){
		this.id = id;
		this.name = name;
		this.desc = desc;
	}
	
	public String toString(){
		return id + "." + name + "," + desc;
	}
	
	public static DDPathType type(DDpathNode ddNode){
		DDPathType type = null;
		if(ddNode == null || ddNode.size() == 0) return null;
		if(ddNode.size() ==1){
			int inDegree = ddNode.getNode(0).inDegree();
			int outDegree = ddNode.getNode(0).outDegree();
			if(inDegree == 0){
				return ONE;
			}else if(outDegree == 0){
				return TWO;
			}else if(inDegree >=2 || outDegree >=2){
				return THREE;
			}else if(inDegree ==1 && outDegree ==1){
				return FOUR;
			}
		}else{
			boolean flag = true;
			for(CFGNode cfgNode : ddNode.getCfgNodeList()){
				if(cfgNode.inDegree() !=1 || cfgNode.outDegree() !=1){
					flag = false;
				}
			}
			if(flag){
				return FIVE;
			}
		}
		
		return type;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
