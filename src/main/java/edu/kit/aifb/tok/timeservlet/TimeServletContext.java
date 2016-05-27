package edu.kit.aifb.tok.timeservlet;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.semanticweb.yars.nx.Nodes;

@WebListener
public class TimeServletContext implements ServletContextListener {

	final static Logger _log = Logger.getLogger(TimeServletContext.class.getName());

	ServletContext _ctx;

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		_ctx = sce.getServletContext();

		// Register Servlet
		ServletRegistration sr = _ctx.addServlet("Publishing the current time",
				org.glassfish.jersey.servlet.ServletContainer.class);
		sr.addMapping("/*");
		sr.setInitParameter("jersey.config.server.provider.packages", this.getClass().getPackage().getName() + ";");

		FilterRegistration fr;

		// Take CORS implementations from the containers.
		if (_ctx.getServerInfo().startsWith("jetty")) {
			fr = _ctx.addFilter("cross-origin", "org.eclipse.jetty.servlets.CrossOriginFilter");
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		} else if (_ctx.getServerInfo().toLowerCase().contains("tomcat")) {
			fr = _ctx.addFilter("cross-origin", "org.apache.catalina.filters.CorsFilter");
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		} else {
			_log.warning("Please configure CORS for your server.");
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
