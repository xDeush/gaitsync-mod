package com.gaitsync.client;

import java.lang.reflect.Field;

import com.gaitsync.GaitSync;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

/**
 * Zeruje faze chodu gracza.
 *
 * Faza narasta z przebytego dystansu, liczac od powstania encji. Dwa nagrania
 * maja wiec inna faze, bo gracz przeszedl przed nimi inny dystans. Wyzerowanie
 * jej w obu instancjach tuz przed nagraniem ustawia oba przebiegi w tym samym
 * punkcie cyklu -- dalej animacja leci juz normalnie, z prawdziwego ruchu.
 *
 * Stan chodu siedzi w obiekcie WalkAnimationState wewnatrz LivingEntity.
 * Nie ma publicznej metody, ktora ustawilaby pozycje, wiec docieramy do pola
 * refleksja. Gdyby nazwa sie zmienila, mod mowi o tym w logu i nie robi nic.
 */
public final class GaitReset {
    private GaitReset() {}

    private static Field walkAnimationField;
    private static Field positionField;
    private static boolean searched;

    /**
     * @return komunikat do pokazania graczowi
     */
    public static String reset() {
        Minecraft client = Minecraft.getInstance();
        LivingEntity player = client.player;
        if (player == null) {
            return "Brak gracza -- wejdz najpierw do swiata.";
        }

        if (!resolve(player)) {
            return "Nie znalazlem pola fazy chodu. Szczegoly w logu gry.";
        }

        try {
            Object walkAnimation = walkAnimationField.get(player);
            positionField.setFloat(walkAnimation, 0.0F);
            return "Faza chodu wyzerowana. Zrob to samo w drugiej instancji "
                    + "i ruszcie w tym samym momencie.";
        } catch (IllegalAccessException | RuntimeException e) {
            GaitSync.LOGGER.warn("Nie udalo sie wyzerowac fazy chodu", e);
            return "Nie udalo sie wyzerowac fazy. Szczegoly w logu gry.";
        }
    }

    private static boolean resolve(LivingEntity player) {
        if (searched) {
            return walkAnimationField != null && positionField != null;
        }
        searched = true;

        walkAnimationField = findByType(player.getClass(), "WalkAnimationState");
        if (walkAnimationField == null) {
            GaitSync.LOGGER.warn("Nie ma pola typu WalkAnimationState w {}",
                    player.getClass().getName());
            return false;
        }

        try {
            Object walkAnimation = walkAnimationField.get(player);
            positionField = findFloat(walkAnimation.getClass());
            if (positionField == null) {
                GaitSync.LOGGER.warn("Brak pola float w {}",
                        walkAnimation.getClass().getName());
                return false;
            }
            GaitSync.LOGGER.info("Faza chodu: {}.{}",
                    walkAnimation.getClass().getSimpleName(), positionField.getName());
            return true;
        } catch (IllegalAccessException | RuntimeException e) {
            GaitSync.LOGGER.warn("Nie moge odczytac stanu chodu", e);
            return false;
        }
    }

    /** Szuka pola danego typu w klasie i jej nadklasach. */
    private static Field findByType(Class<?> type, String simpleName) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType().getSimpleName().equals(simpleName)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * Pole pozycji w stanie chodu.
     *
     * Stan trzyma dwie liczby: pozycje w cyklu i predkosc. Pozycja jest
     * pierwsza -- to ona narasta, a predkosc opada do zera po zatrzymaniu.
     */
    private static Field findFloat(Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
            if (field.getType() == float.class
                    && !java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }
}
