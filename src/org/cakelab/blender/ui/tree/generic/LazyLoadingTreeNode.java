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
	
	protected void lazyLoadChildren() {
		if (!this.loaded) {
			if (this.children == null) {
				this.children = new Vector<>();
			}
			try {
				Vector<MutableTreeNode> vectorChildren = new Vector<>();
				this.loadChildren(vectorChildren);
				// make sure parent is set for all children
				for (MutableTreeNode n : vectorChildren) {
					n.setParent(this);
					this.children.add(n);
				}
				this.loaded = true;
			}
			catch (Throwable t) {
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
