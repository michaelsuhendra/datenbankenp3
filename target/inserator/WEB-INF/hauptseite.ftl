<html>
<head>
<title>Inserator</title>

</head>

<body>
<h1>Übersicht aller Kleinanzeigen</h1>
<p>Sie sind eingeloggt als ${benutzer}</p>

<#list alleanzeigen as an>
<h2> <a href="\anzeige_details?id=${an.id}&benutzer=${benutzer}" title="Click for details"> ${an.titel} </a> </br> </h2>
Seit: ${an.erstellungsdatum}
Preis: ${an.preis} EUR </br>
<a href="\user_profil?ersteller=${an.ersteller}"> ${an.ersteller} </a> </br>
</br>
</#list>

<form action="anzeige_erstellen">
<input type="hidden" name="ersteller" value=${benutzer}>
<input type="submit" value="Neue Kleinanzeige erstellen">
</form>
</body>
</html>