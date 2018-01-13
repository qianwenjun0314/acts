/**
 * Copyright (C), 2015-2018
 * FileName: Schema
 * Author:   qianwenjun
 * Date:     2018/1/13 14:12
 * Description:
 */
package db;

/**
 * 〈〉
 *
 * @author qianwenjun
 * @create 2018/1/13
 * @since 1.0.0
 */
public enum Schema {
    chediandian,
    morder,
    worder,
    pay_center,
    shop,
    dcm,
    drm,
    ndsm,
    platform_center,
    cloud_stock;

    public static void main(String[] args) {

        Schema sun = Schema.chediandian;
        System.out.println(sun); // 输出 SUN
    }
}