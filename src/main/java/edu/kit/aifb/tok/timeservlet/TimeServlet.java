package edu.kit.aifb.tok.timeservlet;

import java.util.Collections;
import java.util.GregorianCalendar;

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

	@GET
	public Response getTime(@Context UriInfo uriinfo) {

		Node[] triple = new Node[] { new Resource(uriinfo.getAbsolutePath().toString()), DCTERMS.CREATED,
				new Literal(
						Iso8601Parser.getCanonicalRepresentation((GregorianCalendar) GregorianCalendar.getInstance(),
								true, true, true, true, true),
						XsdDateTime.DT) };

		return Response.ok(new GenericEntity<Iterable<Node[]>>(Collections.singleton(triple)) {
		}).build();

	}
}
