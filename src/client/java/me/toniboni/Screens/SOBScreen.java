package me.toniboni.Screens;

import me.toniboni.Util.DataBase;
import me.toniboni.Util.Time;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.sql.SQLException;

//SumOfBest
public class SOBScreen extends Screen {
    private int heightUnit;
    private Screen parent;
    private Time time;

    protected SOBScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }


    @Override
    protected void init() {
        try {
            time = DataBase.getSumOfBest();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        heightUnit = height / 100;

        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"), (widget) -> MinecraftClient.getInstance().setScreen(parent))
                .dimensions(20, 40, 100, 20)
                .build();

        addDrawableChild(backButton);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float Delta){
        super.render(context, mouseX, mouseY, Delta);

        int columns = 17;
        int y = heightUnit * 30;

        context.fill(width / 2 - 200, 100, width / 2  + 200, 110, 0xFF000000);
        context.fill(25, heightUnit * 28, width - 25, heightUnit * 28 + 5, 0xFF000000);
        context.drawTexture(Identifier.of("nextfightelytraracehelper", "textures/timestitle.png"), width / 2 - 128, 0, 0, 0, 256, 100, 256, 100);
        context.drawText(this.textRenderer, Text.of("Total"), 130 + (16 * (width - 150) / columns), heightUnit * 26, 0xFF000000, false);
        context.drawText(this.textRenderer, Text.of(String.valueOf(time.total)), 130 + (16 * (width - 150) / columns), y, 0xFF000000, false);

        //Columns
        for (int i = 0; i < columns; i++) {
            if (i + 1 < columns){
                int x = 130 + (i * (width - 150) / columns);
                context.drawText(this.textRenderer, Text.of("Ring " + (i + 1)), x, heightUnit * 26, 0xFF000000, false);
                context.drawText(this.textRenderer, Text.of(String.valueOf(time.rings.get(i))), x, y, 0xFF000000, false);
            }
            int x = 120 + (i * (width - 150) / columns);
            context.fill(x, heightUnit * 25, 125 + (i * (width - 150) / columns), heightUnit * 25 + 50, 0xFF000000);
        }
    }
}
