package org.cakelab.blender;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.blender.dna.BlenderObject;
import org.blender.utils.MainLib;
import org.cakelab.blender.doc.Documentation;
import org.cakelab.blender.doc.DocumentationProvider;
import org.cakelab.blender.generator.DocGenerator;
import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.io.FileVersionInfo;
import org.cakelab.blender.ui.EditorPanel;
import org.cakelab.blender.ui.FileChooser;
import org.cakelab.blender.ui.tree.NodeBlendFile;
import org.cakelab.blender.ui.tree.dna.NodeDNAModel;
import org.cakelab.blender.ui.tree.dna.NodeStruct;
import org.cakelab.blender.ui.tree.generic.LazyLoadingTree;
import org.cakelab.blender.ui.tree.generic.TreeModel;
import org.cakelab.blender.ui.tree.generic.TreeModel.TreePathCondition;
import org.cakelab.json.JSONException;

@SuppressWarnings("serial")
public class FileDebugger extends JFrame {

	private FileChooser fcBlend = new FileChooser(true);
	private FileChooser fcDoc = new FileChooser(false);
	private BlenderFile blenderFile = null;
	private LazyLoadingTree tree;
	private EditorPanel editorPanel;
	private File docroot;
	
	private DocumentationProvider docs;
	private TypeCastProvider typeCastProvider;
	
	
	public FileDebugger() {
		this(new File(System.getProperty("user.dir")));
	}
	
	
	public FileDebugger(File docfolder) {
		
		setDocRoot(docfolder);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		setTitle("Java .Blend File Debugger -- (" + MainLib.BLENDER_MINVERSION_STRING + "," + MainLib.BLENDER_VERSION_STRING + ")");
		
		Dimension mainDims = new Dimension(640,480);
		
		setPreferredSize(mainDims);
		setJMenuBar(new Menu());
		
		tree = new LazyLoadingTree();
		tree.addTreeSelectionListener(new TreeListener());

		JScrollPane scrollTree = new JScrollPane(tree);
		scrollTree.setMinimumSize(new Dimension(50, mainDims.height));
		
		
		editorPanel = new EditorPanel(this);
		JComponent rightPane = editorPanel;

		JSplitPane lrSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollTree, rightPane);
		lrSplit.setOneTouchExpandable(true);
		lrSplit.setDividerLocation(150);
		
		getContentPane().add(lrSplit);

