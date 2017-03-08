package com.mobanker.shanyidai.api.service.index;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.borrow.BorrowHistory;
import com.mobanker.shanyidai.api.dto.borrow.BorrowInfo;
import com.mobanker.shanyidai.api.dto.borrow.BorrowProcess;
import com.mobanker.shanyidai.api.dto.borrow.QlBorrowing;
import com.mobanker.shanyidai.api.dto.index.Index;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.repay.PayResult;
import com.mobanker.shanyidai.api.dto.user.UserCompleteness;
import com.mobanker.shanyidai.api.enums.BorrowProcessingEnum;
import com.mobanker.shanyidai.api.enums.PayResultStatus;
import com.mobanker.shanyidai.api.service.borrow.BorrowService;
import com.mobanker.shanyidai.api.service.borrow.util.BorrowBusinessConvert;
import com.mobanker.shanyidai.api.service.product.ProductService;
import com.mobanker.shanyidai.api.service.repay.RepayService;
import com.mobanker.shanyidai.api.service.user.UserAuthService;
import com.mobanker.shanyidai.api.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.mobanker.shanyidai.api.service.index.util.IndexBusinessConvert.initBorrowStatus;
import static com.mobanker.shanyidai.api.service.index.util.IndexBusinessConvert.initIndex;


/**
 * @author hantongyang
 * @description
 * @time 2017/1/10 14:22
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private ProductService productService;
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private BorrowService borrowService;
    @Resource
    private RepayService repayService;
    @Resource
    private UserService userService;

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.index.Index
     * @description 获取首页信息
     * 1、查询产品模板详情
     * 2、查询资料认证进度
     * 3、查询闪闻
     * 4、查询借款详情
     * 4.1 查询是否登陆，未登录直接返回
     * 4.2 查询是否在前隆借款中，如果在前隆借款中，则根据借款单号查询历史接口获取详情
     * 4.2.1 借款状态是下款成功之前，需要查询
     * 4.2.2 借款状态是下款成功，需要查询逾期等信息
     * 4.3 有借款查询借款次数
     * @author hantongyang
     * @time 2017/1/14 13:48
     * @method index
     */
    @Override
    public Index index(SydRequest request) {
        //1、查询产品模板详情
        ProductTemp productTemp = null;
        try {
            productTemp = productService.getProductTemp(request);
        } catch (Exception e) {
            throw new SydException(ReturnCode.ADD_REALNAME_AUTH_FAILED);
        }
        //2、查询资料认证进度
        UserCompleteness completeness = null;
        try {
            completeness = userAuthService.getCompleteness(request);
        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_USER_COMPLENTE);
        }
        //如果资料认证进度小于3，表示未认证成功，无需查询借款详情信息
        //TODO
//        if (completeness != null && completeness.getCompleteProgressCou() < 3) {
//            return initIndex(productTemp, completeness, null, null, status, 0, borrowProcess,0);
//        }
        //3、查询闪闻
        //TODO

        //4、查询借款详情
        int borrowCou = 0;
        int bankCardAmount = 0;
        List<BorrowProcess> borrowProcess = null;
        String status = "-1";
        BorrowInfo borrowInfo = null;
        PayBackInfo backInfo = null;
        //4.1 查询是否登陆，未登录直接返回
        if (request != null && request.getUserId() != null) {
            //4.2 查询是否在前隆借款中，如果在前隆借款中，则根据借款单号查询历史接口获取详情
            QlBorrowing qlBorrowing = borrowService.getQlBorrowing(request);
            if (qlBorrowing != null) {
                String nid = qlBorrowing.getBorrowNid();
                //封装借款单状态
                status = qlBorrowing.getStatus() + "";
                //根据订单号查询不带有还款信息的订单详情
                borrowInfo = borrowService.getBorrowInfo(nid);
                BorrowProcessingEnum statusEnum = BorrowProcessingEnum.getByStatus(status);
                switch (statusEnum) {
                    case AUDITINT:
                    case AUDIT_SUCCESS:
                    case AUDIT_FAILED:
                        if (borrowInfo != null) {
                            //组装借款单进度数据
                            borrowProcess = BorrowBusinessConvert.getBorrowProcess(borrowInfo, statusEnum);
                        }
                        break;
                    case LOAN_SUCCESS:
                        //查询我要还款信息
                        backInfo = repayService.getRepayDetail(nid, request);
                        if (backInfo == null) {
                            break;
                        }
                        //查询是否有正在处理的
                        PayResult repayStatus = repayService.getRepayStatus(nid);
                        if (repayStatus == null) {
                            break;
                        }
                        PayResultStatus payResultStatus = PayResultStatus.getInstance(repayStatus.getStatusCode());
                        switch (payResultStatus) {
                            case PAY_FAILED:
                                status = BorrowProcessingEnum.REPAY_FAILED.getStatusCode();
                                borrowInfo.setStatus(Integer.valueOf(status));
                                borrowProcess = BorrowBusinessConvert.getBorrowProcess(borrowInfo, BorrowProcessingEnum.REPAY_FAILED);
                                break;
                            case PAY_SUCCESS:
                                status = BorrowProcessingEnum.REPAY_SUCCESS.getStatusCode();
                                borrowInfo.setStatus(Integer.valueOf(status));
                                borrowProcess = BorrowBusinessConvert.getBorrowProcess(borrowInfo, BorrowProcessingEnum.REPAY_SUCCESS);
                                break;
                            case PROCESSING:
                                status = BorrowProcessingEnum.REPAY_PROCESSING.getStatusCode();
                                borrowInfo.setStatus(Integer.valueOf(status));
                                borrowProcess = BorrowBusinessConvert.getBorrowProcess(borrowInfo,  BorrowProcessingEnum.REPAY_PROCESSING);
                                break;
                            case NO_RECORD:
                                break;
                        }
                        break;
                    case LOAN_FAILED:
                    default:
                        break;
                    //throw new SydException(ReturnCode.ERROR_BORROW_STATUS);
                }

            }
            //4.3 有借款，需要查询借款次数
            List<BorrowHistory> borrowHistoryList = borrowService.getBorrowHistoryList(request, null);
            if (borrowHistoryList != null && !borrowHistoryList.isEmpty()) {
                borrowCou = borrowHistoryList.size();
            }
            //4.4 重新封装状态字段
            status = initBorrowStatus(status);
            //4.5 获取银行卡个数 
            bankCardAmount = userService.bankCardAmount(request);

        }
        return initIndex(productTemp, completeness, borrowInfo, backInfo, status, borrowCou, borrowProcess,bankCardAmount);
    }
}
