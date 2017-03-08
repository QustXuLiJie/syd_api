/**
 * 
 */
package com.mobanker.shanyidai.api.application;

import com.mobanker.framework.server.MobankerApplication;
import com.mobanker.framework.server.netty.annotation.NettyBootstrap;

/**
 * 应用程序运行入口
 * 
 * @author chenjianping
 * @data 2016年12月9日
 */
@NettyBootstrap(springApplicationContext = "classpath:/mobanker-syd-application.xml", springServletContext = "classpath:/mobanker-syd-servlet.xml")
public class Server {
	public static void main(String[] args) {
		MobankerApplication.run(Server.class, args);
	}
}
