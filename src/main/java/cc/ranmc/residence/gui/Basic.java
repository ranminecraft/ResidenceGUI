package cc.ranmc.residence.gui;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Basic extends JavaPlugin {

    public static String PREFIX = "§b[RESGUI] ";
    public static String AUTHOR = "§dRanica";
    public static String WEB = "https://www.ranmc.cn/";
    public final boolean folia = isFolia();

    public void showEdge(Player p, Location lowloc, Location highloc) {
        for (int i = 0; i <= highloc.getBlockX()-lowloc.getBlockX(); i++) {
            Location lowlocloccopy = new Location(lowloc.getWorld(),lowloc.getBlockX(),lowloc.getBlockY(),lowloc.getBlockZ());
            if (lowlocloccopy.getChunk().isLoaded()) {
                lowlocloccopy.setX(lowloc.getBlockX()+i);
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setZ(highloc.getBlockZ());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setY(highloc.getBlockY());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setZ(lowloc.getBlockZ());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
            }
        }
        for (int i = 0; i <= highloc.getBlockZ()-lowloc.getBlockZ(); i++) {
            Location lowlocloccopy = new Location(lowloc.getWorld(),lowloc.getBlockX(),lowloc.getBlockY(),lowloc.getBlockZ());
            if (lowlocloccopy.getChunk().isLoaded()) {
                lowlocloccopy.setZ(lowloc.getBlockZ()+i);
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setX(highloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setY(highloc.getBlockY());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setX(lowloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
            }
        }
        for (int i = 0; i <= highloc.getBlockY()-lowloc.getBlockY(); i++) {
            Location lowlocloccopy = new Location(lowloc.getWorld(),lowloc.getBlockX(),lowloc.getBlockY(),lowloc.getBlockZ());
            if (lowlocloccopy.getChunk().isLoaded()) {
                lowlocloccopy.setY(lowloc.getBlockY()+i);
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setX(highloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setZ(highloc.getBlockZ());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
                lowlocloccopy.setX(lowloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, Material.RED_STAINED_GLASS.createBlockData());
            }
        }
    }

    public void hideEdge(Player p, Location lowloc, Location highloc) {
        for (int i = 0; i <= highloc.getBlockX() - lowloc.getBlockX(); i++) {
            Location lowlocloccopy = new Location(lowloc.getWorld(), lowloc.getBlockX(), lowloc.getBlockY(), lowloc.getBlockZ());
            if (lowlocloccopy.getChunk().isLoaded()) {
                lowlocloccopy.setX(lowloc.getBlockX() + i);
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setZ(highloc.getBlockZ());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setY(highloc.getBlockY());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setZ(lowloc.getBlockZ());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
            }
        }
        for (int i = 0; i <= highloc.getBlockZ() - lowloc.getBlockZ(); i++) {
            Location lowlocloccopy = new Location(lowloc.getWorld(), lowloc.getBlockX(), lowloc.getBlockY(), lowloc.getBlockZ());
            if (lowlocloccopy.getChunk().isLoaded()) {
                lowlocloccopy.setZ(lowloc.getBlockZ() + i);
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setX(highloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setY(highloc.getBlockY());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setX(lowloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
            }
        }
        for (int i = 0; i <= highloc.getBlockY() - lowloc.getBlockY(); i++) {
            Location lowlocloccopy = new Location(lowloc.getWorld(), lowloc.getBlockX(), lowloc.getBlockY(), lowloc.getBlockZ());
            if (lowlocloccopy.getChunk().isLoaded()) {
                lowlocloccopy.setY(lowloc.getBlockY() + i);
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setX(highloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setZ(highloc.getBlockZ());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
                lowlocloccopy.setX(lowloc.getBlockX());
                p.sendBlockChange(lowlocloccopy, lowlocloccopy.getBlock().getType(), (byte) 0);
            }
        }
    }

    /**
     * 是 Folia 端
     *
     * @return boolean
     */
    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    /**
     * 输出日志
     */
    public void info(String text) {
        Bukkit.getConsoleSender().sendMessage(text);
    }

    /**
     * 颜色替换
     * @param text 文本
     * @return 颜色文本
     */
    public String color(String text) {
        if (text == null) {
            text = "";
            info(PREFIX + "§c加载文本错误");
        }else {
            text=text.replace("&", "§");
        }
        return text;
    }

    /**
     * 检查更新
     */
    public void updateCheck() {
        String lastest = null;
        try {
            URL url=new URL(WEB + "plugins/resgui.txt");
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            lastest = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lastest == null) {
            info(color(PREFIX + "§c检查更新失败,请检查网络"));
        } else if (getDescription().getVersion().equalsIgnoreCase(lastest)) {
            info(color(PREFIX + "§a当前已经是最新版本"));
        } else {
            info(color(PREFIX + "§e检测到最新版本" + lastest));
        }
    }

    /**
     * 玩家聊天
     * @param player 玩家
     * @param chat 文本
     */
    public void chat(Player player, String chat) {
        if (folia) {
            player.chat(chat);
        } else {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this,
                    () -> player.chat(chat));
        }
    }

    /**
     * 删除中括号文本
     * @param text 文本
     * @return 文本
     */
    public static String removeBrackets(String text) {
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("(\\[[^]]*])");
        Matcher m = p.matcher(text);
        while (m.find()) {
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        for (String tmp : list) {
            text = text.replace("[" + tmp + "]", "");
        }
        return text;
    }

    /**
     * 设置领地权限
     * @param event 事件
     * @param perm 权限
     * @param claimedResidence 领地
     */
    public void setFlag(InventoryClickEvent event, String perm, ClaimedResidence claimedResidence) {
        Inventory inventory = event.getClickedInventory();
        if (event.getClick()== ClickType.RIGHT) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c未设置"));
            claimedResidence.getPermissions().setFlag(perm, FlagPermissions.FlagState.NEITHER);
            return;
        }
        if (claimedResidence.getPermissions().getFlags().get(perm) == null) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c是"));
            claimedResidence.getPermissions().setFlag(perm, FlagPermissions.FlagState.TRUE);
        } else if (claimedResidence.getPermissions().getFlags().get(perm)) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c否"));
            claimedResidence.getPermissions().setFlag(perm, FlagPermissions.FlagState.FALSE);
        } else if (!claimedResidence.getPermissions().getFlags().get(perm)) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c是"));
            claimedResidence.getPermissions().setFlag(perm, FlagPermissions.FlagState.TRUE);
        }
    }

    public void setFlag(InventoryClickEvent event,String perm,ClaimedResidence claimedResidence,String playerName) {
        Inventory inventory = event.getClickedInventory();
        if (event.getClick()==ClickType.RIGHT) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c未设置"));
            claimedResidence.getPermissions().setFlag(perm, FlagPermissions.FlagState.NEITHER);
            return;
        }
        if (claimedResidence.getPermissions().getPlayerFlags(playerName).get(perm) == null) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c是"));
            claimedResidence.getPermissions().setPlayerFlag(playerName, perm, FlagPermissions.FlagState.TRUE);
        } else if (claimedResidence.getPermissions().getPlayerFlags(playerName).get(perm)) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c否"));
            claimedResidence.getPermissions().setPlayerFlag(playerName, perm, FlagPermissions.FlagState.FALSE);
        } else if (!claimedResidence.getPermissions().getPlayerFlags(playerName).get(perm)) {
            Objects.requireNonNull(inventory).setItem(event.getRawSlot(), setItemLore(Objects.requireNonNull(event.getCurrentItem()), 0, "&e权限状态: &c是"));
            claimedResidence.getPermissions().setPlayerFlag(playerName, perm, FlagPermissions.FlagState.TRUE);
        }
    }

    /**
     * 设置物品LORE
     * @param item 物品
     * @param i 行数
     * @param s 内容
     * @return 物品
     */
    public ItemStack setItemLore(ItemStack item, int i, String s) {
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> Lore = (ArrayList<String>) Objects.requireNonNull(meta).getLore();
        Objects.requireNonNull(Lore).set(i, color(s));
        meta.setLore(Lore);
        if (s.contains("是")) {
            meta.addEnchant(Enchantment.LUCK,1,true);
        } else {
            meta.removeEnchant(Enchantment.LUCK);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 创建权限按钮
     * @param claimedResidence 领地
     * @param inventory 背包
     * @param material 材质
     * @param slot 坐标
     * @param name 名字
     * @param message 信息
     */
    public void CreatePermButton(ClaimedResidence claimedResidence, Inventory inventory, Material material, int slot, String name, String message) {
        String permName = name.substring(name.indexOf("(")+1,name.indexOf(")"));
        if (claimedResidence.getPermissions().getFlags().get(permName) == null) {
            inventory.setItem(slot, createPermItem(material,name,"&e权限状态: &c未设置",message));
        } else if (claimedResidence.getPermissions().getFlags().get(permName)) {
            inventory.setItem(slot, createPermItem(material,name,"&e权限状态: &c是",message));
        } else if (!claimedResidence.getPermissions().getFlags().get(permName)) {
            inventory.setItem(slot, createPermItem(material,name,"&e权限状态: &c否",message));
        }
    }

    public void CreatePermButton(ClaimedResidence claimedResidence,Inventory inventory,Material material,int slot,String name,String message,String playerName) {
        String permName = name.substring(name.indexOf("(")+1,name.indexOf(")"));
        if (claimedResidence.getPermissions().getPlayerFlags(playerName).get(permName) == null) {
            inventory.setItem(slot, createPermItem(material,name,"&e权限状态: &c未设置",message));
        } else if (claimedResidence.getPermissions().getPlayerFlags(playerName).get(permName)) {
            inventory.setItem(slot, createPermItem(material,name,"&e权限状态: &c是",message));
        } else if (!claimedResidence.getPermissions().getPlayerFlags(playerName).get(permName)) {
            inventory.setItem(slot, createPermItem(material,name,"&e权限状态: &c否",message));
        }
    }

    /**
     * 创建物品
     * @param m 材质
     * @param name 名字
     * @return 物品
     */
    public ItemStack createItem(Material m, String name) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(color(name));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItem(Material m, String name, String... lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(color(name));
        List<String> loreList = Arrays.asList(lore);
        loreList.replaceAll(this::color);
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createPermItem(Material m, String name, String... lore) {
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(color(name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> loreList = new ArrayList<>();
        for (String text : lore) {
            loreList.add(color(text));
        }
        loreList.add(color("&e点击设置权限"));
        loreList.add(color("&e右键取消设置"));
        if (loreList.get(0).contains("是")) {
            meta.addEnchant(Enchantment.LUCK,1,true);
        }
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createSkullItem(Material m, Player player, String... lore) {
        ItemStack item = new ItemStack(m);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        Objects.requireNonNull(meta).setOwningPlayer(player);
        meta.setDisplayName(color("&b" + player.getName()));
        List<String> loreList = Arrays.asList(lore);
        loreList.replaceAll(this::color);
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }
}
