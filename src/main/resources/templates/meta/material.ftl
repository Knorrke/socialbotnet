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
	    <div class="tabbed">
			<input checked="checked" id="java" type="radio" name="language" />
			<input id="onlineIDE" type="radio" name="language" />
			<input id="python" type="radio" name="language" />
		
			<nav>
				<label for="java">Java (BlueJ)</label>
				<label for="onlineIDE">Java (Online-IDE)</label>
				<label for="python">Python</label>
			</nav>
		   
			<figure>
				<div class="javaTab">
			        <div class="media-heading">
			        	<h2 class="media-title">Projektvorlagen</h2>
			        </div>
			        <div class="media-body">
			        	Die Projektvorlagen stellen eine Klasse <code>NetzwerkVerbindung</code> zur Verfügung, die den Verbindungsaufbau und die Verarbeitung von Datenströmen abnimmt.
			        	<ul>
			        		<li><a href="/assets/v4.2.0/material/java/json-vorlage.zip">Projekt nur mit JSON</a> &mdash; In diesem Projekt muss man die JSON-Daten direkt als JSON verarbeiten. Das ist etwas anspruchsvoller, aber funktioniert auch mit APIs von anderen Websiten.</li>
			        		<li><a href="/assets/v4.2.0/material/java/objekte-vorlage.zip">Projekt mit Objekten</a> &mdash; In diesem Projekt steht eine Hilfsklasse zur Verfügung, die die JSON-Antworten in richtige Objekte übersetzt. Der Code ist dadurch anschaulicher</li>
			        	</ul>
			        </div>
			        <div class="media-heading">
			        	<h2 class="media-title">Handouts</h2>
			        </div>
			        <div class="media-body">
			        	Die Handouts stellen die notwendige Theorie dar und führen Beispiele zur Anwendung im SocialBotNet vor.
			        	<ul>
							<li><a href="/assets/v4.2.0/material/java/handout.pdf">Handout zu GET- und POST-Anfragen</a></li>
							<li><a href="/assets/v4.2.0/material/java/objekte-handout.pdf">Handout zum Arbeiten mit der Objekt-Vorlage</a></li>
							<li><a href="/assets/v4.2.0/material/java/json-handout.pdf">Handout zum Arbeiten mit der JSON-Vorlage</a></li>
			        	</ul>
			        </div>		      
				</div>
				<div class="onlineIDETab">
			        <div class="media-heading">
			        	<h2 class="media-title">Projektvorlagen</h2>
			        </div>
			        <div class="media-body">
			        	<p>Die Projektvorlage für die <a href="https://online-ide.de/">Online-IDE</a> stellt eine Klasse <code>SocialBot</code> bereit, die das senden von HTTP-Anfragen vereinfacht und als Oberklasse für eigene Bots verwendet werden kann.</p>
			        	<p>Zudem gibt es einen <code>Satzgenerator</code>, der in Bots genutzt werden kann um zufällige Beiträge zu erstellen.</p>
			        	<p><a href="/assets/v4.2.0/material/java/onlineide-socialbot-vorlage.json" download>Vorlage für die Online-IDE</a>.
			        </div>
			        <div class="media-heading">
			        	<h2 class="media-title">Arbeitsblatt</h2>
			        </div>
			        <div class="media-body">
			        	<p> Im zugehörigen Arbeitsblatt lernen die SchülerInnen die Projektvorlage und die Webseite kennen und wichtige Ergebnisse festhalten.</p>
			        	<p>Arbeitsblatt als <a href="/assets/v4.2.0/material/java/onlineide-handout.odt" download>ODT</a> oder <a href="/assets/v4.2.0/material/java/onlineide-handout.pdf">PDF</a>.
			        </div>		      
			        <hr>
			        <div class="media-body">
			        	<p>Die Materialien wurden dankenswerterweise von Jürgen Horzella erstellt und hier zum Download zur Verfügung gestellt.</p>
			        </div>
				</div>
				<div class="pythonTab">
					<div class="media-heading">
						<h2 class="media-title">Projektvorlagen</h2>
					</div>
					<div class="media-body">
						<p>Die Projektvorlage stellt eine Klasse <code>NetzwerkVerbindung</code> zur Verfügung, die den Verbindungsaufbau und die Verarbeitung von Datenströmen abnimmt. Zudem können die Methoden, die man in der Klasse SocialBot implementiert, über eine kleine GUI ausgeführt werden.</p>
						<p>Du kannst entweder direkt die JSON-Daten verarbeiten, oder die Hilfsklassen verwenden, die die JSON-Daten zu Python Objekten umwandelt.</p>
						<p><a href="/assets/v4.2.0/material/python/vorlage.zip">Projektvorlage herunterladen</a></p>
					</div>
					<div class="media-heading">
						<h2 class="media-title">Handouts</h2>
					</div>
					<div class="media-body">
						Die Handouts stellen die notwendige Theorie dar und führen Beispiele zur Anwendung im SocialBotNet vor.
						<ul>
			        		<li><a href="/assets/v4.2.0/material/python/handout.pdf">Handout zu GET- und POST-Anfragen</a></li>
			        		<li><a href="/assets/v4.2.0/material/python/objekte-handout.pdf">Handout zum Arbeiten mit den Objekten</a></li>
			        		<li><a href="/assets/v4.2.0/material/python/json-handout.pdf">Handout zum Arbeiten mit den JSON-Daten</a></li>
						</ul>
					</div>
					<hr>
			        <div class="media-body">
			        	<p>Die Materialien wurden dankenswerterweise von Christopher Frank erstellt und hier zum Download zur Verfügung gestellt.</p>
			        </div>
				</div>
			</figure>
		</div>
    </div>
</@layout.masterTemplate>