package me.toniboni.Screens;

import me.toniboni.Util.DataBase;
import me.toniboni.Util.SortType;
import me.toniboni.Util.Time;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.sql.SQLException;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TimesScreen extends Screen {
    public static Screen parent;
    public static SortType sortType;
    public static boolean asc = false;
    public static int page;
    private int heightUnit = height / 100;
    private final int dataBaseSize = DataBase.getSize();
    private final List<Time> times;
    private int columns = 17;

    public TimesScreen(Screen parent, int page, SortType sortType, boolean asc) throws SQLException {
        super(Text.literal("Times"));
        this.parent = parent;
        this.page = page;
        this.times = DataBase.getTimes(sortType,asc , page);
        this.sortType = sortType;
        this.asc = asc;
    }



    @Override
    protected void init() {
        this.heightUnit = height / 100;

        for (int i = 2; i < SortType.values().length - 7; i++) {
            SortWidget sortWidget = new SortWidget(160 + ((i - 2) * (width - 150) / columns), heightUnit * 26 - 8, SortType.values()[i]);
            addDrawableChild(sortWidget);
        }
        for (int i = 11; i < SortType.values().length; i++) {
            SortWidget sortWidget = new SortWidget(165 + ((i - 2) * (width - 150) / columns), heightUnit * 26 - 8, SortType.values()[i]);
            addDrawableChild(sortWidget);
        }

        SortWidget sortWidgetTotal = new SortWidget(155 + (16 * (width - 150) / columns), heightUnit * 26 - 8, SortType.TOTAL);
        SortWidget sortWidgetDate = new SortWidget(84, heightUnit * 26 - 8, SortType.DATE);
        addDrawableChild(sortWidgetTotal);
        addDrawableChild(sortWidgetDate);

        ButtonWidget prevButton = ButtonWidget.builder(Text.of("previous"), (widget)-> {
            try {
                this.client.setScreen(new TimesScreen(this.parent, Math.max(this.page - 1, 0), sortType, asc));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).dimensions(20, height - 40, 100, 20).build();
        ButtonWidget nextButton = ButtonWidget.builder(Text.of("next"), (widget) -> {
            try {
                this.client.setScreen(new TimesScreen(this.parent, Math.min(this.page + 1, dataBaseSize / 10), sortType, asc));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).dimensions(width - 120, height - 40, 100, 20).build();

        ButtonWidget SOBButton = ButtonWidget.builder(Text.of("SumOfBest"), (widget) -> this.client.setScreen(new SOBScreen(this)))
                .dimensions(20, 40, 100, 20)
                .build();

        addDrawableChild(SOBButton);
        addDrawableChild(prevButton);
        addDrawableChild(nextButton);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float Delta){
        super.render(context, mouseX, mouseY, Delta);
        int hcolumn;
        int hrow;


        context.fill(25, heightUnit * 28, width - 25, heightUnit * 28 + 5, 0xFF000000);
        context.drawText(this.textRenderer, Text.of("Date"), 60, heightUnit * 26, 0xFF000000, false);
        context.drawText(this.textRenderer, Text.of("Total"), 130 + (16 * (width - 150) / columns), heightUnit * 26, 0xFF000000, false);

        hrow = mouseX - 120;
        hcolumn = mouseY - (heightUnit * 28 + 5);

        int selectHeight = (height - heightUnit * 10) - (heightUnit * 28 + 5);
        int selectWidth = (120 + (16 * (width - 150) / columns)) - 120;

        selectWidth /= 16;
        selectHeight /= 10;

        hrow /= selectWidth;
        hcolumn /= selectHeight;

        context.drawText(this.textRenderer, Text.of(page + " / " + (dataBaseSize / 10)),width / 2, height - 40, 0xFF000000, false);


        for (int i = 0; i < columns; i++) {
            if (i + 1 < columns){
                int x = 130 + (i * (width - 150) / columns);
                int y = heightUnit * 26;
                context.drawText(this.textRenderer, Text.of("Ring " + (i + 1)), x, y, 0xFF000000, false);
            }
            int x = 120 + (i * (width - 150) / columns);
            context.fill(x, heightUnit * 25, 125 + (i * (width - 150) / columns), height - heightUnit * 10, 0xFF000000);
        }

        for (int i = 0; i < 10; i++) {
            if (i < times.size()){
                int ccolor;

                if (i == hcolumn){
                    ccolor = 0xFFb567a3;
                }else {
                    ccolor = 0xFF000000;
                }

                int y = heightUnit * 30 + (i * (height - heightUnit * 35) / 10);
                for (int j = 0; j < 16; j++) {
                    int color;

                    if (hrow == j){
                        if (ccolor == 0xFFb567a3){
                            color = 0xFF4ed4d1;
                        }else {
                            color = 0xFF72c286;
                        }
                    }else {
                        color = ccolor;
                    }
                    context.drawText(this.textRenderer, Text.of(String.valueOf(times.get(i).rings.get(j))), 130 + (j * (width - 150) / columns), y, color, false);
                }
                String time = times.get(i).timestamp.toString();
                context.drawText(this.textRenderer, Text.of(time.substring(0, time.lastIndexOf("."))), 10, y, ccolor, false);
                context.drawText(this.textRenderer, Text.of(String.valueOf(times.get(i).total)), 130 + (16 * (width - 150) / columns), y, ccolor, false);
            }
        }

        context.fill(width / 2 - 200, 100, width / 2  + 200, 110, 0xFF000000);
        context.drawTexture(Identifier.of("nextfightelytraracehelper", "textures/timestitle.png"), width / 2 - 128, 0, 0, 0, 256, 100, 256, 100);
    }
}