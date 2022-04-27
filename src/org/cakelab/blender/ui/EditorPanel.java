package org.cakelab.blender.ui;

import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import org.cakelab.blender.FileDebugger;
import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.io.block.Block;
import org.cakelab.blender.io.block.BlockList;
import org.cakelab.blender.metac.CMetaModel;
import org.cakelab.blender.metac.CStruct;
import org.cakelab.blender.metac.CType;
import org.cakelab.blender.metac.CType.CKind;
import org.cakelab.blender.type.JavaMapping;
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
import org.cakelab.blender.ui.tree.lib.NodeLibEntry;

/**
 * Gathers data and selects appropriate editor/viewer.
 * @author homac
 *
 */
@SuppressWarnings("serial")
public class EditorPanel extends JScrollPane {
	private FileDebugger debugger;

	private HtmlViewer currentViewer;
	private HtmlViewer standardViewer;
	private ViewFileHeader viewFileheader;
	private ViewStruct viewStruct;
	private ViewBlock viewBlock;

	private ViewBlockList viewBlockList;

	private ViewDNA viewDNA;

	private ViewField viewField;
	
	public EditorPanel(FileDebugger debugger) {
		this.debugger = debugger;
		standardViewer = new HtmlViewer();
		viewFileheader = new ViewFileHeader();
		viewBlock = new ViewBlock();
		viewBlockList = new ViewBlockList();
		viewDNA = new ViewDNA();
		viewStruct = new ViewStruct(debugger);
		viewField = new ViewField(debugger);
	}
	
	
	private void setEditor(HtmlViewer editor) {
		if (currentViewer != editor) {
			if (currentViewer != null) currentViewer.clear();
			setViewportView(editor);
			currentViewer = editor;
		}
	}


	public void clear() {
		currentViewer.clear();
		currentViewer = standardViewer;
		setEditor(currentViewer);
	}
	
	
	public void show(TreeNode node) {
		HtmlViewer selected = currentViewer;
		if (node instanceof NodeBlendFile) {
			viewFileheader.show(((NodeBlendFile)node).getBlenderFile());
			selected = viewFileheader;
		} else if (node instanceof NodeBlockList) {
			BlenderFile blend = getBlenderFile(node);
			BlockList blocks = ((NodeBlockList)node).getBlockList();
			viewBlockList.show(blend, blocks);
			selected = viewBlockList;
		} else if (node instanceof NodeBlock) {
			NodeBlock nBlock = ((NodeBlock)node);
			BlenderFile blend = getBlenderFile(nBlock);
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
		} else if (node instanceof NodeLibEntry) {
			selected = selectView((NodeLibEntry)node);
		} else {
			selected.clear();
		}
		
		setEditor(selected);
		
	}


	private BlenderFile getBlenderFile(TreeNode node) {
		NodeBlendFile filenode = getNodeBlendFile(node);
		return filenode != null ? filenode.getBlenderFile() : null;
	}


	private HtmlViewer selectView(NodeLibEntry node) {
		Object value = node.getEntry();
		NodeBlendFile blend = getNodeBlendFile(node);

		CMetaModel cmodel = blend.getMetaModel();
		Class<?> clazz = value == null ? node.getType() : value.getClass();
		CStruct cstruct = getCStruct(cmodel, clazz);
		
		TreeNode parent = node.getParent();
		if (parent instanceof NodeLibEntry) {
			Object parentObject = ((NodeLibEntry) parent).getEntry();
			String structName = parentObject.getClass().getSimpleName();
			String fieldName = node.getName();
			viewField.show(value, structName, fieldName, cstruct, debugger.getStructDocs());
			return viewField;
		}
		
		if (cstruct != null) {
			viewStruct.show(cmodel, (CStruct)cstruct, debugger.getStructDocs());
			return viewStruct;
		}

		
		// haven't found anything useful --> clear content
		currentViewer.clear();
		return currentViewer;
	}


	private CStruct getCStruct(CMetaModel cmodel, Class<?> clazz) {
		if (!JavaMapping.isScalar(clazz)) {
			CType cstruct = cmodel.getType(clazz.getSimpleName());
			if (cstruct != null && cstruct.getKind() == CKind.TYPE_STRUCT) {
				return (CStruct) cstruct;
			}
		}
		return null;
	}


	NodeBlendFile getNodeBlendFile(TreeNode node) {
		while (node != null) {
			if (node instanceof NodeBlendFile) {
				return ((NodeBlendFile)node);
			}
			node = node.getParent();
		}
		return null;
	}
	
	
	
}
