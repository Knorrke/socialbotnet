<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />

<@layout.masterTemplate pageTitle="Materialien" colored=true>
    <#if success??>
    	<div class="success">
    		${success}
    	</div>
    </#if>
    <#if error??>
    	<div class="error">
    		<strong>Error:</strong> ${error}
    	</div>
    </#if>
    
    <div class="media media-info">
        <div class="media-heading">
        	<h3 class="media-title">Projektvorlagen f&uuml;r Java</h3>
        </div>
        <div class="media-body">
        	Die Projektvorlagen stellen eine Klasse <code>NetzwerkVerbindung</code> zur Verf&uuml;gung, die den Verbindungsaufbau und die Verarbeitung von Datenstr&ouml;men abnimmt.
        	<ul>
        		<li><a href="/assets/material/json-vorlage.zip">Projekt nur mit JSON</a> &mdash; In diesem Projekt muss man die JSON-Daten direkt als JSON verarbeiten. Das ist etwas anspruchsvoller, aber funktioniert auch mit APIs von anderen Websiten.</li>
        		<li><a href="/assets/material/objekte-vorlage.zip">Projekt mit Objekten</a> &mdash; In diesem Projekt steht eine Hilfsklasse zur Verf&uuml;gung, die die JSON-Antworten in richtige Objekte &uuml;bersetzt. Der Code ist dadurch anschaulicher</li>
        	</ul>
        </div>
    </div>
    
	<div class="media media-info">
        <div class="media-heading">
        	<h3 class="media-title">Handouts</h3>
        </div>
        <div class="media-body">
        	Die Handouts stellen die notwendige Theorie dar und f&uuml;hren Beispiele zur Anwendung im SocialBotNet vor.
        	<ul>
        		<li><a href="/assets/material/handout.pdf">Handout zu GET- und POST-Anfragen</a></li>
        		<li><a href="/assets/material/json-handout.pdf">Handout zum Arbeiten mit der JSON-Vorlage</a></li>
        		<li><a href="/assets/material/objekte-handout.pdf">Handout zum Arbeiten mit der Objekt-Vorlage</a></li>
        	</ul>
        </div>
    </div>
</@layout.masterTemplate>