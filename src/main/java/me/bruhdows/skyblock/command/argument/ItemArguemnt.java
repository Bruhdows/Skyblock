package me.bruhdows.skyblock.command.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.core.item.Item;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class ItemArguemnt extends ArgumentResolver<CommandSender, Item> {

    private static final Map<String, Item> ITEM_ARGUMENTS = new HashMap<>();

    static {
        ITEM_ARGUMENTS.putAll(SkyblockPlugin.getInstance().getItemManager().getItems());
    }

    @Override
    protected ParseResult<Item> parse(Invocation<CommandSender> invocation, Argument<Item> context, String argument) {
        Item item = ITEM_ARGUMENTS.get(argument.toUpperCase());

        if (item == null) {
            return ParseResult.failure("Item not found!");
        }

        return ParseResult.success(item);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Item> argument, SuggestionContext context) {
        return SuggestionResult.of(ITEM_ARGUMENTS.keySet());
    }

}
