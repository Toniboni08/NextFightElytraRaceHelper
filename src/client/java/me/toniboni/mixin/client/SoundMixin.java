package me.toniboni.mixin.client;

import me.toniboni.NextfightElytraRaceHelperClient;
import me.toniboni.Util.DataBase;
import me.toniboni.Util.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.sql.SQLException;

@Mixin(SoundSystem.class)
public abstract class SoundMixin {

    @Shadow public abstract boolean isPlaying(SoundInstance sound);

    @Shadow public abstract void play(SoundInstance sound);

    @Shadow public abstract void pauseAll();

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"))
    public void play(SoundInstance sound, CallbackInfo ci) throws SQLException {
        ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler != null){
            ServerInfo serverInfo = handler.getServerInfo();
            if (serverInfo != null && serverInfo.address.equals("nextfight.net")){
                String path = sound.getId().getPath();
                if (path.equals("item.elytra.flying")){
                    System.out.println("start");
                    Timer.startTimer();
                }

                if (MinecraftClient.getInstance().player.isUsingRiptide()){
                    switch (path) {
                        case "entity.ender_dragon.flap":
                            Timer.addTime();
                            System.out.println("add");
                            break;
                        case "entity.player.levelup":
                            Timer.addTime();
                            DataBase.addTimes(Timer.getTimes());
                            System.out.println("db");
                            if (NextfightElytraRaceHelperClient.sumOfBest > DataBase.getSumOfBest().total){
                                NextfightElytraRaceHelperClient.sumOfBest = DataBase.getSumOfBest().total;
                                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("You broke your previous SumOfBest: " + NextfightElytraRaceHelperClient.sumOfBest + "ms").copy().withColor(0xFFe481f7));
                            }
                            break;
                    }
                }
            }
        }
    }
}
