package gay.nyako.vanityslots;

import com.google.gson.Gson;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.vanityslots.config"));

            ConfigCategory general = builder.getOrCreateCategory(Text.of(""));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startStrList(Text.translatable("key.vanityslots.config.blacklist"), new ArrayList<>(VanitySlots.config.itemBlacklist)).setSaveConsumer(val -> VanitySlots.config.itemBlacklist = new HashSet<>(val)).setDefaultValue(new ArrayList<>())
                    .setCreateNewInstance(baseListEntry -> new StringListListEntry.StringListCell("minecraft:", baseListEntry)).build());

            builder.setSavingRunnable(() -> {
                var configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "vanityslots.json");
                try {
                    configFile.createNewFile();
                    Gson gson = new Gson();
                    FileWriter writer = new FileWriter(configFile);
                    gson.toJson(VanitySlots.config, writer);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return builder.build();
        };
    }
}