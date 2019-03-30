# 1. 前言

RSA密码是1978年美国麻省理工学院三位密码学者R.L.Rivest、A.Shamir和L.Adleman提出的一种基于大合数因子分解困难性的公开密钥密码。由于RSA密码既可用于加密，又可用于数字签名，通俗易懂，因此RSA密码已成为目前应用最广泛的公开密钥密码。

# 2. RSA的密钥生成过程

1.随机地选择两个大素数p和q，而且保密；        

2.计算n=pq，将n公开；

3.计算φ(n)=(p-1)(q-1)，对φ(n)保密；

4.随机地选取一个正整数e，1<e<φ(n)且(e,φ(n))=1，将e公开；

5.根据ed=1(mod φ(n))，求出d，并对d保密；

6.加密运算：c=p^e(mod n)；

7.解密运算：p=c^d(mod n)。

注意：在加密运算和解密运算中，M和C的值都必须小于n，也就是说，如果明文（或密文）太大，必须进行分组加密（或解密）。

 比如爱丽丝选择了61和53。（实际应用中，这两个质数越大，就越难破解。）

爱丽丝就把61和53相乘:n= 61×53 = 3233；n的长度就是密钥长度。3233写成二进制是110010100001，一共有12位，所以这个密钥就是12位。实际应用中，RSA密钥一般是1024位，重要场合则为2048位。

 根据公式：φ(n)= (p-1)(q-1)，爱丽丝算出φ(3233)等于60×52，即3120。

 爱丽丝就在1到3120之间，随机选择了17。（实际应用中，常常选择65537。）

计算ed≡ 1 (mod φ(n))带入e=17，求解方程组：17x+ 3120y= 1,这个方程可以用"扩展欧几里得算法"求解，得到(x,y)=(2753,-15)其中私钥d=2753

# 3. RSA解密正确性证明

命题:解密者使用自己的私钥d可以恢复正确的明文m。 

证明:由加密过程c=m^e modn，所以存在某整数k，满足 c^d modn=(m^e)^dmodn =m^kφ(n)modn 

分两种情况:

1. (m, n)=1，由Euler定理m^φ(n) modn=1， 因此 m^kφ(n)modn=1，于是m^kφ(n)+1modn=m，即c^d mod n=m 
2. (m, n) ≠1，设m=tp, 0<t<q。
    因为(tp, q)=1，由Euler定理得m^φ(n)modq=1。 所以存在整数r，满足m^φ(n)=1+rq。 等式两边同乘以m，得m^kφ(n)+1=m+rtpq 因此，c^dmodn=m^φ(n)+1modn=(m+rtpq)modn=m 

# 4. RSA算法细节

实现RSA算法，主要需要实现以下几个部分：

　　1.对大数的素数判定；

　　2.模逆运算；

　　3.模指运算。

## 4.1 对大数的素数判定

一个较小的数是否为素数，可以用<u>试除法</u>来判定，而如果这个数很大的话，试除法的效率就会变得很低下。也就是说，试除法不适用于对大数进行素数判定，所以对大数的素数判定一般采用<u>素数的概率性检验算法</u>，其中又以**Miller算法**最为常见。

使用素数的概率性检验算法判定一个数是否为素数，虽然相比试除法而言效率非常之高，但是对该数的判定结果并<u>不准确</u>。该算法通过循环使用Miller算法来提高判定结果的正确性。

<u>素数的概率性检验算法的流程：对于奇整数n，在2~n-2之间随机地选取k个互不相同的整数，循环使用Miller算法来检验n是否为素数。若结果为true，则认为n可能为素数，否则肯定n为合数。</u>

一轮Miller算法判定大整数n不是素数的概率≤4^-1，所以，素数的概率性检验算法判定大整数n不是素数的概率≤4^-k（k为Miller算法的循环次数）。

###  4.1.1 Miller算法

若n为奇素数，则对∀a∈[2,n-2]，由于a与n互素，根据欧拉定理可得a^φ(n)=a^(n-1)=1(mod n)。

若n是奇素数，则不存在1(mod n)的非平凡平方根，即对于x^2=1(mod n)的解有且仅有±1。

若n是奇素数，则n-1是偶数。不妨令n=2^t*m+1（t≥1），则m为n-1的最大奇因子。根据上述两点，不难得出，对∀a∈[2,n-2]，∃τ∈[1,t]使得

