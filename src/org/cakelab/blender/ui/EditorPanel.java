package org.cakelab.blender.ui;

import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import org.cakelab.blender.FileDebugger;
import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.io.block.Block;
import org.cakelab.blender.io.block.BlockList;
import org.cakelab.blender.ui.editors.HtmlViewer;
import org.cakelab.blender.ui.editors.ViewBlock;
import org.cakelab.blender.ui.editors.ViewBlockList;
import org.cakelab.blender.ui.editors.ViewDNA;
import org.cakelab.blender.ui.editors.ViewFileHeader;
import org.cakelab.blender.ui.editors.ViewStruct;
import org.cakelab.blender.ui.tree.NodeBlendFile;
import org.cakelab.blender.ui.tree.blocks.NodeBlock;
import org.cakelab.blender.ui.tree.blocks.NodeBlockList;
import org.cakelab.blender.ui.tree.dna.NodeDNAModel;
import org.cakelab.blender.ui.tree.dna.NodeStruct;

/**
 * Gathers data and selects appropriate editor/viewer.
 * @author homac
 *
 */
@SuppressWarnings("serial")
public class EditorPanel extends JScrollPane {
	private FileDebugger debugger;

	HtmlViewer current;
	HtmlViewer standard;
	private ViewFileHeader viewFileheader;
	private ViewStruct viewStruct;
	private ViewBlock viewBlock;

	private ViewBlockList viewBlockList;

	private ViewDNA viewDNA;
	
	public EditorPanel(FileDebugger debugger) {
		this.debugger = debugger;
		standard = new HtmlViewer();
		viewFileheader = new ViewFileHeader();
		viewBlock = new ViewBlock();
		viewBlockList = new ViewBlockList();
		viewDNA = new ViewDNA();
		viewStruct = new ViewStruct(debugger);
	}
	
	
	private void setEditor(HtmlViewer editor) {
		if (current != editor) {
			if (current != null) current.clear();
			setViewportView(editor);
			current = editor;
		}
	}


	public void clear() {
		current.clear();
		current = standard;
		setEditor(current);
	}
	
	
	public void show(TreeNode node) {
		HtmlViewer selected = current;
		if (node instanceof NodeBlendFile) {
			viewFileheader.show(((NodeBlendFile)node).getBlenderFile());
			selected = viewFileheader;
		} else if (node instanceof NodeBlockList) {
			BlenderFile blend = ((NodeBlendFile)node.getParent()).getBlenderFile();
			BlockList blocks = ((NodeBlockList)node).getBlockList();
			viewBlockList.show(blend, blocks);
			selected = viewBlockList;
		} else if (node instanceof NodeBlock) {
			NodeBlock nBlock = ((NodeBlock)node);
			NodeBlockList nBlockList = (NodeBlockList) node.getParent();
			NodeBlendFile nBlend = (NodeBlendFile)nBlockList.getParent();
			BlenderFile blend = nBlend.getBlenderFile();
			Block block = nBlock.getBlock();
			viewBlock.show(blend, block);
			selected = viewBlock;
		} else if (node instanceof NodeDNAModel) {
			NodeDNAModel n = ((NodeDNAModel)node);
			viewDNA.show(n.getDNAModel());
			selected = viewDNA;
		} else if (node instanceof NodeStruct) {
			NodeStruct n = ((NodeStruct)node);
			NodeDNAModel nDNA = ((NodeDNAModel)node.getParent());
			viewStruct.show(nDNA.getDNAModel(), n.getStruct(), debugger.getStructDocs());
			selected = viewStruct;
		} else {
			selected.clear();
		}
		
		setEditor(selected);
		
	}

	
	
	
}
