package cn.sicnu.ming.util;

import java.security.MessageDigest;

/**
 * @author frank ming
 * @createTime 2020061717 19:28
 * @description md5加密的工具类
 */
public class Md5HashUtil {

    public Md5HashUtil() {
    }

    public final static String SALT = "MD5";

    public final static String md5(String s) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };

        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance(SALT);
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    public final static String md5x2(String s) {
        return md5(md5(s));
    }

    /**
     * 使用MD5 对两端加密后的密文进行比较
     *
     * @author hezhao
     * @param strOne
     *            未加密的字符串
     * @param strTwo
     *            已加密的字符串
     * @return boolean
     */
    public static boolean check(String strOne, String strTwo) {
        if (md5x2(strOne).equals(strTwo))
            return true;
        else
            return false;
    }

//    public static void main(String[] args) {
//        System.out.println(Md5HashUtil.md5x2("12345"));
//        System.out.println(Md5HashUtil.md5x2("123456111111111111111"));
//        System.out.println(Md5HashUtil.check("12345","CF7D4BDD2AFBB023F0B265B3E99BA1F9"));
//    }
}
