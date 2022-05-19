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
			<input id="python" type="radio" name="language" />
		
			<nav>
				<label for="java">Java</label>
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
			        		<li><a href="/assets/material/java/json-vorlage.zip">Projekt nur mit JSON</a> &mdash; In diesem Projekt muss man die JSON-Daten direkt als JSON verarbeiten. Das ist etwas anspruchsvoller, aber funktioniert auch mit APIs von anderen Websiten.</li>
			        		<li><a href="/assets/material/java/objekte-vorlage.zip">Projekt mit Objekten</a> &mdash; In diesem Projekt steht eine Hilfsklasse zur Verfügung, die die JSON-Antworten in richtige Objekte übersetzt. Der Code ist dadurch anschaulicher</li>
			        	</ul>
			        </div>
			        <div class="media-heading">
			        	<h2 class="media-title">Handouts</h2>
			        </div>
			        <div class="media-body">
			        	Die Handouts stellen die notwendige Theorie dar und führen Beispiele zur Anwendung im SocialBotNet vor.
			        	<ul>
							<li><a href="/assets/material/java/handout.pdf">Handout zu GET- und POST-Anfragen</a></li>
							<li><a href="/assets/material/java/objekte-handout.pdf">Handout zum Arbeiten mit der Objekt-Vorlage</a></li>
							<li><a href="/assets/material/java/json-handout.pdf">Handout zum Arbeiten mit der JSON-Vorlage</a></li>
			        	</ul>
			        </div>		      
				</div>
				<div class="pythonTab">
					<div class="media-heading">
						<h2 class="media-title">Projektvorlagen</h2>
					</div>
					<div class="media-body">
						<p>Die Projektvorlage stellt eine Klasse <code>NetzwerkVerbindung</code> zur Verfügung, die den Verbindungsaufbau und die Verarbeitung von Datenströmen abnimmt. Zudem können die Methoden, die man in der Klasse SocialBot implementiert, über eine kleine GUI ausgeführt werden.</p>
						<p>Du kannst entweder direkt die JSON-Daten verarbeiten, oder die Hilfsklassen verwenden, die die JSON-Daten zu Python Objekten umwandelt.</p>
						<p><a href="/assets/material/python/vorlage.zip">Projektvorlage herunterladen</a></p>
					</div>
					<div class="media-heading">
						<h2 class="media-title">Handouts</h2>
					</div>
					<div class="media-body">
						Die Handouts stellen die notwendige Theorie dar und führen Beispiele zur Anwendung im SocialBotNet vor.
						<ul>
			        		<li><a href="/assets/material/python/handout.pdf">Handout zu GET- und POST-Anfragen</a></li>
			        		<li><a href="/assets/material/python/objekte-handout.pdf">Handout zum Arbeiten mit den Objekten</a></li>
			        		<li><a href="/assets/material/python/json-handout.pdf">Handout zum Arbeiten mit den JSON-Daten</a></li>
						</ul>
					</div>
				</div>
			</figure>
		</div>
    </div>
</@layout.masterTemplate>