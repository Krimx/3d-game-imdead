package engineTester;


public class BoundingBox {
	private float x,y,z,width,height,depth;
	private float l,r,f,b,u,d;

	public BoundingBox(float x, float y, float z, float width, float height, float depth) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.depth = depth;

		this.l = this.x - (this.width / 2.0f);
		this.r = this.x + (this.width / 2.0f);
		this.f = this.z - (this.depth / 2.0f);
		this.b = this.z + (this.depth / 2.0f);
		this.u = this.y + (this.height / 2.0f);
		this.d = this.y - (this.height / 2.0f);
	}
	
	public BoundingBox(BoundingBox template) {
		this.x = template.getX();
		this.y = template.getY();
		this.z = template.getZ();
		this.width = template.getWidth();
		this.height = template.getHeight();
		this.depth = template.getDepth();

		this.l = this.x - (this.width / 2.0f);
		this.r = this.x + (this.width / 2.0f);
		this.f = this.z - (this.depth / 2.0f);
		this.b = this.z + (this.depth / 2.0f);
		this.u = this.y + (this.height / 2.0f);
		this.d = this.y - (this.height / 2.0f);
	}
	
	public BoundingBox getClone() {
		return new BoundingBox(this.x, this.y, this.z, this.width, this.height, this.depth);
	}

	public float getX() {return x;}
	public void setX(float x) {this.x = x;}
	public float getY() {return y;}
	public void setY(float y) {this.y = y;}
	public float getZ() {return z;}
	public void setZ(float z) {this.z = z;}
	public float getWidth() {return width;}
	public void setWidth(float width) {this.width = width;}
	public float getHeight() {return height;}
	public void setHeight(float height) {this.height = height;}
	public float getDepth() {return depth;}
	public void setDepth(float depth) {this.depth = depth;}
	public float getLeft() {return this.l;}
	public float getRight() {return this.r;}
	public float getForward() {return this.f;}
	public float getBack() {return this.b;}
	public float getUp() {return this.u;}
	public float getDown() {return this.d;}
	
	public void update(float x, float y, float z, float width, float height, float depth) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.depth = depth;

		this.l = this.x - (this.width / 2.0f);
		this.r = this.x + (this.width / 2.0f);
		this.f = this.z - (this.depth / 2.0f);
		this.b = this.z + (this.depth / 2.0f);
		this.u = this.y + (this.height / 2.0f);
		this.d = this.y - (this.height / 2.0f);
	}
	
	public int getDirCollision(BoundingBox b) {
		int dir = -1;
		if (b == null) return dir;
		else {
			if (isColliding(b)) {
				float dLeft = 0;
				float dRight = 0;
				float dUp = 0;
				float dDown = 0;
				float dForward = 0;
				float dBack = 0;
				
				/*
				 * What this is intended to do is move a fake hitbox a little bit in each direction until the fake hitbox is no longer colliding.
				 * Based on whichever direction it was moved in when it stopped colliding, it outputs a number to represent the direction.
				 * Main issue is that im unfamiliar with working in 3d so im rly tripped up. Plus i just copy+pasted this from a 2d engine that i made so it probably didnt translate too well
				 */
				
				//TODO fix this cause i dont think its right
				while (true) {
					BoundingBox temp;
					
					temp = new BoundingBox(this.x, this.y + dUp, this.z, this.width, this.height, this.depth);
					if (!isColliding(temp, b)) {
						dir = 0;
						break;
					}
					else dUp += 0.1;
					dUp = round(dUp);
					
					temp = new BoundingBox(this.x + dRight, this.y, this.z, this.width, this.height, this.depth);
					if (!isColliding(temp, b)) {
						dir = 1;
						break;
					}
					else dRight += 0.1;
					dRight = round(dRight);

					temp = new BoundingBox(this.x, this.y + dDown, this.z, this.width, this.height, this.depth);
					if (!isColliding(temp, b)) {
						dir = 2;
						break;
					}
					else dDown -= 0.1;
					dDown = round(dDown);

					temp = new BoundingBox(this.x + dLeft, this.y, this.z, this.width, this.height, this.depth);
					if (!isColliding(temp, b)) {
						dir = 3;
						break;
					}
					else dLeft -= 0.1;
					dLeft = round(dLeft);

					temp = new BoundingBox(this.x, this.y, this.z + dForward, this.width, this.height, this.depth);
					if (!isColliding(temp, b)) {
						dir = 4;
						break;
					}
					else dForward -= 0.1;
					dForward = round(dForward);

					temp = new BoundingBox(this.x, this.y, this.z + dBack, this.width, this.height, this.depth);
					if (!isColliding(temp, b)) {
						dir = 5;
						break;
					}
					else dBack -= 0.1;
					dBack = round(dBack);
				}
			}
		}

		
		
		return dir;
	}
	
	public float round(float in) {
		return Math.round(in*100)/100.0f;
	}
	
	public boolean isColliding (BoundingBox b) {
		if (b == null) return false;
		else {
			if (
					(this.l <= b.getRight()) &&
					(this.r >= b.getLeft()) &&
					(this.f <= b.getBack()) &&
					(this.b >= b.getForward()) &&
					(this.d <= b.getUp()) &&
					(this.u >= b.getDown())) {
				return true;
			}
			
			else {
				return false;
			}
		}
	}
	
	public boolean isColliding (BoundingBox a, BoundingBox b) {
		if (b == null) return false;
		else {
			if (
					(a.getLeft() <= b.getRight()) &&
					(a.getRight() >= b.getLeft()) &&
					(a.getForward() <= b.getBack()) &&
					(a.getBack() >= b.getForward()) &&
					(a.getDown() <= b.getUp()) &&
					(a.getUp() >= b.getDown())) {
				return true;
			}
			
			else {
				return false;
			}
		}
	}
}