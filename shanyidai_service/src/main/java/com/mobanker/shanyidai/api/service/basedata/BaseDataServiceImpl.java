package com.mobanker.shanyidai.api.service.basedata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mobanker.shanyidai.api.business.basedata.BaseDataBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.dto.basedata.JobType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouqianqian on 2017/2/8.
 */

@Service
public class BaseDataServiceImpl implements BaseDataService {
    @Resource
    private BaseDataBusiness baseDataBusiness;
    @Resource
    private RedisBusiness redisBusiness;

    /**
     * @return String
     * @description 获取职位列表
     * @author zhouqianqian
     * @method listJobType
     */
    @Override
    public String listJobType() {

        //获取职业类型列表
        String result = null;
        String bean = redisBusiness.getValue(RedisKeyEnum.SYD_JOBTYPE_INFO.getValue());
        if (!StringUtils.isBlank(bean)) {
            return bean;
        }

        try {
            result = baseDataBusiness.listJobType();
            if (StringUtils.isBlank(result)) {
                return null;
            }
            Map<Integer, JobType> jobMap = convertJobMap(result);

            if (!jobMap.isEmpty()) {
                redisBusiness.setValue(RedisKeyEnum.SYD_JOBTYPE_MAP.getValue(), JSON.toJSONString(jobMap));
            }
        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_JOBTYPE_GET_INFO.getCode(), "获取职业类型列表失败", e);
        }

        redisBusiness.setValue(RedisKeyEnum.SYD_JOBTYPE_INFO.getValue(), result);
        return result;
    }

    /**
     * @param result
     * @return java.util.Map<java.lang.Integer,com.mobanker.shanyidai.api.dto.basedata.JobType>
     * @description 将返回信息封装成map
     * @author Richard Core
     * @time 2017/3/4 13:55
     * @method convertJobMap
     */
    private Map<Integer, JobType> convertJobMap(String result) {
        JSONArray jsonAry = JSONArray.parseArray(result);
        Map<Integer,JobType> jobMap = new HashMap<Integer, JobType>();
        for (int i = 0; i < jsonAry.size(); i++) {
            JobType jobType = jsonAry.getObject(i, JobType.class);
            mapJobType2Map(jobType, jobMap);
        }
        return jobMap;
    }

    /**
     * @param jobType
     * @param jobMap
     * @return void
     * @description 递归将jobType放到map里
     * @author Richard Core
     * @time 2017/3/4 13:56
     * @method mapJobType2Map
     */
    private void mapJobType2Map(JobType jobType, Map<Integer, JobType> jobMap) {
        if (jobMap == null ) {
            jobMap = new HashMap<Integer, JobType>();
        }
        if (jobType == null) {
            return;
        }
        JobType jobTmp = new JobType();
        jobTmp.setId(jobType.getId());
        jobTmp.setPid(jobType.getPid());
        jobTmp.setName(jobType.getName());
        jobMap.put(jobType.getId(), jobTmp);
        List<JobType> child = jobType.getChild();
        if (child != null && !child.isEmpty()) {
            for (JobType job : child) {
                mapJobType2Map(job, jobMap);
            }
        }
    }

    /**
     * @return String
     * @description 获取联系人关系列表
     * @author zhouqianqian
     * @method getRelation
     */
    @Override
    public String getRelation(String product) {
        //获取联系人关系
        String result = null;
        String bean = redisBusiness.getValue(RedisKeyEnum.SYD_RELATION_INFO.getValue(), String.class);
        if (!StringUtils.isEmpty(bean)) {
            return bean;
        }

        try {
            result = baseDataBusiness.getRelation(product);

        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_RELATION_GET_INFO.getCode(), "获取联系人关系列表失败", e);
        }

        redisBusiness.setValue(RedisKeyEnum.SYD_RELATION_INFO.getValue(), result);
        return result;
    }


    /**
     * @return String
     * @description 获取学历列表
     * @author zhouqianqian
     * @method getEducation
     */
    @Override
    public String getEducation(String product) {

        //获取学历列表
        String result = null;
        String bean = redisBusiness.getValue(RedisKeyEnum.SYD_EDUCATION_INFO.getValue(), String.class);
        if (!StringUtils.isEmpty(bean)) {
            return bean;
        }

        try {
            result = baseDataBusiness.getEducation(product);

        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_EDUCATION_GET_INFO.getCode(), "获取学历列表失败", e);
        }

        redisBusiness.setValue(RedisKeyEnum.SYD_EDUCATION_INFO.getValue(), result);
        return result;
    }




}
