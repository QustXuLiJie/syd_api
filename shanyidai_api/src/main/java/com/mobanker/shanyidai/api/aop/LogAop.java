package com.mobanker.shanyidai.api.aop;

import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.common.annotation.BusinessFlowAnnotation;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.thread.ThreadPoolUtil;
import com.mobanker.shanyidai.api.dto.log.ApiServiceLog;
import com.mobanker.shanyidai.api.dto.system.Dictionary;
import com.mobanker.shanyidai.api.service.log.ServiceLogService;
import com.mobanker.shanyidai.api.service.system.SystemService;
import com.mobanker.shanyidai.esb.common.constants.EsbSystemEnum;
import com.mobanker.shanyidai.esb.common.constants.ReqTypeEnum;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.esb.common.utils.MapRunable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

import static com.mobanker.shanyidai.api.common.constant.AppConstants.LOG_THREAD_POOL_NUM;
import static com.mobanker.shanyidai.api.common.constant.AppConstants.THREAD_POOL_NUM;

/**
 * @author hantongyang
 * @description 用于service层控制台日志打印用
 * @time 2016/12/20 13:47
 */
public class LogAop {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ServiceLogService serviceLogService;
    @Resource
    private CommonBusiness commonBusiness;
    @Resource
    private SystemService systemService;

    private static final String BS_FLOW_DIC = "bs_flow";

    //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
//    private static ExecutorService POOL = Executors.newFixedThreadPool(100);
    private ThreadPoolExecutor shortPool = ThreadPoolUtil.newInstanct(THREAD_POOL_NUM);
    private ThreadPoolExecutor pool = ThreadPoolUtil.newInstanct(LOG_THREAD_POOL_NUM);

