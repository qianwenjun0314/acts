/**
 * Copyright (C), 2015-2018
 * FileName: TextFile
 * Author:   qianwenjun
 * Date:     2018/1/12 15:40
 * Description:
 */
package file;

import java.io.*;

/**
 * @Description: 文本文件的操作
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class TextFile {

    /**
     * 读取文本文件的内容
     *
     * @return 以字符串的形式返回，每行进行换行处理
     */
    public static String read(String fileName) {
        File file = createFile(fileName);

        StringBuffer result = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                result.append(line + "\r\n");
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                isr.close();
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 将字符串写入文本文件中,默认不追加
     *
     * @param fileName 完全路径名
     * @return 以字符串的形式返回，每行进行换行处理
     */
    public static void write(String fileName, String content) {
        write(fileName, content, false);
    }


    /**
     * 将字符串写入文本文件中
     *
     * @param fileName 完全路径名
     * @return 以字符串的形式返回，每行进行换行处理
     */
    public static void write(String fileName, String content, boolean isAppend) {
        File file = createFile(fileName);

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file, isAppend));
            osw.write(content);
            osw.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                osw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 将源文件的内容读取出来，写入目标文件中
     *
     * @param srcFileName 源文件名
     * @param dstFileName 目标文件名
     * @param isAppend    是否追加 true，追加，否则，不追加
     * @return 以字符串的形式返回，每行进行换行处理
     */
    public static void writeFromFile(String srcFileName, String dstFileName, boolean isAppend) {
        File srcfile = createFile(srcFileName);
        File dstfile = createFile(dstFileName);

        String line = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStreamWriter osw = null;
        try {
            isr = new InputStreamReader(new FileInputStream(srcfile));
            br = new BufferedReader(isr);
            osw = new OutputStreamWriter(new FileOutputStream(dstfile, isAppend));

            line = br.readLine();
            while (line != null) {
                osw.write(line + "\r\n");
                line = br.readLine();
            }
            osw.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                isr.close();
                br.close();
                osw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private static File createFile(String fileName) {
        if (fileName == null || fileName.equals("")) {
            throw new NullPointerException("无效的文件路径名");
        }
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();  //文件不存在进行创建
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }

}