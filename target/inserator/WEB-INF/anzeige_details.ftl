<html>
<head>
<title>Anzeige Details</title>

</head>
 
<body>
<h1>Anzeige Details</h1> </br>
<h2>${titel}</h2>
Seit: ${erstellungsdatum} von ${ersteller} </br>
${text} </br>
<h3>Preis: ${preis} EUR</h3> </br> 

<#if status == 'verkauft'>
Gekauft von: ${kaeufer} </br>

<#else>
<form name="kaufen" action="kaufen" method="post">
    <input type="hidden" name="anzeigeid" value=${id}>
    <input type="hidden" name="benutzer" value=${benutzer}>
    <a href="/anzeige_details.ftl" class="button">Kaufen</a>
  </form>
</#if>

<#if ersteller == benutzer>
  <form name="editieren" action="editieren" method="post">
    <input type="hidden" name="anzeigeid" value=${id}>
    <input type="submit" value="Editieren">
  </form>
  
  <form name="loeschen" action="loeschen" method="post">
    <input type="hidden" name="anzeigeid" value=${id}>
    <input type="submit" value="Löschen">
  </form>
</#if>

<hr>

<h2>Kommentare</h2>

<#list allekommentare as ko>
<b>${ko.geschriebenVon} :</b> ${ko.text} </br>
</#list>


  <form name="kommentare" action="kommentar" method="post">
    Kommentar <input type="text" name="kommentartext"> </br>
    <input type="hidden" name="anzeigeid" value=${id}>
    <input type="hidden" name="benutzer" value=${benutzer}>
    <input type="submit" value="Kommentar hinzufügen">
  </form>
 
  
</body>
</html>