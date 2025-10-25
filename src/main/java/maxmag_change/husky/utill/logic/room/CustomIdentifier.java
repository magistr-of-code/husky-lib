package maxmag_change.husky.utill.logic.room;

import net.minecraft.util.Identifier;

public class CustomIdentifier {

    private final String namespace;
    private final String path;

    public CustomIdentifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    private CustomIdentifier(String[] id) {
        this(id[0], id[1]);
    }

    @Override
    public String toString() {
        return "CustomIdentifier{" +
                "namespace='" + namespace +
                ", path='" + path +
                '}';
    }

    public Identifier toIdentifier() {
        return new Identifier(namespace,path);
    }
}

