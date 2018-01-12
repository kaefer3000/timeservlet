package edu.kit.aifb.tok.timeservlet;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.dt.datetime.Iso8601Parser;
import org.semanticweb.yars.nx.dt.datetime.XsdDateTime;
import org.semanticweb.yars.nx.namespace.DCTERMS;
import org.semanticweb.yars.nx.namespace.RDF;
import org.semanticweb.yars.nx.namespace.XSD;

@Path("")
public class TimeServlet {

	@Context
	ServletContext _ctx;

	@GET
	public Response getTime(@Context UriInfo uriinfo) {
		
		Calendar now = GregorianCalendar.getInstance();
		long diff = now.getTimeInMillis() - (long) _ctx.getAttribute(TimeServletContext.STARTUPTIME_KEY);
		now.setTimeInMillis((long) _ctx.getAttribute(TimeServletContext.STARTUPTIME_KEY)
				+ (long) ((double) _ctx.getAttribute(TimeServletContext.SPEEDUPFACTOR_KEY) * diff));

		Node[] triple = new Node[] { new Resource(uriinfo.getAbsolutePath().toString()), DCTERMS.CREATED,
				new Literal(
						Iso8601Parser.getCanonicalRepresentation((GregorianCalendar) now,
								true, true, true, true, true),
						XsdDateTime.DT) };

		List<Node[]> triples = new LinkedList<Node[]>();
		triples.add(triple);
		BNode nowbn2 = new BNode("_:now2");
		triples.add(new Node[] { new Resource(uriinfo.getAbsolutePath().toString()), DCTERMS.CREATED, nowbn2 });
		triples.add(new Node[] { nowbn2, RDF.TYPE,
				new Resource("<http://www.w3.org/2006/time#DateTimeDescription>", true) });
		triples.add(
				new Node[] { nowbn2, new Resource("<http://www.w3.org/2006/time#dayOfWeek>", true),
						new Resource("<http://www.w3.org/2006/time#"
								+ now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.ENGLISH) + ">",
								true) });
		triples.add(new Node[] { nowbn2, new Resource("<http://www.w3.org/2006/time#hour>", true),
				new Literal("\"" + now.get(Calendar.HOUR_OF_DAY) + "\"^^" + XSD.INTEGER, true) });

		return Response.ok(new GenericEntity<Iterable<Node[]>>(triples) {
		}).build();

	}
}
