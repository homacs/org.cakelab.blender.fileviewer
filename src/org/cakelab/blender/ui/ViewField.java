package org.cakelab.blender.ui;

import org.cakelab.blender.FileDebugger;
import org.cakelab.blender.doc.DocumentationProvider;
import org.cakelab.blender.metac.CStruct;
import org.cakelab.blender.ui.editors.ViewStruct;

public class ViewField extends ViewStruct {
	private static final long serialVersionUID = 1L;

	public ViewField(FileDebugger debugger) {
		super(debugger);
	}


	public void show(Object value, String structName, String fieldName, CStruct cstruct,
			DocumentationProvider docs) {
		String html = beginHtml();
		html += getFieldDoc(structName, fieldName, docs);
		if (cstruct != null) html += getStructDoc(cstruct, docs);

		endHtml(html);
	}


	private String getFieldDoc(String structName, String fieldName, DocumentationProvider docs) {
		String html = h1("Field Documentation");
		return html += docs.getFieldDoc(structName, fieldName);
	}


}
