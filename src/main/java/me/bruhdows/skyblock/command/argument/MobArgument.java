package me.bruhdows.skyblock.command.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import me.bruhdows.skyblock.SkyblockPlugin;
import me.bruhdows.skyblock.core.mob.Mob;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MobArgument extends ArgumentResolver<CommandSender, Mob> {

    private static final Map<String, Mob> MOB_ARGUMENTS = new HashMap<>();

    static {
        MOB_ARGUMENTS.putAll(SkyblockPlugin.getInstance().getMobManager().getMobs());
    }

    @Override
    protected ParseResult<Mob> parse(Invocation<CommandSender> invocation, Argument<Mob> context, String argument) {
        Mob mob = MOB_ARGUMENTS.get(argument.toUpperCase());

        if (mob == null) {
            return ParseResult.failure("Mob not found!");
        }

        return ParseResult.success(mob);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Mob> argument, SuggestionContext context) {
        return SuggestionResult.of(MOB_ARGUMENTS.keySet());
    }

}