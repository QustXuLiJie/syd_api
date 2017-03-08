package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/11 15:24
 */
public enum BorrowProcessingEnum {
    //       状态码  代表流程  作为标题显示 当前进度显示  补充进度显示
    UNKNOWN("-1","未知","未知","未知","未知"),
    AUDITINT("0","审核中","审核中","申请提交成功","正在审核"),
    AUDIT_SUCCESS("1","审核通过","下款中","审核成功","正在下款"),
    AUDIT_FAILED("2","审核失败","借款失败","审核失败",""),
    LOAN_SUCCESS("3","放款成功","待还款","下款成功",""),
    LOAN_FAILED("4","放款失败","借款失败","下款失败",""),
    REPAYMENT_SUCCESS("5","还款成功","还款完成","还款完成",""),
    REPAY_PROCESSING("30","放款成功","还款处理中","下款成功","还款处理中"),
    REPAY_SUCCESS("31","还款成功","还款完成","还款完成",""),
    REPAY_FAILED("32","下款成功","还款失败","下款成功","还款失败"),
    ;

    private String statusCode;//状态编号
    private String statusMsg;//状态说明 流程节点
    private String statusTitle;//状态说明 标题显示
    private String statusDisplay;//当前进度展示信息
    private String fixMsg;//补充进度显示信息

    public static BorrowProcessingEnum getByStatus(String status) {
        if (StringUtils.isBlank(status)) {
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for (BorrowProcessingEnum borrowStatusEnum : BorrowProcessingEnum.values()) {
            if (borrowStatusEnum.getStatusCode().equals(status)) {
                return borrowStatusEnum;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }


    BorrowProcessingEnum(String statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    BorrowProcessingEnum(String statusCode, String statusMsg, String statusTitle, String statusDisplay, String fixMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.statusTitle = statusTitle;
        this.statusDisplay = statusDisplay;
        this.fixMsg = fixMsg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public String getFixMsg() {
        return fixMsg;
    }
}
