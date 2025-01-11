
Az API dokumentáció elérhető a következő linken:
Api dokuemntáció: https://app.swaggerhub.com/apis/TAMASDURO_1/ponte-vote_api-homeWork/1.0.0

A link kimásolásával és a böngésző címsorába való beillesztésével megtekinthető az összes 
API végpont részletes dokumentációja. 
Ez a dokumentáció a [resource/openapi.yaml] fájlban is megtalálható,
azonban a hivatkozás biztosítja a gyorsabb elérhetőséget.

A rendszerben a tokenek 24 percig érvényesek, utána újra be kell jelentkezni.

Adatbázis kapcsolat:

    url: jdbc:postgresql://localhost:5432/pontehw
    username: ponte
    password: ponte123

Az API végpontok eléréséhez egy Bearer Token szükséges.
A token kezelése a Postmanben a következő módon végezhető:

    Authorization fül használata:
        Navigáljon a Postman Authorization fülre.
        Az Auth Type legördülő menüből válassza a Bearer Token lehetőséget.
        A Token mezőbe illessze be a rendszer által generált tokent.

    Token hozzáadása Headerben:
        Alternatív megoldásként a token manuálisan is hozzáadható az HTTP Headerben:
            Key: Authorization
            Value: Bearer <token>

A program a token meglétét a Headerben vizsgálja, és ennek alapján validálja a felhasználót. 
A validáció során a rendszer figyelembe veszi a felhasználó szerepkörét, amely meghatározza,
hogy milyen végpontok érhetők el számára.


Token kinézete: eyJhbGciOiJIUzI1NiJ9.
eyJyb2xlIjoiVVNFUiIsInN1YiI6Im1haWxAbWFpbC5jb20iLCJpYXQiOjE3MzY1NjgxNDEsImV4cCI6MTczNjU2OTU4MX0
.ivjJmWn2o7TZSpJlHEMYiazSMTtI4oBdpxTP2S6AUd0


Végpontok Szerepkörök Szerint
Az API végpontokat szerepkörök alapján csoportosítva érhetjük el:

    Nyilvános végpontok (bárki elérheti):
        URL: http://localhost:8080/api/auth

    Adminisztrátor végpontok:
        URL: http://localhost:8080/api/admin

    Felhasználói végpontok:
        URL: http://localhost:8080/api/idea


Postman Végpontok

A Postmanben minden végponthoz előre elkészített HTTP metódusok állnak rendelkezésre.
Ezáltal a végpontok külön létrehozása nem szükséges.
Az assets/PonteHomeWork.postman_collection.json fájlt kell beimportáli a postmban alkalmazásba.

Fájl útvonal: [postman_json](assets/PonteHomeWork.postman_collection.json)


Adatbázis Inicializáció és Adminisztrátor Bejelentkezés

A program indítása után a Flyway automatikusan inicializálja az adatbázist. 
Ennek keretében az adminisztrátor felhasználó is bekerül az adatbázisba, így az azonnal használható.

Az adminisztrátor belépési adatai:

    Email: admin
    Jelszó: admin

Bejelentkezés után a rendszer visszaad egy tokent, amely felhasználható az API végpontok eléréséhez.
A token beállításához kövesse a fentiekben leírt Postman utasításokat.


Tesztelés és Kódfedettség

A rendszerhez több teszt is készült, amelyek a service és controller osztályokra fókuszálnak.
A tesztek futtatása után a kódlefedettség ellenőrizhető, így pontos adatot ad a lefedett funkciókról,
vagy rögtön megtekinthető az alábbi linkre kattintva.

 Dátum: 2025.01.11. [Teszt lefedettség](assets/TestCoverage.png) 


További Fejlesztési Útmutató

    Adminisztrátor szerepkörhöz kapcsolódó funkciók:
    Az adminisztrátor szerepkörhöz kapcsolódó funkciók már a bejelentkezés során validálják,
    hogy az admin nem szavazhat. Az adminisztrátor ezenkívül képes elfogadni (ACCEPT) vagy elutasítani 
    (DECLINE) egy beküldött ötletet.

    Felhasználói hozzáférés:
    A felhasználók kizárólag az ACCEPTED (elfogadott) státuszú ötletekhez férhetnek hozzá. 
    Ez biztosítja, hogy csak a jóváhagyott ötletek kerüljenek a nyilvános felhasználói szintre, 
    míg a még elbírálás alatt álló vagy elutasított ötletek rejtve maradnak.

    Szavazási Korlátozások

    Egy szavazó egy adott napon csak egyszer szavazhat:
    Ez a korlátozás biztosítja a rendszer igazságosságát, és megakadályozza a visszaéléseket. 
    Minden felhasználó szavazási lehetősége éjfélkor automatikusan visszaáll, így másnap újra szavazhat. 
    Ezt egy ütemezett (scheduled) metódus végzi, amely az éjféli időpontban lefut, és frissíti az 
    adatbázisban a szavazási státuszt.

    Az ötletgazda nem szavazhat a saját ötletére:
    Ez a szabály megakadályozza, hogy a felhasználók önérdekűen manipulálják a szavazás eredményét.
    Az API minden szavazási kérelem előtt ellenőrzi, hogy a szavazó nem az ötlet gazdája.

    Végpontok strukturálása:
    A végpontok szerepkörök szerinti strukturálása segíti a logikai elkülönítést, és biztosítja,
    hogy minden szerepkör kizárólag a számára engedélyezett funkciókat érhesse el. 
    Ez a megoldás egyértelművé és biztonságossá teszi az API használatát.

    Flyway adatbázis-kezelés:
    A Flyway használata automatikusan kezeli az adatbázis sémák inicializálását, 
    amely lehetővé teszi a gyors tesztelést és fejlesztést. Az adminisztrátor kezdeti létrehozása is a 
    Flyway segítségével történik, így az alkalmazás azonnal használatra kész.

CORS Konfiguráció

Az alkalmazásban beállításra került egy CORS (Cross-Origin Resource Sharing) konfiguráció,
amely lehetővé teszi, hogy különböző forrásokból érkező kliensek (pl. frontend alkalmazások)
hozzáférjenek az API végpontokhoz.

Miért van szükség a CORS konfigurációra?

A modern webböngészők biztonsági okokból alapértelmezés szerint blokkolják a különböző domain-ről
érkező HTTP kéréseket (ún. cross-origin requests). Ez azt jelenti, hogy ha az API-d a 
http://localhost:8080 címen fut, akkor a http://localhost:4200 címen futó frontend kliens mint például
az Angular alapértelmezés szerint nem férhet hozzá.

A CORS konfigurációval lehetővé tehető, hogy a különböző domainekről érkező kéréseket is elfogadja az API.

Miért készült el így?

A jelenlegi konfiguráció célja, hogy a fejlesztési fázisban,
könnyen elérhető legyen az API bármely frontend alkalmazásból, anélkül hogy korlátozásokat állítanánk be. 
Tesztelési és debugolási célokra egyszerű legyen a hozzáférés biztosítása.

Viszont nem szabad megfeletkezni arról, hogy a jelenlegi beállítások nem biztonságosak hosszú távon,
mert minden domain hozzáférhet az API-hoz.