		pack();
		setVisible(true);
	}

	private DocumentationProvider loadDocumentation(BlenderFile blend) {
		//
		// load DNA structs documentation
		//
		
		FileVersionInfo versionInfo;
		boolean debug = false;
		DocumentationProvider docs = null;
		
		try {
			versionInfo = blend.readFileGlobal();
		
		
			File[] docfiles = null;
			File docfolder = Documentation.getDocFolder(docroot, versionInfo);
			if (docfolder == null) {
				System.err.println("Warning: can't find appropriate doc folder for version '" + versionInfo.getVersion() + "' in doc folder '" + docroot.toString() + "'");
				docfiles = new File[0];
			} else {
				System.out.println("Info: selected documentation: " + docfolder.getPath());
				docfiles = new File[] {
						new File(docfolder, "/added/doc.json"),
						new File(docfolder, "/pyapi/doc.json"),
						new File(docfolder, "/dnasrc/doc.json")
				};
			}
			docs = new DocGenerator(docfiles, debug);
		} catch (IOException | JSONException e) {
			onError(e.getMessage());
			try {
				// Fallback to empty doc
				docs = new DocGenerator(new File[0], debug);
			} catch (IOException | JSONException e1) {
				onError("internal error while trying to load fallback doc provider: " + e.getMessage());
				docs = null;
			}
		}
		return docs;
	}

	
	private TypeCastProvider loadTypeCasts(BlenderFile blend) throws IOException {
		FileVersionInfo versionInfo = blend.readFileGlobal();
		File folder = TypeCastProvider.getMatchingFolder(new File("typecasts"), versionInfo);
		TypeCastProvider typecasts = new TypeCastProvider(BlenderObject.class.getPackage().getName());
		
		typecasts.load(folder);
		
		return typecasts;
	}
	
	private void clearEditor() {
		editorPanel.clear();
	}

	public void select(String dnaRef) {
		String PROTOCOL_DNA = "dna://";
		TreeModel model = (TreeModel) tree.getModel();
		if(dnaRef.startsWith(PROTOCOL_DNA)) {
			
			String structName = dnaRef.substring(PROTOCOL_DNA.length());

			TreePathCondition c = TreeModel.condition()
					.object(blenderFile)
					.type(NodeDNAModel.class)
					.name(structName);
			TreePath path = model.findFirstPath(model.getRoot(), c);
			
			tree.setSelectionPath(path);
		}
	}



	
	public void onWarning(String message) {
		System.err.println("WARNING: " + message);
	}
	
	public void onError(String message) {
		System.err.println("ERROR: " + message);
	}
	
	
	public boolean setDocRoot(File rootdir) {
		if (!rootdir.isDirectory()) return false;
		
		docroot = rootdir;
		
		if (blenderFile != null) {
			docs = loadDocumentation(blenderFile);
		}
		
		return true;
	}
	
	
	public boolean openBlenderFile(File f) {
		boolean result = false;
		//
		// open and check if its a blender file
		// 
		try {
			BlenderFile blend = new BlenderFile(f);
			FileVersionInfo versions = blend.readFileGlobal();

			if (!MainLib.doVersionCheck(versions)) {
				onWarning("Conversion will probably fail due to version mismatch!");
			}
	
			if (blenderFile != null) closeBlenderFile();
			blenderFile = blend;
			docs = loadDocumentation(blend);
			typeCastProvider = loadTypeCasts(blend);
			tree.openFile(blend, docs, typeCastProvider);

		} catch (IOException e) {
			onError(e.getMessage());
		}
		
		return result;
	}
	
	
	
	


	private void closeBlenderFile() {
		try {
			if (blenderFile != null) {
				tree.closeFile(blenderFile);
				blenderFile.close();
				blenderFile = null;
			}
		} catch (Throwable t) {
			onWarning("while closing: " + t.getMessage());
		}
	}


	void chooseBlenderFile() {
		int result = fcBlend.showOpenDialog(this);
		
		if (result == JFileChooser.APPROVE_OPTION) {
            File file = fcBlend.getSelectedFile();
            openBlenderFile(file);
        }
	}

	void chooseDocRoot() {
		int result = fcDoc.showOpenDialog(this);
		
		if (result == JFileChooser.APPROVE_OPTION) {
            File file = fcDoc.getSelectedFile();
            setDocRoot(file);
        }
	}

	public DocumentationProvider getStructDocs() {
		return docs;
	}


	

	class Menu extends JMenuBar implements ActionListener {
		private JMenuItem miBlendOpen;
		private JMenuItem miDocOpen;

		public Menu() {
			
			// FILE
			JMenu mfile = new JMenu("File");
			miBlendOpen = new JMenuItem("Open");
			miBlendOpen.addActionListener(this);
			mfile.add(miBlendOpen);
			
			add(mfile);
			
			
			JMenu mdoc = new JMenu("Doc");
			miDocOpen = new JMenuItem("Open");
			miDocOpen.addActionListener(this);
			mdoc.add(miDocOpen);
			add(mdoc);
			
			
			JMenu minfo = new JMenu("?");
			add(minfo);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == miBlendOpen) {
				FileDebugger.this.chooseBlenderFile();
			} else if (e.getSource() == miDocOpen) {
				FileDebugger.this.chooseDocRoot();
			}
		}
	}

	
	public class TreeListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (e.getSource() == tree) {
				if(e.isAddedPath()) {
					assert(e.getPath().getLastPathComponent() == e.getNewLeadSelectionPath().getLastPathComponent());
					editorPanel.show((MutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent());
				} else {
					clearEditor();
				}
			}
		}


	}


	
	public static void main(String[] args) {
		FileDebugger debugger = new FileDebugger();
		debugger.setDocRoot(new File("/home/homac/repos/git/cakelab.org/playground/org.cakelab.blender.dnadoc/resources/dnadoc"));
		debugger.openBlenderFile(new File("/home/homac/repos/git/cakelab.org/playground/org.cakelab.blender.viewer/examples/cube.blend"));
	}




}
