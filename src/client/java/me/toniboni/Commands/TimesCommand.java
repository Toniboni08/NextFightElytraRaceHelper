package me.toniboni.Commands;

import com.mojang.brigadier.context.CommandContext;
import me.toniboni.Screens.TimesScreen;
import me.toniboni.Util.SortType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.sql.SQLException;

public class TimesCommand {
    public static int execute(CommandContext<FabricClientCommandSource> source) {
        source.getSource().getClient().send(() -> {
            try {
                source.getSource().getClient().setScreen(new TimesScreen(source.getSource().getClient().currentScreen, 0, SortType.TOTAL, true));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return 0;
    }
}
