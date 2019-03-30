import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EC_p {
	BigInteger p;
	BigInteger a;
	BigInteger b;

	public EC_p(BigInteger p, BigInteger a, BigInteger b) {
		this.p = p;
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return "y²=x³+" + a.toString(16) +"x+" + b.toString(16) + "(mod " + p.toString(16) + ")";
	}

	/**
	 * 两解点相加
	 * @param p1
	 * @param p2
	 * @return
	 */
	ECPoint add(ECPoint p1, ECPoint p2) {
		if (p1.isO()) return p2;
		if (p2.isO()) return p1;
		ECPoint p3 = new ECPoint();
		BigInteger lambda;
		if (p1.x.compareTo(p2.x) == 0) {
			if (p1.y.compareTo(p2.y) == 0) {
				lambda = new BigInteger("3").multiply(p1.x.pow(2)).add(a).multiply(new BigInteger("2").multiply(p1.y).modPow(new BigInteger("-1"), p)).mod(p);
				p3.x = lambda.pow(2).subtract(new BigInteger("2").multiply(p1.x)).mod(p);
				p3.y = lambda.multiply(p1.x.subtract(p3.x)).subtract(p1.y).mod(p);
				return p3;
			}
			if (p1.y.compareTo(p.subtract(p2.y)) == 0) 
				return p3;
		}
		lambda = p2.y.subtract(p1.y).multiply(p2.x.subtract(p1.x).modPow(new BigInteger("-1"), p)).mod(p);
		p3.x = lambda.pow(2).subtract(p1.x).subtract(p2.x).mod(p);
		p3.y = lambda.multiply(p1.x.subtract(p3.x)).subtract(p1.y).mod(p);
		return p3;
	}

	/**
	 * 倍乘
	 * @param p
	 * @param n
	 * @return  np
	 */
	ECPoint multiply(ECPoint p, BigInteger n) {
		ECPoint q = add(p, new ECPoint());
		ECPoint r = new ECPoint();
		do {
			if (n.and(new BigInteger("1")).intValue() == 1) 
				r = add(r, q);
			q = add(q, q);
			n = n.shiftRight(1);
		} while (n.intValue() != 0);
		return r;
	}

	/**
	 * 求阶
	 * @param p  生成元
	 * @return   p对应的阶
	 */
	BigInteger o(ECPoint p) {
		BigInteger r = new BigInteger("1");
		while (! p.isO()) {
			r = r.add(new BigInteger("1"));
			p = multiply(p, r);
		}
		return r;
	}

	/**
	 * 求所有解点
	 * @return
	 */
	List<ECPoint> solutionPoints() {
		List<ECPoint> r = new ArrayList<ECPoint>();
		List<BigInteger> l = new ArrayList<BigInteger>();
		for (BigInteger y = new BigInteger("1"); y.compareTo(p.divide(new BigInteger("2"))) != 1; y = y.add(new BigInteger("1"))) 
			l.add(y.modPow(new BigInteger("2"), p));
		for (BigInteger x = new BigInteger("0"); x.compareTo(p) == -1; x = x.add(new BigInteger("1"))) {
			BigInteger t = x.pow(3).add(a.multiply(x)).add(b).mod(p);
			if (isExist(t, l) != -1) {
				BigInteger y = new BigInteger(isExist(t, l) + "");
				r.add(new ECPoint(x, y));
				r.add(new ECPoint(x, p.subtract(y)));
			}
		}
		r.add(new ECPoint());
		return r;
	}
	static int isExist(BigInteger b, List<BigInteger> l) {
		for (int i = 0; i < l.size(); i++) 
			if (l.get(i).compareTo(b) == 0) return (i + 1);
		return -1;
	}
}