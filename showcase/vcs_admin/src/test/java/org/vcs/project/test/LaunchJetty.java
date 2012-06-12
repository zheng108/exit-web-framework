package org.vcs.project.test;

import org.eclipse.jetty.server.Server;
import org.exitsoft.common.unit.JettyFactory;

public class LaunchJetty {
	
	public static final int PORT = 8087;
	public static final String CONTEXT = "/project";

	public static void main(String[] args) throws Exception {
		Server server = JettyFactory.buildNormalServer(PORT, CONTEXT);
		server.start();
	}
}
