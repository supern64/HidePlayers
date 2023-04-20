package me.sad.hideplayers.commands;

import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import me.sad.hideplayers.HidePlayers;
import me.sad.hideplayers.utils.ConfigUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RenderPlayersCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "hideplayers";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hideplayers (toggle/help/list/add/remove/mode/range) [player/mode/range]";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("hp");
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return addTabCompletionOptions(sender, args, pos);
    }
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "toggle", "help", "list", "add", "remove");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                return getListOfStringsMatchingLastWord(args, HidePlayers.players);
            } else if (args[0].equalsIgnoreCase("add")) {
                NetHandlerPlayClient connection = Minecraft.getMinecraft().getNetHandler();
                List<NetworkPlayerInfo> playerInfo = new ArrayList(connection.getPlayerInfoMap());
                List<String> playerList = Lists.newArrayList();
                for (NetworkPlayerInfo networkPlayerInfo : playerInfo) {
                    if (networkPlayerInfo.getResponseTime() > 0 && networkPlayerInfo.getGameProfile().getName().matches("^[a-zA-Z0-9_]*$"))
                        playerList.add(networkPlayerInfo.getGameProfile().getName());
                }
                return getListOfStringsMatchingLastWord(args, playerList);
            }
        }
        return null;
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        processCommand(sender, args);
    }
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) HidePlayers.toggleRenderer(sender);
        else {
            switch (args[0].toLowerCase()) {
                case "toggle":
                    HidePlayers.toggleRenderer(sender);
                    break;
                case "help":
                    sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + getCommandUsage(sender)));
                    break;
                case "list":
                    sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + HidePlayers.players.toString().substring(1, HidePlayers.players.toString().length() - 1)));
                    break;
                case "add":
                    if (args.length == 1) {
                        sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + getCommandUsage(sender)));
                    } else {
                        if (HidePlayers.players.contains(args[1].toLowerCase())) {
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "\u00a7b" + args[1] + "\u00a7f is already in the list!"));
                        } else {
                            HidePlayers.players.add(args[1].toLowerCase());
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "\u00a7b" + args[1] + "\u00a7f has been added to the list!"));
                            try {
                                ConfigUtils.writeConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case "remove":
                    if (args.length == 1) {
                        sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + getCommandUsage(sender)));
                    } else {
                        if (!HidePlayers.players.contains(args[1].toLowerCase())) {
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "\u00a7b" + args[1] + "\u00a7f is not in the list!"));
                        } else {
                            HidePlayers.players.remove(args[1].toLowerCase());
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "\u00a7b" + args[1] + "\u00a7f has been removed from the list!"));
                            try {
                                ConfigUtils.writeConfig();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case "mode":
                    if (args.length == 1) {
                        sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Current mode is \u00a7b" + HidePlayers.mode.name() + "\u00a7f!"));
                    } else {
                        try {
                            HidePlayers.mode = HidePlayers.Mode.valueOf(args[1].toUpperCase());
                            ConfigUtils.writeConfig();
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Set mode to \u00a7b" + HidePlayers.mode.name() + "\u00a7f!"));
                        } catch (IllegalArgumentException ignored) {
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Available modes are: \u00a7b" + Arrays.stream(HidePlayers.Mode.values()).map(Enum::name).collect(Collectors.joining("\u00a7f, \u00a7b"))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "range":
                    if (args.length == 1) {
                        sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Current range is \u00a7b" + Math.sqrt(HidePlayers.range) + "\u00a7f blocks!"));
                    } else {
                        try {
                            HidePlayers.range = Math.pow(Double.parseDouble(args[1]), 2);
                            ConfigUtils.writeConfig();
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Set range to \u00a7b" + args[1] + "\u00a7f blocks!"));
                        } catch (NumberFormatException ignored) {
                            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Please enter a valid number!"));
                        }  catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + getCommandUsage(sender)));
            }
        }
    }
}
