package com.mobanker.shanyidai.api.service.basedata;

/**获取职位类型列表
 * Created by zhouqianqian on 2017/2/8.
 */
public interface BaseDataService {
    /**
     * 获取职位类型列表
     * Created by zhouqianqian on 2017/2/13.
     */
    String listJobType();

    /**
     * 获取联系人关系
     * Created by zhouqianqian on 2017/2/13.
     */
    String getRelation(String product);

    /**
     * 获取学历列表
     * Created by zhouqianqian on 2017/2/13.
     */
    String getEducation(String product);
}
