package engineTester;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;

//TODO: Fix directional collision cause i dont think its getting the right directions but i did just copy+paste the code so like idk just look in the BoundingBox.java file and go to the getDirCollision() method
public class MainGameLoop {
	
	public static boolean running = true;
	public static int tps = 60;
	
	public static Loader loader;
	public static StaticShader shader;
	public static ArrayList<Entity> entities;
	
	public static Light light;
	public static Camera camera;
	
	public static MasterRenderer renderer;
	
	public static long prevTime = 0, newTime = 0;
	
	public static Player player = new Player(0,5,0,3,5,0,0,0);
	public static Terrain terrain;
	public static Terrain terrain2;

	public static ArrayList<Structure> structures = new ArrayList<>();
	public static ArrayList<Terrain> terrains = new ArrayList<>();
	public static ArrayList<Block> blocks = new ArrayList<>();
	
	public static float mouseDX = 0, mouseDY = 0;
	public static int mousePullRate = 120;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		loader = new Loader();
		shader = new StaticShader();
		entities = new ArrayList<>();
		light = new Light(new Vector3f(0,0,0), new Vector3f(1,1,1));
		camera = new Camera();
		renderer = new MasterRenderer();

		//structures.add(new Structure(entities, loader, "roomTest", "roomHigherRes", 0, 0.1f, -20, 0, 90, 0, 1));
		structures.add(new Structure(entities, loader, "cube", "checker", 0, 1f, -20, 0, 90, 0, 1));
		//structures.get(0).getTexture().setUseFakeLighting(false);
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				terrains.add(new Terrain(i,j, loader, new ModelTexture(loader.loadTexture("grassSide"))));
			}
		}
		
		blocks.add(new Block(0,1,-5,2,2,2, "checker", loader, entities, false));

		Mouse.setGrabbed(true);
	    
	    long prevTime = 0, newTime = 0;
	    MousePulling mousePull = new MousePulling();
	    mousePull.start();
	    
	    run();

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	    System.exit(0); //Once done with game loop, shut everything down
		
	}
	
	public static void run() {
		long lastTime = System.nanoTime(); //Init time var
	    final double ns = 1000000000.0 / tps; //Control ticks per second
	    double delta = 0;
	    while(running) { //Game loop boolean (!Display.isCloseRequested() is original incase we need it)
	        long now = System.nanoTime(); //Far for now time
	        delta += (now - lastTime) / ns; //While in running loop, wait until two time vars are equal
	        lastTime = now; //Reset
	        while(delta >= 1) { //Wait until time is less than one
	        	mainLogic();
	        	renderLogic();
	            delta--; //Remove
            }
        }
	}
	
	public static void mainLogic() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) running = false;

		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			structures.get(0).setReflectivity(entities, 0);
			structures.get(0).setShineDamper(entities, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			structures.get(0).setReflectivity(entities, 10);
			structures.get(0).setShineDamper(entities, 10);
		}
		player.update(mouseDX, mouseDY, blocks);
		player.lockCamera(camera);
		
	}
	
	public static void renderLogic() {
		prevTime = System.nanoTime();
    	
    	for (Entity entity : entities) {
			renderer.processEntity(entity);
		}

    	for (Terrain terrain : terrains) {
    		renderer.processTerrain(terrain);
    	}
    	
    	for (Block block : blocks) {
    		renderer.processEntity(entities.get(block.getEntityIndex()));
    	}
    	
		renderer.render(light, camera);
		DisplayManager.updateDisplay();
		
		newTime = System.nanoTime();
    	long elapsed = newTime - prevTime;
    	int fps = (int) (1000000000.0f / (float) elapsed);
    	//System.out.println(fps);
	}
	
	public static class MousePulling extends Thread {
		public void run() {
			long lastTime = System.nanoTime(); //Init time var
		    final double ns = 1000000000.0 / mousePullRate; //Control ticks per second
		    double delta = 0;
		    while(running) { //Game loop boolean (!Display.isCloseRequested() is original incase we need it)
		        long now = System.nanoTime(); //Far for now time
		        delta += (now - lastTime) / ns; //While in running loop, wait until two time vars are equal
		        lastTime = now; //Reset
		        while(delta >= 1) { //Wait until time is less than one
		        	mouseDX = Mouse.getDX();
					mouseDY = Mouse.getDY();
					//if (mouseDX != 0 || mouseDY != 0) System.out.println(mouseDX + ", " + mouseDY);

					if (mouseDX > player.getMaxlookAround()) mouseDX = player.getMaxlookAround();
					if (mouseDX < -player.getMaxlookAround()) mouseDX = -player.getMaxlookAround();
					if (mouseDY > player.getMaxlookAround()) mouseDY = player.getMaxlookAround();
					if (mouseDY < -player.getMaxlookAround()) mouseDY = -player.getMaxlookAround();
		            delta--; //Remove
	            }
	        }
		}
	}
}
