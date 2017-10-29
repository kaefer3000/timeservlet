package edu.kit.aifb.tok.timeservlet;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.datetime.Iso8601Parser;
import org.semanticweb.yars.nx.dt.datetime.XsdDateTime;
import org.semanticweb.yars.nx.namespace.DCTERMS;

@Path("")
public class TimeServlet {

	@Context
	ServletContext _ctx;

	@GET
	public Response getTime(@Context UriInfo uriinfo) {
		
		Calendar now = GregorianCalendar.getInstance();
		long diff = now.getTimeInMillis() - (long) _ctx.getAttribute(TimeServletContext.STARTUPTIME_KEY);
		now.setTimeInMillis(now.getTimeInMillis()
				+ (long) ((double) _ctx.getAttribute(TimeServletContext.SPEEDUPFACTOR_KEY) * diff));

		Node[] triple = new Node[] { new Resource(uriinfo.getAbsolutePath().toString()), DCTERMS.CREATED,
				new Literal(
						Iso8601Parser.getCanonicalRepresentation((GregorianCalendar) now,
								true, true, true, true, true),
						XsdDateTime.DT) };

		return Response.ok(new GenericEntity<Iterable<Node[]>>(Collections.singleton(triple)) {
		}).build();

	}
}
