/**
 *
 */
package com.mobanker.shanyidai.api.business.system;

import com.mobanker.shanyidai.api.dto.version.VersionReq;
import com.mobanker.shanyidai.api.dto.version.VersionRsp;

/**
 * 版本相关业务声明
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface VersionBusiness {

    public VersionRsp getVersion(VersionReq versionReq);
}
