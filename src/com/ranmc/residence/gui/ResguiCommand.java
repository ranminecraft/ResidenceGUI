package com.ranmc.residence.gui;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResguiCommand implements CommandExecutor {

    private Main plugin;

    public ResguiCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender,
                             Command cmd,
                             String label,
                             String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("resgui.admin")) {
                    plugin.loadConfig();
                    sender.sendMessage(Basic.PREFIX + "§e重载完成");
                } else {
                    sender.sendMessage(Basic.PREFIX + "§c你没有权限这么做");
                }
                return true;
            }

            //帮助指令
            if (args[0].equalsIgnoreCase("help")) {
                if (sender.hasPermission("resgui.user")) {
                    sender.sendMessage("§e-----------------------");
                    sender.sendMessage(Basic.PREFIX + "§dBy "+ Basic.AUTHOR);
                    sender.sendMessage("§bVersion: " + plugin.getDescription().getVersion());
                    sender.sendMessage("§c" + Basic.WEB);
                    sender.sendMessage("§e-----------------------");
                } else {
                    sender.sendMessage(Basic.PREFIX + "§c你没有权限这么做");
                }
                return true;
            }
        }

        //以下指令不能在控制台输入
        if (!(sender instanceof Player)) {
            plugin.info(plugin.color(Basic.PREFIX + "&c该指令不能在控制台输入"));
            return true;
        }

        Player p = (Player) sender;

        //打开玩家权限管理菜单指令
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("perm")) {
                if (sender.hasPermission("resgui.user")) {
                    ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
                    if (claimedResidence == null) {
                        p.sendMessage(plugin.color("&c你当前不在领地内"));
                        return true;
                    }

                    List<String> permList = Arrays.asList(plugin.removeBrackets(claimedResidence.getPermissions().listPlayersFlags().replace("§f", "")).split(" "));
                    Boolean admin = false;
                    if (permList.contains(p.getName())) {
                        admin = claimedResidence.getPermissions().getPlayerFlags(p.getName()).get("admin");
                    }
                    if ((admin==null || !admin) && (!claimedResidence.getOwner().equalsIgnoreCase(p.getName()))) {
                        p.sendMessage(plugin.color("&c你没有权限设置该领地"));
                        return true;
                    }

                    Inventory inventory = Bukkit.createInventory(null, 54, plugin.color("&e&l领地管理丨玩家权限"));
                    int inventorySize = 0;
                    for (int i = 0; i < permList.size(); i++) {
                        if (i < 45 && !permList.get(i).equals(claimedResidence.getOwner())) {
                            String line1 = "&e基础权限: &c否";
                            if (claimedResidence.getPermissions().getPlayerFlags(permList.get(i))==null) {
                                line1 = "&e基础权限: &c否";
                            } else if (claimedResidence.getPermissions().getPlayerFlags(permList.get(i)).get("build")==null) {
                                line1 = "&e基础权限: &c未设置";
                            } else if (claimedResidence.getPermissions().getPlayerFlags(permList.get(i)).get("build")==true) {
                                line1 = "&e基础权限: &c是";
                            }
                            inventory.setItem(inventorySize, plugin.createPermItem(Material.PLAYER_HEAD,"&b"+permList.get(i),line1,"&e查看更多详情"));
                            inventorySize++;
                        }
                    }

                    inventory.setItem(49, plugin.createItem(Material.OAK_SIGN,"&b添加玩家", "§e添加你受信任玩家", "§e给予基本领地权限"));
                    ItemStack closeItem = plugin.createItem(Material.BARRIER, "&b返回菜单");
                    inventory.setItem(45, closeItem);
                    inventory.setItem(53, closeItem);
                    p.openInventory(inventory);
                } else {
                    sender.sendMessage(Basic.PREFIX + "§c你没有权限这么做");
                }
                return true;
            }
        }

        //打开公共权限管理菜单指令
        if (args.length == 0) {
            if (sender.hasPermission("resgui.user")) {



                ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
                if (claimedResidence==null) {
                    p.sendMessage(plugin.color("&c你当前不在领地内"));
                    return true;
                }

                List<String> permList = Arrays.asList(plugin.removeBrackets(claimedResidence.getPermissions().listPlayersFlags().replace("§f", "")).split(" "));
                Boolean admin = false;
                if (permList.contains(p.getName())) {
                    admin = claimedResidence.getPermissions().getPlayerFlags(p.getName()).get("admin");
                }
                if ((admin==null || !admin) && !claimedResidence.getOwner().equalsIgnoreCase(p.getName())) {
                    p.sendMessage(plugin.color("&c你没有权限设置该领地"));
                    return true;
                }

                Inventory inventory = Bukkit.createInventory(null, 54, plugin.color("&e&l领地管理丨公共权限"));

                plugin.CreatePermButton(claimedResidence,inventory,Material.LEATHER_BOOTS,0,"&b移动&f(move)","&e是否允许进入领地并移动");
                plugin.CreatePermButton(claimedResidence,inventory,Material.DIAMOND_PICKAXE,1,"&b建筑&f(build)","&e是否允许放置或破坏方块");
                plugin.CreatePermButton(claimedResidence,inventory,Material.GRASS_BLOCK,2,"&b放置&f(place)","&e是否允许放置方块");
                plugin.CreatePermButton(claimedResidence,inventory,Material.IRON_AXE,3,"&b破坏&f(destroy)","&e是否允许破坏方块");
                plugin.CreatePermButton(claimedResidence,inventory,Material.LEVER,4,"&b交互&f(use)","&e是否允许与拉杆/门/工作台等交互");
                plugin.CreatePermButton(claimedResidence,inventory,Material.CHEST_MINECART,5,"&b容器&f(container)","&e是否允许与箱子/漏斗等交互");
                plugin.CreatePermButton(claimedResidence,inventory,Material.ENDER_EYE,6,"&b传送&f(tp)","&e是否允许传送至领地");
                plugin.CreatePermButton(claimedResidence,inventory,Material.SADDLE,7,"&b骑乘&f(riding)","&e是否允许骑乘生物");
                plugin.CreatePermButton(claimedResidence,inventory,Material.COOKED_BEEF,8,"&b攻击动物&f(animalkilling)","&e是否允许攻击动物");
                plugin.CreatePermButton(claimedResidence,inventory,Material.ROTTEN_FLESH,9,"&b攻击怪物&f(mobkilling)","&e是否允许攻击怪物");
                plugin.CreatePermButton(claimedResidence,inventory,Material.SHEEP_SPAWN_EGG,10,"&b生成动物&f(animals)","&e是否允许生成动物");
                plugin.CreatePermButton(claimedResidence,inventory,Material.SKELETON_SPAWN_EGG,11,"&b生成怪物&f(monsters)","&e是否允许生成怪物");
                plugin.CreatePermButton(claimedResidence,inventory,Material.LILY_PAD,12,"&b怪物进入&f(nomobs)","&e是否允许怪物进入");
                plugin.CreatePermButton(claimedResidence,inventory,Material.WATER_BUCKET,13,"&b液体流动&f(flow)","&e是否允许液体流动");
                plugin.CreatePermButton(claimedResidence,inventory,Material.ENDER_PEARL,14,"&b末影珍珠&f(enderpearl)","&e是否允许使用末影珍珠进入");
                plugin.CreatePermButton(claimedResidence,inventory,Material.OAK_BOAT,15,"&b破坏载具&f(vehicledestroy)","&e是否允许破坏载具");
                plugin.CreatePermButton(claimedResidence,inventory,Material.DIRT,16,"&b耕地保护&f(trample)","&e是否开启耕地保护");
                plugin.CreatePermButton(claimedResidence,inventory,Material.CHORUS_FRUIT,17,"&b紫颂果&f(chorustp)","&e是否允许使用紫颂果");
                plugin.CreatePermButton(claimedResidence,inventory,Material.NOTE_BLOCK,18,"&b音符盒&f(note)","&e是否允许使用音符");
                plugin.CreatePermButton(claimedResidence,inventory,Material.WHEAT_SEEDS,19,"&b生长&f(grow)","&e是否允许农作物生长");
                plugin.CreatePermButton(claimedResidence,inventory,Material.LEAD,20,"&b拴绳&f(leash)","&e是否允许牵引动物");
                plugin.CreatePermButton(claimedResidence,inventory,Material.FLINT_AND_STEEL,21,"&b点火&f(ignite)","&e是否允许使用打火石");
                plugin.CreatePermButton(claimedResidence,inventory,Material.BLAZE_POWDER,22,"&b火势蔓延&f(firespread)","&e是否允许火势蔓延");
                plugin.CreatePermButton(claimedResidence,inventory,Material.TNT,23,"&b爆炸&f(explode)","&e是否允许爆炸伤害");
                plugin.CreatePermButton(claimedResidence,inventory,Material.DIAMOND_SWORD,24,"&b格斗&f(pvp)","&e是否允许玩家伤害");
                plugin.CreatePermButton(claimedResidence,inventory,Material.ARROW,25,"&b射箭&f(shoot)","&e是否允许使用弓弩");
                plugin.CreatePermButton(claimedResidence,inventory,Material.INK_SAC,26,"&b染色&f(dye)","&e是否允许使用染色");
                plugin.CreatePermButton(claimedResidence,inventory,Material.PISTON,27,"&b活塞&f(piston)","&e是否允许活塞工作");
                plugin.CreatePermButton(claimedResidence,inventory,Material.SHEARS,28,"&b剪取&f(shear)","&e是否允许剪取羊毛");
                plugin.CreatePermButton(claimedResidence,inventory,Material.ICE,29,"&b融化&f(icemelt)","&e是否允许冰雪融化");
                plugin.CreatePermButton(claimedResidence,inventory,Material.BLUE_ICE,30,"&b阻止冰霜行者&f(iceform)","&e是否阻止冰霜行者生效");
                plugin.CreatePermButton(claimedResidence,inventory,Material.YELLOW_DYE,31,"&b永昼&f(day)","&e是否开启领地内白天");
                plugin.CreatePermButton(claimedResidence,inventory,Material.BLACK_DYE,32,"&b永夜&f(night)","&e是否开启领地内夜天");
                plugin.CreatePermButton(claimedResidence,inventory,Material.SUNFLOWER,33,"&b晴天&f(sun)","&e是否开启领地内晴天");
                plugin.CreatePermButton(claimedResidence,inventory,Material.BLUE_ORCHID,34,"&b雨天&f(rain)","&e是否开启领地内雨天");
                plugin.CreatePermButton(claimedResidence,inventory,Material.FEATHER,35,"&b飞行&f(svip)","&e是否允许s/vip领地内飞行");

                ItemStack item2 = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta meta2 = item2.getItemMeta();
                meta2.setDisplayName(plugin.color("&b领地详情"));
                ArrayList<String> Lore2 = new ArrayList<>();
                Lore2.add(plugin.color("&e领地名: "+ claimedResidence.getResidenceName()));
                Lore2.add(plugin.color("&e领地主人: "+ claimedResidence.getOwner()));
                Lore2.add(plugin.color("&e领地规格: "+ (int)claimedResidence.getTotalSize()+"m³"));
                if (claimedResidence.getEnterMessage()!=null) {
                    Lore2.add(plugin.color("&e进入消息: "+ claimedResidence.getEnterMessage()));
                } else {
                    Lore2.add(plugin.color("&e进入消息: 无"));
                }
                if (claimedResidence.getLeaveMessage()!=null) {
                    Lore2.add(plugin.color("&e离开消息: "+ claimedResidence.getLeaveMessage()));
                } else {
                    Lore2.add(plugin.color("&e离开消息: 无"));
                }
                meta2.setLore(Lore2);
                item2.setItemMeta(meta2);
                inventory.setItem(49, item2);

                inventory.setItem(46, plugin.createItem(Material.BEACON, "&b设置传送点", "§e记录你所在位置", "§e设为领地传送点"));
                inventory.setItem(47, plugin.createItem(Material.POWERED_RAIL, "&b显示边界", "§e显示领地实际边界"));
                inventory.setItem(48, plugin.createItem(Material.LAVA_BUCKET, "&b删除领地", "§e删除不会退还金币", "§e请谨慎决定后操作"));
                inventory.setItem(50, plugin.createItem(Material.WOODEN_HOE, "&b调整范围", "§e扩大领地看着需要扩大范围",
                        "§e输入指令/res expand 格数",
                        "§e缩小领地看着需要缩小方向",
                        "§e输入指令/res contrace 格数"));
                inventory.setItem(51, plugin.createItem(Material.LEATHER, "&b设置提示", "§e左键设置进入领地消息", "§e右键设置离开领地消息"));
                inventory.setItem(52, plugin.createItem(Material.PLAYER_HEAD, "&b分享权限", "§e你可以分享权限给好友",
                        "§e不要分享权限给陌生人",
                        "§e否则造成损失后果自负"));

                ItemStack closeItem = plugin.createItem(Material.BARRIER, "&b关闭菜单");
                inventory.setItem(45, closeItem);
                inventory.setItem(53, closeItem);
                p.openInventory(inventory);

            } else {
                sender.sendMessage(Basic.PREFIX + "§c你没有权限这么做");
            }
            return true;
        }

        sender.sendMessage(Basic.PREFIX + "§c未知指令");
        return true;
    }
}

