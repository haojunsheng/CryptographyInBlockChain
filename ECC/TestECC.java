import java.math.BigInteger;

public class TestECC {
	public static void main(String[] args) {
		BigInteger M = new BigInteger("1234567890abcdef", 16);
		System.out.println("明文：M=" + M.toString(16));
		ECC_p ecc;
		BigInteger k = new BigInteger("abcdef", 16);
		BigInteger[] C;
		ECPoint X1;

		System.out.println("P-192");
		ecc = new ECC_p(192);
		C = ecc.encrypt(M, k);
		X1 = new ECPoint(C[0], C[1]);
		System.out.println("密文：(" + X1 + ", " + C[2].toString(16) +")");
		M = ecc.decrypt(C);
		System.out.println("明文：M=" + M.toString(16));

		System.out.println("P-224");
		ecc = new ECC_p(224);
		C = ecc.encrypt(M, k);
		X1 = new ECPoint(C[0], C[1]);
		System.out.println("密文：(" + X1 + ", " + C[2].toString(16) +")");
		M = ecc.decrypt(C);
		System.out.println("明文：M=" + M.toString(16));

		System.out.println("P-256");
		ecc = new ECC_p(256);
		C = ecc.encrypt(M, k);
		X1 = new ECPoint(C[0], C[1]);
		System.out.println("密文：(" + X1 + ", " + C[2].toString(16) +")");
		M = ecc.decrypt(C);
		System.out.println("明文：M=" + M.toString(16));

		System.out.println("P-384");
		ecc = new ECC_p(384);
		C = ecc.encrypt(M, k);
		X1 = new ECPoint(C[0], C[1]);
		System.out.println("密文：(" + X1 + ", " + C[2].toString(16) +")");
		M = ecc.decrypt(C);
		System.out.println("明文：M=" + M.toString(16));

		System.out.println("P-521");
		ecc = new ECC_p(521);
		C = ecc.encrypt(M, k);
		X1 = new ECPoint(C[0], C[1]);
		System.out.println("密文：(" + X1 + ", " + C[2].toString(16) +")");
		M = ecc.decrypt(C);
		System.out.println("明文：M=" + M.toString(16));
	}
}