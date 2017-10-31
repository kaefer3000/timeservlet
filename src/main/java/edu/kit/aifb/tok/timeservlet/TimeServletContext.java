package edu.kit.aifb.tok.timeservlet;

import java.util.EnumSet;
import java.util.GregorianCalendar;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class TimeServletContext implements ServletContextListener {

	public static final String STARTUPTIME_KEY = "timeservlet.startuptime";
	public static final String SPEEDUPFACTOR_KEY = "timeservlet.speedupfactor";
	public static final double SPEEDUPFACTOR_DEFAULT = 1;

	ServletContext _ctx;

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		_ctx = sce.getServletContext();
		_ctx.setAttribute(STARTUPTIME_KEY, GregorianCalendar.getInstance().getTimeInMillis());

		double speedupfactor = SPEEDUPFACTOR_DEFAULT;
		try {
			speedupfactor = Double.parseDouble(System.getProperty(SPEEDUPFACTOR_KEY));
		} catch (NumberFormatException e) {
			_ctx.log("Could not parse property from system property: ", e);
		} catch (NullPointerException e) {
			// null means no value
		}
		try {
			speedupfactor = Double.parseDouble(_ctx.getInitParameter(SPEEDUPFACTOR_KEY));
		} catch (NumberFormatException e) {
			_ctx.log("Could not parse property from servlet context: ", e);
		} catch (NullPointerException e) {
			// null means no value
		}
		_ctx.log("running with speedup factor " + speedupfactor);
		_ctx.setAttribute(SPEEDUPFACTOR_KEY, speedupfactor);

		// Register Servlet
		ServletRegistration sr = _ctx.addServlet("Publishing the current time",
				org.glassfish.jersey.servlet.ServletContainer.class);
		sr.addMapping("/*");
		sr.setInitParameter(org.glassfish.jersey.server.ServerProperties.PROVIDER_PACKAGES,
				this.getClass().getPackage().getName() + ","
						+ org.semanticweb.yars.jaxrs.JerseyAutoDiscoverable.class.getPackage().getName());

		FilterRegistration fr;

		// Take CORS implementations from the containers.
		if (_ctx.getServerInfo().startsWith("jetty")) {
			fr = _ctx.addFilter("cross-origin", "org.eclipse.jetty.servlets.CrossOriginFilter");
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		} else if (_ctx.getServerInfo().toLowerCase().contains("tomcat")) {
			fr = _ctx.addFilter("cross-origin", "org.apache.catalina.filters.CorsFilter");
			fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		} else {
			_ctx.log("Please configure CORS for your server.");
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
