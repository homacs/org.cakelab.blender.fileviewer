package org.cakelab.blender.ui.tree.blocks;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.cakelab.blender.io.block.Block;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;

@SuppressWarnings("serial")
public class NodeBlock extends LazyLoadingTreeNode {

	public NodeBlock(Block b) {
		super(b);
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws Throwable {
		// TODO Auto-generated method stub

	}

	public Block getBlock() {
		return (Block)getUserObject();
	}
	
	
	
	@Override
	public String toString() {
		Block b = getBlock();
		return b.header.getCode().toString();
	}

}
