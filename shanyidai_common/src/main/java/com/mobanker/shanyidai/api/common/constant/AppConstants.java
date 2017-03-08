package com.mobanker.shanyidai.api.common.constant;
/** 
 * @author: hanty
 * @date 创建时间：2016-12-13 11:15
 * @version 1.0 
 * @parameter  
 * @return  
 */
public interface AppConstants {

	public static final int THREAD_POOL_NUM = 100;
	public static final int LOG_THREAD_POOL_NUM = 300;

    /**
     * 临时公私钥
     */
    public interface RSA {
        public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8K7T84gTf0E1dIH1rB1KrzlEC/rtThdD8hzfS+hYzilY6YzQ7/umsXmpYnsxVPqcva0LKod4/rAJbfwFBG+LAGEZoDtm4HFt8CaPIKCt2c81LlJo9r4wtodLTgIpf4AL0A0VT3rA0RJVD7563aiJYdCA9VEYuTqw56cQKsl8PbQIDAQAB";  //编码
        public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALwrtPziBN/QTV0gfWsHUqvOUQL+u1OF0PyHN9L6FjOKVjpjNDv+6axealiezFU+py9rQsqh3j+sAlt/AUEb4sAYRmgO2bgcW3wJo8goK3ZzzUuUmj2vjC2h0tOAil/gAvQDRVPesDRElUPvnrdqIlh0ID1URi5OrDnpxAqyXw9tAgMBAAECgYEAo0tmp+HYiwXwbTWpwTy8oH3NzcSTedrxzoPljQAcTiPpyoeWp84CqOPSdA9ykTNq0HrLnp80CJtT/GTOCNuTPNTjXEaH8SRwGVlYpuSVDBRS+mUF6CWde+ciVIuK3R1Ud1nNEFcvJS+d/H2gInRufefLmdJzMUujgewyHnwaLaECQQDpu8rIXeaOWRPXOwEr+trFb5ceVA/Rh5Yg7T54jyEq9Sm9XMg3mguzKnmLVfXOrVAGpkwhAv8HfArvjiXQOnllAkEAzhi+zRoOpzA3nzDwKDU7gW4CfuyYKX8mU1tGAfdFmjAQa2JMpJmEWYiEM2y8Bez2gJmTAbePs0TPa+l+5JRhaQJAed5jtiNXwmLpuHBYhRDwHr+3YKXd9ZcnjRWGXB/s4FQiJk0JTAxzC0EbTK5OUywErOLqkM/aH5Hqtcs9JhxHDQJAS2eOV6hS+CSSFTJoi61+Sgqf6yRRP81/jjv0zz9TPeib+U4L0KVCYSerhs0fteNPBRorSROKBgMFCOxzOtp3EQJBAMlt3k0K/g+KmVAI3alw020a57EEIXZhzpK87zOgsvMn2rBG2W5yfRWzGX6DVJCQYqlpBwYQiFPxLKBlPnWeqxg=";  // 成功
    }
    
    /**
     * 字典
     */
    public interface DICT {
    	public static String IP = "ip";
    	public static String CHANNELENV = "channel";
    	public static String PRODUCTENV = "product";
    	public static String REQUSERID = "userId";
    	public static String REQCONTENT = "content";
    	public static String REQCODE = "code";
    	public static String REQUUID = "uuid";
    	public static String REQDATA = "reqData";
    	public static String VERSIONENV = "version";
    }

}
