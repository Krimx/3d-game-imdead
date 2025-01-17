package renderEngine;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	public static int WIDTH = 800, HEIGHT = 800;
	public static int FPS_CAP = 60;
	public static String TITLE = "LWJGL Project by ThinMatrix";

	public static void createDisplay() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		//WIDTH = (int) size.getWidth();
		//HEIGHT = (int) size.getHeight();
		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(TITLE);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static void setTitle(String title) {
		TITLE = title;
		Display.setTitle(TITLE);
	}
}
