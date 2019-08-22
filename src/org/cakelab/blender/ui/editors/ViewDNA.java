package org.cakelab.blender.ui.editors;


import org.cakelab.blender.metac.CMetaModel;

@SuppressWarnings("serial")
public class ViewDNA extends HtmlViewer {

	public void show(CMetaModel dnaModel) {
		String html = new String();
		html += "<html><head/><body>";

		
		
		html += h1("Type Information (DNA)");
		html += p("So-called DNA of a blender file. It is the list of declarations of all C types of data stored in any blender file. This information is stored in block DNA1 in every blender file.");
		html += "<table>";
		html += tr("Number of structs: ", dnaModel.getStructs().size());
		html += "</table>";
		

		html += "</body></html>";
		setContentType("text/html");
		setText(html.toString());
	}
	
	
	
	
	
}
