package org.maxgamer.quickshop.Database;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.Util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A Util to execute all SQLs.
 */
public class DatabaseHelper {
    private Database db;
    private QuickShop plugin;
    private Queue<PreparedStatement> sqlQueue = new LinkedBlockingQueue<>();

    public DatabaseHelper(QuickShop plugin, Database db) throws SQLException {
        this.db = db;
        this.plugin = plugin;
        if (!db.hasTable(QuickShop.instance.getDbPrefix() + "shops_v3")) {
            createShopsTable();
        }
        if (!db.hasTable(QuickShop.instance.getDbPrefix() + "messages_v3")) {
            createMessagesTable();
        }
    }

    public void cleanMessage(long weekAgo) {
        try {
            //QuickShop.instance.getDB().execute("DELETE FROM " + QuickShop.instance
            //        .getDbPrefix() + "messages WHERE time < ?", weekAgo);
            String sqlString = "DELETE FROM " + QuickShop.instance
                    .getDbPrefix() + "messages_v3 WHERE time < ?";
            PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
            ps.setLong(1, weekAgo);
            plugin.getDatabaseManager().add(ps);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void cleanMessageForPlayer(@NotNull UUID player) {
        try {
            String sqlString = "DELETE FROM " + QuickShop.instance.getDbPrefix() + "messages_v3 WHERE owner = ?";
            PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
            ps.setString(1, player.toString());
            plugin.getDatabaseManager().add(ps);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Creates the database table 'messages'
     *
     * @return Create failed or successed.
     * @throws SQLException If the connection is invalid
     */
    private boolean createMessagesTable() throws SQLException {
        Statement st = db.getConnection().createStatement();
        String createTable = "CREATE TABLE " + QuickShop.instance.getDbPrefix()
                + "messages_v3 (owner  VARCHAR(255) NOT NULL, message  TEXT(25) NOT NULL, time  BIGINT(32) NOT NULL );";
        return st.execute(createTable);
    }
    //owner x y z world shop extra
    public void createShop(@NotNull String owner, @NotNull String world, int x, int y, int z, @NotNull String shopObject, @Nullable String extra)
            throws SQLException {
        removeShop(x,y,z,world); //First purge old exist shop before create new shop.
        String sqlString = "INSERT INTO " + QuickShop.instance
                .getDbPrefix() + "shops_v3 (owner, x, y, z, world, shop, extra) VALUES (?, ?, ?, ?, ?, ?, ?)";
        //QuickShop.instance.getDB().execute(q, owner, price, Util.serialize(item), x, y, z, world, unlimited, shopType);
        PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
        ps.setString(1, owner);
        ps.setInt(2, x);
        ps.setInt(3, y);
        ps.setInt(4, z);
        ps.setString(5, world);
        ps.setString(6, shopObject);
        ps.setString(7, extra);
        plugin.getDatabaseManager().add(ps);
    }

    /**
     * Creates the database table 'shops'.
     *
     * @throws SQLException If the connection is invalid.
     */
    private void createShopsTable() throws SQLException { //owner x y z world shop extra
        Statement st = db.getConnection().createStatement();
        String createTable = "CREATE TABLE " + QuickShop.instance
                .getDbPrefix() + "shops_v3 (owner  VARCHAR(255) NOT NULL, x  INTEGER(32) NOT NULL, y  INTEGER(32) NOT NULL, z  INTEGER(32) NOT NULL, world VARCHAR(32) NOT NULL, shop LONGTEXT NOT NULL, extra LONGTEXT NULLABLE, PRIMARY KEY (x, y, z, world) );";
        st.execute(createTable);
    }

    public boolean removeShop(int x, int y, int z, @NotNull String worldName) throws SQLException {
        String sqlString = "DELETE FROM " + QuickShop.instance
                .getDbPrefix() + "shops_v3 WHERE x = ? AND y = ? AND z = ? AND world = ?" + (db.getCore() instanceof MySQLCore ?
                " LIMIT 1" :
                "");

        PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
        ps.setInt(1, x);
        ps.setInt(2, y);
        ps.setInt(3, z);
        ps.setString(4, worldName);
        return ps.execute();
    }

    public ResultSet selectAllMessages() throws SQLException {
        Statement st = db.getConnection().createStatement();
        String selectAllShops = "SELECT * FROM " + QuickShop.instance.getDbPrefix() + "messages_v3";
        return st.executeQuery(selectAllShops);
    }

    public ResultSet selectAllShops() throws SQLException {
        Statement st = db.getConnection().createStatement();
        String selectAllShops = "SELECT * FROM " + QuickShop.instance.getDbPrefix() + "shops_v3";
        return st.executeQuery(selectAllShops);
    }

    public void sendMessage(@NotNull UUID player, @NotNull String message, long time) {
        try {
            String sqlString = "INSERT INTO " + QuickShop.instance
                    .getDbPrefix() + "messages_v3 (owner, message, time) VALUES (?, ?, ?)";
            PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
            ps.setString(1, player.toString());
            ps.setString(2, message);
            ps.setLong(3, time);
            plugin.getDatabaseManager().add(ps);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void updateOwner2UUID(@NotNull String ownerUUID, int x, int y, int z, @NotNull String worldName)
            throws SQLException {
        String sqlString = "UPDATE " + QuickShop.instance
                .getDbPrefix() + "shops_v3 SET owner = ? WHERE x = ? AND y = ? AND z = ? AND world = ?" + (db
                .getCore() instanceof MySQLCore ? " LIMIT 1" : "");
        PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
        ps.setString(1, ownerUUID);
        ps.setInt(2, x);
        ps.setInt(3, y);
        ps.setInt(4, z);
        ps.setString(5, worldName);
        plugin.getDatabaseManager().add(ps);
    }

    public void updateShop(@NotNull String owner, @NotNull String world, int x, int y, int z, @NotNull String shopObject, @Nullable String extra) {
        try {
            String sqlString = "UPDATE " + QuickShop.instance
                    .getDbPrefix() + "shops_v3 SET owner = ? shop = ? extra = ?, WHERE x = ? AND y = ? and z = ? and world = ?";
            PreparedStatement ps = db.getConnection().prepareStatement(sqlString);
            ps.setString(1, owner);
            ps.setString(2, shopObject);
            ps.setString(3, extra);
            ps.setInt(4, x);
            ps.setInt(5, y);
            ps.setInt(6, z);
            ps.setString(7, world);
            plugin.getDatabaseManager().add(ps);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}