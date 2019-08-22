package org.cakelab.blender.ui.tree;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import org.blender.utils.MainLib;
import org.cakelab.blender.TypeCastProvider;
import org.cakelab.blender.doc.DocumentationProvider;
import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.ui.tree.blocks.NodeBlockList;
import org.cakelab.blender.ui.tree.dna.NodeDNAModel;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTreeNode;
import org.cakelab.blender.ui.tree.lib.NodeMainLib;

@SuppressWarnings("serial")
public class NodeBlendFile extends LazyLoadingTreeNode {


	private DocumentationProvider docs;
	private TypeCastProvider typeCastProvider;

	public NodeBlendFile(BlenderFile blend, DocumentationProvider docs, TypeCastProvider typeCastProvider) {
		super(blend);
		this.docs = docs;
		this.typeCastProvider = typeCastProvider;
	}
	
	public DocumentationProvider getDocumentation() {
		return docs;
	}

	public BlenderFile getBlenderFile() {
		return (BlenderFile)getUserObject();
	}

	@Override
	public String toString() {
		BlenderFile blend = getBlenderFile();
		String str = blend.getFile().toString();
		int i = str.lastIndexOf(File.separatorChar);
		if (i > 1) {
			str = str.substring(i+1, str.length());
		}
		return str;
	}

	@Override
	public void loadChildren(Vector<MutableTreeNode> children) throws IOException {
		BlenderFile bf = getBlenderFile();
		children.add(new NodeDNAModel(bf.getMetaModel()));
		children.add(new NodeBlockList(bf.getBlocks()));
		children.add(new NodeMainLib(new MainLib(bf), typeCastProvider));
	}

	
}
