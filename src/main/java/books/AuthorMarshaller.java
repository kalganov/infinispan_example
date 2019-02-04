package books;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class AuthorMarshaller implements MessageMarshaller<Author> {
    @Override
    public void writeTo(ProtoStreamWriter writer, Author author) throws IOException {
        writer.writeString("name", author.getName());
        writer.writeString("surname", author.getSurname());
    }

    @Override
    public Author readFrom(ProtoStreamReader reader) throws IOException {
        String name = reader.readString("name");
        String surname = reader.readString("surname");
        return new Author(name, surname);
    }

    @Override
    public Class<? extends Author> getJavaClass() {
        return Author.class;
    }

    @Override
    public String getTypeName() {
        return "books.Author";
    }
}
