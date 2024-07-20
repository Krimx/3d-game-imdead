package engineTester;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class Structure {
	private Entity entity;
	private TexturedModel staticModel;
	private ModelTexture texture;
	private int entityIndex;
	
	private float x,y,z,rotX,rotY,rotZ,scale;
	
	public Structure(ArrayList<Entity> entities, Loader loader, String fileName, String textureName, float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		

		ModelData data = OBJFileLoader.loadOBJ(fileName);
		RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		this.staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(textureName)));
		this.texture = staticModel.getTexture();
		texture.setShineDamper(0f);
		texture.setReflectivity(0f);

		Entity entity = new Entity(staticModel, new Vector3f(this.x, this.y, this.z), this.rotX, this.rotY, this.rotZ, this.scale);
		
		entityIndex = entities.size();
		entities.add(entity);
	}
	
	public void setShineDamper(ArrayList<Entity> entities, float shineDamper) {
		entities.get(this.entityIndex).getModel().getTexture().setShineDamper(shineDamper);
	}
	
	public void setReflectivity(ArrayList<Entity> entities, float reflectivity) {
		entities.get(this.entityIndex).getModel().getTexture().setReflectivity(reflectivity);
	}
	
	public ModelTexture getTexture() {return this.texture;}
}
