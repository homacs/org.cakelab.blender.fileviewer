package org.cakelab.blender.ui.editors;


import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.io.FileHeader;

@SuppressWarnings("serial")
public class ViewFileHeader extends HtmlViewer {

	public ViewFileHeader() {
		setContentType("text/html");
	}
	
	public void show(BlenderFile blend) {
		FileHeader header = blend.getHeader();
		
		String html = new String();
		
		html += "<html><head/><body>";
		
		html += "<h1>Blender File</h1>";
		
		html += "<table>";
		
		html += tr("path:", blend.getFile().toString());
		html += tr("pointer size:", header.getPointerSize());
		html += tr("byte order:", header.getByteOrder());
		html += tr("version:", header.getVersion());
		html += "</table>";

		html += "</body></html>";
		setContentType("text/html");
		setText(html.toString());
	}


	
	
	
	
}
