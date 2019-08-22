package org.cakelab.blender.ui.editors;


import java.awt.Rectangle;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
public class HtmlViewer extends JEditorPane {

	public HtmlViewer() {
		setContentType("text/html");
		setEditable(false);
	}
	
	
	public void addListener() {
		addHyperlinkListener(new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
	                
	                String reference = e.getDescription();
	                if (reference != null && reference.startsWith("#")) { // link must start with # to be internal reference
	                	onLocalLink(reference);
	                } else {
	                	onExternalLink(reference);
	                }
	            }
			}
			
		});
	}
	

	
	
	public void setText(String text) {
		super.setText(text);
		// schedule to scroll to location 0,0
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				scrollRectToVisible(new Rectangle());
			}
		});
	}
	
	protected void onExternalLink(String externalUrl) {
	}
	
	protected void onLocalLink(String reference) {
        reference = reference.substring(1);
        HtmlViewer.this.scrollToReference(reference);
	}
	
	
	public void clear() {
		setText("");
	}


	protected String row(String name, Object value, String description) {
		return "<tr><td>" + name + "</td><td>" + value + "</td><td>" + description + "</td></tr>";
	}

	protected String tr(String name, Object value) {
		return "<tr><td>" + name + "</td><td>" + value + "</td></tr>";
	}

	protected String br() {
		return "<br/>";
	}

	protected String p(String text) {
		return elem("p", text) + br() + br();
	}

	protected String h1(String text) {
		return elem("h1", text);
	}
	
	protected String h2(String text) {
		return elem("h2", text);
	}
	
	protected String elem(String tag, String content) {
		return "<" + tag + ">" + content + "</" + tag + ">";
	}

	
}
