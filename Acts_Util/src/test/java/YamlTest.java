/**
 * Copyright (C), 2015-2017
 * FileName: YamlTest
 * Author:   qianwenjun
 * Date:     2017/12/20 16:41
 * Description:
 */

import ExcelUtils.LogUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2017/12/20
 * @since 1.0.0
 */
public class YamlTest {

    public static void main(String[] args) {

        try {

            Yaml yaml = new Yaml();
            URL url = YamlTest.class.getClassLoader().getResource("test.yaml");
            if(url != null) {
                Object obj = yaml.load(new FileInputStream(url.getFile()));
                LogUtil.printConsoleLog("obj is " + obj);

                Map map = (Map)yaml.load(new FileInputStream(url.getFile()));
                LogUtil.printConsoleLog("map is" + map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}