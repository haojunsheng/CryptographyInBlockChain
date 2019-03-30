import java.math.BigInteger;

public class TestELGamal {
	public static void main(String[] args) {
		BigInteger p = new BigInteger("2579");
		BigInteger alpha = new BigInteger("2");
		BigInteger d = new BigInteger("765");
		ELGamal elgamal = new ELGamal(p, alpha, d);
		System.out.println("p=" + elgamal.p);
		System.out.println("α=" + elgamal.alpha);
		System.out.println("d=" + elgamal.getD());
		System.out.println("y=" + elgamal.y);
		BigInteger M = new BigInteger("1299");
		BigInteger k = new BigInteger("853");
		BigInteger[] C = elgamal.encrypt(M, k);
		System.out.println("明文：M=" + M);
		System.out.println("k=" + k);
		System.out.println("密文：(C1, C2)=(" + C[0] + ", " + C[1] + ")");
		BigInteger decryptM=elgamal.decrypt(C);
		System.out.println("解密：decryptM=" + decryptM);
	}
}