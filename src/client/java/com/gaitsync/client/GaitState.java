package com.gaitsync.client;

/**
 * Stan synchronizacji chodu.
 *
 * Problem, ktory to rozwiazuje: faza kroku w Minecrafcie narasta z przebytego
 * dystansu, wiec przy dwoch osobnych nagraniach tej samej sceny konczyny
 * gracza sa w roznych miejscach cyklu. Przy przenikaniu w montazu widac wtedy
 * dwie pary nog pod innym katem.
 *
 * Rozwiazanie: faza przestaje zalezec od dystansu, a zaczyna byc funkcja
 * czasu w swiecie. Ten sam moment swiata daje ten sam uklad konczyn
 * -- w kazdej instancji gry, przy kazdym nagraniu.
 */
public final class GaitState {
    private GaitState() {}

    /** Czy nadpisujemy faze chodu. Domyslnie tak -- mod ma dzialac od razu. */
    public static boolean enabled = true;

    /**
     * Ile fazy przypada na tick swiata.
     *
     * 0.4 odpowiada mniej wiecej tempu zwyklego marszu. Wartosc nie musi byc
     * dokladna -- wazne, zeby byla TA SAMA w obu nagraniach.
     */
    public static float rate = 0.4F;

    /** Przesuniecie fazy w tickach, gdy nagrania trzeba recznie zgrac. */
    public static float offset = 0.0F;

    /**
     * Stala amplituda wymachu, 0 = zostaw oryginalna.
     *
     * Przy domyslnym 1.0 konczyny wymachuja jednakowo niezaleznie od tego,
     * jak szybko gracz faktycznie szedl -- bez tego jedno nagranie moze miec
     * szerszy krok od drugiego mimo zgodnej fazy.
     */
    public static float amplitude = 1.0F;

    /** Zamrozenie: faza stoi w miejscu na wartosci offsetu. */
    public static boolean frozen = false;

    /** Czy dotykac tylko gracza, czy wszystkich humanoidow. */
    public static boolean playersOnly = false;

    public static String describe() {
        return "gaitsync: " + (enabled ? "wl" : "wyl")
                + ", rate=" + rate
                + ", offset=" + offset
                + ", amplitude=" + (amplitude == 0.0F ? "oryginalna" : amplitude)
                + ", " + (frozen ? "ZAMROZONE" : "plynne")
                + ", zakres=" + (playersOnly ? "gracz" : "wszyscy");
    }
}
