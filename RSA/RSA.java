import java.math.BigInteger;
import java.util.Random;

/**
* 1. 随机选择两个质数p和q（比如61和53），这两个数不相等，且应该是同一个量级。
*   （实际应用中，这两个质数越大，就越难破解。）
* 2. 计算n的值（n=3233），n的长度即是密钥的长度。
*     3233写成二进制是110010100001，一共有12位，所以这个密钥就是12位.
*    实际应用中，RSA密钥一般是1024位，重要场合则为2048位。
* 3. 计算n的欧拉函数φ(n)。
*    根据公式：φ(n)= (p-1)(q-1)，爱丽丝算出φ(3233)等于60×52，即3120。
* 4. 随机选择一个整数e，条件是1<e<φ(n)，且e与φ(n) 互质。
*    爱丽丝就在1到3120之间，随机选择了17。（实际应用中，常常选择65537。）
* 5. 计算e对于φ(n)的模反元素d。  
*    计算ed≡ 1 (mod φ(n))带入e=17，求解方程组：17x+ 3120y= 1
* 6. 将n和e封装成公钥，n和d封装成私钥。
*/
public class RSA {
    private static BigInteger n; // large prime
    private static BigInteger e; // public key
    private static BigInteger d; // private key
    private static BigInteger p; // prime
    private static BigInteger q; // prime
    private static BigInteger o; //means φ(n) 
    public static void main(String[] args) {
        String plaintext = "rsa encrypt & decrypt test";
        RSA rsa = new RSA();
        rsa.giveKey();
        BigInteger[] encrypt = rsa.encrypt(plaintext);
        System.out.println("\nplaintext:" + plaintext + "\n\nencrpyt:");
        for (int i = 0; i < encrypt.length; ++i) {
            System.out.println(encrypt[i]);
        }
        String decrypt = rsa.decrypt(encrypt);
        System.out.println("\ndecrypt:" + decrypt);
    }

    // RSA encryption,逐位进行加密
    // RSA加密过程：加密后的消息p=m^e（mod n)；
    public BigInteger[] encrypt(String plaintext) {
        BigInteger[] encrypt = new BigInteger[plaintext.length()];
        BigInteger m, p;
        for (int i = 0; i < plaintext.length(); ++i) {
            m = BigInteger.valueOf(plaintext.charAt(i));
            p = m.modPow(e, n);
            encrypt[i] = p;
        }
        return encrypt;
    }

    // RSA decryption
    // RSA解密过程：还原消息m=p^d（mod n)；
    public String decrypt(BigInteger[] encrypt) {
        StringBuffer plaintext = new StringBuffer();
        BigInteger m, p;
        for (int i = 0; i < encrypt.length; ++i) {
            p = encrypt[i];
            m = p.modPow(d, n);
            plaintext.append((char) m.intValue());
        }
        return plaintext.toString();
    }

    // give public key and private key
    public void giveKey() {
        // get p,q,n,e,b
        producePQ();
        n = p.multiply(q);
        o = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
        produceEB(o);
        System.out.println("n:" + n + "\np:" + p + "\nq:" + q + "\no:" + o + "\ne:" + e + "\nd:" + d);
    }

    // large prime p and q generation
    public void producePQ() {
        p = BigInteger.probablePrime(32, new Random());
        q = BigInteger.probablePrime(32, new Random());
        while (p.equals(q)) {
            p = BigInteger.probablePrime(32, new Random());
            q = BigInteger.probablePrime(32, new Random());
        }
    }

    // produce public key e,private key b
    public void produceEB(BigInteger eulerN) {
        e = BigInteger.probablePrime((int) (Math.random() * 63 + 2), new Random());
        while (e.compareTo(eulerN) != -1 | eulerN.divide(e).equals(0)) {
            e = BigInteger.probablePrime((int) (Math.random() * 63 + 2), new Random());
        }
        //e = BigInteger.valueOf(65537);//default
        d = e.modInverse(eulerN);
    }
}