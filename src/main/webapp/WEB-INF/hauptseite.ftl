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
<h1>Übersicht aller Kleinanzeigen</h1>
<p>Sie sind eingeloggt als ${benutzer}</p>

<#if link??>
${link}
<#else>
</#if>

<form action="anzeige_erstellen">
<input type="hidden" name="ersteller" value=${benutzer}>
<input type="submit" value="Neue Kleinanzeige erstellen">
</form>
</body>
</html>