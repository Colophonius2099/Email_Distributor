# Email_Distributor
Baut die Email Verteiler Listen

## Wie kann ich das Progamm verwenden?
Wenn du Verteiler generieren willst, dann musst du den 'Download Folder' runterladen und das darin vorhandene ReadMe ordentlich durchlesen. 
Dann sollte es von selbst funktionieren.

## Wie funktioniert der Code?
Das Programm läd zuerst alle Scout ID's runter und läd anhand der Scout ID's alle Mitglieder der Gruppe runter.
Danach läd das Programm jeden Verteiler.
Dies wird gemacht, indem Anhand der Regeln des Verteilers jedes Mitglied überprüft wird ob die Person zum Verteiler 
gehört und später ob die Person Rechte hat auf den Verteiler zu schreiben.
Sobald alle Verteiler gestellt sind, wird die JSON Datei (welche die Einstellungen des Verteilers beinhaltet) und 
die CSV Datei (welche die empfangenden Mail-Boxen erzeugt).
Genauere Erklärungen befinden sich in relevanten Ordnern.
