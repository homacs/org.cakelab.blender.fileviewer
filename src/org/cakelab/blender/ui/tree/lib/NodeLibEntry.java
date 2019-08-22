package org.cakelab.blender.ui.tree.lib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.blender.dna.ListBase;
import org.cakelab.blender.TypeCastProvider;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;

@SuppressWarnings("serial")
public class NodeLibEntry  extends LazyLoadingTreeNode {

	protected String name;
	protected Class<?> type;
	protected TypeCastProvider typeCastProvider;

	public NodeLibEntry(Class<?> type, String name, Object value, TypeCastProvider typeCastProvider) {
		super(value);
		this.type = type;
		this.name = name;
		this.typeCastProvider = typeCastProvider;
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws Throwable {
		Object value = getEntry();
		if (value == null) return;
		
		
		Class<?> clazz = value.getClass();
		
		
		for (Method m : clazz.getDeclaredMethods()) {
			String mname = m.getName();
			m.setAccessible(true);
			
			if (Modifier.isPublic(m.getModifiers()) 
					&& m.getParameterCount() == 0
					&& mname.startsWith("get")) 
			{
				Object attrib = m.invoke(value);
				String name = mname.substring(3).toLowerCase();
				Class<?> type = m.getReturnType();
				addChild(children, type, name, attrib);
			}
		}
		
	}

	
	protected void addChild(Vector<MutableTreeNode> children, Class<?> type, String name, Object value) {
		NodeLibEntry child;
		if (value != null) type = value.getClass();
		if (type.equals(ListBase.class)) {
			child = new NodeListBase(this.type, type, name, (ListBase)value, typeCastProvider);
		} else {
			child = new NodeLibEntry(type, name, value, typeCastProvider);
		}
		children.add(child);
	}
	
	
	
	
	public Object getEntry() {
		return getUserObject();
	}
	
	
	
	@Override
	public String toString() {
		Object value = getEntry();
		if (value == null) {
			return type.getSimpleName() + " " + name + " = null";
		} else if (isScalar(type)) {
			return type.getSimpleName() + " " + name + " = " + value.toString();
		} else {
			return type.getSimpleName() + " " + name;
		}
	}

	private boolean isScalar(Class<?> type) {
		return (
				type.equals(byte.class)
				|| type.equals(char.class)
				|| type.equals(short.class)
				|| type.equals(int.class)
				|| type.equals(long.class)
				|| type.equals(float.class)
				|| type.equals(double.class)
				|| type.equals(boolean.class)
				|| type.equals(Byte.class)
				|| type.equals(Character.class)
				|| type.equals(Short.class)
				|| type.equals(Integer.class)
				|| type.equals(Long.class)
				|| type.equals(Float.class)
				|| type.equals(Double.class)
				|| type.equals(Boolean.class)
				|| type.equals(String.class)
				);
	}

}
