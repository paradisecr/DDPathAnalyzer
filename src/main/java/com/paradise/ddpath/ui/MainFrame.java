package com.paradise.ddpath.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.MenuBar;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import org.apache.commons.collections15.Transformer;

import com.paradise.ddpath.graph.DDpathGraph;
import com.paradise.ddpath.graph.DDpathLink;
import com.paradise.ddpath.graph.DDpathNode;
import com.paradise.ddpath.nio.FileObject;
import com.paradise.ddpath.parser.DDpathParser;
import com.paradise.ddpath.parser.CFGParser;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class MainFrame {
	
	private JFrame frame = new JFrame();
	
	private String title;
	
	private static String PRONAME = "DD路径自动生成器";
	
	private String filePath = null;
	
	private FileObject fileObject;
	
 	//菜单
	private JMenuBar menueBar = new JMenuBar();
	private JMenu file = new JMenu("文件");
	private JMenu run = new JMenu("运行");
	private JMenuItem open = new JMenuItem("打开");
	private JMenuItem parse = new JMenuItem("生成DD路径");
	private JFileChooser fileChooser = new JFileChooser("d:/test");
	//panel 
	private JPanel tablePanel = new JPanel();
	private JScrollPane ddGraphPanel = new JScrollPane();
	private JPanel cfgGraphPanel = new JPanel();
	private JPanel consolePanel = new JPanel();
	private JScrollPane sourcePane = new JScrollPane();;
	//tabpanel 共5个
	private JTabbedPane sourceTab = new JTabbedPane();
	private JTabbedPane tableTab = new JTabbedPane();
	private JTabbedPane ddGraphTab = new JTabbedPane();
	private JTabbedPane cfgGraphTab = new JTabbedPane();
	private JTabbedPane consoleTab = new JTabbedPane();	
	//分割面板
	private JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,sourceTab, tableTab);
	private JSplitPane headSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,ddGraphTab, cfgGraphTab);
	private JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,headSplit, consoleTab);
	private JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,leftSplit, rightSplit);
	//源文件显示区
	private JTextPane txtPanel = new JTextPane();//文本显示
	//路径表格区
	//dd路径图区
	//cfg图区
	//控制台区
	
	public MainFrame(){
		init();
	}
	
	public void init(){
		frame.setTitle(PRONAME);
//		frame.setLayout(new FlowLayout());
//		mainSplit.setLayout(new FlowLayout());
		cfgGraphPanel.setLayout(new FlowLayout());
		//菜单
		file.add(open);
		run.add(parse);
		menueBar.add(file);
		menueBar.add(run);
		frame.setJMenuBar(menueBar);
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showDialog(frame, "选择源文件");
				if(result == JFileChooser.APPROVE_OPTION){
					filePath = fileChooser.getSelectedFile().getPath();
					fileObject = new FileObject(filePath);
					try {
						txtPanel.setText(fileObject.getCharContent().toString());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					filePath = null;
				}
			}
		});
		parse.addActionListener(new ParseActionListener());
		//源码显示区
		sourcePane.setViewportView(txtPanel);
		sourceTab.add("源文件", sourcePane);
		txtPanel.setBorder(new LineNumberBorder());
		//表格区
		tableTab.add("DD路径转换",tablePanel);
		//DD路径图区
		ddGraphTab.add("DD路径图", ddGraphPanel);
		//CFG图区
		cfgGraphTab.setSize(300, 300);
		cfgGraphTab.add("程序图", cfgGraphPanel);
		//控制台区
		consoleTab.add("控制台", consolePanel);
		//加载全部组件
		frame.add(mainSplit);
		frame.pack();
		//关联关闭按钮
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void show(){
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainFrame().show();
	}
	
	public void info(String info){
	}
	
	public void alert(String title,String message){
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void parseFile(){
		CFGParser parser = new CFGParser();
		parser.parseAll(fileObject);
		DDpathParser ddParser = new DDpathParser();
		DDpathGraph ddpathGraph = ddParser.parseCFGGraph(parser.getCFGGraph());
		paintDDGraph(ddpathGraph);
	}
	
	public void paintDDGraph(DDpathGraph ddGraph){
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
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
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
		GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
		//ddGraphPanel.add(gzsp);
		cfgGraphPanel.add(gzsp);
		//alert("info", String.valueOf(ddGraph.getDdPathList().size()));
	}
	
	class ParseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(null == filePath) {
				info("请先选择源文件!\n");
				alert("提示","请先选择源文件!");
			}else{
				parseFile();
			}
		}
		
	}
}