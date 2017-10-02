---
# Fontos:
- UI-os fix (elérési útvonal javítás)
- csúszka maximalizálása (transfer fázis)
- Az attack ablakon le kellene kezelni a Retreat gomb klikkjét
- Szintén az attack-on le kellene kezelni az Attack gomb klikkjét ami a támadást végrehajtatja a controval.
- A modell engedi a next player-t úgy, hogy a jelenlegi player Reinforce-ban van és még van katonája amit le kellene rakni.
- adatszerkezet kell a kártyák mögé (JSON) (ID alapján összekötjük a felületen lévő képekkel. Vagy az se baj ha nem képekkel, csak a nevüket tudjuk és egy listába felsoroljuk és kész. Mivel ha jól emlékszem minden automatikusan történik ezért a kontrollernek nincs is sok dolga az adatáteresztésen kívül)
- illetve balra egy üres helyre majd kirakok egy dobozt, hogy melyik játékosnak hány lerakatlan katonája van, mert az is idegesítő hogy nem látszik
- A modell szerintem nem adja vissza a támadás eredményét (dobott kockák, elvesztett egységek, megmaradt egységek stb.). Ha nagyon muszáj az egységeket tudja számolgatni a contro (de akkor minek a modell trololo), de a kockák mindenképp kellenének
- működjön 3 és max 6(5?) player esetén is
- országokon belül körbe írom majd a számokat, hogy mennyi egység van rajta
- mentés betöltés
- A transfer ablaknál miután megtörtént a transfer frissíteni kellene az ablakban az adatokat (contro's job) vagy megölni az ablakot
---
# Extra:
- nem tudjuk hogy éppen melyik fázis jön, kéne egy csík felülre ami ilyen csúszóablakos, hogy előreláthassunk 4-5 állapotot
- előre láthatóság?
- turbózni képekkel?
---
