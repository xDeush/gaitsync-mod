package com.gaitsync.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

/**
 * Zegar fazy chodu, oparty o czas swiata.
 *
 * Czas swiata jest ten sam w kazdym odtworzeniu tego samego nagrania, wiec
 * faza liczona z niego zgadza sie miedzy instancjami bez zadnej recznej
 * synchronizacji.
 *
 * Ulamek ticku liczymy sami, z czasu od ostatniego ticku. Gdyby faza skakala
 * tylko na tickach, chod chodzilby w dwudziestu krokach na sekunde zamiast
 * plynac -- a to widac przy 60 klatkach w renderze.
 */
public final class GaitClock {
    private GaitClock() {}

    private static long lastTickNanos;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> lastTickNanos = System.nanoTime());
    }

    private static float partialTick() {
        if (lastTickNanos == 0L) {
            return 0.0F;
        }
        float elapsed = (System.nanoTime() - lastTickNanos) / 50_000_000.0F;
        return elapsed < 0.0F ? 0.0F : (elapsed > 1.0F ? 1.0F : elapsed);
    }

    /**
     * Faza chodu do podstawienia pod walkAnimationPos.
     *
     * @return faza w tych samych jednostkach, ktorych uzywa vanilla
     */
    public static float phase() {
        if (GaitState.frozen) {
            return GaitState.offset;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) {
            return GaitState.offset;
        }

        float ticks = client.level.getGameTime() + partialTick();
        return ticks * GaitState.rate + GaitState.offset;
    }
}
