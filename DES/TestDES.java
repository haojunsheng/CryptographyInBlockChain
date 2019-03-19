public class TestDES {
	public static void main(String[] args) {
		String strKey = "0011000100110010001100110011010000110101001101100011011100111000";
		byte[] sKey = strKey.getBytes();
		for (int i = 0; i < sKey.length; i++) 
			sKey[i] -= '0';
		System.out.print("密钥：");
		printByteArr(sKey);
		String strPlain = "0011000000110001001100100011001100110100001101010011011000110111";
		byte[] plaintext = strPlain.getBytes();
		for (int i = 0; i < plaintext.length; i++) 
			plaintext[i] -= '0';
		System.out.print("明文：");
		printByteArr(plaintext);
		byte[] ciphertext = DES.encrypt(plaintext, sKey);
		System.out.print("密文：");
		printByteArr(ciphertext);
		byte[] plainText = DES.decrypt(ciphertext, sKey);
		System.out.print("明文：");
		printByteArr(plainText);
	}

	static void printByteArr(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.print(b[i]);
			if (i % 8 == 7) 
				System.out.print(" ");
		}
		System.out.println();
	}
}