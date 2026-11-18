package Loader;

import java.net.URL;
import org.spongepowered.asm.service.IClassProvider;

public final class MixinClassProvider implements IClassProvider {

    private final GameClassLoader loader;

    public MixinClassProvider(GameClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public URL[] getClassPath() {
        return loader.getURLs();
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, false);
    }

    @Override
    public Class<?> findClass(String name, boolean initialize)
            throws ClassNotFoundException {
        return Class.forName(name, initialize, loader);
    }

    @Override
    public Class<?> findAgentClass(String name, boolean initialize)
            throws ClassNotFoundException {
        return Class.forName(name, initialize, loader.getParent());
    }
}
