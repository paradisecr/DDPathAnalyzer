package com.paradise.ddpath.ui;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import com.paradise.ddpath.graph.DDpathGraph;
import com.paradise.ddpath.graph.DDpathLink;
import com.paradise.ddpath.graph.DDpathNode;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class MainFrame2 {
	private JFrame frame ;
	public  MainFrame2(){
		frame = new JFrame();
		
		//关联关闭按钮
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void show(DDpathGraph ddGraph){
		OrderedSparseMultigraph<DDpathNode, DDpathLink> smGraph = new OrderedSparseMultigraph<DDpathNode, DDpathLink>();
		
		//加入节点和连接
		final Map<DDpathNode,String> cacheMap = new HashMap<DDpathNode, String>();
		for(DDpathNode  node : ddGraph.getDdPathList()){
			String str = node.toString();
			str = "<html>" + str;
			str += "</html>";
			str = str.replaceAll("\n", "<br>");
			smGraph.addVertex(node);
			cacheMap.put(node, str);
		}
		for(DDpathLink link : ddGraph.getLinkList()){
			smGraph.addEdge(link, link.getPreNode(), link.getSucNode(),EdgeType.DIRECTED);
		}
		
		Layout<DDpathNode, DDpathLink> layout = new CircleLayout<DDpathNode, DDpathLink>(smGraph);
		VisualizationViewer<DDpathNode, DDpathLink> vv = new VisualizationViewer<DDpathNode, DDpathLink>(layout);
		//mouse action
		DefaultModalGraphMouse<DDpathNode, DDpathLink> gm = new DefaultModalGraphMouse<DDpathNode, DDpathLink>();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setVertexToolTipTransformer(new Transformer<DDpathNode, String>() {
			@Override
			public String transform(DDpathNode ddNode) {
				// TODO Auto-generated method stub
				//System.out.println(cacheMap.get(ddNode));
				return cacheMap.get(ddNode);
			}
		});
		//Transformer
		Transformer<DDpathNode, Paint> colorTransformer = new Transformer<DDpathNode, Paint>() {
			
			public Paint transform(DDpathNode arg0) {
				// TODO Auto-generated method stub 
				return Color.WHITE;
			}
		};
		vv.getRenderContext().setVertexFillPaintTransformer(colorTransformer);
		vv.getRenderContext().setVertexLabelTransformer(new Transformer<DDpathNode, String>() {

			@Override
			public String transform(DDpathNode node) {
				// TODO Auto-generated method stub
				return node.getId();
			}
		});
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<DDpathLink>());
//		EditingModalGraphMouse<String, String> gm2 = new EditingModalGraphMouse<String, String>(vv.getRenderContext(), null, null);
//		gm2.setMode(ModalGraphMouse.Mode.PICKING);
//		vv.setGraphMouse(gm2);
		frame.getContentPane().add(vv);	
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		DDpathGraph ddGraph = new DDpathGraph();
		
		new MainFrame2().show(ddGraph);
	}
	
}
