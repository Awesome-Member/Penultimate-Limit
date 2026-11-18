package Loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

public final class MixinProperties implements IGlobalPropertyService {

    private final Map<String, Key> keys = new ConcurrentHashMap<>();
    private final Map<IPropertyKey, Object> values = new ConcurrentHashMap<>();

    @Override
    public IPropertyKey resolveKey(String name) {
        return keys.computeIfAbsent(name, Key::new);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProperty(IPropertyKey key) {
        return (T) values.get(key);
    }

    @Override
    public void setProperty(IPropertyKey key, Object propertyValue) {
        if (propertyValue == null) {
            values.remove(key);
        } else {
            values.put(key, propertyValue);
        }
    }

    @Override
    public <T> T getProperty(IPropertyKey key, T defaultValue) {
        T property = getProperty(key);
        return property != null ? property : defaultValue;
    }

    @Override
    public String getPropertyString(IPropertyKey key, String defaultValue) {
        Object propertyValue = values.get(key);
        return propertyValue != null ? propertyValue.toString() : defaultValue;
    }

    private static final class Key implements IPropertyKey {

        private final String name;

        private Key(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
