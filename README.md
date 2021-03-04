[greenfoot]: https://www.greenfoot.org/
[wonderland]: https://www.midnightsynergy.com/wonderland/
[demo]: doc/demo.gif

[first-steps]: maps/1.map
[traps]: maps/2.map
[maps]: maps/
[level-java]: Level.java

[tile-java]: Tile.java
[barrier-java]: Barrier.java
[wall-java]: Wall.java
[floor-java]: Floor.java
[floor-info-java]: FloorInfo.java
[floor-lava-java]: FloorLava.java
[belt-java]: Belt.java
[wall]: images/Wall.gif
[floor]: images/Floor.gif
[floor-info]: images/FloorInfo.gif
[floor-lava]: images/FloorLava.gif
[belt]: images/Belt.gif

[entity-java]: Entity.java
[player-java]: Player.java
[coin-java]: Coin.java
[bonus-java]: Bonus.java
[box-java]: Box.java
[portal-java]: Portal.java
[gate-java]: Gate.java
[player]: images/Player.gif
[coin]: images/Coin.gif
[bonus]: images/Bonus.gif
[box]: images/Box.gif
[portal]: images/Portal.gif
[gate]: images/Gate.gif

[adobe-photoshop]: https://en.wikipedia.org/wiki/Adobe_Photoshop
[images]: images/
[gif-image-java]: GifImage.java
[audacity]: https://en.wikipedia.org/wiki/Audacity_(audio_editor)
[text-label-java]: TextLabel.java
[fonts]: fonts/

[tile-actor-java]: TileActor.java
[interpolator-java]: Interpolator.java

[level-java]: Level.java

# Wonderland
Acest proiect [Greenfoot][greenfoot] constă într-un joc de tip _puzzle_ în cadrul căruia trebuie colectate toate _Monedele Curcubeu_ din cadrul unui nivel. Pentru a ajunge la acestea este necesară trecerea de diverse obstacole. Jocul își propune recrearea într-o maineră 2D a jocului [Wonderland][wonderland] pentru familiarizarea cu platforma [Greenfoot][greenfoot]. Mai jos se află o mică demonstrație a proiectului:

![demo]

## Niveluri disponibile
În stadiul său actual, jocul prezintă 2 niveluri:
1. [Primii pași][first-steps]
2. [Capcane][traps]

Nivelurile se stochează în directorul [maps], iar de acolo sunt încărcate de către joc. Formatul de descriere al unui nivel este unul _plaintext_ pentru o editare cât mai facilă cu un simplu editor de text. Pozițiile elementelor dintr-un nivel sunt determinate de configurația prezentă în fișierul `.map` corespunzător nivelului.

## Formatul unui fișier `.map`

Într-un fișier `.map` se găsesc următoarele, în ordinea apariției:
1. culoarea fundalului
2. mesajele ce vor fi afișate când jucătorul se așază pe un `FloorInfo`
3. configurația propriu-zisă ce descrie atât ce elemente să fie încărcate, cât și proprietățile acestora acolo unde este cazul (fiecărui element îi corespunde un `id` în fișier, `id` determinat de enumerațiile `TileType`, `EntityType` și `FloorType`

Încărcarea nivelului este efectuată de către metoda `loadMap` prezentă în [clasa `Level`][level-java].

## Implementări ale clasei [`Tile`][tile-java]
* [`Barrier`][barrier-java]: blochează accesul jucătorului în zonele în care acesta nu trebuie să ajungă (este invizibil)
* [`Wall`][wall-java]: blochează accesul jucătorului în zonele în care acesta nu trebuie să ajungă (este vizibil)
* [`Floor`][floor-java]: podea pe care jucătorul poate merge
  * [`FloorInfo`][floor-info-java]: când jucătorul calcă pe aceasta este afișat un mesaj pe ecran
  * [`FloorLava`][floor-lava-java]: la un anumit interval de timp specificat se transformă în lavă
* [`Belt`][belt-java]: bandă rulantă ce te transportă într-un anumit sens foarte repede

![wall] ![floor] ![floor-info] ![floor-lava] ![belt]

## Implementări ale clasei [`Entity`][entity-java]
* [`Player`][player-java]: jucătorul care ce este controlat cu săgețile
* [`Coin`][coin-java]: monedă curcubeu ce trebuie colectată
* [`Bonus`][bonus-java]: adaugă puncte bonus la scor
* [`Box`][box-java]: cutie ce poate fi împinsă dacă în fața acesteia nu se află niciun obstacol
* [`Portal`][portal-java]: portal în care intri pentru a finaliza nivelul
* [`Gate`][gate-java]: poartă ce blochează accesul la [`Portal`][portal-java] până când toate _Monedele Curcubeu_ sunt colectate

![player] ![coin] ![bonus] ![box] ![portal] ![gate]

## _Asset_-urile utilizate
Imaginile actorilor au fost realizate în [Adobe Photoshop][adobe-photoshop] (în directorul [images][images] se pot găsi fișierele sursă `.psd`). Fișierele `.gif` animate (e.g. [Coin][coin], [Bonus][bonus], [Portal][portal]) au fost implementate utilizând clasa ajutătoare [`GifImage`][gif-image-java] ce a fost adăugată din meniul `Edit -> Import class...` al [Greenfoot][greenfoot].

Sunetele și muzica au fost descărcate de pe [Freesound](freesound.org) și modificate și normalizate în [Audacity][audacity].

Textele afișate în joc la atingerea unui [`FloorInfo`][floor-info-java] sunt implementate utilizând clasa [`TextLabel`][text-label-java] construită astfel încât să poată utiliza _font_-uri prezente în directorul [fonts][fonts]. De asemenea, la această clasă s-a implementat și posibilitatea de a folosi coduri hexazecimale de culoare în formatul `{rrggbb}` pentru da un pic de personalitate textului, iar secvența de caractere `{n}` denotă trecerea pe o nouă linie.

## Detecția coliziunilor
Detecția coliziunilor se face utilizând funcțiile `isSteppable` și `isPushable` din clasa [`TileActor`][tile-actor-java]. Locul unde aceste două funcții sunt implementate este în metoda `step` ce se găseșe în aceeași clasă. Pentru mișcarea fluidă a jucătorului s-a utilizat clasa [`Interpolator`][interpolator-java] ce a fost implementată în metoda `act` din [`TileActor`][tile-actor-java].

## Mișcarea camerei
Camera poate fi mișcată independent de jucător ținând apăsată tasta `Space`, iar mai apoi utilizând săgețile pentru mutarea acesteia (`Space` acționează ca un _modifier_ pentru săgețile de navigare). Mișcarea fluentă a camerei se realizază tot cu ajutorul clasei [`Interpolator`][interpolator-java], dar de data aceasta utilizată în funcția `act` a clasei [`Level`][level-java].
