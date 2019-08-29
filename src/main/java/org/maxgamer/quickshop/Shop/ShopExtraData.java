package org.maxgamer.quickshop.Shop;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ShopExtraData {
    private Map<String, Map<String, String>> shopExtraData;

    /**
     * Set extra to a shop.
     * @param pluginInstance Plugin's instace, this will use for namespace name.
     * @param key The key.
     * @param value The value, if value is null, the key data will remove from shop extra data.
     */
    public void setExtraData(@NotNull Plugin pluginInstance, @NotNull String key, @Nullable String value){
        if(shopExtraData == null){
            shopExtraData = new HashMap<>();
        }
        String pluginName = pluginInstance.getName();
        if(value == null){
            Map<String, String> dataInNamespace = shopExtraData.get(pluginName);
            if(dataInNamespace == null){
                return;
            }
            dataInNamespace.remove(key);
            return;
        }
        shopExtraData.putIfAbsent(pluginName, new HashMap<>());
        Map<String, String> dataInNamespace = shopExtraData.get(pluginName);
        dataInNamespace.put(key,value);
    }

    /**
     * Get a shop extra data with default value.
     * @param pluginInstance Plugin's instace, this will use for namespace name.
     * @param key The key.
     * @param defaultValue The default value.
     * @return The result, if we found the data with key, it will returned. If nothing we founded, return the defaultValue.
     */
    public @Nullable String getExtraData(@NotNull Plugin pluginInstance, @NotNull String key, @Nullable String defaultValue){
        if(shopExtraData == null){
            shopExtraData = new HashMap<>();
        }
        String pluginName = pluginInstance.getName();
        Map<String, String> dataInNamespace = shopExtraData.get(pluginName);
        if(dataInNamespace == null){
            return null;
        }
        String data = dataInNamespace.get(key);
        if(data == null){
            return defaultValue;
        }
        return data;
    }
    /**
     * Get a shop extra data with default value.
     * @param pluginInstance Plugin's instace, this will use for namespace name.
     * @param key The key.
     * @return The result, if we found the data with key, it will returned. If nothing we founded, return the null.
     */
    public @Nullable String getExtraData(@NotNull Plugin pluginInstance, @NotNull String key){
        String pluginName = pluginInstance.getName();
        Map<String, String> dataInNamespace = shopExtraData.get(pluginName);
        if(dataInNamespace == null){
            return null;
        }
        return dataInNamespace.get(key);
    }
}
