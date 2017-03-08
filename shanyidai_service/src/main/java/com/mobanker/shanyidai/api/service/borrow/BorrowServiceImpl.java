package com.mobanker.shanyidai.api.service.borrow;

import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.borrow.BorrowBusiness;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.common.annotation.BusinessFlowAnnotation;
import com.mobanker.shanyidai.api.common.enums.ExpiresEnum;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.borrow.*;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.user.BankCard;
import com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp;
import com.mobanker.shanyidai.api.dto.user.UserCompleteness;
import com.mobanker.shanyidai.api.enums.BorrowProcessingEnum;
import com.mobanker.shanyidai.api.enums.BorrowStatusEnum;
import com.mobanker.shanyidai.api.enums.ProductEnum;
import com.mobanker.shanyidai.api.service.message.MessageService;
import com.mobanker.shanyidai.api.service.product.ProductService;
import com.mobanker.shanyidai.api.service.repay.RepayService;
import com.mobanker.shanyidai.api.service.user.UserAuthService;
import com.mobanker.shanyidai.api.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.mobanker.shanyidai.api.service.borrow.util.BorrowBusinessConvert.*;

/**
 * @author hantongyang
 * @description 借款相关接口实现
 * @time 2017/1/10 14:48
 */
@Service
@BusinessFlowAnnotation
public class BorrowServiceImpl implements BorrowService {

    @Resource
    private UserAuthService userAuthService;
    @Resource
    private BorrowBusiness borrowBusiness;
    @Resource
    private MessageService messageService;
    @Resource
    private UserService userService;
    @Resource
    private ProductService productService;
    @Resource
    private RedisBusiness redisBusiness;
    @Resource
    private CommonBusiness commonBusiness;
    @Resource
    private RepayService repayService;

    /**
     * @param request
     * @return boolean
     * @description 是否能借款
     * @author hantongyang
     * @time 2017/1/10 14:52
     * @method canBorrow
     */
    @Override
    public void canBorrow(SydRequest request) {
        //如果未登录，直接返回不能借款
        CommonUtil.checkLoginStatus(request);
        //验证用户认证进度
        checkUserVerification(request);

        //2、验证是否已借款，但是未还清
        QlBorrowing qlBorrowing = getQlBorrowing(request);
        if (qlBorrowing != null && StringUtils.isNotBlank(qlBorrowing.getBorrowNid())) {
            throw new SydException(ReturnCode.ERROR_BORROW_QLBORROWING_SYD);
        }
        //3、验证额度是否为零 TODO

    }

