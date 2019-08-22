package org.cakelab.blender.ui.tree.generic;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cakelab.blender.TypeCastProvider;
import org.cakelab.blender.doc.DocumentationProvider;
import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.ui.tree.NodeBlendFile;

@SuppressWarnings("serial")
public class LazyLoadingTree extends JTree {

	private TreeModel model;
	private DefaultMutableTreeNode root;
	private TypeCastProvider typeCastProvider;

	public LazyLoadingTree() {
		setModel(null);
		setRootVisible(false);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		root = new DefaultMutableTreeNode();
		model = new TreeModel(root);
		
		TreeListener listener = new TreeListener();
		addTreeWillExpandListener(listener );
		
	}

	public void openFile(BlenderFile blend, DocumentationProvider docs, TypeCastProvider typeCastProvider) {
		this.typeCastProvider = typeCastProvider;
		NodeBlendFile fileNode = new NodeBlendFile(blend, docs, typeCastProvider);
		root.add(fileNode);
		setModel(model);
	}

	public void closeFile(BlenderFile blend) {
		findFile(blend);
		setModel(null);
	}

	private void findFile(BlenderFile blend) {
		for (int i = 0; i < root.getChildCount(); i++) {
			NodeBlendFile f = (NodeBlendFile) root.getChildAt(i);
			if (f.getBlenderFile() == blend) {
				root.remove(i);
			}
		}
	}


	public class TreeListener implements TreeWillExpandListener {

		private LazyLoadingTreeNode getLazyLoadingNode(TreeExpansionEvent event) {
			TreePath path = event.getPath();
			Object o =  path.getLastPathComponent();
			if (o instanceof LazyLoadingTreeNode) {
				return (LazyLoadingTreeNode) o;
			}
			return null;
		}
		
		
		
		@Override
		public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
			LazyLoadingTreeNode node = getLazyLoadingNode(event);
			if (node != null) {
				node.lazyLoadChildren();
				// This is important, because otherwise the selection
				// will point to nodes, which do not exist anymore.
				model.nodeStructureChanged(node);
			}
		}


		@Override
		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
			LazyLoadingTreeNode node = getLazyLoadingNode(event);
			if (node != null) {
				node.dispose();
				// This is important, because otherwise the selection
				// will point to nodes, which do not exist anymore.
				model.nodeStructureChanged(node);
			}
		}

	}




}
