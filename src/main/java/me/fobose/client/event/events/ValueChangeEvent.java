
package me.fobose.client.event.events;

import me.fobose.client.event.EventStage;
import me.fobose.client.features.setting.Setting;

public class ValueChangeEvent
extends EventStage {
    public Setting setting;
    public Object value;

    public ValueChangeEvent(Setting setting, Object value) {
        this.setting = setting;
        this.value = value;
    }
}

