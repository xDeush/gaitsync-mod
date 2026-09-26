# GaitSync

Mod kliencki do Fabrica. Sprawia, ze animacja chodu gracza jest **taka sama
w kazdym nagraniu tej samej sceny** -- zeby przenikanie miedzy dwoma
renderami nie pokazywalo dwoch par nog pod roznym katem.

## Na czym polega problem

Faza kroku w Minecrafcie narasta z przebytego dystansu. Dwa osobne nagrania
tej samej sceny maja wiec konczyny w roznych miejscach cyklu, nawet gdy
gracz porusza sie identycznie. Przy crossfade w montazu widac to od razu.

## Co robi

Faza chodu przestaje zalezec od dystansu i staje sie funkcja czasu swiata.
Ten sam moment swiata daje ten sam uklad konczyn -- w kazdej instancji gry,
przy kazdym nagraniu, bez recznej synchronizacji.

Dodatkowo amplituda wymachu jest stala, wiec jedno nagranie nie ma szerszego
kroku od drugiego.

## Uzycie

Wrzuc jar do `mods` razem z Fabric API. Dziala od razu, domyslne ustawienia
sa gotowe do renderowania.

Komenda do poprawek bez restartu gry:

```
/gaitsync                  pokaz obecne ustawienia
/gaitsync on | off         wlacz / wylacz synchronizacje
/gaitsync freeze           zamroz faze (konczyny stoja)
/gaitsync rate <0..5>      tempo cyklu, domyslnie 0.4
/gaitsync offset <liczba>  przesuniecie fazy, gdy nagrania trzeba zgrac recznie
/gaitsync amplitude <0..3> szerokosc kroku, 0 = zostaw oryginalna
/gaitsync scope gracz|wszyscy
```

Komenda jest **kliencka**, wiec dziala takze podczas odtwarzania nagrania
we Flashbacku -- tam nie ma serwera, ktory przyjalby zwykla komende.

## Build

Wymaga JDK 25.

```
gradlew.bat build
```

Wynik: `build/libs/gaitsync-0.1.0.jar`
