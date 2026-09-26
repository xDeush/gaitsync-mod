# GaitSync

Mod kliencki do Fabrica. Sprawia, ze animacja chodu gracza jest **taka sama
w kazdym nagraniu tej samej sceny** -- zeby przenikanie miedzy dwoma
renderami nie pokazywalo dwoch par nog pod roznym katem.

## Na czym polega problem

Faza kroku w Minecrafcie narasta z przebytego dystansu. Dwa osobne nagrania
tej samej sceny maja wiec konczyny w roznych miejscach cyklu, nawet gdy
gracz porusza sie identycznie. Przy crossfade w montazu widac to od razu.

## Co robi

Zeruje faze chodu na zadanie. Robisz to w obu instancjach tuz przed
nagraniem, stajesz w miejscu, i ruszacie w tym samym momencie -- oba
przebiegi startuja z tego samego punktu cyklu, a dalej animacja leci
normalnie, z prawdziwego ruchu gracza.

Mod NIE zmienia animacji w zaden inny sposob. Nie nadpisuje jej ciagle,
nie zamraza, nie podmienia tempa -- po prostu ustawia licznik na zero.

## Uzycie

Wrzuc jar do `mods` razem z Fabric API. Potem, przed kazdym nagraniem,
w obu instancjach:

```
/gaitsync reset
```

Tyle. Reszta komendy to tryb awaryjny, opisany nizej.

## Bez moda

To samo daje przelogowanie: faza liczy sie od powstania encji gracza, wiec
wyjscie do menu i powrot zeruje ja tak samo. Mod jest po to, zeby nie
trzeba bylo przerywac nagrania.

## Tryb ciagly (domyslnie wylaczony)

`/gaitsync on` wlacza nadpisywanie fazy z czasu swiata. Animacja przestaje
wtedy wynikac z ruchu gracza i wyglada sztucznie -- ten tryb ma sens tylko,
gdy zerowanie nie wystarcza, bo nagrania rozjezdzaja sie w trakcie.

Komenda do poprawek bez restartu gry:

```
/gaitsync reset            wyzeruj faze chodu (to jest to, czego zwykle chcesz)
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