    private void checkUserVerification(SydRequest request) {
        //1、验证是否资料认证成功
        UserCompleteness completeness = userAuthService.getCompleteness(request);
        //TODO
        //1.1 验证资料认证是否已经完成
//        if (completeness == null || completeness.getCompleteProgressCou() < 3) {
//            throw new SydException(ReturnCode.ERROR_BORROW_COMP_LACK);
//        }
        //1.2 验证单位信息是否3个月未更新
        if (ExpiresEnum.INVALID.getValue().equals(completeness.getJobStatus())) {
            throw new SydException(ReturnCode.ERROR_BORROW_JOB_FAILED);
        }
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfo
     * @description 根据订单号查询用户借款详情
     * @author hantongyang
     * @time 2017/1/12 13:49
     * @method getBorrowInfo
     */
    @Override
    public BorrowInfo getBorrowInfo(SydRequest request) {
        //如果未登录，直接返回不能借款
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        String nid = JSONObject.parseObject(request.getContent()).getString("borrowNid");
        if (StringUtils.isBlank(nid)) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        return getBorrowInfo(nid);
    }

    /**
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfo
     * @description 根据订单号查询用户借款详情
     * @author hantongyang
     * @time 2017/1/14 17:18
     * @method getBorrowInfo
     */
    @Override
    public BorrowInfo getBorrowInfo(String nid) {
        if (StringUtils.isBlank(nid)) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        BorrowInfo borrowInfo = borrowBusiness.getBorrowInfo(nid);

        if (borrowInfo != null) {
            BorrowProcessingEnum borrowStatusEnum = BorrowProcessingEnum.getByStatus(String.valueOf(borrowInfo.getStatus()));
            borrowInfo.setStatusTitle(borrowStatusEnum.getStatusTitle());//标题显示
        }
        return borrowInfo;
    }

    /**
     * @param request
     * @return OrderDetail
     * @description 获取借还款详情
     * @author Richard Core
     * @time 2017/1/15 15:14
     * @method getOrderDetail
     */
    @Override
    public OrderDetail getOrderDetail(SydRequest request) {
        //如果未登录，直接返回不能借款
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        String nid = JSONObject.parseObject(request.getContent()).getString("borrowNid");
        if (StringUtils.isBlank(nid)) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        return getOrderDetail(request, nid);

    }

    /**
     * @param request
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.OrderDetail
     * @description 获取订单借还款详情
     * @author Richard Core
     * @time 2017/1/17 19:47
     * @method getOrderDetail
     */
    @Override
    public OrderDetail getOrderDetail(SydRequest request, String nid) {
        //封装参数
        BorrowInfoParam borrowInfoParam = BeanHelper.cloneBean(request, BorrowInfoParam.class);
        borrowInfoParam.setUserId(request.getUserId());
        borrowInfoParam.setBorrowNid(nid);
        BorrowInfo borrowInfo = borrowBusiness.getBorrowInfo(nid);
        if (borrowInfo == null) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_FAILED);
        }
        //借款信息封装
        OrderDetail orderDetail = BeanHelper.cloneBean(borrowInfo, OrderDetail.class);
        mapBorrowInfo2OrderDetail(borrowInfo, orderDetail);

//        List<BorrowHistory> historyList = borrowBusiness.getHistoryList(borrowInfoParam);
//        //验证返回结果
//        if (historyList == null || historyList.isEmpty()) {
//            throw new SydException(ReturnCode.ERROR_BORROW_NID_FAILED);
//        }
//        BorrowHistory borrowHistory = historyList.get(0);
//        if (borrowHistory == null) {
//            throw new SydException(ReturnCode.ERROR_BORROW_NID_FAILED);
//        }
        PayBackInfo repayDetail = repayService.getRepayDetail(nid, request);
        //还款信息封装
        return mapRepayDetail2OrderDetail(repayDetail, orderDetail);
    }


    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.QlBorrowing
     * @description 获取在是否在前隆借款中
     * @author hantongyang
     * @time 2017/1/14 14:47
     * @method getQlBorrowing
     */
    @Override
    public QlBorrowing getQlBorrowing(SydRequest request) {
        QlBorrowing qlBorrowing = borrowBusiness.checkIsQLBorrowIng(request);
        if (qlBorrowing != null) {
            //TODO 需要编写后续解析逻辑，暂定将有status!=5的数据就表示不能借款
            //检查次新户是否不符合借款条件（被拒绝过且没有申请成功）,借款状态status=2时，不符合条件
            if (BorrowStatusEnum.AUDIT_FAILED.getStatusCode().equals(qlBorrowing.getStatus())) {
                throw new SydException(ReturnCode.ERROR_BORROW_SCORE);
            }
            //需要判断是在哪个产品中有借款未还 TODO
            if (!BorrowStatusEnum.REPAYMENT_SUCCESS.getStatusCode().equals(qlBorrowing.getStatus())) {
                switch (ProductEnum.getProductEnum(qlBorrowing.getAddProduct())) {
                    case SHOUJIDAI:
                        throw new SydException(ReturnCode.ERROR_BORROW_QLBORROWING_SHOUJIDAI);
                    case UZONE:
                        throw new SydException(ReturnCode.ERROR_BORROW_QLBORROWING_UZONE);
                    case YHFQ:
                        throw new SydException(ReturnCode.ERROR_BORROW_QLBORROWING_YHFQ);
                    case SHANYIDAI:
                        return qlBorrowing;
                    default:
                        throw new SydException(ReturnCode.ENUM_ERROR);
                }
            }
        }
        return null;
    }

    /**
     * @param request
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowHistory
     * @description 查询借款单历史
     * @author hantongyang
     * @time 2017/1/14 18:23
     * @method getBorrowHistory
     */
    @Override
    public BorrowHistory getBorrowHistory(SydRequest request, String nid) {
        List<BorrowHistory> historyList = borrowBusiness.getHistoryList(initBorrowInfoParam(request, nid));
        if (historyList == null || historyList.isEmpty()) {
            return null;
        }
        return historyList.get(0);
    }

    /**
     * @param request
     * @param nid
     * @return java.util.List<com.mobanker.shanyidai.api.dto.borrow.BorrowHistory>
     * @description 查询借款单历史，如果不传借款单号，查询所有借款单
     * @author hantongyang
     * @time 2017/1/14 19:10
     * @method getBorrowHistoryList
     */
    @Override
    public List<BorrowHistory> getBorrowHistoryList(SydRequest request, String nid) {
        List<BorrowHistory> historyList = borrowBusiness.getHistoryList(initBorrowInfoParam(request, nid));
        if (historyList == null || historyList.isEmpty()) {
            return null;
        }
        return historyList;
    }

    @Override
    public Object getHistoryList(SydRequest request) {
        //如果未登录，直接返回不能借款
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        String nid = JSONObject.parseObject(request.getContent()).getString("borrowNid");
        List<BorrowHistory> historyList = borrowBusiness.getHistoryList(initBorrowInfoParam(request, nid));
        if (historyList == null || historyList.isEmpty()) {
            return null;
        }
        List<BorrowRecord> resultList = new ArrayList<BorrowRecord>();
        for (BorrowHistory borrowHistory : historyList) {
            if (borrowHistory == null) {
                continue;
            }
            BorrowRecord borrowRecord = BeanHelper.cloneBean(borrowHistory, BorrowRecord.class);
            BorrowStatusEnum byStatus = BorrowStatusEnum.getByStatus(borrowHistory.getStatus().toString());
            borrowRecord.setStatus(byStatus.getStatusCode());
            borrowRecord.setStatusTitle(byStatus.getStatusTitle());
            resultList.add(borrowRecord);
        }
        return historyList;
    }

    /**
     * @param request
     * @return java.lang.Integer
     * @description 借款次数
     * @author Richard Core
     * @time 2017/1/24 11:59
     * @method getBorrowTimes
     */
    @Override
    public Integer getBorrowTimes(SydRequest request) {
        List<BorrowHistory> historyList = borrowBusiness.getHistoryList(initBorrowInfoParam(request, null));
        if (historyList == null || historyList.isEmpty()) {
            return 0;
        }
        return historyList.size();
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 是否在闪宜贷借款中
     * @author Richard Core
     * @time 2017/2/6 9:57
     * @method sydBorrowing
     */
    @Override
    public boolean sydBorrowing(SydRequest request) {
        //如果未登录，直接返回不能借款
        CommonUtil.checkLoginStatus(request);

        //2、验证是否已借款，但是未还清
        QlBorrowing qlBorrowing = getSydBorrowing(request);
        if (qlBorrowing != null && StringUtils.isNotBlank(qlBorrowing.getBorrowNid())) {
            return true;
        }
        return false;
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.QlBorrowing
     * @description 是否在闪宜贷有借款
     * @author Richard Core
     * @time 2017/2/6 10:26
     * @method getSydBorrowing
     */
    private QlBorrowing getSydBorrowing(SydRequest request) {
        QlBorrowing qlBorrowing = borrowBusiness.checkIsQLBorrowIng(request);
        if (qlBorrowing != null) {
            //TODO 需要编写后续解析逻辑，暂定将有status!=5的数据就表示不能借款
            //检查次新户是否不符合借款条件（被拒绝过且没有申请成功）,借款状态status=2时，不符合条件
            if (BorrowStatusEnum.AUDIT_FAILED.getStatusCode().equals(qlBorrowing.getStatus())) {
                return null;
            }
            //需要判断是在哪个产品中有借款未还 TODO
            if (!BorrowStatusEnum.REPAYMENT_SUCCESS.getStatusCode().equals(qlBorrowing.getStatus())) {
                switch (ProductEnum.getProductEnum(qlBorrowing.getAddProduct())) {
                    case SHANYIDAI:
                        return qlBorrowing;
                }
            }
        }
        return null;
    }

    /**
     * @param request
     * @return void
     * @description 生成借款单
     * @author hantongyang
     * @time 2017/1/16 16:45
     * @method addBorrow
     */
    @Override
    public void addBorrow(SydRequest request) {
        //1、验证用户登录情况
        CommonUtil.checkLoginStatus(request);
        //获取参数对象
        BorrowOrderParam param = initBorrowOrderParam4Content(request);
        Long userId = request.getUserId();
        //2、验证订单信息
        //获取产品模板信息
        ProductTemp productTemp = productService.getProductTemp(request);
        checkBorrowOrderParam(param, productTemp);
        //3、验证验证码是否可用
        //获取缓存中的用户实体
        UserBasicInfoRsp user = userService.getUserInfoByUserId(userId);
        //TODO
//        messageService.checkSmsCaptcha(initValidateSmsCaptcha(user.getPhone(), userId, param.getCaptcha(), request));
        //4、验证输入的入账银行是否是用户银行列表中的银行
        List<BankCard> bankCards = userService.listBankCard(request);
        BankCard bankCard = checkAndCallBank(bankCards, param.getBankCard());
        //5、验证缓存中是否有该用户的借款信息，暂定三分钟
        BorrowOrderParam paramRedis = redisBusiness.getValue(RedisKeyEnum.SYD_ADD_BORROW_ORDER.getValue() + userId, BorrowOrderParam.class);
        if (paramRedis != null) {
            throw new SydException(ReturnCode.ERROR_BORROW_QLBORROWING_SYD);
        }
        try {
            //保存数据到缓存中，借款单保存成功后需要删除该缓存
            saveOrderParam2Redis(userId);
            //6、验证能否借款
            //TODO 2017/2/10 测试无法通过canBorrow，所以注释掉
//            canBorrow(request);
            //7、保存借款单
            //7.1 调用服务生成借款单号 没有该接口
            //7.2 封装参数
            BorrowOrderDto dto = initBorrowOrderDto(param, productTemp, user, null, request);
            //7.3 调用服务保存借款单
            borrowBusiness.addBorrowOrder(dto);
        } catch (Exception e) {
            //清除缓存
            redisBusiness.delValue(RedisKeyEnum.SYD_ADD_BORROW_ORDER.getValue() + userId);
            throw e;
        }
    }

    /**
     * @param userId
     * @return void
     * @description 保存借款单参数到缓存中
     * @author hantongyang
     * @time 2017/1/17 10:33
     * @method saveOrderParam2Redis
     */
    private void saveOrderParam2Redis(Long userId) {
        String cacheSysConfig = null;
        try {
            cacheSysConfig = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_ADD_BORROW_ORDER_PARAM.getZkValue());
        } catch (Exception e) {
            throw new SydException(ReturnCode.CONFIG_DATA_NULL);
        }
        String redisParam = JSONObject.toJSONString(initRedisBorrowOrderParam(userId));
        redisBusiness.setValue(RedisKeyEnum.SYD_ADD_BORROW_ORDER.getValue() + userId, redisParam, Integer.parseInt(cacheSysConfig));
    }
}
