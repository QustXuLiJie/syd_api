package com.mobanker.shanyidai.api.business.borrow;

import com.mobanker.shanyidai.api.business.borrow.util.BorrowDubboConvert;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.borrow.*;
import com.mobanker.shanyidai.api.enums.BorrowTypeEnum;
import com.mobanker.shanyidai.dubbo.dto.borrow.BorrowInfoParamDto;
import com.mobanker.shanyidai.dubbo.dto.borrow.BorrowOrderParamDto;
import com.mobanker.shanyidai.dubbo.service.borrow.BorrowDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.esb.common.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.mobanker.shanyidai.api.common.tool.BeanHelper.logger;


/**
 * 借款业务实现类
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class BorrowBusinessImpl implements BorrowBusiness {

    @Resource
    private BorrowDubboService dubboBorrowService;

    /**
     * @param request
     * @return QlBorrowing
     * @description 是否在前隆借款中，即能否借款
     * @author hantongyang
     * @time 2017/1/10 15:46
     * @method checkIsQLBorrowIng
     */
    @Override
    public QlBorrowing checkIsQLBorrowIng(SydRequest request) {
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        ResponseEntity entity = null;
        try {
            entity = dubboBorrowService.checkIsQLBorrowIng(request.getUserId());
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if(!ResponseBuilder.isFinished(entity)){
            throw new SydException(ReturnCode.ERROR_BORROW_CHECK_QL_BORROWING.getCode(), entity.getMsg());
        }
        if(entity.getData() == null){
            return null;
        }
        QlBorrowing qlBorrowing = BeanHelper.cloneBean(entity.getData(), QlBorrowing.class);
        return qlBorrowing;
    }

    /**
     * @description 根据订单号获取借款详情
     * @author hantongyang
     * @time 2017/1/12 14:33
     * @method getBorrowInfo
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfo
     */
    @Override
    public BorrowInfo getBorrowInfo(String nid) {
        if(StringUtils.isBlank(nid)){
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        ResponseEntity entity = null;
        try {
            entity = dubboBorrowService.getBorrowInfo(nid);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if(!ResponseBuilder.isSuccess(entity)){
            throw new SydException(ReturnCode.SERVICE_FAILED.getCode(), entity.getMsg());
        }
        if(entity.getData() == null){
            throw new SydException(ReturnCode.ERROR_BORROW_NID_FAILED);
        }
        BorrowInfo info = BeanUtil.cloneBean(entity.getData(), BorrowInfo.class);
        return info;
    }

    /**
     * @param param
     * @return java.util.List<com.mobanker.shanyidai.api.dto.borrow.BorrowHistory>
     * @description 查询借款历史
     * @author Richard Core
     * @time 2017/1/12 15:32
     * @method getHistoryList
     */
    @Override
    public List<BorrowHistory> getHistoryList(BorrowInfoParam param) {
        //参数处理
        BorrowInfoParamDto dto = BeanHelper.cloneBean(param, BorrowInfoParamDto.class);
        //默认查询单期借款信息
        if (dto.getQueryType() == null) {
            dto.setQueryType(BorrowTypeEnum.DANQI.getQueryType());
        }
        ResponseEntity responseEntity = null;
        try {
            responseEntity = dubboBorrowService.getHistoryList(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            logger.error("查询借款历史信息失败", e1);
            throw new SydException(ReturnCode.ERROR_BORROW_HISTORY.getCode(), e1.getMessage());
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("查询借款历史信息失败", responseEntity);
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        if (responseEntity.getData() == null) {
            return null;
        }
        //返回值处理
        List<BorrowHistory> borrowHistoryDtos = BorrowDubboConvert.getBorrowHistoryDtos(responseEntity);
        return borrowHistoryDtos;
    }

    /**
     * @description 创建下款单
     * @author hantongyang
     * @time 2017/1/17 10:56
     * @method addBorrowOrder
     * @param param
     * @return void
     */
    @Override
    public void addBorrowOrder(BorrowOrderDto param) {
        BorrowOrderParamDto dto = BeanHelper.cloneBean(param, BorrowOrderParamDto.class);
        ResponseEntity responseEntity = null;
        try {
            responseEntity = dubboBorrowService.addBorrowOrder(dto);
            if(!ResponseBuilder.isSuccess(responseEntity)){
                throw new SydException(responseEntity.getError(), responseEntity.getMsg());
            }
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e){
            logger.error("保存借款单信息失败", e);
            throw new SydException(ReturnCode.ERROR_ADD_BORROW_ORDER);
        }
    }
}
