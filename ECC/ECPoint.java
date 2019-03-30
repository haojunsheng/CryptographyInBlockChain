import java.math.BigInteger;

public class ECPoint {
	BigInteger x;
	BigInteger y;

	public ECPoint() {
		x = null;
		y = null;
	}

	public ECPoint(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		if (isO()) 
			return "O";
		return "(" + x.toString(16) + ", " + y.toString(16) + ")";
	}

	boolean isO() {
		if (x == null && y == null) 
			return true;
		return false;
	}
}