    /**
    * @description
    * @author hantongyang
    * @time 2016/12/20 14:03
    * @method
    * @param pjp
    * @return
    */
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        //初始化日志参数
        String packageName = pjp.getSignature().getDeclaringType().getPackage().getName();
        String className = pjp.getSignature().getDeclaringType().getSimpleName();
        String methodName = pjp.getSignature().getName();
        Object[] params = pjp.getArgs();
        ApiServiceLog logDto = initApiServiceLog(packageName, className, methodName, new Date());
        //
        logger.info("enter " + className + getMethodName(pjp) + " parameters[{}]", pjp.getArgs());
        Object result = null;
        try{
            result = pjp.proceed();
            //初始化成功日志信息
            setSuccessInfo(logDto, result);
        }catch (Throwable e){
            logger.error("exit " + className + getMethodName(pjp) + " exception[{}]", e.getMessage());
            //初始化异常错误信息
            setErrorInfo(logDto, e);
            throw e;
        }finally {
            //启用多线程保存日志
            doSaveLog(logDto, params);
            //启用多线程保存流水
            duBusinessFlow(logDto, params, pjp);
        }
        logger.info("exit " + className + getMethodName(pjp) + " result[{}]", result);
        return result;
    }

    /**
     * @description
     * @author hantongyang
     * @time 2016/12/20 13:36
     * @method getMethodName
     * @param pjp
     * @return java.lang.String
     */
    private String getMethodName(ProceedingJoinPoint pjp) throws Exception{
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        return currentMethod.getName();
    }

    /**
     * @param dto
     * @return void
     * @description 保存日志
     * @author hantongyang
     * @time 2017/2/7 11:41
     * @method doSaveLog
     */
    private void doSaveLog(ApiServiceLog dto, Object [] params) {
        //判断业务调用是否正常，，如果业务调用是正常的，判断日志闸口是否需要记录正确的日志
        if(!checkFlag(dto, ZkConfigConstant.SYD_API_SERVICE_LOG_GATE.getZkValue())){
            return;
        }
        //封装参数保存日志
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dto", dto);
        map.put("params", params);
        shortPool.execute(new MapRunable(map) {
            @Override
            public void run() {
                ApiServiceLog dto = (ApiServiceLog) map.get("dto");
                setReqParams((Object[])map.get("params"), dto);
                setDubboMonitorEnd(dto);
                serviceLogService.saveServiceLog(dto);
            }
        });
    }

    /**
     * @param dto
     * @param params
     * @param pjp
     * @return void
     * @description 保存业务流水
     * @author hantongyang
     * @time 2017/2/7 11:41
     * @method doSaveLog
     */
    private void duBusinessFlow(ApiServiceLog dto, Object [] params, ProceedingJoinPoint pjp){
        //1、判断是否有流水注解
        BusinessFlowAnnotation annotation = (BusinessFlowAnnotation)
                pjp.getSignature().getDeclaringType().getAnnotation(BusinessFlowAnnotation.class);
        if(annotation == null){
            return;
        }
        //2、验证是否需要记录日志
        if(!checkFlag(dto, ZkConfigConstant.SYD_API_BUSINESS_FLOW.getZkValue())){
            return;
        }
        //3、获取流水字典信息
        List<Dictionary> dicList = getDictionary();
        if(dicList == null){
            return;
        }
        //4、验证是否是需要记录流水的方法
        boolean exists = false;
        for (Dictionary dictionary : dicList) {
            if(dictionary.getDicContent().indexOf(dto.getMethodName()) >= 0){
                exists = true;
                break;
            }
        }
        if(!exists){
            return;
        }
        //5、记录流水，需要综合服务里面排除拦截
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dto", dto);
        map.put("params", params);
        pool.execute(new MapRunable(map) {
            @Override
            public void run() {
                ApiServiceLog dto = (ApiServiceLog) map.get("dto");
                setReqParams((Object[])map.get("params"), dto);
                setDubboMonitorEnd(dto);
                serviceLogService.saveBusinessFlow(dto);
            }
        });
    }

    /**
     * @param packageName
     * @param className
     * @param methodName
     * @param startTime
     * @return com.mobanker.shanyidai.dubbo.dto.log.ApiServiceLog
     * @description 封装DubboLog实体参数
     * @author hantongyang
     * @time 2017/2/7 10:45
     * @method initApiServiceLog
     */
    private static ApiServiceLog initApiServiceLog(String packageName, String className, String methodName, Date startTime) {
        ApiServiceLog dto = new ApiServiceLog();
        dto.setPackageName(packageName); //包名
        dto.setClassName(className); //类名
        dto.setMethodName(methodName); //方法名
        dto.setReqType(ReqTypeEnum.HTTP.getType());
        dto.setReqStartTime(startTime);
        return dto;
    }

    /**
     * @description 初始化参数
     * @author hantongyang
     * @time 2017/2/13 11:44
     * @method setReqParams
     * @param params
     * @param dto
     * @return void
     */
    private static void setReqParams(Object[] params, ApiServiceLog dto){
        //防止出现无法转换成JSON格式的参数，所以只对SydRequest参数进行转换
        StringBuffer args = new StringBuffer();
        for(Object param : params){
            if(param instanceof SydRequest){
                args.append(JSONObject.toJSONString(param));
            }
        }
        dto.setReqParam(args.toString()); //参数
    }

    /**
     * @param dto
     * @return void
     * @description 初始化访问时间
     * @author hantongyang
     * @time 2017/2/7 11:28
     * @method setDubboMonitorEnd
     */
    private static void setDubboMonitorEnd(ApiServiceLog dto) {
        dto.setReqEndTime(new Date());
        dto.setDuration(dto.getReqEndTime().getTime() - dto.getReqStartTime().getTime());
        dto.setCreateTime(new Date());
        dto.setUpdateTime(new Date());
    }

    /**
     * @description 初始化错误信息
     * @author hantongyang
     * @time 2017/2/7 16:28
     * @method setErrorInfo
     * @param dto
     * @return void
     */
    private static void setErrorInfo(ApiServiceLog dto, Throwable e){
        if(e instanceof SydException){
            SydException exception = (SydException) e;
            dto.setErrorCode(exception.errCode);
            dto.setErrorResult(exception.message);
        }else{
            dto.setErrorCode(ReturnCode.SERVICE_FAILED.getCode());
            //将堆栈信息转换成流保存到库中
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            e.printStackTrace(new PrintWriter(buf, true));
            dto.setErrorResult(buf.toString());
        }
    }

    /**
     * @description 初始化成功返回信息
     * @author hantongyang
     * @time 2017/2/7 16:29
     * @method setSuccessInfo
     * @param dto
     * @param result
     * @return void
     */
    private static void setSuccessInfo(ApiServiceLog dto, Object result){
        //封装返回结果
        if (result != null) {
            //判断结果是否是ResponseEntity，如果不是则表示接口是成功响应，直接封装成JSON对象，如果是ResponseEntity，需要判断响应结果
            if(result instanceof ResponseEntity){
                //是ResponseEntity对象，需要判断响应结果
                ResponseEntity entity = (ResponseEntity) result;
                if (ResponseBuilder.isSuccess(entity)) {
                    dto.setIsSuccess(EsbSystemEnum.DB_STATUS_SUCCESS.getValue());
                    dto.setSuccessResult(JSONObject.toJSONString(entity.getData()));
                } else {
                    dto.setIsSuccess(EsbSystemEnum.DB_STATUS_FAILED.getValue());
                    dto.setErrorCode(entity.getError());
                    dto.setErrorResult(entity.getMsg());
                }
            }else{
                //不是ResponseEntity，表示接口是成功响应，直接封装成JSON对象
                dto.setIsSuccess(EsbSystemEnum.DB_STATUS_SUCCESS.getValue());
                dto.setSuccessResult(JSONObject.toJSONString(result));
            }
        } else {
            dto.setIsSuccess(EsbSystemEnum.DB_STATUS_FAILED.getValue());
            dto.setErrorCode(ReturnCode.SERVICE_FAILED.getCode());
            dto.setErrorResult(ReturnCode.SERVICE_FAILED.getDesc());
        }
    }

    /**
     * @description 验证是否需要保存日志
     * @author hantongyang
     * @time 2017/2/28 10:49
     * @method checkFlag
     * @param dto
     * @return boolean
     */
    private boolean checkFlag(ApiServiceLog dto, String key){
        if(dto != null && SystemConstant.OK.getValue().equals(dto.getIsSuccess())){
            String cacheSysConfig = null;
            try {
                cacheSysConfig = commonBusiness.getCacheSysConfig(key);
            } catch (Exception e) {
                throw new SydException(ReturnCode.CONFIG_DATA_NULL);
            }
            //如果返回值为0表示不需要记录正常的日志
            if(SystemConstant.FAIL.getValue().equals(cacheSysConfig)){
                return false;
            }
        }
        return true;
    }

    /**
     * @description 根据类型获取流水字典数据
     * @author hantongyang
     * @time 2017/2/27 20:31
     * @method getDictionary
     * @param
     * @return java.util.List<com.mobanker.shanyidai.api.dto.system.Dictionary>
     */
    private List<Dictionary> getDictionary(){
        Dictionary dictionary = new Dictionary();
        dictionary.setDicType(BS_FLOW_DIC);
        List<Dictionary> dictionarys = systemService.getDictionary(dictionary);
        return dictionarys;
    }
}
