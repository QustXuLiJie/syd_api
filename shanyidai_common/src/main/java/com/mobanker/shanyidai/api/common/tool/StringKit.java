package com.mobanker.shanyidai.api.common.tool;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * @version 1.0
 * @author: liuyafei
 * @date 创建时间：2016年8月23日
 * @parameter
 * @return
 */
public class StringKit {

    /**
     * 生成32位随机串
     *
     * @return
     * @author: liuyafei
     * @date 创建时间：2016年8月23日
     * @version 1.0
     * @parameter
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 校验字符串的长度
     *
     * @return
     * @author: liuyafei
     * @date 创建时间：2016年8月26日
     * @version 1.0
     * @parameter
     */
    public static boolean checkStringLength(String msg, int num) {
        if (StringUtils.isBlank(msg)) {
            return false;
        }
        if (msg.length() != num) {
            return false;
        }
        return true;
    }
}
