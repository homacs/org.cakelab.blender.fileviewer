package org.cakelab.blender.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.*;

@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {
	
	public FileChooser(File dir, boolean blend) {
		super(dir);
		
		setFileHidingEnabled(true);
		if (blend) {
			setFileSelectionMode(JFileChooser.FILES_ONLY);
			addChoosableFileFilter(new BlendFilter());
		} else  {
			// configure for doc selection
			setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
	}
	
	public FileChooser(boolean blend) {
		this(new File(System.getProperty("user.home")), blend);
	}
	
	@Override
	protected JDialog createDialog(Component parent) throws HeadlessException {
		JDialog dialog = super.createDialog(parent);
		JRadioButton rb = new JRadioButton("show hidden files");
		rb.getModel().setSelected(!isFileHidingEnabled());
		rb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setFileHidingEnabled(!isFileHidingEnabled());
				dialog.invalidate();
			}
			
		});
		dialog.getContentPane().add(rb, BorderLayout.SOUTH);
		dialog.pack();
        dialog.setLocationRelativeTo(parent);
		return dialog;
	}

	
	public class BlendFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.toString().endsWith(".blend");
		}

		@Override
		public String getDescription() {
			return ".blend";
		}
	}

	
}
