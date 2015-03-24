package edu.carleton.comp4601.assignment3.Main;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.carleton.comp4601.assignment3.dao.Transaction;

@Path("/rs")
public class RS {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	final String homePath = System.getProperty("user.home");
	final String dataFolder = "/data/comp4601a3/";
	
	private ContentAnalyzer analyzer;
	private String name;

	public RS() {
		name = "COMP4601 Restful Service: Benjamin Sweett and Brayden Girard";
		analyzer = new ContentAnalyzer();
	}

	// Gets the SDA name as a String
	@GET
	public String printName() {
		return name;
	}

	// Gets the SDA name as XML
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String sayXML() {
		return "<?xml version=\"1.0\"?>" + "<sda> " + name + " </sda>";
	}

	// Gets the SDA name as HTML
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtml() {
		return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
				+ "</body></h1>" + "</html> ";
	}
	
	@GET
	@Path("reset/{dir}")
	@Produces(MediaType.TEXT_HTML)
	public String reset(@PathParam("dir") String dir) {
		
		SocialGraph.getInstance().clearAssignment3Data();
		
		DataParser parser = new DataParser(homePath + dataFolder);
		parser.parseAssignment3Content();
		
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title> Done Parsing </title></head>");
		htmlBuilder.append("<body><p>Done</p>");
		htmlBuilder.append("</body></html>");
		
		return htmlBuilder.toString();
	}
	
	@GET
	@Path("context")
	@Produces(MediaType.TEXT_HTML)
	public String context() {
		
		boolean ready = SocialGraph.getInstance().isA3ParseFinished();
		StringBuilder htmlBuilder = new StringBuilder();
		
		if(!ready) {
			
			htmlBuilder.append("<html>");
			htmlBuilder.append("<head><title> Data Context </title></head>");
			htmlBuilder.append("<body><p>Data is not finished parsing. Please visit /reset and wait for it to display 'Done'</p>");
			htmlBuilder.append("</body></html>");
			
			return htmlBuilder.toString();
		}
		
		analyzer.setDataGraph(SocialGraph.getInstance());
		analyzer.run();
		
		return "";
	}
	
	@GET
	@Path("community")
	@Produces(MediaType.TEXT_HTML)
	public String community() {
		
		return "";
	}
	
	@GET
	@Path("fetch/{user}/{page}")
	@Produces(MediaType.TEXT_HTML)
	public String fetch(@PathParam("user") String user, @PathParam("page") String page) {
		
		return "";
	}
	
	@GET
	@Path("advertising/{category}")
	@Produces(MediaType.TEXT_HTML)
	public String advertising(@PathParam("category") String category) {
		
		return "";
	}
	
	//Assignment 4
	
	@GET
	@Path("apriori")
	@Produces(MediaType.TEXT_HTML)
	public String apriori() {
		
		SocialGraph.getInstance().clearAssignment4Data();
		
		DataParser parser = new DataParser(homePath + dataFolder);
		parser.parseAssignment4Content();
		
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><title> All Transactions </title></head>");
		htmlBuilder.append("<body>");
		for(Transaction tr : SocialGraph.getInstance().getTransactions().values()) {
			htmlBuilder.append(tr.toHTMLString());
		}
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");
		
		return htmlBuilder.toString();
	}
	
	@GET
	@Path("suggest/{products}")
	@Produces(MediaType.TEXT_HTML)
	public String suggest(@PathParam("products") String products) {
		
		boolean ready = SocialGraph.getInstance().isA4ParseFinished();
		StringBuilder htmlBuilder = new StringBuilder();
		
		if(!ready) {
			
			htmlBuilder.append("<html>");
			htmlBuilder.append("<head><title> Retail Suggest </title></head>");
			htmlBuilder.append("<body><p>Transactions are not finished parsing. Please visit /apriori and wait for it to display</p>");
			htmlBuilder.append("</body></html>");
			
			return htmlBuilder.toString();
		}
		
		return "";
	}
}
