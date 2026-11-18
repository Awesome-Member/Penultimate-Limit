package Loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.platform.container.ContainerHandleVirtual;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.MixinEnvironment.Phase;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.logging.LoggerAdapterConsole;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.IClassTracker;
import org.spongepowered.asm.service.IMixinAuditTrail;
import org.spongepowered.asm.service.ITransformer;
import org.spongepowered.asm.service.ITransformerProvider;
import org.spongepowered.asm.service.MixinServiceAbstract;
import org.spongepowered.asm.util.IConsumer;

public final class GameMixinService extends MixinServiceAbstract
        implements IClassBytecodeProvider, IClassTracker, ITransformerProvider {

    private static GameClassLoader loader;
    private IConsumer<Phase> phaseConsumer;
    private final IContainerHandle container = new ContainerHandleVirtual("Last Limit");

    public static void configure(GameClassLoader targetLoader) {
        if (loader != null) {
            throw new IllegalStateException("Mixin service is already configured");
        }
        loader = targetLoader;
    }

    public void startGamePhase() {
        if (phaseConsumer == null) {
            throw new IllegalStateException("Mixin has not initialized its environment");
        }
        phaseConsumer.accept(Phase.DEFAULT);
        MixinEnvironment.getCurrentEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    public IMixinTransformer createTransformer() {
        IMixinTransformerFactory factory = getInternal(IMixinTransformerFactory.class);
        if (factory == null) {
            throw new IllegalStateException("Mixin transformer factory is unavailable");
        }
        return factory.createTransformer();
    }

    @Override
    public void wire(Phase phase, IConsumer<Phase> consumer) {
        this.phaseConsumer = consumer;
    }

    @Override
    public void unwire() {
        this.phaseConsumer = null;
    }

    @Override
    public String getName() {
        return "Last Limit";
    }

    @Override
    protected ILogger createLogger(String name) {
        return new LoggerAdapterConsole(name);
    }

    @Override
    public boolean isValid() {
        return loader != null;
    }

    @Override
    public IClassProvider getClassProvider() {
        return new MixinClassProvider(loader);
    }

    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return this;
    }

    @Override
    public IClassTracker getClassTracker() {
        return this;
    }

    @Override
    public ITransformerProvider getTransformerProvider() {
        return this;
    }

    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }

    @Override
    public Collection<String> getPlatformAgents() {
        return Collections.emptyList();
    }

    @Override
    public IContainerHandle getPrimaryContainer() {
        return container;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return loader.getResourceAsStream(name);
    }

    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        return getClassNode(name, true, 0);
    }

    @Override
    public ClassNode getClassNode(String name, boolean runTransformers)
            throws ClassNotFoundException, IOException {
        return getClassNode(name, runTransformers, 0);
    }

    @Override
    public ClassNode getClassNode(String name, boolean runTransformers, int readerFlags)
            throws ClassNotFoundException, IOException {
        ClassReader reader = new ClassReader(loader.readClassBytes(name.replace('/', '.')));
        ClassNode node = new ClassNode();
        reader.accept(node, readerFlags);
        return node;
    }

    @Override
    public void registerInvalidClass(String name) {
        loader.registerInvalidClass(name);
    }

    @Override
    public boolean isClassLoaded(String name) {
        return loader.isClassLoaded(name);
    }

    @Override
    public String getClassRestrictions(String name) {
        return "";
    }

    @Override
    public Collection<ITransformer> getTransformers() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ITransformer> getDelegatedTransformers() {
        return Collections.emptyList();
    }

    @Override
    public void addTransformerExclusion(String name) {
    }
}
