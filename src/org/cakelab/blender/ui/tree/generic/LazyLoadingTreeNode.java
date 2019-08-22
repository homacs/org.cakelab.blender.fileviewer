package org.cakelab.blender.ui.tree.generic;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;



@SuppressWarnings("serial")
public abstract class LazyLoadingTreeNode extends DefaultMutableTreeNode {

	private boolean loaded = false;
	
	public LazyLoadingTreeNode(Object userObject) {
		super(userObject);
	}
	
	@SuppressWarnings("unchecked")
	protected void lazyLoadChildren() {
		if (!loaded) {
            if (children == null) {
                children = new Vector<MutableTreeNode>();
            }
            try {
            	loadChildren((Vector<MutableTreeNode>)children);
            	// make sure parent is set for all children
            	for (Object n : children) {
            		((MutableTreeNode)n).setParent(this);
            	}
    			loaded = true;
            } catch (Throwable t) {
            	// TODO: report error
            	System.err.println(t);
            }
		}
	}

	public abstract void loadChildren(Vector<MutableTreeNode> children) throws Throwable;
	
	public abstract String toString();
	
	
	public void dispose() {
		
		removeAllChildren();
		loaded = false;
	}
	
	
	@Override
	public boolean isLeaf() {
		return false;
	}

}
