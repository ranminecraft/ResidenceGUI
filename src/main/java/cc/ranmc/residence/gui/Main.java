package cc.ranmc.residence.gui;

import cc.ranmc.sign.SignApi;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Main extends Basic implements Listener {

	private final boolean folia = isFolia();
	//初始化
	@Override
	public void onEnable() {
		info("§e-----------------------");
		info(PREFIX + "§dBy "+ AUTHOR);
		info("§bVersion: " + getDescription().getVersion());
		info("§c" + WEB);
		info("§e-----------------------");

		//注册指令
		Objects.requireNonNull(getCommand("resgui")).setExecutor(new ResguiCommand(this));
		Objects.requireNonNull(getCommand("restp")).setExecutor(new RestpCommand(this));
	    //加载配置
	    loadConfig();
	    //注册Event
        Bukkit.getPluginManager().registerEvents(this, this);
        
		super.onEnable();
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

	//指令补全
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		String[] Commands = { "help", "reload", "perm"};
		List<String> cmdls = new ArrayList<>();
		if (alias.equalsIgnoreCase("resgui") && args.length == 1 && sender.hasPermission("resgui.admin")) {
			cmdls = Arrays.asList(Commands);
		}
		
		return cmdls;
	}
	
	//插件卸载
	@Override
	public void onDisable() {
		info(PREFIX + "§a插件已经成功卸载");
		super.onDisable();
	}
	
	// 加载配置
	public void loadConfig() {
		
		// 配置文件
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
                saveDefaultConfig();
        }
        reloadConfig();
        
        // 加载Residence
        if (Bukkit.getPluginManager().getPlugin("Residence") != null) {
			info(color(PREFIX + "&a成功加载Residence"));
        } else {
			info(color(PREFIX + "&c无法找到Residence"));
       	 	PluginManager pluginManager = getServer().getPluginManager();
			pluginManager.disablePlugin(this);
        }
	}

	@EventHandler
	public void onResidenceChangedEvent(ResidenceChangedEvent event) {
		Player player = event.getPlayer();
		player.getOpenInventory();
		if (player.getOpenInventory().getTitle().contains(color("&e&l领地管理丨"))) {
			player.closeInventory();
		}
	}
	
	//背包点击
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		Player p = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		
		//公共权限菜单
		if (event.getView().getTitle().contains(color("&e&l领地管理丨公共权限"))) {
			//取消点击
			event.setCancelled(true);
			if (clicked==null) return;
			//获取所在领地
			ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
			//判断是否在领地
			if (claimedResidence == null) {
				p.sendMessage(Basic.PREFIX + color("&c你当前不在领地内"));
				p.closeInventory();
				return;
			}
			
			//判断是否拥有该领地
			List<String> permList = Arrays.asList(removeBrackets(claimedResidence.getPermissions().listPlayersFlags().replace("§f", "")).split(" "));
			Boolean admin = false;
			if (permList.contains(p.getName())) {
				admin = claimedResidence.getPermissions().getPlayerFlags(p.getName()).get("admin");
			}
			if ((admin == null || !admin) && !claimedResidence.getOwner().equalsIgnoreCase(p.getName())) {
				p.sendMessage(Basic.PREFIX + color("&c你没有权限设置该领地"));
				return;
			}
			
			//设置权限
			if (event.getRawSlot() <= 35 && event.getCurrentItem() != null) {
				String permName = Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName();
				permName = permName.substring(permName.indexOf("(")+1,permName.indexOf(")"));
				setFlag(event,permName,claimedResidence);
				return;
			}
			
			//设置传送点
			if (event.getRawSlot() == 46) {
				p.chat("/res tpset");
				p.closeInventory();
				return;
			}
			
			//设置领地信息
			if (event.getRawSlot() == 51) {
				//左键点击设置进入消息
				if (event.getClick()==ClickType.LEFT) {
					//打开牌子菜单
					SignApi.newMenu("欢迎%player来到" + claimedResidence.getResidenceName())
				            .response((player, strings) -> {
				            	claimedResidence.setEnterMessage(strings[0]+strings[1]+strings[2]+strings[3]);
				            	p.sendMessage(Basic.PREFIX + color("&e消息文本已设置"));
				                return true;
				            }).open(p);
				}
				// 右键点击设置进入消息
				if (event.getClick()==ClickType.RIGHT) {
					//打开牌子菜单
					SignApi.newMenu("%player离开了" + claimedResidence.getResidenceName())
				            .response((player, strings) -> {
				            	claimedResidence.setLeaveMessage(strings[0]+strings[1]+strings[2]+strings[3]);
				            	p.sendMessage(Basic.PREFIX + color("&e消息文本已设置"));
				                return true;
				            }).open(p);
				}
				return;
			}
			
			// 显示领地边界
			if (event.getRawSlot() == 47) {
				p.closeInventory();
				if (Math.abs(claimedResidence.getTotalSize()) > 15360000) {
					p.chat("/res show");
					return;
				}
				Location lowloc = claimedResidence.getAreaArray()[0].getLowLoc();
				Location highloc = claimedResidence.getAreaArray()[0].getHighLoc();
				showEdge(p, lowloc, highloc);
				
				// 消失边界显示
				long hideDelay = getConfig().getInt("GlassShowTime", 5);
				if (folia) {
					Bukkit.getServer().getAsyncScheduler().runDelayed(this, scheduledTask ->
							hideEdge(p, lowloc, highloc), hideDelay, TimeUnit.SECONDS);
				} else {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () ->
							hideEdge(p, lowloc, highloc), hideDelay * 20);
				}
				return;
			}
			
			//删除领地
			if (event.getRawSlot() == 48) {
				p.chat("/res remove "+claimedResidence.getResidenceName());
				p.closeInventory();
				return;
			}
			
			//打开玩家权限设置
			if (event.getRawSlot() == 52) {
				p.chat("/resgui perm");
				return;
			}
			
			//关闭菜单
			if (event.getRawSlot() == 53 || event.getRawSlot() == 45) {
				p.closeInventory();
				return;
			}
		}
		
		//玩家权限菜单
		if (event.getView().getTitle().contains(color("&e&l领地管理丨玩家权限"))) {
			//取消点击
			event.setCancelled(true);
			if (clicked==null) return;
			//获取所在领地
			ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
			//判断是否在领地
			if (claimedResidence==null) {
				p.sendMessage(Basic.PREFIX + color("&c你当前不在领地内"));
				p.closeInventory();
				return;
			}
			
			//判断是否拥有该领地
			List<String> permList = Arrays.asList(removeBrackets(claimedResidence.getPermissions().listPlayersFlags().replace("§f", "")).split(" "));
			Boolean admin = false;
			if (permList.contains(p.getName())) {
				admin = claimedResidence.getPermissions().getPlayerFlags(p.getName()).get("admin");
			}
			if ((admin==null  || !admin) && (!claimedResidence.getOwner().equalsIgnoreCase(p.getName()))) {
				p.sendMessage(Basic.PREFIX + color("&c你没有权限设置该领地"));
				return;
			}
			
			//返回上页
			if (event.getRawSlot() == 45 || event.getRawSlot() == 53) {
				p.chat("/resgui");
				return;
			}
			
			//打开添加玩家菜单
			if (event.getRawSlot() == 49) {
				Inventory inventory = Bukkit.createInventory(null, 54, color("&e&l领地管理丨添加玩家"));
				int inventorySize = 0;
				for(Player onlinePlayer:Bukkit.getOnlinePlayers()) {
					if (!permList.contains(onlinePlayer.getName())) {
						if (inventorySize<45) {
							inventory.setItem(inventorySize, createSkullItem(Material.PLAYER_HEAD, onlinePlayer, "§e不要分享权限给陌生人", "§e否则造成损失后果自负"));
							inventorySize++;
						}
					}
				}
				inventory.setItem(49, createItem(Material.WRITABLE_BOOK, "&b手动添加", "§e找不到该玩家", "§e可能已经离线", "§e尝试输入名称"));
				ItemStack closeItem = createItem(Material.BARRIER, "&b返回菜单");
				inventory.setItem(45, closeItem);
				inventory.setItem(53, closeItem);
				p.openInventory(inventory);
				return;
			}
			
			if (event.getRawSlot() < 49 && event.getCurrentItem() != null) {
				String playerName = Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName().replace("§b", "");
				Inventory inventory = Bukkit.createInventory(null, 54, color("&e&l领地管理丨玩家"+playerName));
				CreatePermButton(claimedResidence,inventory,Material.LEATHER_BOOTS,0,"&b移动&f(move)","&e是否允许进入领地并移动",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.DIAMOND_PICKAXE,1,"&b建筑&f(build)","&e是否允许放置或破坏方块",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.GRASS_BLOCK,2,"&b放置&f(place)","&e是否允许放置方块",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.IRON_AXE,3,"&b破坏&f(destroy)","&e是否允许破坏方块",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.LEVER,4,"&b交互&f(use)","&e是否允许与拉杆/门/工作台等交互",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.CHEST_MINECART,5,"&b容器&f(container)","&e是否允许与箱子/漏斗等交互",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.ENDER_EYE,6,"&b传送&f(tp)","&e是否允许传送至领地",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.SADDLE,7,"&b骑乘&f(riding)","&e是否允许骑乘生物",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.COOKED_BEEF,8,"&b攻击动物&f(animalkilling)","&e是否允许攻击动物",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.ROTTEN_FLESH,9,"&b攻击怪物&f(mobkilling)","&e是否允许攻击怪物",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.ENDER_PEARL,10,"&b末影珍珠&f(enderpearl)","&e是否允许使用末影珍珠进入",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.OAK_BOAT,11,"&b破坏载具&f(vehicledstroy)","&e是否允许破坏载具",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.CHORUS_FRUIT,12,"&b紫颂果&f(chorustp)","&e是否允许使用紫颂果",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.NOTE_BLOCK,13,"&b音符盒&f(note)","&e是否允许使用音符",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.LEAD,14,"&b拴绳&f(leash)","&e是否允许牵引动物",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.FLINT_AND_STEEL,15,"&b点火&f(ignite)","&e是否允许使用打火石",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.ARROW,16,"&b射箭&f(shoot)","&e是否允许使用弓弩",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.INK_SAC,17,"&b染色&f(dye)","&e是否允许使用染色",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.SHEARS,18,"&b剪取&f(shear)","&e是否允许剪取羊毛",playerName);
		        CreatePermButton(claimedResidence,inventory,Material.PEONY,18,"&b管理&f(admin)","&e是否允许修改领地权限",playerName);

				inventory.setItem(48, createItem(Material.COMPARATOR, "&b删除权限", "§e对目标指定该玩家", "§e移除基本领地权限"));
				inventory.setItem(50, createItem(Material.BREWING_STAND, "&b转让领地", "§e对目标指定该玩家", "§e给予领地的所有权"));

				ItemStack closeItem = createItem(Material.BARRIER, "&b返回菜单");
				inventory.setItem(45, closeItem);
				inventory.setItem(53, closeItem);
				p.openInventory(inventory);
				return;
			}
		}
		
		//管理指定玩家权限菜单
		if (event.getView().getTitle().contains(color("&e&l领地管理丨玩家"))) {
			//取消点击
			event.setCancelled(true);
			if (clicked==null) return;
			//获取所在领地
			ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
			//判断是否在领地
			if (claimedResidence==null) {
				p.sendMessage(Basic.PREFIX + color("&c你当前不在领地内"));
				p.closeInventory();
				return;
			}
			
			//判断是否拥有该领地
			List<String> permList = Arrays.asList(removeBrackets(claimedResidence.getPermissions().listPlayersFlags().replace("§f", "")).split(" "));
			Boolean admin = false;
			if (permList.contains(p.getName())) {
				admin = claimedResidence.getPermissions().getPlayerFlags(p.getName()).get("admin");
			}
			if ((admin==null || !admin) && !claimedResidence.getOwner().equalsIgnoreCase(p.getName())) {
				p.sendMessage(Basic.PREFIX + color("&c你没有权限设置该领地"));
				return;
			}
			
			//返回上页
			if (event.getRawSlot() == 45 || event.getRawSlot() == 53) {
				p.chat("/resgui perm");
				return;
			}
			
			//删除玩家权限
			if (event.getRawSlot() == 48) {
				p.chat("/res pset "+claimedResidence.getResidenceName()+" "+event.getView().getTitle().split("玩家")[1]+" removeall");
				p.chat("/resgui perm");
				return;
			}
			
			//转让领地
			if (event.getRawSlot() == 50) {
				p.chat("/res give "+claimedResidence.getResidenceName()+" "+event.getView().getTitle().split("玩家")[1]);
				p.closeInventory();
				return;
			}
			
			//设置玩家权限
			if (event.getRawSlot() < 49 && event.getCurrentItem() != null) {
				String permName = Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName();
				permName = permName.substring(permName.indexOf("(")+1,permName.indexOf(")"));
				setFlag(event,permName,claimedResidence,event.getView().getTitle().split("玩家")[1]);
				return;
			}
		}
		
		//添加玩家权限菜单
		if (event.getView().getTitle().contains(color("&e&l领地管理丨添加玩家"))) {
			//取消点击
			event.setCancelled(true);
			if (clicked==null) return;
			//获取所在领地
			ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
			//判断是否在领地
			if (claimedResidence==null) {
				p.sendMessage(Basic.PREFIX + color("&c你当前不在领地内"));
				p.closeInventory();
				return;
			}
			
			//判断是否拥有该领地
			List<String> permList = Arrays.asList(removeBrackets(claimedResidence.getPermissions().listPlayersFlags().replace("§f", "")).split(" "));
			Boolean admin = false;
			if (permList.contains(p.getName())) {
				admin = claimedResidence.getPermissions().getPlayerFlags(p.getName()).get("admin");
			}
			if ((admin == null || !admin) && !claimedResidence.getOwner().equalsIgnoreCase(p.getName())) {
				p.sendMessage(Basic.PREFIX + color("&c你没有权限设置该领地"));
				return;
			}
			
			if (event.getRawSlot() == 45 || event.getRawSlot() == 53) {
				p.chat("/resgui perm");
				return;
			}
			
			if (event.getRawSlot() == 49) {
				TextComponent text = new TextComponent(color("&a[点击添加领地权限]"));
				String cmd = "/res padd " + claimedResidence.getResidenceName() + " 玩家名";
				text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(cmd).create()));
				text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
				p.spigot().sendMessage(text);
				p.closeInventory();
				return;
			}
			
			if (event.getRawSlot() < 49 && event.getCurrentItem() != null) {
				p.chat("/res padd "+claimedResidence.getResidenceName() + " " + ChatColor.stripColor(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()));
				p.chat("/resgui perm");
			}
		}
	}
	
	//玩家交互事件
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		//玩家使用锄头领地内右键打开菜单
		if (p.getInventory().getItemInMainHand().getType()==Material.WOODEN_HOE&&event.getAction()==Action.RIGHT_CLICK_AIR&&getConfig().getBoolean("RightClickAction")) {
			ClaimedResidence claimedResidence = ResidenceApi.getResidenceManager().getByLoc(p.getLocation());
			if (claimedResidence != null) {
				p.chat("/resgui");
			}
		}
	}

}
