package library.howto.pojo;

public class Book {

	private final String id;
	private final String name;
	private final String description;
	
	public Book(String name, String description) {
		this(null, name, description);
	}

	public Book(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
