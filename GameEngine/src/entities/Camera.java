package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch, yaw, roll, sensitivity = 0.1f, moveSpeed = 1f;
	private float lockPitch = 0, lockYaw = 0;
	
	public float getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}

	public Camera() {
		
	}
	
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= Math.cos(Math.toRadians(-this.yaw)) * this.moveSpeed;
			position.x -= Math.sin(Math.toRadians(-this.yaw)) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += Math.cos(Math.toRadians(-this.yaw)) * this.moveSpeed;
			position.x += Math.sin(Math.toRadians(-this.yaw)) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.z += Math.sin(Math.toRadians(this.yaw)) * this.moveSpeed;
			position.x += Math.cos(Math.toRadians(this.yaw)) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.z -= Math.sin(Math.toRadians(this.yaw)) * this.moveSpeed;
			position.x -= Math.cos(Math.toRadians(this.yaw)) * this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += this.moveSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= this.moveSpeed;
		}
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getLockPitch() {
		return lockPitch;
	}

	public void setLockPitch(float lockPitch) {
		this.lockPitch = lockPitch;
	}

	public float getLockYaw() {
		return lockYaw;
	}

	public void setLockYaw(float lockYaw) {
		this.lockYaw = lockYaw;
	}

	public void lookAround(boolean alwaysActive) {
		calculatePitch(alwaysActive);
		calculateYaw(alwaysActive);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public void calculatePitch(boolean alwaysActive) {
		if (Mouse.isButtonDown(1) || alwaysActive) {
			float pitchChange = Mouse.getDY() * this.sensitivity * 2;
			this.pitch -= pitchChange;
			
			if (this.lockPitch > 0) {
				if (this.pitch < -lockPitch) this.pitch = -lockPitch;
				if (this.pitch > lockPitch) this.pitch = lockPitch;
			}
		}
	}
	
	public void calculateYaw(boolean alwaysActive) {
		if (Mouse.isButtonDown(1) || alwaysActive) {
			float yawChange = Mouse.getDX() * this.sensitivity;
			this.yaw += yawChange;
		}
	}
}
