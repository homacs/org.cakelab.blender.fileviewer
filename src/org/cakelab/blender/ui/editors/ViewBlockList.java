package org.cakelab.blender.ui.editors;


import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.io.block.BlockList;

@SuppressWarnings("serial")
public class ViewBlockList extends HtmlViewer {

	
	
	
	public void show(BlenderFile blend, BlockList blockList) {
		String html = new String();
		html += "<html><head/><body>";
		
		html += "<h1>Block List</h1>";
		html += p("A blender file contains blocks only. This node provides access to the actual data of each block in the file.");
		html += "<table>";
		html += tr("size:", blockList.size());
		html += "</table>";

		html += "</body></html>";
		setContentType("text/html");
		setText(html.toString());
	}

	
	
}
