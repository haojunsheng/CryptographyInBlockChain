import java.util.Arrays;

/**
*   编写一段将16进制数据转换为base64编码的程序
*   1. 字符串转化为16进制字符串 str2hexString
*   2. 16进制数据转换为byte类型 parseHexStr2Byte
*   3. 数据按照格式填充成为符合标准的数据
*/

public class Base64 {
    private static String hexStr =  "0123456789ABCDEF"; 
    static char[] table= {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P','Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f','g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v','w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    static int[] revTable= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,62,0,0,0,63,52,53,54,55,56,57,58,59,60,61,0,0,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,0,0,0,0,0,0,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,0,0,0,0,0};
    private static String[] binaryArray =   
        {"0000","0001","0010","0011",  
        "0100","0101","0110","0111",  
        "1000","1001","1010","1011",  
        "1100","1101","1110","1111"};  

    //base64 encode
    public static String encode(byte[] binaryData) {
        StringBuffer codes = new StringBuffer();
        int len = binaryData.length;
        for(int i=0;i<len;i+=3) {
            int data=((((0|(len-i>0?binaryData[i]:0)&0xff)<<8)|(len-i-1>0?binaryData[i+1]:0)&0xff)<<8)|(len-i-2>0?binaryData[i+2]:0)&0xff;
            for(int j=18;j>=0;j-=6)
                codes.append(table[(byte)((data>>j)&0x3f)]);
        }
        if(len%3>0) {
            codes.setCharAt(codes.length()-1, '=');
        }
        if(len%3==1) {
            codes.setCharAt(codes.length()-2, '=');
        }
        return codes.toString();
    }

    public static byte[] decode(String s) {
        int len = s.length();
        byte[] codes = new byte[len*3/4-(s.charAt(len-1)=='='?1:0)-(s.charAt(len-2)=='='?1:0)]; 
        int buffer=0,bufferLen=0,p=0;
        for(int i=0;i<codes.length;i++) {
            while(bufferLen<8) {
                if(bufferLen==0) buffer=0;
                buffer=(buffer<<6)|(revTable[(int)s.charAt(p++)]&0x3f);
                bufferLen+=6;
            }
            codes[i]=(byte) ((buffer>>(bufferLen-8))&0xff);
            bufferLen-=8;
        }
        return codes;
    }

    /**
     * @description 将16进制转换为二进制
     * 
     * @param hexStrring
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexString) {
        //hexString的长度对2取整，作为bytes的长度  
        int len = hexString.length()/2;  
        byte[] bytes = new byte[len];  
        byte high = 0;//字节高四位  
        byte low = 0;//字节低四位  
        for(int i=0;i<len;i++){  
             //左移四位得到高位  
             high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);  
             low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));  
             bytes[i] = (byte) (high|low);//高地位做或运算  
        }  
        return bytes;  
    }

    /*  
     * @param bytes 
     * @return 将二进制数组转换为十六进制字符串  2-16
     */  
    public static String bin2HexStr(byte[] bytes){  

        String result = "";  
        String hex = "";  
        for(int i=0;i<bytes.length;i++){  
            //字节高4位  
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
            //字节低4位  
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
            result +=hex;  //+" "
        }  
        return result;  
    } 

        /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2hexString(String s) {
       String str = "";
       for (int i = 0; i < s.length(); i++) {
        int ch = (int) s.charAt(i);
        String s4 = Integer.toHexString(ch);
        str = str + s4;
       }
       return str;
    }

    /**
     * 十六进制转换字符串
     *
     * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
 
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    public static void main(String[] args) {
        //byte[] a = { 1, 2, 3, -7, -9, 110};
        String plaintext = "Base64 test";
        System.out.println("plaintext is:"+plaintext);
       
        String hexString = str2hexString(plaintext);
        System.out.println("hexString is:" + hexString);
        
        byte[] a = parseHexStr2Byte(hexString);
        System.out.println("byte[] is:" + Arrays.toString(a));
        
        String s = encode(a);
        System.out.println("Base64 encode result:"+s);
        
        byte[] b = decode(s);
        System.out.println("Base64 decode byte[] is:" + Arrays.toString(b));

        String decodeHexString = bin2HexStr(b);
        System.out.println("Base64 decodeHexString result:"+decodeHexString);

        String decodeString = hexStr2Str(decodeHexString);
        System.out.println("Base64 decodeString result:"+decodeString);
    }
}