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
  <form name="user" action="anzeige_details?action=editieren" method="post">
  	<h1 style="font-size:32px;">Anzeige editieren</h1>
    Titel <input type="text" name="titel" value=${titel} /> <br/>
    Kategorie </br>
    <input type="checkbox" name="kategorie" value="Digitale Waren"${checked1}>Digitale Waren
    <input type="checkbox" name="kategorie" value="Haus & Garten"${checked2}>Haus & Garten <br/>
    <input type="checkbox" name="kategorie" value="Mode & Kosmetik"${checked3}>Mode & Kosmetik
    <input type="checkbox" name="kategorie" value="Multimedia & Elektronik"${checked4}>Multimedia & Elektronik <br/>
    Preis <input type="text" name="preis" value=${preis} /> <br/>
    Beschreibung <textarea name="text" cols="40" rows="6">${text}</textarea> <br/>
    <input type="hidden" name="benutzer" value=${ersteller}>
    <input type="hidden" name="id" value=${anzeigeid}>
  	<input type="submit" value="Aktualisieren"/>
  	</form>
  	 
 <#if errormessage?has_content> 	
 <p>${errormessage}</p>
 </#if>
 
</body>
</html>