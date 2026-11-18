package Loader;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.service.MixinService;

public final class Bootstrap {

    private Bootstrap() {
    }

    public static void main(String[] args) throws Exception {
        Path modsDirectory = Paths.get(System.getProperty("ll.modsDir", "mods"));
        GameClassLoader loader = prepare(modsDirectory);

        try {
            Class<?> main = Class.forName("applet.Main", false, loader);
            main.getMethod("main", String[].class).invoke(null, (Object) args);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw exception;
        }
    }

    public static GameClassLoader prepare(Path modsDirectory) throws Exception {
        List<URL> sources = new ArrayList<>();
        sources.add(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation());

        List<Path> mods = Collections.emptyList();
        if (Files.exists(modsDirectory)) {
            if (!Files.isDirectory(modsDirectory)) {
                throw new IllegalArgumentException("Mods path is not a directory: " + modsDirectory);
            }
            try (Stream<Path> files = Files.list(modsDirectory)) {
                mods = files.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".jar"))
                        .sorted().collect(Collectors.toList());
            }
        }

        Set<String> configs = new LinkedHashSet<>();
        for (Path mod : mods) {
            sources.add(mod.toUri().toURL());
            try (JarFile jar = new JarFile(mod.toFile())) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory() && entry.getName().endsWith(".mixins.json")) {
                        if (!configs.add(entry.getName())) {
                            throw new IllegalArgumentException("Duplicate mixin config: " + entry.getName());
                        }
                    }
                }
            }
        }

        GameClassLoader loader = new GameClassLoader(
                sources.toArray(new URL[0]), Bootstrap.class.getClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
        GameMixinService.configure(loader);

        MixinBootstrap.init();
        GameMixinService service = (GameMixinService) MixinService.getService();
        MixinEnvironment.getCurrentEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        Mixins.addConfigurations(configs.toArray(new String[0]), null);
        service.startGamePhase();
        loader.setTransformer(service.createTransformer());

        System.out.println("[Last Limit] Mixin ready: " + mods.size() + " mod JAR(s), " + configs.size() + " config(s)");
        return loader;
    }
}
