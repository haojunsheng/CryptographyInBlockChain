import java.math.BigInteger;
import java.util.Random;

public class ECC_p {
	public static BigInteger p, a, b, x, y, n, h;
	static EC_p ec;
	public static ECPoint G, Q;
	private static BigInteger d;

	public ECC_p() {
		int k = new Random().nextInt(5);
		switch (k) {
			case 0 : 
				init(192);
				break;
			case 1 : 
				init(244);
				break;
			case 2 : 
				init(256);
				break;
			case 3 : 
				init(384);
				break;
			case 4 : 
				init(521);
				break;
		}
	}

	public ECC_p(int k) {
		init(k);
	}

	static void init(int k) {
		switch (k) {
			case 192 : 
				p = new BigInteger("2").pow(192).subtract(new BigInteger("2").pow(64)).subtract(new BigInteger("1"));
				a = new BigInteger("-3");
				b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
				x = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16);
				y = new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16);
				n = new BigInteger("ffffffffffffffffffffffff99def836146bc9b1b4d22831", 16);
				h = new BigInteger("1");
				break;
			case 224 : 
				p = new BigInteger("2").pow(224).subtract(new BigInteger("2").pow(96)).subtract(new BigInteger("1"));
				a = new BigInteger("-3");
				b = new BigInteger("b4050a850c04b3abf54132565044b0b7d7bfd8ba270b39432355ffb4", 16);
				x = new BigInteger("b70e0cbd6bb4bf7f321390b94a03c1d356c21122343280d6115c1d21", 16);
				y = new BigInteger("bd376388b5f723fb4c22dfe6cd4375a05a07476444d5819985007e34", 16);
				n = new BigInteger("ffffffffffffffffffffffffffff16a2e0b8f03e13dd29455c5c2a3d", 16);
				h = new BigInteger("1");
				break;
			case 256 : 
				p = new BigInteger("2").pow(256).subtract(new BigInteger("2").pow(224)).add(new BigInteger("2").pow(192)).add(new BigInteger("2").pow(96)).subtract(new BigInteger("1"));
				a = new BigInteger("-3");
				b = new BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16);
				x = new BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16);
				y = new BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16);
				n = new BigInteger("ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551", 16);
				h = new BigInteger("1");
				break;
			case 384 : 
				p = new BigInteger("2").pow(384).subtract(new BigInteger("2").pow(128)).subtract(new BigInteger("2").pow(96)).add(new BigInteger("2").pow(32)).subtract(new BigInteger("1"));
				a = new BigInteger("-3");
				b = new BigInteger("b3312fa7e23ee7e4988e056be3f82d19181d9c6efe8141120314088f5013875ac656398d8a2ed19d2a85c8edd3ec2aef", 16);
				x = new BigInteger("aa87ca22be8b05378eb1c71ef320ad746e1d3b628ba79b9859f741e082542a385502f25dbf55296c3a545e3872760ab7", 16);
				y = new BigInteger("3617de4a96262c6f5d9e98bf9292dc29f8f41dbd289a147ce9da3113b5f0b8c00a60b1ce1d7e819d7a431d7c90ea0e5f", 16);
				n = new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffc7634d81f4372ddf581a0db248b0a77aecec196accc52973", 16);
				h = new BigInteger("1");
				break;
			case 521 : 
				p = new BigInteger("2").pow(521).subtract(new BigInteger("1"));
				a = new BigInteger("-3");
				b = new BigInteger("00000051953eb9618e1c9a1f929a21a0b68540eea2da725b99b315f3b8b489918ef109e156193951ec7e937b1652c0bd3bb1bf073573df883d2c34f1ef451fd46b503f00", 16);
				x = new BigInteger("000000c6858e06b70404e9cd9e3ecb662395b4429c648139053fb521f828af606b4d3dbaa14b5e77efe75928fe1dc127a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66", 16);
				y = new BigInteger("0000011839296a789a3bc0045c8a5fb42c7d1bd998f54449579b446817afbd17273e662c97ee72995ef42640c550b9013fad0761353c7086a272c24088be94769fd16650", 16);
				n = new BigInteger("000001ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff51868783bf2f966b7fcc0148f709a5d03bb5c9b8899c47aebb6fb71e91386409", 16);
				h = new BigInteger("1");
				break;
		}
		ec = new EC_p(p, a, b);
		G = new ECPoint(x, y);
		d = new BigInteger(n.bitLength(), new Random());
		Q = ec.multiply(G, d);
	}

	/**
	 * 加密
	 * @param M
	 * @return
	 */
	BigInteger[] encrypt(BigInteger M) {
		BigInteger k;
		ECPoint X1, X2;
		do {
			k = new BigInteger(n.bitLength(), new Random());
		} while ((X2 = ec.multiply(Q, k)).x == null);
		X1 = ec.multiply(G, k);
		BigInteger[] C = new BigInteger[3];
		C[0] = X1.x;
		C[1] = X1.y;
		C[2] = M.multiply(X2.x).mod(n);
		return C;
	}

	/**
	 * 加密
	 * @param M
	 * @param k
	 * @return
	 */
	BigInteger[] encrypt(BigInteger M, BigInteger k) {
		ECPoint X1 = ec.multiply(G, k);
		ECPoint X2 = ec.multiply(Q, k);
		BigInteger[] C = new BigInteger[3];
		C[0] = X1.x;
		C[1] = X1.y;
		C[2] = M.multiply(X2.x).mod(n);
		return C;
	}

	/**
	 * 解密
	 * @param C
	 * @return
	 */
	BigInteger decrypt(BigInteger[] C) {
		ECPoint X1 = new ECPoint(C[0], C[1]);
		ECPoint X2 = ec.multiply(X1, d);
		BigInteger M = C[2].multiply(X2.x.modPow(new BigInteger("-1"), n)).mod(n);
		return M;
	}
}