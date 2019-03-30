# 前言

**MD5**算法在90年代被广泛使用在各种程序语言中，用以确保资料传递无误等。但是在1996年被证明存在弱点，是可以被破解的。但是其优势是十分的明显的，比如说稳定和快速，所以我们仍然可将其用于普通数据的错误检查领域。

# 1. 什么是MD5 ?

**MD5消息算法摘要**(英语：MD5 Message-Digest Algorithm），一种被广泛使用的密码散列函数，可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。MD5由美国密码学家罗纳德·李维斯特（Ronald Linn Rivest）设计，于1992年公开，用以取代MD4算法。（来源于维基百科）

# 2.  MD5算法的特点

MD5算法具有以下特点：

- 压缩性：任意长度的数据，算出的MD5值长度都是固定的。
- 容易计算：从原数据计算出MD5值很容易。
- 抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。
- 强抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。

# 3. MD5的应用

MD5可以用在文件传输方面。具体来说就是，服务器会把文件进行MD5处理，提供一个MD5校验和，用户下载文件之后，可以使用MD5算法计算下载文件的校验和，通过对比，我们就可以知道文件传输过程中，是否出现了错误。

MD5算法甚至可以用于博彩，可以保证博彩的公平性。

# 4. MD5算法的底层原理

MD5算法用512位分组来处理用户输入的信息，每一个分组将会被划分成16个32位的子分组。经过一系列处理后，算法的输出由四个32位的输出组成，最后再把这四个32位的分组进行连接形成128位的散列值。

MD5算法大概可以分为以下四步：原文处理，设置初始值，循环加工，拼接结果。下面我们进行详细的分析。

## 4.1 原文处理

由于用户输入的信息的长度不一致，所以我们需要对其进行处理，对其进行补位。

<u>MD5算法的原理是：用输入的长度对448取余，不足448的进行补位，填充的方法是第一位补1，其他位补0。经过补位后，现在的长度为512*N+448。事实上，补位之后的结果已经不能代表真正的字符串，所以我们还需要记录下原来字符串的长度，方法是用（512-448=64）来记录实际的长度。</u>

<u>经过上面的处理之后，我们处理之后的信息的长度为512*(N+1)。</u>



## 4.2 设置初始值

MD5的hash结果为128位，可分为ABCD四组，每组为32位，都有其初始值。MD5算法的官方实现中，ABCD的初始值如下：

- A=0x01234567
- B=0x89ABCDEF
- C=0xFEDCBA98
- D=0x76543210

## 4.3 循环加工

这一步是最为复杂的一步。我们先来看一张来自维基百科的图。下面这张图详细展示了ABCD的值是如何发生变化的。                

![image-20190327105253522](https://ws3.sinaimg.cn/large/006tKfTcly1g1hh165ib1j30ie0jcae6.jpg)



 在每一次循环中，旧的ABCD都会产生新的ABCD，那么总共需要循环多少次呢？这个具体要由在 4.1 中处理之后的长度来决定。

假设我们处理之后的长度为M，那么主循环的次数为M / 512，每个主循环中包括512/32*4=64次子循环。上面的图展示的是每次子循环的步骤，下面对上图进行详细的解释：

​        **1. 绿色F：**

​     图中的绿色F，代表非线性函数。官方MD5所用到的函数有四种：

- F(X, Y, Z) =(X&Y) | ((~X) & Z)
- G(X, Y, Z) =(X&Z) | (Y & (~Z))
- H(X, Y, Z) =X^Y^Z
- I(X, Y, Z)=Y^(X|(~Z))

​    （&是与（And），|是或（Or），~是非（Not），^是异或（Xor））        

​        在每个主循环中都要进行64次子循环，FGHI四个函数将会交替使用，每个使用16次。

​    **2. 红色的“田”字**

​    红色的“田”代表相加。

​    **3. Mi**

​        Mi和原文相关，处理后原文的长度是512的整数倍。把原文的每512位再分成16等份，命名为M0~M15，每一等份长度32。在64次子循环中，每16次循环，都会交替用到M1~M16之一。

   **4. Ki**

   一个常量，在64次子循环中，每一次用到的常量都是不同的。

  **5.黄色的<<<S**

   左移S位，S的值也是常量。值得注意的是：这里的左移表示的循环左移。

   **总结**

​       经过上面的变化之后，计算的结果和B相加，然后取代原来的B。

​        新的ABCD的产生过程为：

- 新A = 原D;

- 新B = b+((a+F(b,c,d)+Mj+Ki)<<<s)；

- 新C = 原B；

- 新D = 原D；

  

总结一下主循环中的64次子循环，可以归纳为下面的四部分：

```
FF(a,b,c,d,Mj,s,ti)表示a=b+((a+F(b,c,d)+Mj+ti)<<<s)
第一轮：
   FF(a,b,c,d,M0,7,0xd76aa478）     s[0]=7,   K[0] = 0xd76aa478
　　FF(a,b,c,d,M1,12,0xe8c7b756）   s[1]=12,  K[1] = 0xe8c7b756 
　　FF(a,b,c,d,M2,17,0x242070db)
　　FF(a,b,c,d,M3,22,0xc1bdceee)
　　FF(a,b,c,d,M4,7,0xf57c0faf)
　　FF(a,b,c,d,M5,12,0x4787c62a)
　　FF(a,b,c,d,M6,17,0xa8304613）
　　FF(a,b,c,d,M7,22,0xfd469501）
　　FF(a,b,c,d,M8,7,0x698098d8）
　　FF(a,b,c,d,M9,12,0x8b44f7af)
　　FF(a,b,c,d,M10,17,0xffff5bb1）
　　FF(a,b,c,d,M11,22,0x895cd7be)
　　FF(a,b,c,d,M12,7,0x6b901122）
　　FF(a,b,c,d,M13,12,0xfd987193）
　　FF(a,b,c,d,M14,17, 0xa679438e)
　　FF(a,b,c,d,M15,22,0x49b40821）
　　第二轮：
　　GG(a,b,c,d,M1,5,0xf61e2562）
　　GG(a,b,c,d,M6,9,0xc040b340）
　　GG(a,b,c,d,M11,14,0x265e5a51）
　　GG(a,b,c,d,M0,20,0xe9b6c7aa)
　　GG(a,b,c,d,M5,5,0xd62f105d)
　　GG(a,b,c,d,M10,9,0x02441453）
　　GG(a,b,c,d,M15,14,0xd8a1e681）
　　GG(a,b,c,d,M4,20,0xe7d3fbc8）
　　GG(a,b,c,d,M9,5,0x21e1cde6）
　　GG(a,b,c,d,M14,9,0xc33707d6）
　　GG(a,b,c,d,M3,14,0xf4d50d87）
　　GG(a,b,c,d,M8,20,0x455a14ed)
　　GG(a,b,c,d,M13,5,0xa9e3e905）
　　GG(a,b,c,d,M2,9,0xfcefa3f8）
　　GG(a,b,c,d,M7,14,0x676f02d9）
　　GG(a,b,c,d,M12,20,0x8d2a4c8a)
　　第三轮：
　　HH(a,b,c,d,M5,4,0xfffa3942）
　　HH(a,b,c,d,M8,11,0x8771f681）
　　HH(a,b,c,d,M11,16,0x6d9d6122）
　　HH(a,b,c,d,M14,23,0xfde5380c)
　　HH(a,b,c,d,M1,4,0xa4beea44）
　　HH(a,b,c,d,M4,11,0x4bdecfa9）
　　HH(a,b,c,d,M7,16,0xf6bb4b60）
　　HH(a,b,c,d,M10,23,0xbebfbc70）
　　HH(a,b,c,d,M13,4,0x289b7ec6）
　　HH(a,b,c,d,M0,11,0xeaa127fa)
　　HH(a,b,c,d,M3,16,0xd4ef3085）
　　HH(a,b,c,d,M6,23,0x04881d05）
　　HH(a,b,c,d,M9,4,0xd9d4d039）
　　HH(a,b,c,d,M12,11,0xe6db99e5）
　　HH(a,b,c,d,M15,16,0x1fa27cf8）
　　HH(a,b,c,d,M2,23,0xc4ac5665）
　　第四轮：
　　Ⅱ（a,b,c,d,M0,6,0xf4292244）
　　Ⅱ（a,b,c,d,M7,10,0x432aff97）
　　Ⅱ（a,b,c,d,M14,15,0xab9423a7）
　　Ⅱ（a,b,c,d,M5,21,0xfc93a039）
　　Ⅱ（a,b,c,d,M12,6,0x655b59c3）
　　Ⅱ（a,b,c,d,M3,10,0x8f0ccc92）
　　Ⅱ（a,b,c,d,M10,15,0xffeff47d)
　　Ⅱ（a,b,c,d,M1,21,0x85845dd1）
　　Ⅱ（a,b,c,d,M8,6,0x6fa87e4f)
　　Ⅱ（a,b,c,d,M15,10,0xfe2ce6e0)
　　Ⅱ（a,b,c,d,M6,15,0xa3014314）
　　Ⅱ（a,b,c,d,M13,21,0x4e0811a1）
　　Ⅱ（a,b,c,d,M4,6,0xf7537e82）
　　Ⅱ（a,b,c,d,M11,10,0xbd3af235）
　　Ⅱ（a,b,c,d,M2,15,0x2ad7d2bb)　　　　Ⅱ（a,b,c,d,M9,21,0xeb86d391）
```

上面的步骤完成之后，将abcd在原来的基础上分别加上ABCD。即a = a + A，b = b + B，c = c + C，d = d + D。然后就可以使用下一组分组数据继续进行计算。

## 4.4 拼接结果

最后一步很简单，把最终产生的ABCD，拼接在一块即可。

# 5. md5加密字符串实例

![image-20190327105831097](https://ws4.sinaimg.cn/large/006tKfTcly1g1h63ka1gaj311a0q6grl.jpg)

现以字符串“jklmn”为例。该字符串在内存中表示为（字母对应的ASCII码）：

6A 6B 6C 6D 6E（从左到右为低地址到高地址，后同），信息长度为40（8*5=40） bits， 即0x28。

对其填充，填充至448位，即56字节。结果为：

6A 6B 6C 6D 6E 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00

关于80：第一位为1，其他皆为0，我们只需看第一个字节，即二进制位10000000，转换成16进制即为80。

剩下64位，即8字节填充填充前信息位长，按小端字节序填充剩下的8字节(16进制的28表示原来的字符串长度)，结果为。

6A 6B 6C 6D 6E 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 28 00 00 00 00 00 00 00

（64字节，512 bits）

初始化A、B、C、D四个变量。

将这64字节填充后数据分成16个小组（程序中对应为16个数组），即：

M0：6A 6B 6C 6D （这是内存中的顺序，按照小端字节序原则，对应数组M(0)的值为0x6D6C6B6A，下同）

M1：6E 80 00 00

M2：00 00 00 00

.....

M14：28 00 00 00

M15：00 00 00 00

经过“3. 分组数据处理”后，a、b、c、d值分别为0xD8523F60、0x837E0144、0x517726CA、0x1BB6E5FE

在内存中为a：60 3F 52 D8

b：44 01 7E 83

c：CA 26 77 51

d：FE E5 B6 1B

a、b、c、d按内存顺序输出即为最终结果：603F52D844017E83CA267751FEE5B61B。这就是字符串“jklmn”的MD5值。



# 6. java实现MD5算法

```java
public class MD5 {
    /*
     * 四个链接变量
     */
    private final int A = 0x67452301;
    private final int B = 0xefcdab89;
    private final int C = 0x98badcfe;
    private final int D = 0x10325476;
    /*
     * ABCD的临时变量
     */
    private int Atemp, Btemp, Ctemp, Dtemp;

    /*
     * 常量ti公式:floor(abs(sin(i+1))×(2pow32)
     */
    private final int K[] = { 0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
            0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501, 0x698098d8,
            0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193,
            0xa679438e, 0x49b40821, 0xf61e2562, 0xc040b340, 0x265e5a51,
            0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
            0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905,
            0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681,
            0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60,
            0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
            0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244,
            0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92,
            0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314,
            0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391 };
    /*
     * 向左位移数,计算方法未知
     */
    private final int s[] = { 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7,
            12, 17, 22, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 6, 10,
            15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };

    /*
     * 初始化函数
     */
    private void init() {
        Atemp = A;
        Btemp = B;
        Ctemp = C;
        Dtemp = D;
    }

    /*
     * 移动一定位数
     */
    private int shift(int a, int s) {
        return (a << s) | (a >>> (32 - s));// 右移的时候，高位一定要补零，而不是补充符号位
    }

    /*
     * 主循环
     */
    private void MainLoop(int M[]) {
        int F, g;
        int a = Atemp;
        int b = Btemp;
        int c = Ctemp;
        int d = Dtemp;
        for (int i = 0; i < 64; i++) {
            if (i < 16) {
                F = (b & c) | ((~b) & d);
                g = i;
            } else if (i < 32) {
                F = (d & b) | ((~d) & c);
                g = (5 * i + 1) % 16;
            } else if (i < 48) {
                F = b ^ c ^ d;
                g = (3 * i + 5) % 16;
            } else {
                F = c ^ (b | (~d));
                g = (7 * i) % 16;
            }
            int tmp = d;
            d = c;
            c = b;
            b = b + shift(a + F + K[i] + M[g], s[i]);
            a = tmp;
        }
        Atemp = a + Atemp;
        Btemp = b + Btemp;
        Ctemp = c + Ctemp;
        Dtemp = d + Dtemp;

    }

    /*
     * 填充函数处理后应满足bits≡448(mod512),字节就是bytes≡56（mode64)填充方式为先加一个1,其它位补零
     * 最后加上64位的原来长度
     */
    private int[] add(String str) {
        int num = ((str.length() + 8) / 64) + 1;// 分组，以512位，64个字节为一组
        int strByte[] = new int[num * 16];// 64/4=16，所以有16个整数，ABCD
        for (int i = 0; i < num * 16; i++) {// 全部初始化0
            strByte[i] = 0;
        }
        int i;
        for (i = 0; i < str.length(); i++) {
            strByte[i >> 2] |= str.charAt(i) << ((i % 4) * 8);// 一个整数存储四个字节，小端序
        }
        strByte[i >> 2] |= 0x80 << ((i % 4) * 8);// 尾部添加1
        /*
         * 添加原长度，长度指位的长度，所以要乘8，然后是小端序，所以放在倒数第二个,这里长度只用了32位
         */
        strByte[num * 16 - 2] = str.length() * 8;
        return strByte;
    }

    /*
     * 调用函数
     */
    public String getMD5(String source) {
        init();
        int strByte[] = add(source);
        for (int i = 0; i < strByte.length / 16; i++) {
            int num[] = new int[16];
            for (int j = 0; j < 16; j++) {
                num[j] = strByte[i * 16 + j];
            }
            MainLoop(num);
        }
        return changeHex(Atemp) + changeHex(Btemp) + changeHex(Ctemp)
                + changeHex(Dtemp);

    }

    /*
     * 整数变成16进制字符串
     */
    private String changeHex(int a) {
        String str = "";
        for (int i = 0; i < 4; i++) {
            str += String.format("%2s",
                    Integer.toHexString(((a >> i * 8) % (1 << 8)) & 0xff)).replace(' ', '0');
        }
        return str;
    }

    /*
     * 单例
     */
    private static MD5 instance;
    public static MD5 getInstance() {
        if (instance == null) {
            instance = new MD5();
        }
        return instance;
    }

    private MD5() {
    };
}
```

# 7. 总结

经过上面的学习，我们已经详细的了解了MD5算法的历史，特点，应用，实现原理。其中，实现原理是十分重要的。这四步我们需要牢记，分别为原文处理，初始值，循环加工和拼接。其中，循环加工是最为复杂的，也是MD5的核心。

然后我们还举了一个例子，比较详细的讲解了MD5的加密过程。

最后，我们用java代码实现了MD5加密算法。

