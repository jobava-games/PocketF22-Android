package ro.pub.dadgm.pf22.render.utils.objloader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Parses a Wavefront .MTL (Materials) file.
 * 
 * <p>Based on http://sourceforge.net/projects/objloaderforand/</p>
 */
public class MTLParser {
	
	public static Vector<Material> loadMTL(String file) {
		BufferedReader reader;
		Vector<Material> materials = new Vector<>();
		String line;
		Material currentMtl = null;
		try { //try to open file
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load materials file " + file + "!", e);
		}
		try { // try to read lines of the file
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("newmtl")) {
					if (currentMtl != null)
						materials.add(currentMtl);
					String mtName = line.split("[ ]+", 2)[1];
					currentMtl = new Material(mtName);
					
				} else if (line.startsWith("Ka")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setAmbientColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
					
				} else if (line.startsWith("Kd")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setDiffuseColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
					
				} else if (line.startsWith("Ks")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setSpecularColor(Float.parseFloat(str[1]), Float.parseFloat(str[2]), Float.parseFloat(str[3]));
					
				} else if (line.startsWith("Tr") || line.startsWith("d")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setAlpha(Float.parseFloat(str[1]));
					
				} else if (line.startsWith("Ns")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setShine(Float.parseFloat(str[1]));
					
				} else if (line.startsWith("illum")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setIllum(Integer.parseInt(str[1]));
					
				} else if (line.startsWith("map_Ka")) {
					if (currentMtl == null) continue;
					String[] str = line.split("[ ]+");
					currentMtl.setTextureFile(str[1]);
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Parsing .mtl file failed!", e);
		}
		
		return materials;
	}
}
