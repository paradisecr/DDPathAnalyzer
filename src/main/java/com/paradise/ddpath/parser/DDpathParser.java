package com.paradise.ddpath.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.paradise.ddpath.graph.CFGGraph;
import com.paradise.ddpath.graph.CFGNode;
import com.paradise.ddpath.graph.DDPathType;
import com.paradise.ddpath.graph.DDpathGraph;
import com.paradise.ddpath.graph.DDpathNode;

import static com.paradise.ddpath.graph.DDPathType.*;

/**
 * CFG图解析类，解析生成对应的DDpath图
 * @author 睿
 *
 */
public class DDpathParser {
	private CFGGraph cfgGraph;
	private DDpathGraph ddpathGraph;
	private Set<CFGNode> cfgNodeStack;
	private Map<CFGNode, DDpathNode> map;
	private int count = 0;
	public DDpathParser(){
		cfgNodeStack = new HashSet<CFGNode>();
		map = new HashMap<CFGNode, DDpathNode>();
	}
	
	public DDpathGraph parseCFGGraph(CFGGraph cfgGraph){
		init();
		this.cfgGraph = cfgGraph;
		ddpathGraph = new DDpathGraph();
		CFGNode cfgRoot = cfgGraph.getRoot();
		if(null == cfgRoot) return ddpathGraph;
		//DDpathNode ddNode = ddpathGraph.newNode();
		parse(null,cfgRoot);
		
		//set DDpathNode Content
		ddpathGraph.initNodeContent();
		return ddpathGraph;
	}
	
	private void parse(DDpathNode ddNode, CFGNode cfgNode){
		//处理当前cfgNode节点
		DDpathNode thisDDNode = null;
		if(null == ddNode){
			thisDDNode = ddpathGraph.newNode(getId());
			thisDDNode.addCFGNode(cfgNode);
			thisDDNode.setType(DDPathType.type(thisDDNode));
		}else{
			switch(ddNode.getType()){
			case ONE:
			case TWO:
			case THREE:
				thisDDNode = ddpathGraph.newNode(getId());
				thisDDNode.addCFGNode(cfgNode);
				thisDDNode.setType(DDPathType.type(thisDDNode));
				link(ddNode, thisDDNode);
				break;
			default:
				if(cfgNode.inDegree() ==1 && cfgNode.outDegree() == 1){
					thisDDNode = ddNode;
					thisDDNode.addCFGNode(cfgNode);
					thisDDNode.setType(DDPathType.type(thisDDNode));
				}else{
					thisDDNode = ddpathGraph.newNode(getId());
					thisDDNode.addCFGNode(cfgNode);
					thisDDNode.setType(DDPathType.type(thisDDNode));
					link(ddNode, thisDDNode);
				}
			}
		}
		cfgNodeStack.add(cfgNode);
		map.put(cfgNode, thisDDNode);
		//遍历子节点
		for(CFGNode node : cfgNode.getSucList()){
			if(!cfgNodeStack.contains(node)){
				parse(thisDDNode, node);
			}else{
				link(thisDDNode,map.get(node));
			}
		}
	}
	
	public void init(){
		cfgGraph = null;
		ddpathGraph = null;
		cfgNodeStack.clear();
		count = 0;
	}
	
	public void link(DDpathNode preNode, DDpathNode sucNode){
		ddpathGraph.link(preNode, sucNode);
	}
	
	public String getId(){
		String id = "";
		int tmp = count;
		while(tmp/26 >0){
			id += (char)((tmp/26) + 'A');
			tmp = tmp/26;
		}
		id += (char)(tmp + 'A');
		count++;
		return id;
	}
}
