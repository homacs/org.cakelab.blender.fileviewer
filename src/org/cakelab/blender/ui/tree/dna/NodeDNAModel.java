package org.cakelab.blender.ui.tree.dna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.cakelab.blender.metac.CMetaModel;
import org.cakelab.blender.metac.CStruct;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;

@SuppressWarnings("serial")
public class NodeDNAModel extends LazyLoadingTreeNode {

	public NodeDNAModel(CMetaModel userObject) {
		super(userObject);
	}

	public CMetaModel getDNAModel() {
		return (CMetaModel)getUserObject();
	}
	
	
	@Override
	public void loadChildren(Vector<MutableTreeNode> children) {
		CMetaModel model = getDNAModel();
		ArrayList<NodeStruct> nodes = new ArrayList<NodeStruct>();
		for (CStruct struct : model.getStructs()) {
			nodes.add(new NodeStruct(struct));
		}
		Collections.sort(nodes, new Comparator<NodeStruct>() {
			@Override
			public int compare(NodeStruct o1, NodeStruct o2) {
				String s1 = o1.toString();
				String s2 = o2.toString();
				return String.CASE_INSENSITIVE_ORDER.compare(s1,s2);
			}
		});
		for (NodeStruct node : nodes) {
			children.add(node);
		}
	}

	@Override
	public String toString() {
		return "DNA";
	}

}