![image-20190329170125901](https://ws2.sinaimg.cn/large/006tKfTcly1g1jrtsu7ifj30a40280sr.jpg)

Miller算法正是通过上述的逆否命题而设计出来的，其原理是：对∀a∈[2,n-2]，n是一个合数的充要条件是对∀τ∈[1,t]使得

![image-20190329170139452](https://ws4.sinaimg.cn/large/006tKfTcly1g1jru0kqp6j30a402igln.jpg)　　

Miller算法的设计思路：令b=a^m(mod n)，如果b=±1(mod n)则n可能是一个素数；否则，b=b^2(mod n)，并判断是否满足b=-1(mod n)（满足则n可能是一个素数），由此循环t-1次。如果都满足b≠-1(mod n)，则n一定是一个合数。

e.g.判定221是否为素数

n=221=2^2*55+1,所以m=55，t=2,取a=174，则174^55(mod 221)=47,174^110(mod 221)=220,所以n要么是一个素数，要么a=174是一个“强伪证”, 再取a=137，则137^55(mod 221)=188,137^110(mod 221)=205。所以n是一个合数。

## 4.2 模逆运算

模逆运算就是求满足方程ax=1(mod m)的解x，而ab=1(mod m)有解的前提条件是(a,m)=1，即a和m互素。

对方程ax=1(mod m)的求解可以转换为求解ax+my=1=(a,m)，即转换为扩展欧几里德算法。

e.g.求243^-1(mod 325)

325=1*325+0*243

243=0*325+1*243

82=325-243=1*325+(-1)*243

79=243-2*82=(-2)*325+3*243

3=82-79=3*325+(-4)*243

1=79-26*3=(-80)*325+107*243

所以243^-1(mod 325)=107

## 4.3 模指运算

模指运算就是对a^n(mod m)的计算。当指数n的值较大时，如果先求出b^n再去模m的话，效率会很低下。所以，对于指数n较大的情况一般采用反复平方乘算法。

**反复平方乘算法**

![image-20190329170517221](https://ws1.sinaimg.cn/large/006tKfTcly1g1jrxs8al9j30nk03q3z2.jpg)

　　所以，反复平方乘算法的原理是将指数n转化为2的幂之和的形式，即n=2^kek+2^(k-1)ek-1+…+2e1+e0，然后根据l1=a^2(mod m)，l2=a^4(mod m)=l1^2(mod m)，...，

![image-20190329170552605](https://ws3.sinaimg.cn/large/006tKfTcly1g1jryeco8ej308c0263yi.jpg)

最后根据a^n(mod m)=e0a·e1l1·...·eklk(mod m)求解。

e.g.求23^35(mod 101)

35=32+2+1

23^1(mod 101)=23

23^2(mod 101)=24

23^4(mod 101)=24^2(mod 101)=71

23^8(mod 101)=71^2(mod 101)=92

23^16(mod 101)=92^2(mod 101)=81

23^32(mod 101)=81^2(mod 101)=97

所以2335(mod 101)=97×24×23(mod 101)=14 

# 5. **实际编程中存在的缺陷**

## 5.1 缺陷1：使用相同的N。

多人共用同一模数n，各自选择不同的e和d，这样实现 当然简单，但是不安全。消息以两个不同的密钥加密， 在共用同一个模下，若两个密钥互素(一般如此)，则可以 恢复明文。 

在实现过程中，部分程序员使用相同的N，更改e来达到生成新的公私钥对的目的。比如，一开始选择e=3，由于过于简单更改其e=65537，但是N不变，可能导致该问题。

实验模拟：

​    一、准备

​      攻击者拥有公钥n，e1私钥d1

​      被攻击者拥有公钥n，e2私钥d2

​    二、攻击

​      攻击者通过e1d1≡1(mod φ(n))枚举φ(n)

​        通过φ(n)以及e2生成私钥d2（类似私钥生成过程）

设e1和e2是两个互素的不同密钥，共用模为n，对同一消息m加密得 c1=m^e1 mod n, c2=m^e2 mod n。分析者知道n, e1, e2, c1和c2。因为(e1, e2,)=1，由扩展Euclid算法可以求得整数r,s满足re1+se2=1。从而可得 c1^r c2^s=m mod n。

## 5.2 **缺陷2：e和d的值设置的过小。**

采用小的e可以加快加密和验证签字的速度，且所需的存储密钥空间小，但若加密钥e选择得太小，则容易受到攻击。

实验场景：

  假设在一个网域中，有四个以上的用户。（假设4个）其中一个用户用三个不用用户的公钥(e,n1),(e,n2)和(e,n3)加密同一段明文消息P，得到三个不同的密文C1，C2，C3。攻击者可以由C1，C2，C3反推明文。

实验模拟：

一、获得C1，C2，C3

​        分别使用不同的RSA公私钥对同一段明文P进行加密，公私钥对中选择e=3.并且将加密结果（C1，C2，C3）发送给攻击者，攻击者得到秘文后开始反推明文。

二、还原明文

​      C1=P3mod n1

​     C2=P3mod n2

​     C3=P3mod n3

由中国剩余定理可求出P3从而可以求出明文P

中国剩余定理：令m=n1·n2·n3,

   M1=n2·n3, M2=n1·n3, M3=n1·n2

  Mi'·Mi ≡1(mod mi)     i=1,2,3

  P3=M1'·M1·C1+ M2'·M2·C2+ M3'·M3·C3从而求出P。

## 5.3 **缺陷3：选择密文攻击**

实验模拟：

​     一、被攻击者拥有公私钥e,n,d，并且加密了一个消息m，加密后的消息c=m^e(modn)

​     二、攻击者选择随机数s，计算m'=c*s^e (mod n)

​     三、攻击者将m'交给被攻击者，要求被攻击者解密

​     四、攻击者计算 c’=m'^d(modn)

​            代入得c’=med sed(modn)=ms(mod n)

​     五、攻击者拿到c'后计算m=c's-1(modn)得到了原明文



所以，e不能太小，最常用的e值为3，17，65537(2^16+1),解密指数d需要满足d>n^1/4

# 6. **基于java实现RSA的加解密过程**

```java
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
        System.out.println("n:" + n + "\np:" + p + "\nq:" + q + "\ne:" + e + "\nd:" + d);
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
```

代码执行结果如下所示：

![image-20190329171739308](https://ws4.sinaimg.cn/large/006tKfTcly1g1jsanukgpj30m018knf5.jpg)

更多密码学源码请参考：

```
https://github.com/Anapodoton/Encryption/blob/master/RSA/RSA.java
```





