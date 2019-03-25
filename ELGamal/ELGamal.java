import java.math.BigInteger;
import java.util.Random;

public class ELGamal {
	public BigInteger p, alpha, y;
	private BigInteger d;

	public ELGamal() {
		do {
			p = BigInteger.probablePrime(100, new Random());
		} while (p.subtract(BigInteger.ONE).divide(new BigInteger("2")).isProbablePrime(100));
		do {
			alpha = new BigInteger(100, new Random());
		} while (! isOrigin(alpha, p));
		do {
			d = new BigInteger(100, new Random());
		} while (d.compareTo(BigInteger.ONE) != 1 || d.compareTo(p.subtract(BigInteger.ONE)) != -1);
		y = alpha.modPow(d, p);
	}

	public ELGamal(BigInteger p, BigInteger alpha, BigInteger d) {
		this.p = p;
		this.alpha = alpha;
		this.d = d;
		y = alpha.modPow(d, p);
	}

	/**
	 * 加密
	 * @param M
	 * @return
	 */
	BigInteger[] encrypt(BigInteger M) {
		BigInteger[] C = new BigInteger[2];
		BigInteger k, U;
		do {
			do {
				k = new BigInteger(100, new Random());
			} while (k.compareTo(BigInteger.ONE) != 1 || k.compareTo(p.subtract(BigInteger.ONE)) != -1);
			U = y.modPow(k, p);
		} while (U.intValue() != 1);
		C[0] = alpha.modPow(k, p);
		C[1] = U.multiply(M).mod(p);
		return C;
	}

	/**
	 * 加密
	 * @param M
	 * @param k
	 * @return
	 */
	BigInteger[] encrypt(BigInteger M, BigInteger k) {
		BigInteger[] C = new BigInteger[2];
		BigInteger U = y.modPow(k, p);
		C[0] = alpha.modPow(k, p);
		C[1] = U.multiply(M).mod(p);
		return C;
	}

	/**
	 * 解密
	 * @param C
	 * @return
	 */
	BigInteger decrypt(BigInteger[] C) {
		BigInteger V = C[0].modPow(d, p);
		BigInteger M = C[1].multiply(V.modPow(new BigInteger("-1"), p)).mod(p);
		return M;
	}

	/**
	 * 判断a是否为模m的原根，其中m为素数
	 * @param a
	 * @param m
	 * @return
	 */
	static boolean isOrigin(BigInteger a, BigInteger m) {
		if (a.gcd(m).intValue() != 1) return false;
		BigInteger i = new BigInteger("2");
		while (i.compareTo(m.subtract(BigInteger.ONE)) == -1) {
			if (m.mod(i).intValue() == 0) {
				if (a.modPow(i, m).intValue() == 1)
					return false;
				while (m.mod(i).intValue() == 0)
					m = m.divide(i);
			}
			i = i.add(BigInteger.ONE);
		}
		return true;
	}

	public BigInteger getD() {
		return d;
	}
}