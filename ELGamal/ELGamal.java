import java.math.BigInteger;
import java.util.Random;

public class ELGamal {
	public BigInteger p, alpha, y;
	private BigInteger d;

	public ELGamal() {
		do {
			//1. 随机地选择一个大素数p，且要求p-1有大素数因子，将p公开。
			p = BigInteger.probablePrime(100, new Random());
		} while (p.subtract(BigInteger.ONE).divide(new BigInteger("2")).isProbablePrime(100));
		do {
			//2.选择一个模p的原根α，并将α公开。
			alpha = new BigInteger(100, new Random());
		} while (! isOrigin(alpha, p));
		do {
			//3.随机地选择一个整数d（1＜d＜p-1）作为私钥，并对d保密。
			d = new BigInteger(100, new Random());
		} while (d.compareTo(BigInteger.ONE) != 1 || d.compareTo(p.subtract(BigInteger.ONE)) != -1);
		//4.计算公钥y=α^d(mod p)，并将y公开。
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
	 * 随机地选取一个整数k（1＜k＜p-1）
	 * 计算U=yk(mod p)、C1=αk(mod p)、C2=UM(mod p)
	 * 取(C1,C2)作为密文
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
	 * 计算V=C1^d(mod p);
	 * 计算M=C2V^-1(mod p)
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