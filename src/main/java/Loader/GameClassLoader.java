package Loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

public final class GameClassLoader extends URLClassLoader {

    private IMixinTransformer transformer;
    private final Set<String> invalidClasses = ConcurrentHashMap.newKeySet();

    public void registerInvalidClass(String name) {
        invalidClasses.add(name);
    }

    public boolean isClassLoaded(String name) {
        synchronized (getClassLoadingLock(name)) {
            return findLoadedClass(name) != null;
        }
    }

    public void setTransformer(IMixinTransformer transformer) {
        if (transformer == null) {
            throw new IllegalArgumentException("Transformer cannot be null");
        }

        if (this.transformer != null) {
            throw new IllegalStateException("Transformer already configured");
        }

        this.transformer = transformer;
    }

    public GameClassLoader(URL[] sources, ClassLoader parent) {
        super(sources, parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes;

        try {
            bytes = readClassBytes(name, false);
        } catch (IOException exception) {
            throw new ClassNotFoundException(name, exception);
        }

        bytes = transform(name, bytes);

        try {
            String path = name.replace('.', '/') + ".class";
            URL resource = findResource(path);
            URL source = resource.getProtocol().equals("jar")
                    ? ((JarURLConnection) resource.openConnection()).getJarFileURL()
                    : new URL(resource.toExternalForm().substring(
                            0, resource.toExternalForm().length() - path.length()));

            int separator = name.lastIndexOf('.');
            if (separator > 0) {
                String packageName = name.substring(0, separator);
                if (getPackage(packageName) == null) {
                    definePackage(packageName, null, null, null, null, null, null, null);
                }
            }

            return defineClass(name, bytes, 0, bytes.length,
                    new CodeSource(source, (Certificate[]) null));
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot determine class source: " + name, exception);
        }
    }

    public byte[] readClassBytes(String name)
            throws IOException, ClassNotFoundException {
        return readClassBytes(name, true);
    }

    private byte[] readClassBytes(String name, boolean includeParent)
            throws IOException, ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        URL resource = findResource(path);

        if (resource == null && includeParent) {
            resource = getParent().getResource(path);
        }

        if (resource == null) {
            throw new ClassNotFoundException(name);
        }

        try (InputStream input = resource.openStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }

            return output.toByteArray();
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {

        synchronized (getClassLoadingLock(name)) {
            if (invalidClasses.contains(name)) {
                throw new ClassNotFoundException("Mixin implementation cannot be loaded as a game class: " + name);
            }
            Class<?> loaded = findLoadedClass(name);

            if (loaded == null) {
                boolean shared = name.startsWith("java.")
                        || name.startsWith("javax.")
                        || name.startsWith("sun.")
                        || name.startsWith("com.sun.")
                        || name.startsWith("jdk.")
                        || name.startsWith("Loader.")
                        || name.startsWith("net.bytebuddy.")
                        || name.startsWith("org.spongepowered.")
                        || name.startsWith("org.objectweb.asm.");

                if (shared) {
                    loaded = getParent().loadClass(name);
                } else {
                    if (findResource(name.replace('.', '/') + ".class") != null) {
                        loaded = findClass(name);
                    } else {
                        loaded = getParent().loadClass(name);
                    }
                }
            }

            if (resolve) {
                resolveClass(loaded);
            }

            return loaded;
        }
    }

    private byte[] transform(String name, byte[] bytes) {
        if (transformer == null) {
            throw new IllegalStateException("Mixin transformer is not configured");
        }

        return transformer.transformClassBytes(name, name, bytes);
    }
}
