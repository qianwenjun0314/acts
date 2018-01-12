/**
 * Copyright (C), 2015-2018
 * FileName: XmlFile
 * Author:   qianwenjun
 * Date:     2018/1/12 15:42
 * Description:
 */
package file;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/12
 * @since 1.0.0
 */
public class XmlFile {

    /**
     * 解析文件返回根节点
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Element read(String fileName) throws Exception {
        // 定义工厂API 使应用程序能够从XML文档获取生成DOM对象树的解析器
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 获取此类的实例之后，将可以从各种输入源解析XML
        DocumentBuilder builder = factory.newDocumentBuilder();
        // builder.parse(this.getClass().getResourceAsStream("/" + fileName));
        // Document接口表示整个HTML或XML文档，从概念上讲，它是文档树的根，并提供对文档数据的基本访问
        Document document = builder.parse(fileName);
        // 获取根节点
        Element root = document.getDocumentElement();
        return root;
    }

    /**
     * 获取特子定节点属性
     *
     * @param fileName   要解析的文件
     * @param targetnode 要解析的xml节点名字
     * @return
     */
    public static List<Map<String, String>> getNodeAttributes(String fileName, String targetnode) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        try {
            NodeList nodeList = read(fileName).getElementsByTagName(targetnode);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                // 获取一个节点
                Node node = nodeList.item(i);
                // 获取该节点所有属性
                NamedNodeMap attributes = node.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node attribute = attributes.item(j);
                    map.put(attribute.getNodeName(), attribute.getNodeValue());
                }
                result.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有子节点数据
     *
     * @param fileName   要解析的文件
     * @param targetnode 要解析的xml节点名字
     * @return
     */
    public static List<String> getChildNodes(String fileName, String targetnode) {
        List<String> result = new ArrayList<String>();
        try {
            NodeList nodeList = read(fileName).getElementsByTagName(targetnode);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                // 获取一个节点
                Node node = nodeList.item(i);
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childNode = childNodes.item(j);
                    result.add(childNode.getNodeValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}