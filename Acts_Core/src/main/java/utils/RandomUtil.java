/**
 * Copyright (C), 2015-2018
 * FileName: RandomUtil
 * Author:   qianwenjun
 * Date:     2018/1/12 15:47
 * Description:
 */
package utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * 〈随机数生成工具〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class RandomUtil {

    /**
     * Description: 随机获取UUID（含“-”）
     * @return
     */
    public static String getUUID36() {
        return UUID.randomUUID().toString();
    }

    /**
     * Description: 随机获取UUID（不含“-”）
     * @return
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Description: 随机正整数0~2147483647
     * @return
     */
    public static int randomInt() {
        return randomInt(0,(int)Math.pow(2,32));
    }

    /**
     * Description: 生成指定length位数的随机数字
     * @param length
     * @return
     */
    public static int randomInt(int length) {
        if(length>=10){
            throw new IllegalArgumentException("Interger's max length is 9!");
        }
        return randomInt((int)Math.pow(10, length-1),(int)Math.pow(10, length));
    }

    /**
     * Description: 生成start和end之间的随机整形数字,包含start，不包含end
     * @param
     * @return
     */
    public static int randomInt(int start,int end) {
        return (int) (Math.random()*(end-start)+start);
    }

    /**
     * Description: 生成指定length位数的随机字符串
     * @param length
     * @return
     */
    public static String randomString(int length) {
        String base = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer buf = new StringBuffer();
        //String temp = "";
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int p = rnd.nextInt(base.length());
            buf.append(base.substring(p, p + 1));
            //temp += base.substring(p, p + 1);
        }
        String temp=buf.toString();
        return temp;
    }


    /**
     * Description: 生成指定length位数的随机字符串,一次生成size个  (用于生产兑换码)
     * @param length
     * @param size
     * @return
     */
    public static String[] randomString(int length,int size) {
        Set<String> codeSet = new HashSet<String>();
        Random random = new Random();
        //String base = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String base = "1234567890abcdefghijklmnopqrstuvwxyz";

        while(true){
            //String code = "";
            StringBuffer buf=new StringBuffer();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(base.length());
                //code += base.substring(index, index + 1);
                buf.append(base.substring(index, index + 1));
            }
            String code=buf.toString();
            codeSet.add(code);
            if(codeSet.size() >= size){
                break;
            }
        }

        String[] codes = new String[codeSet.size()];
        return codeSet.toArray(codes);
    }

    public static void main(String[] args) {
        String[] codes = RandomUtil.randomString(5, 5000);
        StringBuffer temp = new StringBuffer();
        for(int i=0;i<codes.length;i++){
            System.out.println("zh"+codes[i]);
            temp.append("zh"+codes[i]+"\n");
        }
        System.out.println(temp.toString());
    }

}