package com.mobanker.shanyidai.api.dto.gather;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Postal extends SydRequest {
	private static final long serialVersionUID = 7344019650251206548L;
	private Long userId; //userId*
	private String content; //短信内容*
	private String mac;
	private String idfa;
	private String imei;
	private String mobileDeviceId;
	
	public void splitMobileDeviceId(String mobileDeviceId, String appType){
		if(StringUtils.isNotBlank(mobileDeviceId)){
			String[] msg = mobileDeviceId.split(",");
			if("android".equals(appType)){
				this.mac=msg[0];
				if(msg.length>1){
					this.imei=msg[1];
				}
			}else if("ios".equals(appType)){
				this.idfa=msg[0];
			}
		}
	}
}
