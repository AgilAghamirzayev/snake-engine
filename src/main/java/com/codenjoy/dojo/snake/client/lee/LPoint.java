package com.codenjoy.dojo.snake.client.lee;

public class LPoint {

	public final int x;
	public final int y;

	public LPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static LPoint of(int x, int y){
		return new LPoint(x,y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public LPoint move(LPoint delta){
		return new LPoint(
				  this.x + delta.x,
				  this.y + delta.y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LPoint lPoint = (LPoint) o;
		return x == lPoint.x &&
				y == lPoint.y;
	}

	@Override
	public int hashCode() {
		return x << 16 + y;
	}

	@Override
	public String toString() {
      return String.format("[%d:%d]",x,y);
	}

}
