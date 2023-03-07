package gay.nyako.vanityslots;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

@Config(name = "vanityslots")
public class VanitySlotsConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public List<String> itemBlacklist = List.of();

    @ConfigEntry.Gui.Tooltip
    public boolean disallowBinding = true;
}
