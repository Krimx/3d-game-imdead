package engineTester;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Player {
	private float x,y,z;
	private float width, height;
	private Head head;
	private float moveSpeed, walkSpeed, runSpeed, sensititivy, lockPitch, maxLookAround;
	private float vForward,vy,vSide;
	private float jumpPower, gravity, acceleration;
	private BoundingBox hitbox;
	
	public float getX() {return x;}
	public void setX(float x) {this.x = x;}
	public float getY() {return y;}
	public void setY(float y) {this.y = y;}
	public float getZ() {return z;}
	public void setZ(float z) {this.z = z;}
	public Head getHead() {return head;}
	public void setHead(Head head) {this.head = head;}

	public float getMaxlookAround() {return this.maxLookAround;}
	public void setMaxLookAround(float maxLookAround) {this.maxLookAround = maxLookAround;}

	public Player() {
		this(0,0,0,2,3,0,0,0);
	}
	
	public Player(float x, float y, float z, float width, float height, float pitch, float yaw, float roll) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		
		this.head = new Head(pitch, yaw, roll);
		
		this.moveSpeed = 0.6f;
		this.walkSpeed = 0.3f;
		this.runSpeed = 0.5f;
		this.lockPitch = 90f;
		this.sensititivy = 0.12f;
		this.maxLookAround = 250;
		
		this.vForward = 0;
		this.vy = 0;
		this.vSide = 0;
		
		this.jumpPower = 1;
		this.gravity = 0.06f;
		this.acceleration = 0.2f;
		
		this.hitbox = new BoundingBox(this.x ,this.y, this.z, this.width, this.height, this.width);
	}
	
	class Head {
		private float pitch, yaw, roll;
		private float x,y,z;
		
		public Head(float pitch, float yaw, float roll) {
			this.pitch = pitch;
			this.yaw = yaw;
			this.roll = roll;
		}

		public float getPitch() {return pitch;}
		public void setPitch(float pitch) {this.pitch = pitch;}
		public float getYaw() {return yaw;}
		public void setYaw(float yaw) {this.yaw = yaw;}
		public float getRoll() {return roll;}
		public void setRoll(float roll) {this.roll = roll;}
		public float getX() {return this.x;}
		public float getY() {return this.y;}
		public float getZ() {return this.z;}
		
		public void update(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public void calculatePitch(float dy, boolean alwaysActive, float sensitivity, float lockPitch) {
			if (Mouse.isButtonDown(1) || alwaysActive) {
				float pitchChange = dy * sensitivity;
				this.pitch -= pitchChange;
				
				if (lockPitch > 0) {
					if (this.pitch < -lockPitch) this.pitch = -lockPitch;
					if (this.pitch > lockPitch) this.pitch = lockPitch;
				}
			}
		}
		
		public void calculateYaw(float dx, boolean alwaysActive, float sensitivity) {
			if (Mouse.isButtonDown(1) || alwaysActive) {
				float yawChange = dx * sensitivity;
				this.yaw += yawChange;
			}
		}
		
		public void lookAround(float dx, float dy, boolean alwaysActive, float sensitivity, float lockPitch) {
			calculatePitch(dy, alwaysActive, sensitivity, lockPitch);
			calculateYaw(dx, alwaysActive, sensitivity);
		}
	}
	
	public void update(float mouseDX, float mouseDY, ArrayList<Block> blocks) {
		lookAround(mouseDX, mouseDY);
		this.head.update(this.x, this.y + (this.height / 2.0f), this.z);
		moveWithVelocity();
		
		//Gravity
		if (this.y > 0) this.vy -= this.gravity;
		for (float i = 0; i < Math.abs(this.vy); i += 0.1) {
			i = Math.round(i * 10.0f) / 10.0f; //Round to nearest tenth because computers cant do math with decimals

			if (this.vy < 0) this.y -= 0.1f;
			if (this.vy > 0) this.y += 0.1f;
			this.y = Math.round(this.y * 10.0f) / 10.0f;
		}
		if (this.y - (this.height / 2.0f) < 0) this.y = 0 + (this.height / 2.0f);
		
		//Jumping
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (this.y - (this.height / 2.0f) == 0) {
				this.vy = this.jumpPower;
			}
		}
		
		for (Block block : blocks) {
			int dir = this.hitbox.getDirCollision(block.getHitbox());
			if (dir > -1) System.out.println(dir);
		}
	}
	
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.z -= Math.cos(Math.toRadians(-this.head.getYaw())) * this.moveSpeed;
			this.x -= Math.sin(Math.toRadians(-this.head.getYaw())) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.z += Math.cos(Math.toRadians(-this.head.getYaw())) * this.moveSpeed;
			this.x += Math.sin(Math.toRadians(-this.head.getYaw())) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.z -= Math.sin(Math.toRadians(this.head.getYaw())) * this.moveSpeed;
			this.x -= Math.cos(Math.toRadians(this.head.getYaw())) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.z += Math.sin(Math.toRadians(this.head.getYaw())) * this.moveSpeed;
			this.x += Math.cos(Math.toRadians(this.head.getYaw())) * this.moveSpeed;
		}
		
		//Flying stuff ig
		/*
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			this.y += this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.y -= this.moveSpeed;
		}
		*/
	}
	
	public void moveWithVelocity() {
		boolean W = false, A = false, S = false, D = false, F = false;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) W = true;
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) S = true;
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) D = true;
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) A = true;
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) F = true;
		
		if (W) {
			if (this.vForward < this.walkSpeed) this.vForward += this.acceleration;
			//Running if holding F
			if (F) {
				if (this.vForward < this.runSpeed) this.vForward += this.acceleration;
			}
		}
		if (S) if (this.vForward > -this.walkSpeed) this.vForward -= this.acceleration;
		if ((W && S) || (!W && !S)) {
			if (this.vForward > 0) this.vForward -= this.acceleration;
			if (this.vForward < 0) this.vForward += this.acceleration;
		}

		if (A) if (this.vSide < this.walkSpeed) this.vSide += this.acceleration;
		if (D) if (this.vSide > -this.walkSpeed) this.vSide -= this.acceleration;
		if ((A && D) || (!A && !D)) {
			if (this.vSide > 0) this.vSide -= this.acceleration;
			if (this.vSide < 0) this.vSide += this.acceleration;
		}

		//Floating point math fix (makes it 0 if its close to 0)
		if (vForward > 0 && vForward < 0.00001f) vForward = 0;
		if (vForward < 0 && vForward > -0.00001f) vForward = 0;
		if (vSide > 0 && vSide < 0.00001f) vSide = 0;
		if (vSide < 0 && vSide > -0.00001f) vSide = 0;
		
		if (W) {
			//System.out.println((Math.cos(Math.toRadians(-this.head.getYaw())) * this.vForward) + ", " + Math.sin(Math.toRadians(-this.head.getYaw())) * this.vForward);
			this.z -= Math.cos(Math.toRadians(-this.head.getYaw())) * this.vForward;
			this.x -= Math.sin(Math.toRadians(-this.head.getYaw())) * this.vForward;
		}
		if (S) {
			this.z -= Math.cos(Math.toRadians(-this.head.getYaw())) * this.vForward;
			this.x -= Math.sin(Math.toRadians(-this.head.getYaw())) * this.vForward;
		}
		if (A) {
			this.z -= Math.sin(Math.toRadians(this.head.getYaw())) * this.vSide;
			this.x -= Math.cos(Math.toRadians(this.head.getYaw())) * this.vSide;
		}
		if (D) {
			this.z -= Math.sin(Math.toRadians(this.head.getYaw())) * this.vSide;
			this.x -= Math.cos(Math.toRadians(this.head.getYaw())) * this.vSide;
		}
		
		this.hitbox.update(this.x ,this.y, this.z, this.width, this.height, this.width);
	}
	
	public void lookAround(float dx, float dy) {
		this.head.lookAround(dx, dy, true, sensititivy, lockPitch);
	}
	
	public void lockCamera(Camera camera) {
		camera.setPitch(this.head.getPitch());
		camera.setYaw(this.head.getYaw());
		camera.setRoll(this.head.getRoll());
		camera.setPosition(new Vector3f(this.head.getX(), this.head.getY(), this.head.getZ()));
	}
}
