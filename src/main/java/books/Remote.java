package books;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class Remote {

    public static void main(String[] args) throws IOException {

        ConfigurationBuilder clientBuilder = new ConfigurationBuilder();
        clientBuilder.addServer()
                .host("127.0.0.1").port(11222)
                .marshaller(new ProtoStreamMarshaller());

        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(clientBuilder.build(true));

        SerializationContext serCtx = ProtoStreamMarshaller.getSerializationContext(remoteCacheManager);

        FileDescriptorSource fds = new FileDescriptorSource();
        fds.addProtoFiles("library.proto");
        serCtx.registerProtoFiles(fds);

        serCtx.registerMarshallerProvider(new MarshallerProvider());

        remoteCacheManager.getCacheNames().forEach(System.out::println);


        boolean cacheIsCreated = remoteCacheManager.getCacheNames().stream().anyMatch(s -> s.equals("books"));

        if (!cacheIsCreated) {
            testPutAndShow(remoteCacheManager);
        } else {
            remoteCacheManager.administration().removeCache("books");
            testPutAndShow(remoteCacheManager);
        }
        RemoteCache<String, Book> books = remoteCacheManager.getCache("books");
        books.values().stream()
                .map(Book::getAuthors)
                .flatMap(Collection::stream)
                .map(author -> author.getName() + " " + author.getSurname())
                .forEach(System.out::println);
    }

    private static void testPutAndShow(RemoteCacheManager remoteCacheManager) {
        remoteCacheManager.administration().createCache("books", (String) null);

        RemoteCache<String, Book> remoteCache = remoteCacheManager.getCache("books");

        remoteCache.put("1", new Book("Гарри Потер", "des", Collections.singleton(new Author("Джоан", "Роулинг"))));
        remoteCache.put("2", new Book("Книга джунглей", "des", Collections.singleton(new Author("Редьярд ", "Киплинг"))));
        remoteCache.put("3", new Book("Игра престолов", "des", Collections.singleton(new Author("Джордж", "Рэймонд Ричард Мартин"))));
        remoteCache.values().stream().map(Book::getTitle).forEach(System.out::println);
    }
}
