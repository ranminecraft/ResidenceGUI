package cc.ranmc.residence.gui;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestpCommand implements CommandExecutor {

    private final Main plugin;

    public RestpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command cmd,
                             String label,
                             String[] args) {

        //以下指令不能在控制台输入
        if (!(sender instanceof Player p)) {
            plugin.info(plugin.color(Basic.PREFIX + "&c该指令不能在控制台输入"));
            return true;
        }

        if (sender.hasPermission("resgui.user")) {
            String name = "spawn";
            if (args.length > 0) name = args[0];
            ClaimedResidence residence = ResidenceApi.getResidenceManager().getByName(name);
            if (residence == null) {
                sender.sendMessage(Basic.PREFIX + "§c领地不存在");
                return true;
            }
            p.teleportAsync(residence.getTeleportLocation(p));
        } else {
            sender.sendMessage(Basic.PREFIX + "§c你没有权限这么做");
        }

        sender.sendMessage(Basic.PREFIX + "§c未知指令");
        return true;
    }
}

