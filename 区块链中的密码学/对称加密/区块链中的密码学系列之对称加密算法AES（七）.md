# 1. 前言

高级数据加密标准（Advanced Encryption Standard），简称AES，由美国政府于1997年开始公开征集的新的数据加密标准算法。经过三轮筛选，美国政府最终于2000年10月2日正式宣布选中密码学家Joan Daemen和Vincent Rijmen提出的RINJDAEL算法作为AES。

RINJDAEL算法之所以能够最终被选为AES的原因是其安全、性能好、效率高、实用、灵活。

RINJDAEL算法是一个数据块长度和密钥长度都可变的分组加密算法，其数据块长度和密钥长度都可独立地选定为大于等于128位且小于等于256位的32位的任意倍数。而美国颁布AES时却规定数据块的长度为128位，密钥的长度可分别选择为128位、192位或256位。

RINJDAEL算法仍然采用分组密码的一种通用结构：对轮函数实施**迭代**的结构。只是轮函数结构采用的是代替/置换的网络结构**（SP结构**）。

[sp结构讲解](https://www.cnblogs.com/anapodoton/p/10613617.html)

# 2. 数学基础

## 2.1 有限域GF(p^n)

什么是有限域？

 **仅含有限个元素的域**。

多项式构造一般有限域GF(p^n) 的方法：

首先取GF(p)上一个n次不可约多项式

![image-20190328120104771](https://ws3.sinaimg.cn/large/006tKfTcly1g1idiya1kfj31040dowms.jpg)

​    有限域的运算：

​    有限域GF(pn) 中的加法运算：对应的多项式系数在GF(p)上相加。

​    有限域GF(pn) 中的减法运算：对应的多项式系数在GF(p)上相减。

​    有限域GF(pn) 中的乘法运算：两个多项式相乘后模f(x)。

​    有限域GF(pn) 中的除法运算：乘以多项式的逆元。

## 2.2 Rijndael的数学基础

一个由比特位b7b6b5b4b3b2b1b0组成的字节B可表示成系数为0或1的二进制多项式：b7x7+b6x6+b5x5+b4x4+b3x3+b2x2+b1x+b0。例如，字节B=10011011与二进制多项式b(x)=x7+x4+x3+x+1相对应。

（1）在GF(2^8)上的加法

在GF(28)上的加法定义为二进制多项式的加法，其系数模2相加。

11000111 ⊕ 01100111= 10101100（异或）

(x7+x4+x3+x+1)+(x6+x5+x3+x2+x+1)=x7+x6+x5+x4+x2

（2）在GF(28)上的乘法

在GF(28)上的乘法定义为二进制多项式的乘积，如果乘积次数大于7次则模一个次数为8的不可约二进制多项式。

GF(28)中两个元素a=a7a6…a0与b=b7b6…b0相乘，是它们对应的多项式之积模m(x)。即a⊙b=c=c7c6…c0， 其中c(x)=c7x7+c5x6+…+c1x+c0=a(x)b(x)mod m(x)。

e.g.对于不可约多项式m(x)=x8+x4+x3+x+1，

(x6+x4+x2+x+1)(x7+x+1)                                               

=(x13+x11+x9+x8+x6+x5+x4+x3+1)(mod x8+x4+x3+x+1)

=x7+x6+1  

（3）在GF(28)上的乘法逆

在GF(28)中，二进制多项式b(x)的乘法逆为满足a(x)b(x)=1的二进制多项式a(x)，并记为a(x)=b-1(x)。其中，'00'的乘法逆为其本身。

可以使用扩展欧几里得算法求得乘法逆，也可以直接将'00'~'FF'这128中情况全部试代求得乘法逆。

e.g.对于不可约多项式m(x)=x8+x4+x3+x+1，因为

x6+x2+x+1=(x8+x4+x3+x+1)+(x6+x4+x2+x+1)

x4=(x6+x4+x2+x+1)+(x6+x2+x+1)=(x8+x4+x3+x+1)+(x2+1)(x6+x4+x2+x+1)

x2+x+1=(x6+x2+x+1)+x2·x4=(x2+1)(x8+x4+x3+x+1)+x4(x6+x4+x2+x+1)

x=x4+(x2+x)(x2+x+1)=x6+x2+x+1=(x4+x+1)(x8+x4+x3+x+1)+(x6+x5+x2+1)(x6+x4+x2+x+1)

1=(x2+x+1)+(x+1)x=(x5+x4)(x8+x4+x3+x+1)+(x7+x5+x4+x3+x2+x+1)(x6+x4+x2+x+1)

所以(x6+x4+x2+x+1)(x7+x5+x4+x3+x2+x+1)=1

 即(x6+x4+x2+x+1)-1=x7+x5+x4+x3+x2+x+1

（4）在GF(2^8)上的倍乘

在GF(28)中，倍乘函数 xtime(b(x))定义为x·b(x) (mod m(x))。即把字节B左移一位，若结果次数大于7次，则加上不可约多项式m(x)。

　　e.g.对于不可约多项式m(x)=x8+x4+x3+x+1，

x(x6+x4+x2+x+1)=x7+x5+x3+x2+x

x(x7+x5+x3+x2+x)=(x8+x6+x4+x3+x2)+(x8+x4+x3+x+1)=x6+x2+x+1

**GF(2^8)上的多项式运算**

有限域GF(28)上的多项式是系数取自GF(28)域元素的多项式。这样，一个4字节的字与一个次数小于4次的GF(28)上的多项式相对应。例如，字c='03010102'与多项式c(x)='03'x3+'01'x2+'01'x+'02'相对应。

（1）GF(28)上的多项式的加法

GF(28)上的多项式的加法定义为相应项系数相加。所以，在域GF(28)上的两个4字节的字相加也就是按位异或。

e.g.

('03'x3+'01'x2+'01'x+'02')+('0B'x3+'0D'x2+'09'x+'0E')='08'x3+'0C'x2+'08'x+'0C'

（2）GF(28)上的多项式的乘法

　　GF(28)上的多项式a(x)=a3x3+a2x2+a1x+a0和b(x)=b3x3+b2x2+b1x+b0相乘，如果乘积次数超过4次，则模x4+1。即对于c(x)=a(x)b(x)=c3x3+c2x2+c1x+c0，

c0=a0b0⊕a3b1⊕a2b2⊕a1b3

c1=a1b0⊕a0b1⊕a3b2⊕a2b3

c2=a2b0⊕a1b1⊕a0b2⊕a3b3

c3=a3b0⊕a2b1⊕a1b2⊕a0b3



上面的多项式可以用矩阵表示如下：

![image-20190328120700752](https://ws4.sinaimg.cn/large/006tKfTcly1g1idp49k7tj30q60cu41w.jpg)

###  

（3）GF(28)上的多项式的倍乘

GF(28)上的多项式b(x)=b3x3+b2x2+b1x+b0的倍乘x·b(x)=b2x3+b1x2+b0x+b3。即多项式的系数循环左移一位。

## 2.3 Rijndael的设计思想

(1) 抗已知所有攻击。

(2) 在多个平台上速度快，编码紧凑。

(3) 设计简单。

# 3. AES的整体框架

![image-20190328120745045](https://ws3.sinaimg.cn/large/006tKfTcly1g1idpvw8bjj30yw0iy498.jpg)

密钥长度为128位，分组长度为128位，加密轮数为10轮。

# 4. AES参数

## 4.1 数据块字数Nb

 在AES算法中，加解密要经过多次数据变换操作，每一次变换操作都会产生一个中间结果，这个结果称为状态。把状态表示为一个4行Nb列的二维字节数组，其中Nb为数据块长度除以32。因为状态数组有4行，所以状态数组的每一列便为一个4字节的字。

例如，对于长度为128的数据块B15B14...B1B0，Nb=128÷32=4，即该数据块可以表示为状态数组。

![image-20190328120822825](https://ws2.sinaimg.cn/large/006tKfTcly1g1idqjs3x3j308m0a0jrr.jpg)

 AES算法中规定数据块长度为128位，即Nb=128÷32=4。

## 4.2 密钥字数Nk

类似地，密钥也可以表示为4行Nk列的二维字节数组，其中Nk为密钥长度除以32。同样地，密钥数组的每一列为一个4字节的字。 

例如，对于长度为128的密钥K15K14...K1K0，Nk=128÷32=4，即该密钥可以表示为密钥数组。

![image-20190328120856569](https://ws3.sinaimg.cn/large/006tKfTcly1g1idr4pwmij308s0a8dg8.jpg)

AES算法中规定密钥长度为128位、192位或256位，即Nk取值为4、6或8。

## 4.3 迭代轮数Nr

AES算法的迭代轮数Nr由Nb和Nk共同决定：

![image-20190328120931392](https://ws2.sinaimg.cn/large/006tKfTcly1g1idrqus2zj30fe0a274v.jpg)

AES规定Nb=4，所以对应Nk取值4、6、8，Nr取值分别为10、12、14，即在Nb=4的情况下，Nr=Nk+6。



**为什么AES的迭代轮数比DES要少呢？**

因为DES使用的轮函数是Feistel网络,并没有在每轮迭代中对整个分组进行加密，DES一次加密32位，AES一次性加密128位。



## 4.4 不可约多项式m(x)

 在AES算法中，不可约多项式建议为：m(x)=x8+x4+x3+x+1，其系数的十六进制表示为m='11B'。

# 5. AES算法详细实现过程

AES的轮函数由以下3层组成：

　　1.非线性层：进行非线性S盒变换，由16个S盒并置而成，起混淆的作用；

　　2.线性混合层：进行行移位变换和列混合变换以确保多轮之上的高度扩散；

　　3.密钥加层：进行轮密钥加变换，将轮密钥简单地异或到中间状态上。

![image-20190328121009163](https://ws4.sinaimg.cn/large/006tKfTcly1g1idse4mcmj30ti0pytb2.jpg)

　无论是加密过程还是解密过程，都由以下部分组成：

　　1.一个初始轮密钥加。

　　2.Nr-1轮的标准轮函数（包括S盒变换、行移位、列混合、轮密钥加）。

　　3.最后一轮的非标准轮函数（只包括S盒变换、行移位、轮密钥加，不需要列混合）。

## 5.1 S盒替换

S盒变换是按字节进行的代替变换，是作用在状态中每个字节上的一种非线性字节变换。

加密过程，加密过程中的S盒变换按以下2步进行：

　　（1）把字节的值用它的乘法逆来代替；

　　（2）进行如下的仿射变换：

​            xi'=xi⊕xi+4⊕xi+5⊕xi+6⊕xi+7

​            y7y6y5y4y3y2y1y0=(x7'x6'x5'x4'x3'x2'x1'x0')⊕(01100011)

![image-20190328121135405](https://ws1.sinaimg.cn/large/006tKfTcly1g1idtw00nnj30so0b80ul.jpg) 加密过程的S盒表如下：

![image-20190328121156945](https://ws1.sinaimg.cn/large/006tKfTcly1g1idu9kc2mj30va0mo4b2.jpg)

**解密过程：**解密过程中的S盒变换按以下2步进行：

（1）进行如下的仿射变换：

xi'=xi+2⊕xi+5⊕xi+7

 y7y6y5y4y3y2y1y0=(x7'x6'x5'x4'x3'x2'x1'x0')⊕(00000101)

![image-20190328121229296](https://ws1.sinaimg.cn/large/006tKfTcly1g1idutow4gj30sc0amgnh.jpg)

（2）把字节的值用它的乘法逆来代替。

 解密过程的S盒表如下：

![image-20190328121320502](https://ws4.sinaimg.cn/large/006tKfTcly1g1idvpiy4oj30vk0ms4au.jpg)

## 5.2 行移位变换

行移位变换是对状态的行进行循环移位变换。移位值C1、C2、C3与Nb有关：

![image-20190328121344664](https://ws1.sinaimg.cn/large/006tKfTcly1g1idw4ilv1j308m0a6dg7.jpg)

AES规定Nb=4，所以C1=1，C2=2，C3=3。

**加密过程**

加密过程中的行移位变换，状态的第0行不移位，第1行循环左移C1字节，第2行循环左移C2字节，第3行循环左移C3字节。

**解密过程**

解密过程中的行移位变换，状态的第0行不移位，第1行循环左移Nb-C1字节，第2行循环左移Nb-C2字节，第3行循环左移Nb-C3字节。

## 5.3 列混合变换

列混合变换是对状态的列进行混合变换。

**加密过程**

加密过程中的列混合变换，把状态中的每一列看作GF(28)上的多项式，并与固定多项式c(x)='03'x3+'01'x2+'01'x+'02'相乘。

**解密过程**

解密过程中的列混合变换，把状态中的每一列看作GF(28)上的多项式，并与固定多项式c(x)='0B'x3+'0D'x2+'09'x+'0E'相乘。

## 5.4 轮密钥加变换

轮密钥加变换是利用轮密钥对状态进行模2相加的变换。轮密钥长度等于数据块长度。在这个操作中，轮密钥被简单地异或到状态中去。

## 5.5 轮密钥产生算法

![image-20190328152635300](https://ws4.sinaimg.cn/large/006tKfTcly1g1ijgtffupj30nm0pg44c.jpg)



轮密钥根据轮密钥产生算法由主密钥产生得到。轮密钥产生分2步进行：密钥扩展和轮密钥选择，且遵循以下原则：

1.轮密钥的比特总数为数据块长度与轮数加1的，即Nb(Nr+1)。

2.首先将用户密钥扩展为一个扩展密钥。

3.再从扩展密钥中选出轮密钥：第1个轮密钥由扩展密钥中的前Nb个字组成，第2个轮密钥由接下来的Nb个字组成，以此类推。

**加密过程**

加密过程的轮密钥产生分以下2步进行：

1.密钥扩展：用1个字元素的一维数组W[Nb(Nr+1)]存储扩展密钥。把主密钥放在数组W最开始的Nk个字中，其他的字由它前面的字经过处理（处理过程参照代码部分）后得到。分Nk≤6和Nk＞6两种情况进行密钥扩展，两种情况的密钥扩展策略稍有不同。

2.轮密钥选择：轮密钥i由轮密钥缓冲区W[Nb*i]到W[Nb*(i+1)-1]的字组成。

**解密过程**

解密过程的轮密钥产生分以下2步进行：

1.加密过程的轮密钥产生。

2.把解密过程的列混合变换应用到除第一个和最后一个轮密钥之外的所有轮密钥上。

# 6. Java版的AES算法

Word类：

```java
package AES;
​
public class word {
  byte[] word;
​
  public word(byte[] b) {
    word = new byte[4];
    for (int i = 0; i < 4; i++) 
      word[i] = b[i];
  }
​
  public word(word w) {
    word = new byte[4];
    for (int i = 0; i < 4; i++)
      word[i] = w.word[i];
  }
​
  @Override
  public String toString() {
    String str = "";
    for (byte b : word) 
      str += Integer.toHexString((b & 0xff) + 0x100).substring(1);
    return str;
  }
​
  /**
   * 在GF(2^8)上的多项式加法
   * @param a
   * @param b
   * @return
   */
  static word add(word a, word b) {
    word c = new word(new byte[4]);
    for (int i = 0; i < 4; i++)
      c.word[i] = add(a.word[i], b.word[i]);
    return c;
  }
​
  /**
   * 在GF(2^8)上的多项式乘法
   * @param a
   * @param b
   * @return
   */
  static word multiply(word a, word b) {
    word c = new word(new byte[4]);
    c.word[0] = add(
        add(
            add(
                multiply(a.word[0], b.word[0]), multiply(a.word[3], b.word[1])), 
            multiply(a.word[2], b.word[2])), 
        multiply(a.word[1], b.word[3]));
    c.word[1] = add(
        add(
            add(
                multiply(a.word[1], b.word[0]), multiply(a.word[0], b.word[1])), 
            multiply(a.word[3], b.word[2])), 
        multiply(a.word[2], b.word[3]));
    c.word[2] = add(
        add(
            add(
                multiply(a.word[2], b.word[0]), multiply(a.word[1], b.word[1])), 
            multiply(a.word[0], b.word[2])), 
        multiply(a.word[3], b.word[3]));
    c.word[3] = add(
        add(
            add(
                multiply(a.word[3], b.word[0]), multiply(a.word[2], b.word[1])), 
            multiply(a.word[1], b.word[2])), 
        multiply(a.word[0], b.word[3]));
    return c;
  }
​
  /**
   * 在GF(2^8)上的多项式倍乘
   * @param a
   * @return
   */
  static word xtime(word a) {
    word b = new word(new byte[4]);
    for (int i = 0; i < 4; i++)
      b.word[i] = a.word[(i + 1) % 4];
    return b;
  }
/***************************************************************************************************/
  static int m = 0x11b;   //m=100011011
​
  /**
   * 在GF(2^8)上的加法
   * @param a
   * @param b
   * @return
   */
  static byte add(byte a, byte b) {
    return (byte) (a ^ b);
  }
​
  /**
   * 在GF(2^8)上的求模
   * @param a
   * @param b
   * @return
   */
  static byte mod(int a, int b) {
    String str_a = Integer.toBinaryString(a);
    String str_b = Integer.toBinaryString(b);
    if (str_a.length() < str_b.length()) 
      return (byte) a;
    return mod(a ^ (b << (str_a.length() - str_b.length())), b);
  }
​
  /**
   * 在GF(2^8)上的乘法
   * @param a
   * @param b
   * @return
   */
  static byte multiply(byte a, byte b) {
    int op = a & 0xff;
    char[] c = Integer.toBinaryString((b & 0xff) + 0x100).substring(1).toCharArray();
    int r = 0;
    for (int i = 0; i < c.length; i++) 
      if (c[i] == '1') 
        r ^= op << (7 - i);
    return mod(r, m);
  }
​
  /**
   * 在GF(2^8)上的乘法逆
   * @param a
   * @return
   */
  static byte inverse(byte a) {
    if (a == 0) return 0;
    byte b = -128;
    while (mod(multiply(a, b), m) != 1) 
      b++;
    return b;
  }
​
  /**
   * 在GF(2^8)上的倍乘
   * @param a
   * @return
   */
  static byte xtime(byte a) {
    int r = (a & 0xff) << 1;
    if (r > 127) 
      return mod(r, m);
    return (byte) r;
  }
}
```

AES类：

```java
package AES;
​
public class AES {
  static int Nb;                 //数据块字数
  static int Nk;                 //密钥字数
  static int Nr;                 //迭代轮数
  static word[][] RoundKey;      //加密轮密钥
  static word[][] InvRoundKey;   //解密轮密钥
​
  /**
   * 加密
   * @param plaintext  4个字长度的明文
   * @param CipherKey  4、6或8个字长度的密钥
   * @return
   */
  public static word[] encrypt(word[] plaintext, word[] CipherKey) {
    Nb = 4;
    Nk = CipherKey.length;
    Nr = Nk + 6;
​
    // 轮密钥产生算法
    // 轮密钥根据轮密钥产生算法由主密钥产生得到。轮密钥产生分2步进行：密钥扩展和轮密钥选择。
    // 加密密钥扩展
    RoundKey = KeyExpansion(CipherKey);
​
    word[] ciphertext = new word[plaintext.length];
    for (int i = 0; i < plaintext.length; i++) 
      ciphertext[i] = new word(plaintext[i]);
    //初始轮密钥加
    ciphertext = AddRoundKey(ciphertext, RoundKey[0]);
​
    //轮函数
    for (int i = 1; i < Nr + 1; i++) {
      //S盒变换
      ciphertext = ByteSub(ciphertext);
      //行移位
      ciphertext = ShiftRow(ciphertext);
      //列混合
      if (i != Nr) ciphertext = MixColumn(ciphertext);
      //轮密钥加
      ciphertext = AddRoundKey(ciphertext, RoundKey[i]);
    }
    return ciphertext;
  }
​
  /**
   * 解密
   * @param ciphertext  4个字长度的密文
   * @param CipherKey   4、6或8个字长度的密钥
   * @return
   */
  public static word[] decrypt(word[] ciphertext, word[] CipherKey) {
    Nb = 4;
    Nk = CipherKey.length;
    Nr = Nk + 6;
    //解密密钥扩展
    InvRoundKey = InvKeyExpansion(CipherKey);
    word[] plaintext = new word[ciphertext.length];
    for (int i = 0; i < ciphertext.length; i++) 
      plaintext[i] = new word(ciphertext[i]);
    //初始轮密钥加
    plaintext = AddRoundKey(plaintext, InvRoundKey[Nr]);
    //轮函数
    for (int i = Nr - 1; i >= 0; i--) {
      //S盒变换
      plaintext = InvByteSub(plaintext);
      //行移位
      plaintext = InvShiftRow(plaintext);
      //列混合
      if (i != 0) plaintext = InvMixColumn(plaintext);
      //轮密钥加
      plaintext = AddRoundKey(plaintext, InvRoundKey[i]);
    }
    return plaintext;
  }
/**************************************************************************************************/
  /**
   * S盒变换
   * S盒变换是按字节进行的代替变换，是作用在状态中每个字节上的一种非线性字节变换。
   * （1）把字节的值用它的乘法逆来代替；
　　   * （2）进行如下的仿射变换：
   *    xi'=xi⊕xi+4⊕xi+5⊕xi+6⊕xi+7
   *    y7y6y5y4y3y2y1y0=(x7'x6'x5'x4'x3'x2'x1'x0')⊕(01100011)
   * @param state
   * @return
   */
  static word[] ByteSub(word[] state) {
    for (int i = 0; i < Nb; i++) 
      for (int j = 0; j < 4; j++) {
        //乘法逆代替
        state[i].word[j] = word.inverse(state[i].word[j]);
        //仿射变换
        state[i].word[j] = AffineTransformation(state[i].word[j], 'C');
      }
    return state;
  }
​
  /**
   * 行移位变换
   * 行移位变换是对状态的行进行循环移位变换。移位值C1、C2、C3与Nb有关：
   * AES规定Nb=4，所以C1=1，C2=2，C3=3。
   * @param state
   * @return
   */
  static word[] ShiftRow(word[] state) {
    byte[][] b = new byte[4][Nb];
    for (int j = 0; j < Nb; j++) 
      for (int i = 0; i < 4; i++) 
        b[i][j] = state[j].word[i];
    for (int i = 1; i < 4; i++) 
      for (int k = 0; k < i; k++) {
        byte t = b[i][0];
        for (int j = 0; j < Nb - 1; j++) 
          b[i][j] = b[i][j + 1];
        b[i][Nb - 1] = t;
      }
    for (int j = 0; j < Nb; j++) 
      for (int i = 0; i < 4; i++) 
        state[j].word[i] = b[i][j];
    return state;
  }
​
  /**
   * 列混合变换
   * 列混合变换是对状态的列进行混合变换。
   * @param state
   * @return
   */
  static word[] MixColumn(word[] state) {
    byte[] b = {(byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x03};
    word a = new word(b);
    for (int i = 0; i < Nb; i++) 
      state[i] = word.multiply(a, state[i]);
    return state;
  }
​
  /**
   * 轮密钥加变换
   * 轮密钥加变换是利用轮密钥对状态进行模2相加的变换。轮密钥长度等于数据块长度。
   * 在这个操作中，轮密钥被简单地异或到状态中去。
   * @param state
   * @param key
   * @return
   */
  static word[] AddRoundKey(word[] state, word[] key) {
    for (int i = 0; i < Nb; i++) 
      state[i] = word.add(state[i], key[i]);
    return state;
  }
​
  /**
   * 加密密钥扩展
   * 用1个字元素的一维数组W[Nb(Nr+1)]存储扩展密钥。
   * 把主密钥放在数组W最开始的Nk个字中，其他的字由它前面的字经过处理（处理过程参照代码部分）后得到。
   * 分Nk≤6和Nk＞6两种情况进行密钥扩展，两种情况的密钥扩展策略稍有不同。
   * 轮密钥i由轮密钥缓冲区W[Nb*i]到W[Nb*(i+1)-1]的字组成
   * @param CipherKey
   * @return
   */
  static word[][] KeyExpansion(word[] CipherKey) {
    word[] W = new word[Nb * (Nr + 1)];
    //密钥扩展
    word Temp;
    if (Nk <= 6) {
      for (int i = 0; i < Nk; i++) 
        W[i] = CipherKey[i];
      for (int i = Nk; i < W.length; i++) {
        Temp = new word(W[i - 1]);
        if (i % Nk ==0) 
          Temp = word.add(SubByte(Rotl(Temp)), Rcon(i / Nk));
        W[i] = word.add(W[i - Nk], Temp);
      }
    } else {
      for (int i = 0; i < Nk; i++) 
        W[i] = CipherKey[i];
      for (int i = Nk; i < W.length; i++) {
        Temp = new word(W[i - 1]);
        if (i % Nk ==0) 
          Temp = word.add(SubByte(Rotl(Temp)), Rcon(i / Nk));
        else if (i % Nk == 4) 
          Temp = SubByte(Temp);
        W[i] = word.add(W[i - Nk], Temp);
      }
    }
    //轮密钥选择
    word[][] RoundKey = new word[Nr + 1][Nb];
    for (int i = 0; i < Nr + 1; i++) 
      for (int j = 0; j < Nb; j++) 
        RoundKey[i][j] = W[Nb * i + j];
    return RoundKey;
  }
​
  /**
   * S盒逆变换
   * @param state
   * @return
   */
  static word[] InvByteSub(word[] state) {
    for (int i = 0; i < Nb; i++) 
      for (int j = 0; j < 4; j++) {
        //仿射变换
        state[i].word[j] = AffineTransformation(state[i].word[j], 'D');
        //乘法逆代替
        state[i].word[j] = word.inverse(state[i].word[j]);
      }
    return state;
  }
​
  /**
   * 行移位逆变换
   * @param state
   * @return
   */
  static word[] InvShiftRow(word[] state) {
    byte[][] b = new byte[4][Nb];
    for (int j = 0; j < Nb; j++) 
      for (int i = 0; i < 4; i++) 
        b[i][j] = state[j].word[i];
    for (int i = 1; i < 4; i++) 
      for (int k = 0; k < Nb - i; k++) {
        byte t = b[i][0];
        for (int j = 0; j < Nb - 1; j++) 
          b[i][j] = b[i][j + 1];
        b[i][Nb - 1] = t;
      }
    for (int j = 0; j < Nb; j++) 
      for (int i = 0; i < 4; i++) 
        state[j].word[i] = b[i][j];
    return state;
  }
​
  /**
   * 列混合逆变换
   * @param state
   * @return
   */
  static word[] InvMixColumn(word[] state) {
    byte[] b = {(byte) 0x0E, (byte) 0x09, (byte) 0x0D, (byte) 0x0B};
    word a = new word(b);
    for (int i = 0; i < Nb; i++) 
      state[i] = word.multiply(a, state[i]);
    return state;
  }
​
  /**
   * 解密密钥扩展
   * @param CipherKey
   * @return
   */
  static word[][] InvKeyExpansion(word[] CipherKey) {
    word[][] InvRoundKey = KeyExpansion(CipherKey);
    for (int i = 1; i < Nr; i++) 
      InvRoundKey[i] = InvMixColumn(InvRoundKey[i]);
    return InvRoundKey;
  }
/**************************************************************************************************/
  static word SubByte(word a) {
    word w = new word(a);
    for (int i = 0; i < 4; i++) {
      //乘法逆代替
      w.word[i] = word.inverse(w.word[i]);
      //仿射变换
      w.word[i] = AffineTransformation(w.word[i], 'C');
    }
    return w;
  }
​
  static word Rotl(word a) {
    word w = new word(a);
    byte b = w.word[0];
    for (int i = 0; i < 3; i++) 
      w.word[i] = w.word[i + 1];
    w.word[3] = b;
    return w;
  }
​
  static word Rcon(int n) {
    word Rcon = new word(new byte[4]);
    byte RC = 1;
    for (int i = 1; i < n; i++) 
      RC = word.xtime(RC);
    Rcon.word[0] = RC;
    return Rcon;
  }
​
  /**
   * 仿射变换
   * @param b
   * @param sign  C：加密  D：解密
   * @return
   */
  static byte AffineTransformation(byte b, char sign) {
    byte[] x = Integer.toBinaryString((b & 0xff) + 0x100).substring(1).getBytes();
    for (int i = 0; i < x.length; i++) x[i] -= '0';
    if (sign == 'C') {
      byte[] x_ = new byte[8];
      byte b_ = 0;
      for (int i = 0; i < 8; i++) {
        x_[i] = (byte) (x[i] ^ x[(i + 1) % 8] ^ x[(i + 2) % 8] ^ x[(i + 3) % 8] ^ x[(i + 4) % 8]);
        b_ += x_[i] * Math.pow(2, 7 - i);
      }
      return (byte) (b_ ^ 0x63);
    } else {
      byte[] x_ = new byte[8];
      byte b_ = 0;
      for (int i = 0; i < 8; i++) {
        x_[i] = (byte) (x[(i + 1) % 8] ^ x[(i + 3) % 8] ^ x[(i + 6) % 8]);
        b_ += x_[i] * Math.pow(2, 7 - i);
      }
      return (byte) (b_ ^ 0x05);
    }
  }
}
```



测试：

```
package AES;

public class TestAES {
  public static void main(String[] args) {
    byte[] plain = {
        (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, 
        (byte) 0x01, (byte) 0xa1, (byte) 0x98, (byte) 0xaf, 
        (byte) 0xda, (byte) 0x78, (byte) 0x17, (byte) 0x34, 
        (byte) 0x86, (byte) 0x15, (byte) 0x35, (byte) 0x66
    };
    byte[] key = {
        (byte) 0x00, (byte) 0x01, (byte) 0x20, (byte) 0x01, 
        (byte) 0x71, (byte) 0x01, (byte) 0x98, (byte) 0xae, 
        (byte) 0xda, (byte) 0x79, (byte) 0x17, (byte) 0x14, 
        (byte) 0x60, (byte) 0x15, (byte) 0x35, (byte) 0x94
    };
    word[] plaintext = toWordArr(plain);
    System.out.println("明文：" + wordArrStr(plaintext));
    word[] CipherKey = toWordArr(key);
    System.out.println("密钥：" + wordArrStr(CipherKey));
    word[] cipherText = AES.encrypt(plaintext, CipherKey);
    System.out.println("密文：" + wordArrStr(cipherText));
    word[] newPlainText = AES.decrypt(cipherText, CipherKey);
    System.out.println("明文：" + wordArrStr(newPlainText));
  }

  static word[] toWordArr(byte[] b) {
    int len = b.length / 4;
    if (b.length % 4 != 0) len++;
    word[] w = new word[len];
    for (int i = 0; i < len; i++) {
      byte[] c = new byte[4];
      if (i * 4 < b.length) {
        for (int j = 0; j < 4; j++)
          c[j] = b[i * 4 + j];
      }
      w[i] = new word(c);
    }
    return w;
  }

  static String wordArrStr(word[] w) {
    String str = "";
    for (word word : w)
      str += word;
    return str;
  }
}
```



**测试结果如下：**

![image-20190328121644741](https://ws3.sinaimg.cn/large/006tKfTcly1g1idz9vmlzj310e06cgr6.jpg)



更多代码请参考：https://github.com/Anapodoton/Encryption/blob/master/AES/AES.java