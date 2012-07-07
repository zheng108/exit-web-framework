package org.exitsoft.showcase.vcsadmin.app;

import org.eclipse.jetty.server.Server;
import org.exitsoft.common.unit.JettyFactory;

/**
 * 启动jetty服务,运行后通过http://localhost:8087/vcs-admin/来访问项目路径s
 * 
 * @author vincent
 *
 */
public class LaunchJetty {
	
	/**
	 * 端口
	 */
	public static final int PORT = 8087;
	/**
	 * 项目名称
	 */
	public static final String CONTEXT = "/vcs-admin";

	public static void main(String[] args) throws Exception {
		Server server = JettyFactory.buildNormalServer(PORT, CONTEXT);
		server.start();
	}
}
