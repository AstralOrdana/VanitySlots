package gay.nyako.vanityslots;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.vanityslots.config"));

            ConfigCategory general = builder.getOrCreateCategory(Text.of(""));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startStrList(new TranslatableText("key.vanityslots.config.blacklist"), new ArrayList<>(VanitySlots.config.itemBlacklist)).setSaveConsumer(val -> VanitySlots.config.itemBlacklist = new HashSet<>(val)).setDefaultValue(new ArrayList<>())
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