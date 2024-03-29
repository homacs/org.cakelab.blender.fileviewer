package org.cakelab.blender.ui.editors;


import javax.swing.SwingUtilities;

import org.cakelab.blender.FileDebugger;
import org.cakelab.blender.doc.DocumentationProvider;
import org.cakelab.blender.metac.CField;
import org.cakelab.blender.metac.CMetaModel;
import org.cakelab.blender.metac.CStruct;
import org.cakelab.blender.metac.CType;

@SuppressWarnings("serial")
public class ViewStruct extends HtmlViewer {

	private FileDebugger debugger;


	public ViewStruct(FileDebugger debugger) {
		this.debugger = debugger;
		addListener();
	}
	
	
	public void show(CMetaModel cMetaModel, CStruct struct, DocumentationProvider docs) {
		String html = beginHtml();

		html += getStructDoc(struct, docs);

		endHtml(html);
	}

	
	protected void endHtml(String html) {
		html += "</body></html>";
		setContentType("text/html");
		setText(html.toString());
	}


	protected String beginHtml() {
		return "<html><head/><body>";
	}


	public String getStructDoc(CStruct struct, DocumentationProvider docs) {
		String html = new String();
		html += h1("Type " + struct.getSignature());
		html += p("DNA index: " + struct.getSdnaIndex());
		html += p(decodeDoc(docs.getStructDoc(struct.getSignature())));
		html += h2("C Declaration");
		html += "struct " + struct.getSignature() + " {";
		html += "<table>";
		for (CField e : struct.getFields()) {
			String type = e.getType().getSignature();
			CType referenced = getReferencedType(e);

			if (referenced != null) {
				type = type.replaceAll(referenced.getSignature(), "<a href=\"dna://" + referenced.getSignature() + "\">" + referenced.getSignature() + "</a>");
			}
			html += row(type, e.getName(), ";");
		}
		html += "</table>";
		html += "};";
		return html;
	}
	
	
	private CType getReferencedType(CField e) {
		CType referenced  = e.getType();
		if (referenced == null) {
			System.err.println("incomplete type");
		}
		while (referenced.getReferencedType() != null) {
			referenced = referenced.getReferencedType();
		}
		switch (referenced.getKind()) {
		case TYPE_ARRAY:
			System.err.println("ERROR: resolved referenced type is an array: " + referenced.getSignature());
			return null;
		case TYPE_POINTER:
			System.err.println("ERROR: resolved referenced type is a pointer: " + referenced.getSignature());
			return null;
		case TYPE_FUNCTION_POINTER:
		case TYPE_SCALAR:
		case TYPE_VOID:
			// there is no link available for these type kinds.
			referenced = null;
			break;
		case TYPE_STRUCT:
			break;
		default:
			break;
		}
		return referenced;
	}


	private String decodeDoc(String jdoc) {
		return jdoc.replaceAll("\\{@link ([^\\}]*)\\}", "<a href=\"dna://$1\">$1</a>");
	}


	@Override
	protected void onExternalLink(String dnaRef) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				debugger.select(dnaRef);
			}
			
		});
	}
	

	
	
	
}
