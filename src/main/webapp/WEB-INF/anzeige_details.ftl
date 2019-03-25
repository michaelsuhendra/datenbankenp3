<html>
<head>
<title>Anzeige Details</title>
<style>
body{
	font-family: 'Helvetica', sans-serif;
}
</style>
</head>
 
<body>
<h1>Anzeige Details</h1> </br>
<h2>${titel}</h2>
Seit: ${erstellungsdatum} von <a href="user_profil?ersteller=${ersteller}">${ersteller}</a> </br>
${text} </br>
<h3>Preis: ${preis} EUR</h3> </br> 

<#if status == 'verkauft'>
Gekauft von: ${kaeufer} </br>
<#else>

<#if ersteller != benutzer>
<form name="kaufen" action="anzeige_details?action=kaufen" method="post">
    <input type="hidden" name="id" value=${id}>
    <input type="hidden" name="benutzer" value=${benutzer}>
    <input type="submit" value="Kaufen">
  </form>

<#else>
  <form name="anzeige_editieren" action="anzeige_editieren" method="get">
    <input type="hidden" name="anzeigeid" value=${id}>
    <input type="hidden" name="ersteller" value=${benutzer}>
    <input type="submit" value="Editieren">
  </form>
  
  <form name="loeschen" action="anzeige_details?action=loeschen" method="post">
    <input type="hidden" name="id" value=${id}>
    <input type="hidden" name="benutzer" value=${benutzer}>
    <input type="submit" value="Löschen">
  </form>
</#if>

</#if>

<hr>

<h2>Kommentare</h2>

<#list allekommentare as ko>
<b>${ko.geschriebenVon} :</b> ${ko.text} </br>
</#list>


  <form name="kommentare" action="anzeige_details?action=kommentar" method="post">
    Kommentar <input type="text" name="kommentartext"> </br>
    <input type="hidden" name="id" value=${id}>
    <input type="hidden" name="benutzer" value=${benutzer}>
    <input type="submit" value="Kommentar hinzufügen">
  </form>
 
  
</body>
</html>