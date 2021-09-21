
package me.fobose.client.event.events;

import me.fobose.client.event.EventStage;

public class Render3DEvent
extends EventStage {
    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

