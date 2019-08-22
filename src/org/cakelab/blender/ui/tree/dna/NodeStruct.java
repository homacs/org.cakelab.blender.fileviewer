package org.cakelab.blender.ui.tree.dna;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.cakelab.blender.metac.CStruct;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;

@SuppressWarnings("serial")
public class NodeStruct extends LazyLoadingTreeNode implements MutableTreeNode {

	public NodeStruct(CStruct struct) {
		super(struct);
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return getStruct().getSignature();
	}

	public CStruct getStruct() {
		return (CStruct) getUserObject();
	}

}
