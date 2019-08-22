package org.cakelab.blender.ui.tree.blocks;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.cakelab.blender.io.block.Block;
import org.cakelab.blender.io.block.BlockList;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;

@SuppressWarnings("serial")
public class NodeBlockList extends LazyLoadingTreeNode {


	public NodeBlockList(BlockList blocks) {
		super(blocks);
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws Throwable {
		BlockList blocks = getBlockList();
		for (Block b : blocks) {
			NodeBlock nb = new NodeBlock(b);
			children.add(nb);
		}
		
	}

	@Override
	public String toString() {
		return "Data";
	}

	public BlockList getBlockList() {
		return (BlockList) getUserObject();
	}

}
