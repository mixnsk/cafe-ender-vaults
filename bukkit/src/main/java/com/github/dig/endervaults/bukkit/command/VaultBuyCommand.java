package com.github.dig.endervaults.bukkit.command;

import com.github.dig.endervaults.api.EnderVaultsPlugin;
import com.github.dig.endervaults.api.VaultPluginProvider;
import com.github.dig.endervaults.api.lang.Lang;
import com.github.dig.endervaults.api.lang.Language;
import com.github.dig.endervaults.api.permission.UserPermission;
import com.github.dig.endervaults.api.vault.Vault;
import com.github.dig.endervaults.api.vault.VaultRegistry;
import com.github.dig.endervaults.api.vault.metadata.VaultDefaultMetadata;
import com.github.dig.endervaults.bukkit.ui.selector.SelectorInventory;
import com.github.dig.endervaults.bukkit.vault.BukkitVault;
import com.github.dig.endervaults.bukkit.vault.BukkitVaultFactory;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.util.Tristate;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

public class VaultBuyCommand implements CommandExecutor {

    private final EnderVaultsPlugin plugin = VaultPluginProvider.getPlugin ();
    private final Language language = plugin.getLanguage ();
    FileConfiguration config = (FileConfiguration) plugin.getConfigFile().getConfiguration();

    //private final UserPermission<Player> permission = plugin.getPermission();
    LuckPerms api = LuckPermsProvider.get ();

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble (strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length == 1) {
            if (isNumeric (args[0])) {
                User user = api.getPlayerAdapter (Player.class).getUser (player);
                if (!hasPermission (user, "endervaults.vault." + args[0])) {
                    if (plugin.getEconomy ().has (player, config.getInt ("enderchest.price"))) {
                        if (!(Integer.parseInt (args[0]) > 54)) {
                            addPermission (user, "endervaults.vault." + args[0]);
                            plugin.getEconomy ().withdrawPlayer (player, config.getInt ("enderchest.price"));
                            player.sendMessage (ChatColor.translateAlternateColorCodes ('&', language.get (Lang.UNLOCK_VAULT) + args[0]));
                        } else
                            player.sendMessage (ChatColor.translateAlternateColorCodes ('&', language.get (Lang.MAX_NUMBER_OF_VAULT)));
                    } else
                        player.sendMessage (ChatColor.translateAlternateColorCodes ('&', language.get (Lang.NOT_ENOUGH_MONEY)));
                } else
                    player.sendMessage (ChatColor.translateAlternateColorCodes ('&', language.get (Lang.ALREADY_OWNED) + args[0]));
            } else
                player.sendMessage (ChatColor.translateAlternateColorCodes ('&', language.get (Lang.NOT_NUMBER_ERROR)));
        } else
            player.sendMessage (ChatColor.translateAlternateColorCodes ('&', language.get (Lang.MUST_ENTER_NUMBER)));
        return true;
    }

    public void addPermission(User user, String permission) {
        // Add the permission
        user.data ().add (Node.builder (permission).build ());

        // Now we need to save changes.
        api.getUserManager ().saveUser (user);
    }

    public boolean hasPermission(User user, String permission) {
        return user.getCachedData ().getPermissionData ().checkPermission (permission).asBoolean ();
    }
}
