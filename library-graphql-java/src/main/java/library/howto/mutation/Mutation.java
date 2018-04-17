package library.howto.mutation;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import library.howto.pojo.Book;
import library.howto.repository.BookRepository;

public class Mutation implements GraphQLRootResolver {

	private final BookRepository bookRepository;

	public Mutation(BookRepository bookRepository) {
		this.bookRepository = bookRepository;

	}

	public Book createBook(String name, String description) {
		Book newBook = new Book(name, description);
		bookRepository.saveBook(newBook);
		return newBook;
	}

}
