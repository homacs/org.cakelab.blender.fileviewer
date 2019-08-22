package org.cakelab.blender.ui.tree.generic;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


@SuppressWarnings("serial")
public class TreeModel extends DefaultTreeModel {
	public TreeModel(TreeNode root) {
		super(root, true);
	}
	
	public TreeNode findUserObject(Object o) {
		return findUserObject(root, o);
		
	}
	
	public TreeNode getRoot() {
		return (TreeNode)root;
	}
	
	
	/** searches for first node n in root which satisfies (n.getUserObject().equals(o))*/
	public TreeNode findUserObject(TreeNode parent, Object o) {
		return findFirst(parent, Condition.object(o));
	}
	/** searches for first node n in root which satisfies (clazz.isInstance(n))*/
	public <T extends TreeNode> T findNodeType(Class<T> clazz) {
		return findNodeType(root, clazz);
	}

	/** searches for first child node n in parent which satisfies (clazz.isInstance(n))*/
	@SuppressWarnings("unchecked")
	public <T extends TreeNode> T findNodeType(TreeNode parent, Class<T> clazz) {
		return (T)findFirst(parent, Condition.type(clazz));
	}
	/** searches for first node n in root which satisfies (clazz.isInstance(n))*/
	public TreeNode findNodeName(String name) {
		return findNodeName(root, name);
	}

	/** searches for first node n in parent which satisfies (clazz.isInstance(n))*/
	public TreeNode findNodeName(TreeNode parent, String name) {
		
		return findFirst(parent, Condition.name(name));
	}

	private TreeNode findFirst(TreeNode parent, Condition condition) {
		@SuppressWarnings("unchecked")
		Enumeration<TreeNode> children = parent.children();
		while (children.hasMoreElements()) {
			TreeNode child = children.nextElement();
			if (condition.valid(child)) {
				return child;
			}
		}
		return null;
	}
	
	public TreeNode findFirst(TreeNode node, Condition ... conditions) {
		if (conditions.length == 0) return null;
		for (int i = 0; i < conditions.length; i++) {
			node = findFirst(node, conditions[i]);
			if (node == null) {
				// does not exist
				break;
			}
		}
		return node;
	}
	
	
	public TreePath findFirstPath(TreeNode node, Condition ... conditions) {
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		getParents(node, nodes);
		nodes.add(node);
		for (int i = 0; i < conditions.length; i++) {
			node = findFirst(node, conditions[i]);
			if (node == null) {
				// does not exist
				return null;
			}
			nodes.add(node);
		}
		return new TreePath(nodes.toArray(new TreeNode[0]));
	}
	
	public TreeNode findFirst(TreePathCondition conditions) {
		return findFirst(root, conditions.toArray());
	}

	public TreeNode findFirst(TreeNode node, TreePathCondition conditions) {
		return findFirst(node, conditions.toArray());
	}

	public TreePath findFirstPath(TreePathCondition conditions) {
		return findFirstPath(root, conditions.toArray());
	}

	public TreePath findFirstPath(TreeNode node, TreePathCondition conditions) {
		return findFirstPath(node, conditions.toArray());
	}
	

	/** adds list of parent nodes of parents (not the node itself) */
	private void getParents(TreeNode node, ArrayList<TreeNode> nodes) {
		TreeNode parent = node.getParent();
		if (parent != null) {
			getParents(parent, nodes);
			nodes.add(parent);
		}
	}


	
	
	public static TreePathCondition condition() {
		return new TreePathCondition();
	}
	
	
	/**
	 * A path of conditions for find*() methods.
	 * 
	 * <h4>Example:</h4>
	 * 
	 * 
	 * 
	 * treeModel.findFirst(TreeModel.condition().name("root").type(NodeFile.class).add(myCondition));
	 * 
	 * @author homac
	 *
	 */
	public static class TreePathCondition {
		ArrayList<Condition> conditions = new ArrayList<Condition>();
		public TreePathCondition() {
		}
		
		public Condition[] toArray() {
			return conditions.toArray(new Condition[0]);
		}

		public TreePathCondition object(Object o) {
			conditions.add(Condition.object(o));
			return this;
		}
		public TreePathCondition type(Class<?> clazz) {
			conditions.add(Condition.type(clazz));
			return this;
		}
		public TreePathCondition name(String name) {
			conditions.add(Condition.name(name));
			return this;
		}
		
		public TreePathCondition add(Condition c) {
			conditions.add(c);
			return this;
		}
		
	}
	
	public static interface Condition {
		boolean valid(TreeNode n);
		
		/** new condition testing a tree node's name (n.toString().equals()) */
		static Condition name(String name) {
			return new Condition() {
				@Override
				public boolean valid(TreeNode n) {
					return (n.toString().equals(name));
				}
			};
		}

		/** tests a tree node against the list of given conditions until the first one fails (c1 and c2 and ...) */
		static Condition and(Condition ... conditions) {
			return new Condition() {
				@Override
				public boolean valid(TreeNode n) {
					for (Condition c : conditions) {
						if (!c.valid(n)) return false;
					}
					return true;
				}
			};
		}

		/** tests a tree node against all given conditions (c1 or c2 or ...) */
		static Condition or(Condition ... conditions) {
			return new Condition() {
				@Override
				public boolean valid(TreeNode n) {
					for (Condition c : conditions) {
						if (c.valid(n)) return true;
					}
					return false;
				}
			};
		}

		static Condition not(Condition c) {
			return new Condition() {
			@Override
			public boolean valid(TreeNode n) {
				return !c.valid(n);
			}
		};
	}

		
		
		/** new condition testing a tree node's type */
		static Condition type(Class<?> clazz) {
			return new Condition() {
				@Override
				public boolean valid(TreeNode n) {
					return (clazz.isInstance(n));
				}
			};
		}

		/** new condition testing a tree node's identity (n.equals(node)) */
		static Condition node(TreeNode node) {
			return new Condition() {
				@Override
				public boolean valid(TreeNode n) {
					return (n.equals(node));
				}
			};
		}

		/** new condition testing a tree node's user object (DefaultMutableTreeNode) */
		static Condition object(Object o) {
			return new Condition() {
				@Override
				public boolean valid(TreeNode n) {
					if (n instanceof DefaultMutableTreeNode) {
						return ((DefaultMutableTreeNode)n).getUserObject().equals(o);
					} else {
						return false;
					}
				}
				
			};
		}
		
		
	}


}
