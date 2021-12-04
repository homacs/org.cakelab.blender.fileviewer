package org.cakelab.blender.ui.tree.lib;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.blender.dna.ListBase;
import org.cakelab.blender.TypeCastProvider;
import org.cakelab.blender.nio.CPointer;

@SuppressWarnings("serial")
public class NodeListBase extends NodeLibEntry {

	private Class<?> typecast;
	public NodeListBase(Class<?> parenttype, Class<?> type, String name, ListBase attrib, TypeCastProvider typeCastProvider) {
		super(type, name, attrib, typeCastProvider);
		
		this.typecast = getTypeCast(parenttype, name);
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws Throwable {
		ListBase base = (ListBase) getEntry();
		if (base == null) return;
		
		CPointer<?> first = base.getFirst();
		CPointer<?> last = base.getLast();
		if (typecast != null) {
			first = first.cast(typecast);
			last = last.cast(typecast);
		}
		addChild(children, first.getClass(), "first", first);
		addChild(children, last.getClass(), "last", last);
	}
	
	private Class<?> getTypeCast(Class<?> parenttype, String attrib) {
		return typeCastProvider.getTypeCast(TypeCastProvider.Category.ListBase, parenttype, attrib);
	}

}
