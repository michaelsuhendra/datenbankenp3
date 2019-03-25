<html>
<head>
<title>Inserator</title>
<style>
body{
	font-family: 'Helvetica', sans-serif;
}
p{
	color: red;
}
</style>

</head>
 
<body>
  <form name="user" action="hauptseite" method="post">
  	<h1 style="font-size:32px;">Anzeige erstellen</h1>
    Titel <input type="text" name="titel" /> <br/>
    Kategorie </br>
    <input type="checkbox" name="kategorie" value="Digitale Waren">Digitale Waren
    <input type="checkbox" name="kategorie" value="Haus & Garten">Haus & Garten <br/>
    <input type="checkbox" name="kategorie" value="Mode & Kosmetik">Mode & Kosmetik
    <input type="checkbox" name="kategorie" value="Multimedia & Elektronik">Multimedia & Elektronik <br/>
    Preis <input type="text" name="preis" /> <br/>
    Beschreibung <textarea name="text" cols="40" rows="6"></textarea> <br/>
    <input type="hidden" name="ersteller" value=${ersteller}>
    <input type="hidden" name="benutzer" value=${ersteller}>
  	<input type="submit" value="Erstellen">
  	</form>
 
 <#if errormessage?has_content> 	
 <p>${errormessage}</p>
 </#if>
 
</body>
</html>