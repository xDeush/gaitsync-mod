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

    /**
     * Ciagle nadpisywanie fazy. Domyslnie WYLACZONE.
     *
     * W tym trybie animacja przestaje wynikac z ruchu gracza i staje sie
     * funkcja czasu -- chod wyglada wtedy sztucznie, bo nie reaguje na to,
     * jak gracz naprawde sie porusza. Do zgrania dwoch nagran wystarczy
     * jednorazowe /gaitsync reset przed nagraniem.
     */
    public static boolean enabled = false;

    /**
     * Ile fazy przypada na tick swiata.
     *
     * 0.7 odpowiada tempu zwyklego marszu. Pierwsza wersja miala 0.4
     * i chod wygladal na spowolniony -- vanilla przesuwa faze mniej wiecej
     * o tyle, ile wynosi predkosc konczyn, a ta przy marszu to okolo 0.7.
     *
     * Wartosc nie musi byc co do joty dokladna -- wazne, zeby byla TA SAMA
     * w obu nagraniach.
     */
    public static float rate = 0.7F;

    /** Przesuniecie fazy w tickach, gdy nagrania trzeba recznie zgrac. */
    public static float offset = 0.0F;

    /**
     * Stala amplituda wymachu, 0 = zostaw oryginalna.
     *
     * 0.7 to szerokosc kroku przy zwyklym marszu. Przy 1.0 konczyny
     * wymachiwaly przesadnie szeroko, bo to juz poziom biegu.
     */
    public static float amplitude = 0.7F;

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
