package com.paradise.ddpath.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.io.output.DemuxOutputStream;

import com.paradise.ddpath.batik.MyJSVGCanvas;
import com.paradise.ddpath.graph.CFGGraph;
import com.paradise.ddpath.graph.CFGNode;
import com.paradise.ddpath.graph.DDpathGraph;
import com.paradise.ddpath.graph.DDpathLink;
import com.paradise.ddpath.graph.DDpathNode;
import com.paradise.ddpath.nio.FileObject;
import com.paradise.ddpath.parser.DDpathParser;
import com.paradise.ddpath.parser.CFGParser;
import com.paradise.ddpath.ui.MainFrame.ParseActionListener;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class MainFrame3 {

	private JFrame frame = new JFrame();
	
	private String title;
	
	private static String PRONAME = "DD路径自动生成器";
	
	private String filePath = null;
	
	private String fileName = null;
	
	private FileObject fileObject;
	
	private static final String SEPARATOR = "/";
	
	private static final String TMP_DIR = "tmp" + SEPARATOR;
	
	private static final String CUR_PATH = System.getProperty("user.dir");
	
	private static final String[] TABLE_TITLE = {"DD路径名称","程序图节点","定义情况"};
	
	private String[][] row=new String[0][3];
	
	private MyJSVGCanvas svgCanvas = new MyJSVGCanvas();

	MyJSVGCanvas svgCanvas1 = new MyJSVGCanvas();
	MyJSVGCanvas svgCanvas2 = new MyJSVGCanvas();
 	//菜单
	private JMenuBar menueBar = new JMenuBar();
	private JMenu file = new JMenu("文件");
	private JMenu run = new JMenu("运行");
	private JMenuItem open = new JMenuItem("打开");
	private JMenuItem parse = new JMenuItem("生成DD路径");
	private JFileChooser fileChooser = new JFileChooser("input");
	//panel 
	private JScrollPane tablePanel = new JScrollPane();
	private JScrollPane ddGraphPanel = new JScrollPane();
	private JScrollPane cfgGraphPanel = new JScrollPane();
	private JScrollPane consolePanel = new JScrollPane();
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

	private DefaultTableModel tableModel = new DefaultTableModel(row, TABLE_TITLE);
	private JTable table = new JTable(tableModel);
	//dd路径图区
	//cfg图区
	//控制台区
	private JTextArea textArea = new JTextArea();
	public MainFrame3(){
		init();
	}
	
	public void init(){
		frame.setTitle(PRONAME);
//		frame.setLayout(new FlowLayout());
//		mainSplit.setLayout(new FlowLayout());
//		cfgGraphPanel.setLayout(new FlowLayout());
		sourcePane.setPreferredSize( new Dimension(200, 300));
		ddGraphPanel.setPreferredSize( new Dimension(300, 450));
		cfgGraphPanel.setPreferredSize( new Dimension(300, 450));
		tablePanel.setPreferredSize(new Dimension(200, 300));
		consolePanel.setPreferredSize(new Dimension(300, 150));

		cfgGraphPanel.setViewportView(svgCanvas1);
		ddGraphPanel.setViewportView(svgCanvas2);
		//菜单
		file.add(open);
		run.add(parse);
		menueBar.add(file);
		menueBar.add(run);
		frame.setJMenuBar(menueBar);
		open.addActionListener(new OpenFileActionListener());
		parse.addActionListener(new ParseActionListener());
		//源码显示区
		sourcePane.setViewportView(txtPanel);
		sourceTab.add("源文件", sourcePane);
		txtPanel.setBorder(new LineNumberBorder());
		txtPanel.setEditable(false);
		//表格区
//		DefaultTableCellRenderer cellrender = new DefaultTableCellRenderer();
//		cellrender.setHorizontalAlignment(JTextField.CENTER);
//		table.setDefaultRenderer(String.class, cellrender);
		tablePanel.setViewportView(table);
		tableTab.add("DD路径转换",tablePanel);
		//DD路径图区
		ddGraphTab.add("DD路径图", ddGraphPanel);
		//CFG图区
		cfgGraphTab.setSize(300, 300);
		cfgGraphTab.add("程序图", cfgGraphPanel);
		//控制台区
		consoleTab.add("控制台", consolePanel);
		consolePanel.setViewportView(textArea);
		//加载全部组件
		frame.add(mainSplit);
		frame.pack();
		//关联关闭按钮
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void show(){
		frame.setVisible(true);
	}
	
	public synchronized void info(String info){
		textArea.append(info + "\n");
	}
	
	public void alert(String title,String message){
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void parseFile(){
		CFGParser parser = new CFGParser();
		parser.parseAll(fileObject);
		CFGGraph cfgGraph = parser.getCFGGraph();
		DDpathParser ddParser = new DDpathParser();
		DDpathGraph ddpathGraph = ddParser.parseCFGGraph(parser.getCFGGraph());
		paint(cfgGraph,ddpathGraph);
		info("成功。");
	}
	
	public void paint(CFGGraph cfgGraph,DDpathGraph ddGraph){
		Map<String, String> tipsMap = new HashMap<String, String>();
		tipsMap.putAll(cfgGraph.getContentMap());
		tipsMap.putAll(ddGraph.getContentMap());
		svgCanvas.setTipsMap(tipsMap); 
		//解析dot生成SVG
		String prefixName = fileName.substring(0,fileName.lastIndexOf("."));
		String cfgSVGPath = TMP_DIR + prefixName + "_cfggraph" + ".svg";
		String ddSVGPath = TMP_DIR + prefixName + "_ddgraph" + ".svg";
		cfgGraph.parse2SVG(prefixName + "_cfggraph",cfgSVGPath);
		ddGraph.parse2SVG(prefixName + "_ddgraph", ddSVGPath);
		initTableData(ddGraph);
		//渲染SVG
		svgCanvas1.setTipsMap(tipsMap); 
		svgCanvas2.setTipsMap(tipsMap); 
		svgCanvas1.setURI("file:" + SEPARATOR + CUR_PATH + File.separator + cfgSVGPath);
		svgCanvas2.setURI("file:" + SEPARATOR + CUR_PATH + File.separator + ddSVGPath);
		System.out.println("file:" + SEPARATOR + CUR_PATH + File.separator + cfgSVGPath);
		info("文件解析完毕");
		info("正在生成结构图...");
	}
	
	public void initTableData(DDpathGraph ddGraph){
		while(tableModel.getRowCount() > 0){
			tableModel.removeRow(0);
		}
		for(DDpathNode ddNode : ddGraph.getDdPathList()){
			String ddNodeId = ddNode.getId();
			String[] row = new String[3];
			row[0] = ddNodeId;
			row[1] = null;
			row[2] = ddNode.getType().getName();
			for(CFGNode node : ddNode.getCfgNodeList()){
				if(row[1] == null){
					row[1] = String.valueOf(node.getId());
				}else{
					row[1] += ",";
					row[1] += String.valueOf(node.getId());
				}
			}
			tableModel.addRow(row);
		}
	}
	
	public void addTable(String cfgNode,String ddNode,String type){
		String[] row = new String[3];
		row[0] = cfgNode;
		row[1] = ddNode;
		row[2] = type;
		tableModel.addRow(row);
	}
	
	class ParseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(null == filePath) {
				info("请先选择源文件!");
				alert("提示","请先选择源文件!");
			}else{
				info("正在解析...");
				new Thread(new ParseTask()).start();
			}
		}
		
	}
	class OpenFileActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int result = fileChooser.showDialog(frame, "选择源文件");
			if(result == JFileChooser.APPROVE_OPTION){
				File file = fileChooser.getSelectedFile();
				filePath = file.getPath();
				fileObject = new FileObject(filePath);
				fileName = file.getName();
				sourceTab.setTitleAt(0, "源文件 " + fileName);
				try {
					txtPanel.setText(fileObject.getCharContent().toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				info("源文件已打开");
			}else{
				filePath = null;
			}
		}
	}
	
	class MyCenterCellRender extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			      boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setHorizontalAlignment(SwingConstants.CENTER);
			return this;
		}
	}
//	Class AfterRenderAction imple
	
	class ParseTask implements Runnable{

		@Override
		public void run() {
			parseFile();
		}
		
	}
	public static void main(String[] args) {
		new MainFrame3().show();
	}
}
