package ro.pub.dadgm.pf22.render.objects.game;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.IOException;
import java.io.InputStream;

import ro.pub.dadgm.pf22.activity.MainActivity;
import ro.pub.dadgm.pf22.render.Scene3D;
import ro.pub.dadgm.pf22.render.objects.AbstractObject3D;
import ro.pub.dadgm.pf22.render.utils.BufferUtils;
import ro.pub.dadgm.pf22.render.utils.objloader.OBJParser;
import ro.pub.dadgm.pf22.render.utils.objloader.TDModel;
import ro.pub.dadgm.pf22.render.utils.objloader.TDModelPart;

/**
 * Implements a 3D fighter jet model.
 */
public class FighterJet3D extends AbstractObject3D {

	/**
	 * The asset path of the model's resources.
	 */
	protected final String MODEL_PATH = "objects/f22_raptor/"; 
	
	/**
	 * The fighter jet's model object.
	 */
	protected TDModel modelObj;
	
	
	/**
	 * Initializes the fighter jet 3D object.
	 *
	 * @param scene The parent scene object.
	 * @param tag An optional tag.
	 * @param priority An optional priority.
	 */
	public FighterJet3D(Scene3D scene, String tag, int priority) {
		super(scene, tag, priority);
		
		// get shader program
		shader = scene.getShaderManager().getShader("s3d_simple_ilum");
		
		// load the object's assets
		OBJParser parser = new OBJParser();
		InputStream modelStream, materialStream = null;
		try {
			modelStream = MainActivity.getAppContext().getAssets().open(MODEL_PATH + "model.obj");
			materialStream = MainActivity.getAppContext().getAssets().open(MODEL_PATH + "materials.mtl");
			
		} catch (IOException e) {
			throw new RuntimeException("Unable to read fighter jet model file!", e);
		}
		
		modelObj = parser.parseOBJ(modelStream, materialStream);
	}
	
	@Override
	public void draw() {
		Matrix.setIdentityM(modelMatrix, 0);
		// Matrix.translateM(modelMatrix, 0, 2f, 2f, -3f);
		Matrix.scaleM(modelMatrix, 0, 0.3f, 0.3f, 0.3f);
		Matrix.rotateM(modelMatrix, 0, -90, 1, 0, 0);
		
		float[] normalMatrix = scene.getCamera().computeNormalMatrix(modelMatrix);
		
		shader.use();
		
		// get shader attributes' locations
		int a_position = shader.getAttribLocation("a_position");
		int a_normal = shader.getAttribLocation("a_normal");
		
		// get shader uniforms' locations
		int u_normalMatrix = shader.getUniformLocation("u_normalMatrix");
		int u_modelMatrix = shader.getUniformLocation("u_modelMatrix");
		
		// send the matrices
		GLES20.glUniformMatrix4fv(u_modelMatrix, 1, false, modelMatrix, 0);
		GLES20.glUniformMatrix4fv(u_normalMatrix, 1, false, normalMatrix, 0);
		
		// send the vertex data to the shader
		GLES20.glEnableVertexAttribArray(a_position);
		GLES20.glVertexAttribPointer(a_position, 3 /* coords */, GLES20.GL_FLOAT, false,
				3 * 4 /* bytes */, modelObj.getVertexBuf());

		GLES20.glEnableVertexAttribArray(a_normal);
		GLES20.glVertexAttribPointer(a_normal, 3 /* coords */, GLES20.GL_FLOAT, false,
				3 * 4 /* bytes */, modelObj.getNormalBuf());
		
		// send the faces (parts)
		for (TDModelPart part: modelObj.getParts()) {
			// draw!
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, part.getFacesCount(), GLES20.GL_UNSIGNED_SHORT, part.getFaceBuffer());
		}
	}
	
}