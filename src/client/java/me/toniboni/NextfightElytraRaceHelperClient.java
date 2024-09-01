package me.toniboni;

import me.toniboni.Commands.TimesCommand;
import me.toniboni.Util.DataBase;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import java.io.File;
import java.sql.SQLException;

public class NextfightElytraRaceHelperClient implements ClientModInitializer {
    public static long sumOfBest = 999999L;
    @Override
	public void onInitializeClient() {
        NextfightElytraRaceHelper.LOGGER.info("client init;");
        File dbDirectory = new File("ElytraHelper");
        if (!dbDirectory.exists()){
            dbDirectory.mkdir();
        }

        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("times").executes(TimesCommand::execute));
        }));

        try {
            Class.forName("org.sqlite.JDBC");
            DataBase.init();
            sumOfBest = DataBase.getSumOfBest().total;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}