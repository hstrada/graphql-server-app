package library.howto;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import library.howto.mutation.Mutation;
import library.howto.query.Query;
import library.howto.repository.BookRepository;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

	private static final BookRepository bookRepository;

	static {
		MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://admin:admin@192.168.100.17:27017"));
		MongoDatabase mongoDatabase = mongo.getDatabase("library");
		bookRepository = new BookRepository(mongoDatabase.getCollection("books"));
	}

	public GraphQLEndpoint() {
		super(buildSchema());
	}
	
	private static GraphQLSchema buildSchema() {
		
		return SchemaParser
				.newParser()
				.file("schema.graphqls")
				.resolvers(
						new Query(bookRepository)
						, new Mutation(bookRepository))
				.build()
				.makeExecutableSchema();
	}

}