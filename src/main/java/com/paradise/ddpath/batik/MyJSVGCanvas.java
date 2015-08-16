package com.paradise.ddpath.batik;

import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;

import org.apache.batik.bridge.UserAgent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.Messages;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLConstants;
import org.apache.batik.util.gui.JErrorPane;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

public class MyJSVGCanvas extends JSVGCanvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1850808923949130997L;
	
	private Map<String, String> tipsMap;
	
	public MyJSVGCanvas(){
		super();
	}
	
	protected UserAgent createUserAgent(){
		return new MyCanvasUserAgent();
	}
	
	public void setTipsMap(Map tipsMap){
		this.tipsMap = tipsMap;
	}
	
	public String getTip(String key){
		if(tipsMap != null){
			return tipsMap.get(key);
		}
		return null;
	}
	
	// ----------------------------------------------------------------------
    // User agent implementation
    // ----------------------------------------------------------------------

    /**
     * The <code>CanvasUserAgent</code> only adds tooltips to the behavior of the
     * default <code>BridgeUserAgent</code>. A tooltip will be displayed
     * wheneven the mouse lingers over an element which has a &lt;title&gt; or a
     * &lt;desc&gt; child element.
     */
    protected class MyCanvasUserAgent extends BridgeUserAgent

        implements XMLConstants {

        final String TOOLTIP_TITLE_ONLY
            = "JSVGCanvas.CanvasUserAgent.ToolTip.titleOnly";
        final String TOOLTIP_DESC_ONLY
            = "JSVGCanvas.CanvasUserAgent.ToolTip.descOnly";
        final String TOOLTIP_TITLE_AND_TEXT
            = "JSVGCanvas.CanvasUserAgent.ToolTip.titleAndDesc";

        /**
         * The handleElement method builds a tool tip from the
         * content of a &lt;title&gt; element, a &lt;desc&gt;
         * element or both. <br/>
         * Because these elements can appear in any order, here
         * is the algorithm used to build the tool tip:<br />
         * <ul>
         * <li>If a &lt;title&gt; is passed to <code>handleElement</code>
         *     the method checks if there is a &gt;desc&gt; peer. If
         *     there is one, nothing is done (because the desc will do
         *     it). If there in none, the tool tip is set to the value
         *     of the &lt;title&gt; element content.</li>
         * <li>If a &lt;desc&gt; is passed to <code>handleElement</code>
         *     the method checks if there is a &lt;title&gt; peer. If there
         *     is one, the content of that peer is pre-pended to the
         *     content of the &lt;desc&gt; element.</li>
         * </ul>
         */
        public void handleElement(Element elt, Object data){
            super.handleElement(elt, data);

            // Don't handle tool tips unless we are interactive.
            if (!isInteractive()) return;

            if (!SVGConstants.SVG_NAMESPACE_URI.equals(elt.getNamespaceURI()))
                return;

            // Don't handle tool tips for the root SVG element.
            if (elt.getParentNode() ==
                elt.getOwnerDocument().getDocumentElement()) {
                return;
            }

            Element parent;
            // When node is removed data is old parent node
            // since we can't get it otherwise.
            if (data instanceof Element) parent = (Element)data;
            else                         parent = (Element)elt.getParentNode();

            Element descPeer = null;
            Element titlePeer = null;
            if (elt.getLocalName().equals(SVGConstants.SVG_TITLE_TAG)) {
                if (data == Boolean.TRUE)
                    titlePeer = elt;
                descPeer = getPeerWithTag(parent,
                                           SVGConstants.SVG_NAMESPACE_URI,
                                           SVGConstants.SVG_DESC_TAG);
            } else if (elt.getLocalName().equals(SVGConstants.SVG_DESC_TAG)) {
                if (data == Boolean.TRUE)
                    descPeer = elt;
                titlePeer = getPeerWithTag(parent,
                                           SVGConstants.SVG_NAMESPACE_URI,
                                           SVGConstants.SVG_TITLE_TAG);
            }

            String titleTip = null;
            if (titlePeer != null) {
                titlePeer.normalize();
                if (titlePeer.getFirstChild() != null)
                    titleTip = titlePeer.getFirstChild().getNodeValue();
            }

            String descTip = null;
            if (descPeer != null) {
                descPeer.normalize();
                if (descPeer.getFirstChild() != null)
                    descTip = descPeer.getFirstChild().getNodeValue();
            }
            //System.out.println("before:"+titleTip);
            titleTip = getTip(titleTip);
            final String toolTip ;
            if(null != titleTip){
            	toolTip = Messages.formatMessage
                      (TOOLTIP_TITLE_ONLY,
                       new Object[]{toFormattedHTML(titleTip)});
            	//System.out.println("after:" + toolTip);
            }else{
            	toolTip = null;
            }
//            if ((titleTip != null) && (titleTip.length() != 0)) {
//                if ((descTip != null) && (descTip.length() != 0)) {
//                    toolTip = Messages.formatMessage
//                        (TOOLTIP_TITLE_AND_TEXT,
//                         new Object[] { toFormattedHTML(titleTip),
//                                        toFormattedHTML(descTip)});
//                } else {
//                    toolTip = Messages.formatMessage
//                        (TOOLTIP_TITLE_ONLY,
//                         new Object[]{toFormattedHTML(titleTip)});
//                }
//            } else {
//                if ((descTip != null) && (descTip.length() != 0)) {
//                    toolTip = Messages.formatMessage
//                        (TOOLTIP_DESC_ONLY,
//                         new Object[]{toFormattedHTML(descTip)});
//                } else {
//                    toolTip = null;
//                }
//            }
            if (toolTip == null) {
                removeToolTip(parent);
                return;
            }

            if (lastTarget != parent) {
                setToolTip(parent, toolTip);
            } else {
                // Already has focus check if it already has tip text.
                Object o = null;
                if (toolTipMap != null) {
                    o = toolTipMap.get(parent);
                    toolTipMap.put(parent, toolTip);
                }

                if (o != null) {
                    // Update components tooltip text now.
                    EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                setToolTipText(toolTip);
                                MouseEvent e = new MouseEvent
                                    (MyJSVGCanvas.this,
                                     MouseEvent.MOUSE_MOVED,
                                     System.currentTimeMillis(),
                                     0,
                                     locationListener.getLastX(),
                                     locationListener.getLastY(),
                                     0,
                                     false);
                                ToolTipManager.sharedInstance().mouseMoved(e);
                            }
                        });
                } else {
                    EventQueue.invokeLater(new ToolTipRunnable(toolTip));
                }
            }
        }
        /**
         * Converts line breaks to HTML breaks and encodes special entities.
         * Poor way of replacing '<', '>' and '&' in content.
         */
        public String toFormattedHTML(String str) {
            StringBuffer sb = new StringBuffer(str);
            replace(sb, XML_CHAR_AMP, XML_ENTITY_AMP);  // Must go first!
            replace(sb, XML_CHAR_LT, XML_ENTITY_LT);
            replace(sb, XML_CHAR_GT, XML_ENTITY_GT);
            replace(sb, XML_CHAR_QUOT, XML_ENTITY_QUOT);
            // Dont' quote "'" apostrphe since the display doesn't
            // seem to understand it.
            // replace(sb, XML_CHAR_APOS, XML_ENTITY_APOS);
            replace(sb, '\n', "<br>");
            return sb.toString();
        }

        protected void replace(StringBuffer sb, char c, String r) {
            String v = sb.toString();
            int i = v.length();

            while( (i=v.lastIndexOf(c, i-1)) != -1 ) {
                sb.deleteCharAt(i);
                sb.insert(i, r);
            }
        }

        /**
         * Checks if there is a peer element of a given type.  This returns the
         * first occurence of the given type or null if none is found.
         */
        public Element getPeerWithTag(Element parent,
                                      String nameSpaceURI,
                                      String localName) {

            Element p = parent;
            if (p == null) {
                return null;
            }

            for (Node n=p.getFirstChild(); n!=null; n = n.getNextSibling()) {
                if (!nameSpaceURI.equals(n.getNamespaceURI())){
                    continue;
                }
                if (!localName.equals(n.getLocalName())){
                    continue;
                }
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    return (Element)n;
                }
            }
            return null;
        }

        /**
         * Returns a boolean defining whether or not there is a peer of
         * <code>elt</code> with the given qualified tag.
         */
        public boolean hasPeerWithTag(Element elt,
                                      String nameSpaceURI,
                                      String localName){

            return !(getPeerWithTag(elt, nameSpaceURI, localName) == null);
        }

        /**
         * Sets the tool tip on the input element.
         */
        public void setToolTip(Element elt, String toolTip){
            if (toolTipMap == null) {
                toolTipMap = new WeakHashMap();
            }
            if (toolTipDocs == null) {
                toolTipDocs = new WeakHashMap();
            }
            SVGDocument doc = (SVGDocument)elt.getOwnerDocument();
            if (toolTipDocs.put(doc, MAP_TOKEN) == null) {
                NodeEventTarget root;
                root = (NodeEventTarget)doc.getRootElement();
                // On mouseover, it sets the tooltip to the given value
                root.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI,
                                        SVGConstants.SVG_EVENT_MOUSEOVER,
                                        toolTipListener,
                                        false, null);
                // On mouseout, it removes the tooltip
                root.addEventListenerNS(XMLConstants.XML_EVENTS_NAMESPACE_URI,
                                        SVGConstants.SVG_EVENT_MOUSEOUT,
                                        toolTipListener,
                                        false, null);
            }

            toolTipMap.put(elt, toolTip);

            if (elt == lastTarget)
                EventQueue.invokeLater(new ToolTipRunnable(toolTip));
        }

        public void removeToolTip(Element elt) {
            if (toolTipMap != null)
                toolTipMap.remove(elt);
            if (lastTarget == elt) { // clear ToolTip.
                EventQueue.invokeLater(new ToolTipRunnable(null));
            }
        }

        /**
         * Displays an error message in the User Agent interface.
         */
        public void displayError(String message) {
            if (svgUserAgent != null) {
                super.displayError(message);
            } else {
                JOptionPane pane =
                    new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
                JDialog dialog =
                    pane.createDialog(MyJSVGCanvas.this, "ERROR");
                dialog.setModal(false);
                dialog.setVisible(true); // Safe to be called from any thread
            }
        }

        /**
         * Displays an error resulting from the specified Exception.
         */
        public void displayError(Exception ex) {
            if (svgUserAgent != null) {
                super.displayError(ex);
            } else {
                JErrorPane pane =
                    new JErrorPane(ex, JOptionPane.ERROR_MESSAGE);
                JDialog dialog = pane.createDialog(MyJSVGCanvas.this, "ERROR");
                dialog.setModal(false);
                dialog.setVisible(true); // Safe to be called from any thread
            }
        }
    }
}
