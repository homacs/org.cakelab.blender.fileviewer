package org.cakelab.blender.ui.tree.lib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.blender.utils.MainLib;
import org.cakelab.blender.TypeCastProvider;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;

@SuppressWarnings("serial")
public class NodeMainLib extends LazyLoadingTreeNode {

	private MainLib mainLib;
	private TypeCastProvider typeCastProvider;

	public NodeMainLib(MainLib mainLib, TypeCastProvider typeCastProvider) {
		super(mainLib);
		this.mainLib = mainLib;
		this.typeCastProvider = typeCastProvider;
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws Throwable {
		for (Method m : MainLib.class.getDeclaredMethods()) {
			String mname = m.getName();
			m.setAccessible(true);
			if (Modifier.isPublic(m.getModifiers()) 
					&& mname.startsWith("get") 
					&& !mname.equals("getPrev") 
					&& !mname.equals("getNext")) 
			{
				Object entry = m.invoke(mainLib);
				if (entry != null) {
					String name = mname.substring(3).toLowerCase();
					
					NodeLibEntry child = new NodeLibEntry(m.getReturnType(), name, entry, typeCastProvider);
					children.add(child);
				}
			}
		}
	}


	@Override
	public String toString() {
		return "lib";
	}


}
