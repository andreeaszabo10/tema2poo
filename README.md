# Tema 2 POO
## Szabo Cristina-Andreea
Pentru etapa a 2 a a proiectului am implementat următoarele operatii:
1. Search - am mai făcut o functie exact ca cea din schelet doar ca intrarile sunt de tip User nu LibraryEntry
2. AddUser - am initializat un nou user și l am adăugat în lista de useri din admin  i am adăugat și type ul care e important
3. DeleteUser - dacă user ul care trebuie șters e de tip artist, am iterat prin lista de albume ca să găsesc albumele user ului și am șters toate cântecele din albume din lista songs și le am șters și din listele de likedsongs ale fiecărui user. Apoi am șters fiecare album din lista de albume. Dacă user ul e normal, am scăzut numărul de followeri pentru playlist urile pe care le urmarea și am șters playlist urile lui din listele de followed ale altor useri
4. ShowAlbums - am iterat prin lista de albume și am afișat numele albumului și cântecele lui dacă owner ul se potrivea cu username ul primit
5. ShowPodcasts - am iterat prin lista de podcast uri și am afișat numele podcast ului și episoadele lui dacă owner ul se potrivea cu username ul primit
6. AddAlbum - am adăugat noile cântece în lista de songs, am adăugat un nou artist în lista artists daca nu exista deja și albumul in lista albums, asta dacă vreo melodie din album nu se repeta sau dacă artistul nu are deja un album cu numele dorit, cazuri în care afișez mesaje de eroare
7. RemoveAlbum - am verificat ca userul sa fie artist, sa aibă un album cu numele cerut, sa existe și albumul sa nu fie selectat de un alt user folosind o variabila selected in album, apoi am șters albumul din lista de albums
8. AddEvent, AddMerch, AddAnnouncement - am verificat posibilele erori si am afisat mesajele corespunzatoare, am creat câte o clasa pentru fiecare tip și am adăugat în noile listele de anunțuri, evenimente și merch
9. RemoveEvent, RemoveMerch, RemoveAnnouncement - am verificat ca ce vreau sa sterg exista, apoi am eliminat din lista respectiva ce se dorea a fi șters
10. AddPodcast - verific dacă userul e host și dacă nu mai are alt playlist cu același nume, îl adaug la lista podcasts
11. RemovePodcast - verific dacă vreun episod din podcast e in playerul unui user luând pe rand fiecare fișier audio din playere și comparând cu fiecare episod, apoi șterg podcast ul dacă nu exista alta eroare
12. SwitchConnectionStatus - am o variabila care retine dacă userul e online, iar dacă devine offline, pun pauza la playerul userului
13. GetTop5Albums - am o variabila in album care retine like urile, apoi sortez albumele in functie de like uri și după alfabetic după nume
14. GetTop5Artists - calculez like urile pentru fiecare artist adunând like urile fiecărei melodii care are ca owner artistul curent, apoi sortez lista și iau primii 5 artiști
15. GetTop5Playlists - sortez lista de playlist-uri după numărul de followeri și afișez primele 5
16. GetOnlineUsers - verific dacă userul e normal și dacă statusul lui e online și îl afișez
17. GetAllUsers - iterez prin toți userii și adaug la lista de output mai întâi userii normali, apoi pe artiști, apoi host și afișez lista
Am folosit scheletul dat ca rezolvare pentru tema 1 și ChatGpt pentru funcția isValidDate și ca să aflu de existenta stringbuilder, stringbuilder.append().