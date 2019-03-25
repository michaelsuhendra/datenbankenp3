<html>
<head>
<title>Inserator</title>

</head>
 
<body>
<h1>Profil von ${person.benutzername}</h1>
<h2>${person.name}</h2>
Beitrittsdatum: ${person.eintrittsdatum} </br>
Anzahl verkaufter Artikel: ${anzahl}

<hr>

<h2>Angeboten</h2>
<#list angeboten as an>
<h3>${an.titel}</h3>
Seit: ${an.erstellungsdatum}
Preis: ${an.preis} EUR </br>
${an.status} </br>
</#list>

<hr>

<h2>Gekauft</h2>
<#list gekauft as gk>
<h3>${gk.titel}</h3>
Seit: ${gk.erstellungsdatum}
Preis: ${gk.preis} EUR </br>
${gk.ersteller} </br>
${an.status} </br>
</#list>
  
  
</body>
</html>