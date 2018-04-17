package library.howto.query;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import library.howto.pojo.Book;
import library.howto.repository.BookRepository;

public class Query implements GraphQLRootResolver {

	private final BookRepository bookRepository;

	public Query(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> allBooks() {
		return bookRepository.getAllBooks();
	}

}
