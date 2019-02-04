package books;

import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.MessageMarshaller;
import org.infinispan.protostream.SerializationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MarshallerProvider implements SerializationContext.MarshallerProvider {
    private static final Map<Class, MessageMarshaller> CLASS_MESSAGE_MARSHALLER_MAP;
    private static final Map<String, MessageMarshaller> TYPENAME_MARSHALLER_MAP;

    static {
        Map<Class, MessageMarshaller> classMarshallerMap = new HashMap<>();
        classMarshallerMap.put(Book.class, new BookMarshaller());
        classMarshallerMap.put(Author.class, new AuthorMarshaller());

        CLASS_MESSAGE_MARSHALLER_MAP = Collections.unmodifiableMap(classMarshallerMap);

        Map<String, MessageMarshaller> typeMarshallerMap = new HashMap<>();
        for (Map.Entry<Class, MessageMarshaller> entry : classMarshallerMap.entrySet()) {
            typeMarshallerMap.put(entry.getKey().getName(), entry.getValue());
        }
        TYPENAME_MARSHALLER_MAP = Collections.unmodifiableMap(typeMarshallerMap);
    }

    public static Map<Class, MessageMarshaller> getClassMessageMarshallerMap() {
        return CLASS_MESSAGE_MARSHALLER_MAP;
    }

    @Override
    public BaseMarshaller<?> getMarshaller(String typeName) {
        return TYPENAME_MARSHALLER_MAP.get(typeName);
    }

    @Override
    public BaseMarshaller<?> getMarshaller(Class<?> javaClass) {
        return CLASS_MESSAGE_MARSHALLER_MAP.get(javaClass);
    }
}
