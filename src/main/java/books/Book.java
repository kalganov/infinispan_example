package books;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ProtoDoc("@Indexed")
public class Book implements Serializable {
    String title;
    String description;
    Set<Author> authors = new HashSet<Author>();

    public Book() {
    }

    public Book(String title, String description, Set<Author> authors) {
        this.title = title;
        this.description = description;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    @ProtoField(number = 1, required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    @ProtoField(number = 2, required = true)
    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    @ProtoField(number = 3, collectionImplementation = HashSet.class)
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}