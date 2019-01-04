package net.moddedminecraft.mmcrestrict.Commands;

import net.moddedminecraft.mmcrestrict.Main;
import net.moddedminecraft.mmcrestrict.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Help implements CommandExecutor {

    private final Main plugin;
    public Help(Main instance) {
        plugin = instance;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        showHelp(src);
        return CommandResult.success();
    }

    void showHelp(CommandSource sender) {
        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();

        List<Text> contents = new ArrayList<>();
        if (sender.hasPermission(Permissions.ADD_BANNED_ITEM)) contents.add(plugin.fromLegacy("&3/restrict &badd - &7从您的手中添加物品到禁止列表."));
        if (sender.hasPermission(Permissions.REMOVE_BANNED_ITEM)) contents.add(plugin.fromLegacy("&3/restrict &bremove [itemID] - &7从禁止列表中删除禁止物品."));
        if (sender.hasPermission(Permissions.EDIT_BANNED_ITEM)) contents.add(plugin.fromLegacy("&3/restrict &bedit (option) (value) - &7列出禁用物品的选项或编辑选项."));
        //if (sender.hasPermission(Permissions.SEARCH_WORLD)) contents.add(plugin.fromLegacy("&3/restrict &bsearch (itemID) - &7Search active chunks for a block"));
        if (sender.hasPermission(Permissions.LIST_BANNED_ITEMS)) contents.add(plugin.fromLegacy("&3/restrict &blist &6| &3/banneditems &b- &7列出所有当前禁用的物品"));

        if (contents.isEmpty()) {
            contents.add(plugin.fromLegacy("&c您目前没有使用此插件的任何权限."));
        }
        paginationService.builder()
                .title(plugin.fromLegacy("&6MMCRestrict 帮助"))
                .contents(contents)
                .header(plugin.fromLegacy("&3[] = 必须  () = 可选填"))
                .padding(Text.of("="))
                .sendTo(sender);
    }
}
