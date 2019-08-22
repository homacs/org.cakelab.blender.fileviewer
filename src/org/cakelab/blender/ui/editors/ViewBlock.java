package org.cakelab.blender.ui.editors;


import java.io.IOException;

import org.cakelab.blender.io.BlenderFile;
import org.cakelab.blender.io.block.Block;
import org.cakelab.blender.io.block.BlockCodes;
import org.cakelab.blender.metac.CMetaModel;
import org.cakelab.blender.metac.CStruct;

@SuppressWarnings("serial")
public class ViewBlock extends HtmlViewer {

	
	
	public void show(BlenderFile blend, Block block) {
		
		
		CMetaModel ctypes = null;
		
		try {
			ctypes = blend.getMetaModel();
		} catch (IOException e) {
			// TODO: error handling
		}
		
		
		String html = new String();
		
		html += "<html><head/><body>";
		
		html += "<h1>Block</h1>";
		
		html += p("One block of data in the selected blender file. Each block has a header describing the contents of the its data portion.");
		
		html += "<h2>Header</h2>";
		html += "<table>";
		String description = "";
		if (block.header.getCode().equals(BlockCodes.ID_AC)) {
			description = "Action.";
		} else if (block.header.getCode().equals(BlockCodes.ID_AR)) {
			description = "Armature.";
		} else if (block.header.getCode().equals(BlockCodes.ID_BR)) {
			description = "Brush.";
		} else if (block.header.getCode().equals(BlockCodes.ID_CA)) {
			description = "Camera.";
		} else if (block.header.getCode().equals(BlockCodes.ID_CO)) {
			description = "Constraint.";
		} else if (block.header.getCode().equals(BlockCodes.ID_CU)) {
			description = "Curve.";
		} else if (block.header.getCode().equals(BlockCodes.ID_DATA)) {
			description = "Block contains data which belongs to a preceeding block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_DNA1)) {
			description = "Block contains the description of each C data type (see DNA).";
		} else if (block.header.getCode().equals(BlockCodes.ID_ENDB)) {
			description = "End block. Indicates end of file";
		} else if (block.header.getCode().equals(BlockCodes.ID_FLUIDSIM)) {
			description = "Fluid.";
		} else if (block.header.getCode().equals(BlockCodes.ID_GD)) {
			description = "Grease Pencil.";
		} else if (block.header.getCode().equals(BlockCodes.ID_GLOB)) {
			description = "So called 'file global' contains general information about the file.";
		} else if (block.header.getCode().equals(BlockCodes.ID_GR)) {
			description = "Group block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_IM)) {
			description = "Image block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_IP)) {
			description = "Ipo (depricated, replaced by FCurves).";
		} else if (block.header.getCode().equals(BlockCodes.ID_KE)) {
			description = "Key (shape key).";
		} else if (block.header.getCode().equals(BlockCodes.ID_LA)) {
			description = "Lamp block. Light source specification.";
		} else if (block.header.getCode().equals(BlockCodes.ID_LI)) {
			description = "Library.";
		} else if (block.header.getCode().equals(BlockCodes.ID_LS)) {
			description = "Freestyle line style.";
		} else if (block.header.getCode().equals(BlockCodes.ID_LT)) {
			description = "Lattice.";
		} else if (block.header.getCode().equals(BlockCodes.ID_MA)) {
			description = "Material block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_MB)) {
			description = "Meta Ball.";
		} else if (block.header.getCode().equals(BlockCodes.ID_MC)) {
			description = "Movie clip.";
		} else if (block.header.getCode().equals(BlockCodes.ID_ME)) {
			description = "Mesh.";
		} else if (block.header.getCode().equals(BlockCodes.ID_MSK)) {
			description = "Mask.";
		} else if (block.header.getCode().equals(BlockCodes.ID_NLA)) {
			description = "Used in outliner...";
		} else if (block.header.getCode().equals(BlockCodes.ID_NT)) {
			description = "Node Tree.";
		} else if (block.header.getCode().equals(BlockCodes.ID_OB)) {
			description = "Object block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_PA)) {
			description = "Particle settings.";
		} else if (block.header.getCode().equals(BlockCodes.ID_PO)) {
			description = "Pose.";
		} else if (block.header.getCode().equals(BlockCodes.ID_REND)) {
			description = "Render settings.";
		} else if (block.header.getCode().equals(BlockCodes.ID_SCE)) {
			description = "Scene block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_SCR)) {
			description = "Screen.";
		} else if (block.header.getCode().equals(BlockCodes.ID_SEQ)) {
			description = "fake id used by outliner";
		} else if (block.header.getCode().equals(BlockCodes.ID_SO)) {
			description = "Sound";
		} else if (block.header.getCode().equals(BlockCodes.ID_SPK)) {
			description = "Speaker";
		} else if (block.header.getCode().equals(BlockCodes.ID_TE)) {
			description = "Texture block.";
		} else if (block.header.getCode().equals(BlockCodes.ID_TEST)) {
			description = "Test (?)";
		} else if (block.header.getCode().equals(BlockCodes.ID_TXT)) {
			description = "Text";
		} else if (block.header.getCode().equals(BlockCodes.ID_VF)) {
			description = "Vector Font";
		} else if (block.header.getCode().equals(BlockCodes.ID_WM)) {
			description = "Window Manager";
		} else if (block.header.getCode().equals(BlockCodes.ID_WO)) {
			description = "World";
		} else if (block.header.getCode().equals(BlockCodes.ID_ID)) {
			description = "ID (internal use only).";
		} else if (block.header.getCode().equals(BlockCodes.ID_SCRN)) {
			description = "depricated, but still in use";
		} else if (block.header.getCode().equals(BlockCodes.ID_PAL)) {
			description = "Palette";
		} else if (block.header.getCode().equals(BlockCodes.ID_PC)) {
			description = "PaintCurve";
		} else if (block.header.getCode().equals(BlockCodes.ID_CF)) {
			description = "CacheFile";
		} else if (block.header.getCode().equals(BlockCodes.ID_WS)) {
			description = "WorkSpace";
		} else if (block.header.getCode().equals(BlockCodes.ID_LP)) {
			description = "LightProbe";
		} else {
			description = "no description found";
		}
		html += row("ID:", block.header.getCode(), description);
		html += row("size:", block.header.getSize(), "Size of the data portion.");
		html += row("address:", block.header.getAddress(), "Used to identify the block and resolve pointers.");
		
		
		String typeinfo = "";
		if (ctypes != null) {
			int index = block.header.getSdnaIndex();
			CStruct struct = ctypes.getStruct(index);
			typeinfo = struct.getSignature();
		}
		
		html += row("type:", typeinfo + " (" + block.header.getSdnaIndex() + ")",  "Index into DNA type list." );
		html += row("count:", block.header.getCount(), "Number of data elements of the above type in the data portion.");
		html += "</table>";

		html += "</body></html>";
		
		setText(html.toString());
	}
	
	
	
	
	
	
}
