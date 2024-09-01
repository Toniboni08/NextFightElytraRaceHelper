package me.toniboni.Screens;

import me.toniboni.Util.SortType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.sql.SQLException;


public class SortWidget extends ClickableWidget {
    private SortState state = SortState.NONE;
    private SortType type;
    public SortWidget(int x, int y, SortType type) {
        super(x, y, 20, 20, Text.empty());
        this.type = type;

        if (type == TimesScreen.sortType){
            if (TimesScreen.asc) this.state = SortState.ASC;
            else this.state = SortState.DESC;
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (isHovered()){
            context.drawTexture(Identifier.of("nextfightelytraracehelper", "SortIcons/" + state.name().toLowerCase() + "_selected.png"), getX(), getY(), 0, 0, 20, 20, 32, 32);
        }else {
            context.drawTexture(Identifier.of("nextfightelytraracehelper", "SortIcons/" + state.name().toLowerCase() + ".png"), getX(), getY(), 0, 0, 20, 20, 32, 32);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void onClick(double mouseX, double mouseY){
        SortState sortState = SortState.values()[(this.state.ordinal() + 1) % 3];

        if (sortState == SortState.NONE){
            sortState = SortState.values()[(this.state.ordinal() + 2) % 3];
        }

        this.state = sortState;
        MinecraftClient.getInstance().send(() -> {
            try {
                MinecraftClient.getInstance().setScreen(new TimesScreen(TimesScreen.parent, TimesScreen.page, this.type, this.state == SortState.ASC));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
