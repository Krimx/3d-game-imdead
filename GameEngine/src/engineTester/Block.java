package engineTester;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.Loader;
import textures.ModelTexture;

public class Block {
	private float x,y,z,w,h,d;
	private BoundingBox hitbox;

	private Entity entity;
	private TexturedModel staticModel;
	private ModelTexture texture;
	private int entityIndex;
	
	public Block(float x, float y, float z, float w, float h, float d, String textureFileName, Loader loader, ArrayList<Entity> entities, boolean cubeWrap) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = h;
		this.d = d;
		this.hitbox = new BoundingBox(x,y,z,w,h,d);

		if (!cubeWrap) {
			ModelData data = OBJFileLoader.loadOBJ("cube");
			RawModel model = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
			this.staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(textureFileName)));
			this.texture = staticModel.getTexture();
			texture.setShineDamper(0f);
			texture.setReflectivity(0f);
			
			Entity entity = new Entity(staticModel, new Vector3f(this.x, this.y, this.z), 0,0,0, 1);
			
			entityIndex = entities.size();
			entities.add(entity);
		}
		/*
		else {
			RawModel model = loader.loadToVAO(verts, textureCoords, normals, indices);
			this.staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture(textureFileName)));
			this.texture = staticModel.getTexture();
			texture.setShineDamper(0f);
			texture.setReflectivity(0f);

			Entity entity = new Entity(staticModel, new Vector3f(this.x, this.y, this.z), 0,0,0, 1);
			
			entityIndex = entities.size();
			entities.add(entity);
		}
		*/
		
		
	}
	public int getEntityIndex() {return this.entityIndex;}
	public float getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public float getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public float getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public float getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	public BoundingBox getHitbox() {
		return hitbox;
	}
	public void setHitbox(BoundingBox hitbox) {
		this.hitbox = hitbox;
	}
	
	
}
