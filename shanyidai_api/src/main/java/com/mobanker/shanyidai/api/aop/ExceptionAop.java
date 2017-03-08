/**
 * 
 */
package com.mobanker.shanyidai.api.aop;

import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import com.mobanker.framework.tracking.EE;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.ResponseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 异常处理切面
 * 
 * @author chenjianping
 * @data 2016年12月9日
 */
public class ExceptionAop {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 处理运行异常的切面
	 * */
	public Object deal(ProceedingJoinPoint pjp) throws Throwable {
		ResponseEntity resultEntity = null;
		String className = pjp.getTarget().getClass().getCanonicalName();
		Transaction trans = EE.newTransaction("URL", "" + pjp.getSignature());
		logger.info("enter " + className + getMethodName(pjp) + " request[{}]", pjp.getArgs());
		try {
			resultEntity = new ResponseEntity(SystemConstant.OK.getValue(), ReturnCode.SUCCESS.getCode(), null, pjp.proceed());
			trans.setStatus(Transaction.SUCCESS);
		} catch (SydException e1) {
			EE.logEvent("Monitor_SYD_Exception", pjp.getSignature() + "-" + e1.message, Event.SUCCESS, className);
			resultEntity = new ResponseEntity(SystemConstant.FAIL.getValue(), e1.errCode, e1.message);
			trans.setStatus(Transaction.SUCCESS);
		} catch (Exception e2) {
			EE.logError("闪宜贷API-方法[" + pjp.getSignature().getName() + "]发生运行时异常-", e2);
			resultEntity = new ResponseEntity(SystemConstant.FAIL.getValue(), ReturnCode.SYS_EXCEPTION.getCode(),  ReturnCode.SYS_EXCEPTION.getDesc());
			trans.setStatus(e2);
		} finally {
			trans.complete();
		}
		logger.info("exit " + className + getMethodName(pjp) + " result[{}]", resultEntity);
		return resultEntity;

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
}
