package library.howto.repository;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import library.howto.pojo.Book;

public class BookRepository {

	private final MongoCollection<Document> books;

	public BookRepository(MongoCollection<Document> books) {
        this.books = books;
    }

    public Book findById(String id) {
        Document doc = books.find(eq("_id", new ObjectId(id))).first();
        return book(doc);
    }
    
    private Book book(Document doc) {
        return new Book(
                doc.get("_id").toString(),
                doc.getString("name"),
                doc.getString("description"));
    }
    
    public List<Book> getAllBooks() {
      List<Book> allBooks = new ArrayList<Book>();
      for (Document doc : books.find()) {
    	  Book book = new Book(
                  doc.get("_id").toString(),
                  doc.getString("name"),
                  doc.getString("description")
          );
    	  allBooks.add(book);
      }
      return allBooks;
  }
    
    public void saveBook(Book book) {
        Document doc = new Document();
        doc.append("name", book.getName());
        doc.append("description", book.getDescription());
        books.insertOne(doc);
    }

}
