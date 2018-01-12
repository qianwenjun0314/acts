/**
 * Copyright (C), 2015-2018
 * FileName: PropertiesFile
 * Author:   qianwenjun
 * Date:     2018/1/12 13:58
 * Description:
 */
package file;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 〈对一些配置属性文件的操作〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class PropertiesFile {

    private static Logger logger = Logger.getLogger(PropertiesFile.class);

    /**
     * 读取文件配件
     *
     * @param p_fileName
     *        p_fileName:配置文件的路径应为"相对路径"; eg："config/conf.properties";
     * 		  调用该类方法的类在/src/java/main目录下，对应的配置文件要放在/src/java/resources目录下；
     *        调用该类方法的类在/src/java/test目录下，对应的配置文件要放在/src/test/resources目录下；
     * @param key 配置文件的键
     * @return key 对应的value值
     */
    public static String readFile(String p_fileName, String key) {

        if (p_fileName == null || p_fileName.equals("")) {
            throw new NullPointerException("无效的文件路径名");
        }

        InputStream inputStream = null;
        String value = null;

        try {
            logger.info("文件路径："+ClassLoader.getSystemResource(p_fileName)+"-->key:"+key);
            inputStream = ClassLoader.getSystemResourceAsStream(p_fileName);
            Properties properties = new Properties();
            properties.load(inputStream);
            value = properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    /**
     * 将键值对写入properties文件中。默认不追加
     *
     * @param p_fileName
     *        p_fileName:配置文件的路径应为"相对路径"; eg："config/conf.properties";
     * 		  调用该类方法的类在/src/java/main目录下，对应的配置文件要放在/src/java/resources目录下；
     *        调用该类方法的类在/src/java/test目录下，对应的配置文件要放在/src/test/resources目录下；
     * @param content  键值对，eg：key=value
     * @return
     */
    public static void write(String p_fileName, String content) {
        write(p_fileName,content,false);
    }

    public static void write(String p_fileName, String content,Boolean isAppend) {

        FileOutputStream fileOutputStream = null ;
        String rootPath = System.class.getResource("/").getPath();
        String filePath = rootPath + "\\" +p_fileName;

        try {
            fileOutputStream = new FileOutputStream(filePath,isAppend);

            Properties properties = new Properties();
            String[] kv = content.split("=");
            if (kv.length == 2) {
                properties.setProperty(kv[0],kv[1]);
                properties.store(fileOutputStream,"内容写入文件");
            } else {
                throw new IllegalArgumentException("参数格式有误，正确的格式如：key=valuec");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}