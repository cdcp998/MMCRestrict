package net.moddedminecraft.mmcrestrict;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class Config {

    private final Main plugin;

    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    public static CommentedConfigurationNode config;

    public Config(Main main) throws IOException, ObjectMappingException {
        plugin = main;
        loader = HoconConfigurationLoader.builder().setPath(plugin.defaultConf).build();
        config = loader.load();
        configCheck();
    }

    public static boolean logToFile;

    public static List<String> sendToChestWhitelist;

    public static String defaultReason = "";
    public static Boolean defaultUsage = true;
    public static Boolean defaultBreaking = true;
    public static Boolean defaultPlacing = true;
    public static Boolean defaultOwnership = true;
    public static Boolean defaultDrop = false;
    public static Boolean defaultCraft = true;
    public static Boolean defaultWorld = false;

    //Auto Purge
    public static Boolean defaultAutoPurge = false;
    public static Integer defaultAutoPurgeInterval = 20;


    public static Boolean notifyStaff = true;

    public void configCheck() throws IOException, ObjectMappingException {

        if (!Files.exists(plugin.defaultConf)) {
            Files.createFile(plugin.defaultConf);
        }

        if (config.getNode("send-to-chest", "whitelist").hasListChildren()) {
            sendToChestWhitelist = check(config.getNode("send-to-chest", "whitelist"), Collections.emptyList()).getList(TypeToken.of(String.class));
        } else {
            sendToChestWhitelist = config.getNode("send-to-chest", "whitelist").setValue(Collections.emptyList()).getList(TypeToken.of(String.class));
        }

        config.getNode("defaults").setComment("Default values for any newly added banned item.");
        defaultReason = check(config.getNode("defaults", "reason"), defaultReason).getString();
        defaultUsage = check(config.getNode("defaults", "usage"), defaultUsage).getBoolean();
        defaultBreaking = check(config.getNode("defaults", "breaking"), defaultBreaking).getBoolean();
        defaultPlacing = check(config.getNode("defaults", "placing"), defaultPlacing).getBoolean();
        defaultOwnership = check(config.getNode("defaults", "ownership"), defaultOwnership).getBoolean();
        defaultDrop = check(config.getNode("defaults", "drop"), defaultDrop).getBoolean();
        defaultCraft = check(config.getNode("defaults", "craft"), defaultCraft).getBoolean();
        defaultWorld = check(config.getNode("defaults", "world"), defaultWorld).getBoolean();

        config.getNode("world-auto-purge").setComment("Check all Loaded Chunks for banned items and remove them. If you experience lag, disable this and just use /restrict checkchunks manually");
        defaultAutoPurge = check(config.getNode("world-auto-purge", "is-enabled"), defaultAutoPurge).getBoolean();

        defaultAutoPurgeInterval = check(config.getNode("world-auto-purge", "interval-in-minutes"), defaultAutoPurgeInterval, "Check for banned items around the World... interval should be more than 5").getInt();
        if (defaultAutoPurgeInterval < 5){
            defaultAutoPurgeInterval = 5;
        }

        notifyStaff = check(config.getNode("notify-staff"), notifyStaff, "If enabled, will notify staff if someone attempts to use a banned item.").getBoolean();

        logToFile = check(config.getNode("log-to-file"), true, "Log any banned action or banned item change to a file.").getBoolean();
        loader.save(config);
    }

    private CommentedConfigurationNode check(CommentedConfigurationNode node, Object defaultValue, String comment) {
        if (node.isVirtual()) {
            node.setValue(defaultValue).setComment(comment);
        }
        return node;
    }

    private CommentedConfigurationNode check(CommentedConfigurationNode node, Object defaultValue) {
        if (node.isVirtual()) {
            node.setValue(defaultValue);
        }
        return node;
    }
}
