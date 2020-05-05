- SM2为基于椭圆曲线密码的公钥密码算法标准，包含数字签名、密钥交换和公钥加密，用于替换RSA / Diffie-Hellman / ECDSA / ECDH 等国际算法。
- SM3为密码哈希算法，用于替代MD5 / SHA-1 / SHA-256等国际算法。
- SM4为分组密码，用于替代DES / AES等国际算法。

| **PKCS标准汇总**                                    |      |                                                              |                                                              |
| :-------------------------------------------------- | :--- | :----------------------------------------------------------- | :----------------------------------------------------------- |
|                                                     | 版本 | 名称                                                         | 简介                                                         |
| PKCS #1                                             | 2.1  | RSA密码编译标准（RSA Cryptography Standard）                 | 定义了RSA的数理基础、公/私钥格式，以及加/解密、签/验章的流程。1.5版本曾经遭到攻击。 |
| PKCS #2                                             | -    | *撤销*                                                       | 原本是用以规范RSA加密摘要的转换方式，现已被纳入PKCS#1之中。  |
| PKCS #3                                             | 1.4  | DH密钥协议标准（Diffie-Hellman key agreement Standard）      | 规范以DH密钥协议为基础的密钥协议标准。其功能，可以让两方通过金议协议，拟定一把会议密钥(Session key)。 |
| PKCS #4                                             | -    | *撤销*                                                       | 原本用以规范转换RSA密钥的流程。已被纳入PKCS#1之中。          |
| PKCS #5                                             | 2.0  | 密码基植加密标准（Password-based Encryption Standard）       | 参见RFC 2898与[PBKDF2](https://baike.baidu.com/item/PBKDF2)。 |
| PKCS #6                                             | 1.5  | 证书扩展语法标准（Extended-Certificate Syntax Standard）     | 将原本X.509的证书格式标准加以扩充。                          |
| PKCS #7                                             | 1.5  | 密码消息语法标准（Cryptographic Message Syntax Standard）    | 参见RFC 2315。规范了以[公开密钥基础设施](https://baike.baidu.com/item/公开密钥基础设施)（PKI）所产生之签名/密文之格式。其目的一样是为了拓展数字证书的应用。其中，包含了[S/MIME](https://baike.baidu.com/item/S%2FMIME)与[CMS](https://baike.baidu.com/item/CMS)。 |
| PKCS #8                                             | 1.2  | 私钥消息表示标准（Private-Key Information Syntax Standard）. | Apache读取证书私钥的标准。                                   |
| PKCS #9                                             | 2.0  | 选择属性格式（Selected Attribute Types）                     | 定义PKCS#6、7、8、10的选择属性格式。                         |
| PKCS #10                                            | 1.7  | 证书申请标准（Certification Request Standard）               | 参见RFC 2986。规范了向证书中心申请证书之CSR（certificate signing request）的格式。 |
| [PKCS #11](https://baike.baidu.com/item/PKCS %2311) | 2.20 | 密码设备标准接口（Cryptographic Token Interface (Cryptoki)） | 定义了密码设备的应用程序接口（API）之规格。                  |
| PKCS #12                                            | 1.0  | 个人消息交换标准（Personal Information Exchange Syntax Standard） | 定义了包含私钥与公钥证书（public key certificate）的文件格式。私钥采密码(password)保护。常见的PFX就履行了PKCS#12。 |
| PKCS #13                                            | –    | [椭圆曲线密码学](https://baike.baidu.com/item/椭圆曲线密码学/2249951)标准（Elliptic curve cryptography Standard） | 制定中。规范以椭圆曲线密码学为基础所发展之密码技术应用。椭圆曲线密码学是新的密码学技术，其强度与效率皆比现行以指数运算为基础之密码学算法来的优秀。然而，该算法的应用尚不普及。 |
| PKCS #14                                            | –    | 拟随机数产生器标准（Pseudo-random Number Generation）        | 制定中。规范拟随机数产生器的使用与设计。                     |
| PKCS #15                                            | 1.1  | 密码设备消息格式标准（Cryptographic Token Information Format Standard） | 定义了密码设备内部数据的组织结构。                           |