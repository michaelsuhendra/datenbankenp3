<html>
<head>
<title>Inserator</title>
<style>
body{
	font-family: 'Helvetica', sans-serif;
}
</style>
</head>
 
<body>
<h2>Konversation mit Benutzer ${empfaenger}</h2>

<#if nachrichten?has_content>
<#list nachrichten as na>
<b>${na.absender}: </b> ${na.text} </br>
</#list>
</br>
<#else>
<h3>Noch keine Konversation vorhanden</h3>
</#if>

<form name="nachricht" action="privatnachrichten" method="post">
<textarea name="text" cols="40" rows="6"></textarea> </br>
<input type="hidden" name="empfaenger" value=${empfaenger}>
<input type="hidden" name="absender" value=${absender}>
<input type="submit" value="Senden">
</form>

</body>
</html>