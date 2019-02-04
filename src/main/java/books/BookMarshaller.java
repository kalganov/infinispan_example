package books;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BookMarshaller implements MessageMarshaller<Book> {

    @Override
    public String getTypeName() {
        return "books.Book";
    }

    @Override
    public Class<? extends Book> getJavaClass() {
        return Book.class;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Book book) throws IOException {
        writer.writeString("title", book.getTitle());
        writer.writeString("description", book.getDescription());
        writer.writeCollection("authors", book.getAuthors(), Author.class);
    }

    @Override
    public Book readFrom(ProtoStreamReader reader) throws IOException {
        String title = reader.readString("title");
        String description = reader.readString("description");
        Set<Author> authors = reader.readCollection("authors", new HashSet<>(), Author.class);
        return new Book(title, description, authors);
    }
}