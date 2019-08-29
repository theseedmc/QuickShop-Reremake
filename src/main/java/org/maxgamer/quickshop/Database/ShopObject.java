package org.maxgamer.quickshop.Database;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.Shop.ShopCommodity;
import org.maxgamer.quickshop.Shop.ShopExtraData;
import org.maxgamer.quickshop.Shop.ShopType;

import java.util.UUID;
@AllArgsConstructor
@Data
public class ShopObject {
    @NotNull UUID owner; //Shop Owner, if UUID = 00000000-0000-0000-0000-000000000000 that mean the owner is system
    @NotNull String world;
    int blockX;
    int blockY;
    int blockZ;
    @NotNull ShopCommodity shopCommodity;
    @NotNull ShopType shopType;
    double price;
    boolean systemShop;
    boolean unlimited;
    @Nullable ShopExtraData extra; //Extra data for future

    public static ShopObject deserialize(@NotNull String serilized) throws JsonSyntaxException {
        //Use Gson deserialize data
        Gson gson = new Gson();
        return gson.fromJson(serilized, ShopObject.class);
    }

    public static String serialize(@NotNull ShopObject shopObject) {
        Gson gson = new Gson();
        return gson.toJson(shopObject); //Use Gson serialize this class
    }
}
