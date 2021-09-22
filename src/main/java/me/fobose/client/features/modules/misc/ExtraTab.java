
package me.fobose.client.features.modules.misc;

import me.fobose.client.Fobose;
import me.fobose.client.features.modules.Module;
import me.fobose.client.features.setting.Setting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class ExtraTab
extends Module {
    public Setting<Integer> size = this.register(new Setting<Integer>("Size", 250, 1, 1000));
    private static ExtraTab INSTANCE = new ExtraTab();

    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Module.Category.MISC, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String name;
        String string = name = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), (String)networkPlayerInfoIn.getGameProfile().getName());
        if (Fobose.friendManager.isFriend(name)) {
            return "\u00a7b" + name;
        }
        return name;
    }

    public static ExtraTab getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraTab();
        }
        return INSTANCE;
    }
}

