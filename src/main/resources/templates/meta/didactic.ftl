<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />
<#import "../common/mail-obfuscator.ftl" as mailObfuscator />

<@layout.masterTemplate pageTitle="Informationen für Lehrkräfte" colored=true>
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
    		<h2 class="media-title">Eigener Server</h2>
    	</div>
    	<div class="media-body">
    		<p>Mit einem Account bei <a href="https://render.com" target="_blank" rel="noopener noreferrer">Render.com</a> lässt sich ein eigener Server mit nur einem Klick erstellen:</p>
    		<p styles="text-align:center;">
				<a class="prevent-external" href="https://render.com/deploy?repo=https://github.com/Knorrke/socialbotnet" target="_blank" rel="noopener noreferrer">
					<img src="https://render.com/images/deploy-to-render-button.svg" alt="Deploy to Render">
				</a>
			</p>
			<p> <strong>Einschränkungen</strong> siehe <a href="https://render.com/docs/free">https://render.com/docs/free</a>, insbesondere:
			<ul>
				<li>Nach 15 Minuten Inaktivität wird die App in einen Ruhezustand versetzt, sodass der erste Seitenaufruf eine gewisse Zeit dauert.</li>
				<li>Die ausgehende Bandbreite ist beschränkt auf 100 GB pro Monat.</li>
			</ul>
    	</div>
    </div>    
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Überblick der Didaktik</h2>
        </div>
        <div class="media-body">
        	<h3 class="media-title">Ziel dieser Webseite</h3>
        	<div class="media-body">
        		<p>Die Webseite bietet eine einfache, offene Programmierschnittstelle ("API") über HTTP und HTTPS an, über die die Daten der Webseite abgerufen und neue Inhalte erstellt werden können. Dadurch können Schüler*innen selbst Bots programmieren und so erfahren, wie Social Bots funktionieren. Die Auseinandersetzung durch die eigene Programmierung ermöglicht ihnen, die Technologie zu reflektieren und Gefahren sowie Grenzen einschätzen zu können.</p>
        		<p>Mit einem Account auf der Webseite können Beiträge auf der eigenen Pinnwand oder an bestimmte Nutzer geschrieben werden, Posts geliket und ein Profil gestaltet werden. Besondere Herausforderung ist, mit den eigenen Beiträgen in die Top-Trends auf der Startseite zu gelangen.</p>
        		<p>Hinweis für die Durchführung an der Schule: Beim Einsatz von Bots kann es durch die SSL-Handshakes bei HTTPS zu Verzögerungen kommen. Ist dies der Fall, sollte auf HTTP ausgewichen werden.</p>
        	</div>
        	<h3 class="media-title">Beispiel eines Unterrichtskonzeptes</h3>
        	<div class="media-body">
        		<p>Ein mögliches Unterrichtskonzept für die Oberstufe über den Verlauf von 4 Schulstunden wird im Folgenden kurz dargestellt. Ausführlichere Informationen finden sich insbesondere in der <a href="/assets/v4.2.0/material/gi_unterrichtspreis.pdf" title="Bewerbung für den GI Unterrichtspreis 2019 herunterladen" download>Ausarbeitung für den GI Unterrichtspreis 2019</a></p>
        		<p>Neben den Programmierkenntnissen (sicherer Umgang mit Datentypen, Objekten und for-Wiederholung) ist Vorwissen zum Schichtenmodell und dem Client-Server-Prinzip für die Unterrichtssequenz hilfreich.</p>
        		<h4>Erste Stunde: Einführung zu Social Bots und Verknüpfung mit Protokollen</h4>
        		<ol>
        			<li>Aktivierung: Sammeln von Vorerfahrungen oder zeigen eines Videos. Gut geeignet z.B. <a href="https://youtu.be/HVuB1QPxdT0" target="_blank" rel="noopener noreferrer">"Social Bots" von ZDF heuteplus</a> und <a href="https://youtu.be/j14s00N3clg" target="_blank" rel="noopener noreferrer">"Fake News & Social Bots in 3 Minuten erklärt" von explain-it</a></li>
        			<li>"Nutzersicht" der Webseite: Die Lernenden erstellen einen Account und analysieren, welche Möglichkeiten es auf der Webseite gibt und welche Stellen anfällig für Manipulationen sind.</li>
        			<li>Erläuterung der "Programmierersicht": Gleiches Protokoll (HTTP), aber andere Schnittstelle. Wegen des gleichen Protokolls kann die API auch im Browser betrachtet werden. Die Lernenden rufen als Beispiel <a href="/api/posts?sortby=trending">/api/posts?sortby=trending</a> auf und vergleichen es mit der Benutzersicht der Startseite.</li>        				
        		</ol>
        		<iframe style="width: 100%" height="150" src="/api/posts?sortby=trending&limit=3">Top 3 der Trends in der API. Leider konnte das eingebettete Frame nicht geladen werden. Folgen Sie bitte dem Link oben.</iframe>
        		<h4>Zweite und Dritte Stunde: Programmieren eines eigenen Social Bots</h4>
        		<p>Ausgehend von einfachen Programmen, die per POST-Anfrage vorgefertigte Nachrichten schreiben, werden immer komplexere Anwendungen geschrieben. Anregungen:
        		<ul>
        			<li>Posten vorgefertigter Texte auf der eigenen Pinnwand. <em>(einfach)</em></li>
        			<li>Liken aller Beiträge auf der eigenen Pinnwand.<em>(einfach)</em></li>
        			<li>Posten von Texten, die aus mehreren Satzteilen zufällig kombiniert werden, z.B. wie der <a href="https://buzzomat.de" target="_blank" rel="noopener noreferrer">Buzz-o-Mat</a>.<em>(mittel)</em></li>
        			<li>Posts nach Schlüsselwörtern durchsuchen und dann liken.<em>(anspruchsvoll)</em></li>
        			<li>Nutzerprofile nach Schlüsselwörtern durchsuchen und dann vorgefertigte Texte an diese Nutzer schreiben. <em>(anspruchsvoll)</em></li>
        			<li>Anbinden externer Webservices wie <a href="https://pokeapi.co/" target="_blank" rel="noopener noreferrer">pokeapi.co</a> oder <a href="https://api.openweathermap.org" target="_blank" rel="noopener noreferrer">openweathermap.org</a>. <em>(sehr anspruchsvoll)</em>
        			<p>Eine Übersicht über zahlreiche offene APIs gibt es <a href="https://github.com/public-apis-dev/public-apis" target="_blank" rel="noopener noreferrer">im Github-Projekt public-apis-dev</a>. Über Rückmeldungen zu gut verwendbaren APIs daraus würde ich mich freuen!</p>
        			</li>
        		</ul>
        		<p>Die <a href="/material">Arbeitsblätter und Projektvorlagen</a> dienen zur Unterstützung der selbstgesteuerten Arbeit der Lernenden. </p>
        		<h4>Vierte Stunde: Sicherung und Reflexion</h4>
        		<p>Die abschließende Stunde dient der Sicherung und Reflexion des Gelernten. Anhand konkreter Schülerprogramme und dem zugehörigen Kommunikationsverlauf kann die Netzwerkkommunikation zwischen Client und Servern wiederholt werden. Eine Diskussion der gesellschaftlichen Auswirkungen von Social Bots, insbesondere der möglichen Einflussnahme auf politische Debatten, rundet das Thema ab.</p>
        	</div>
        </div>
    </div>
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Beispielprojekte</h2>
        </div>
	    <div class="tabbed">
				<input checked="checked" id="java" type="radio" name="language" />
				<input id="python" type="radio" name="language" />
				<input id="js" type="radio" name="language" />
			
				<nav>
					<label for="java">Java</label>
					<label for="python">Python</label>
					<label for="js">JavaScript</label>
				</nav>		   

				<figure>
					<div class="javaTab">
								<div class="media-body">
									<p> Hier kann ein <a href="/assets/v4.2.0/material/java/socialbot-examples.zip">Javaprojekt mit Beispielbots</a> heruntergeladen werden. Enthalten sind:</p>
							<ul>
										<li> ein einfacher und ein fortgeschrittener Social Bot im Projekt mit Objekten, </li>
										<li> der einfache Bot nur mit JSON zum Vergleich, </li>
										<li> ein Bot, der mit der Wetter-API von <a href="https://api.openweathermap.org" target="_blank" rel="noopener noreferrer">openweathermap.org</a> kommuniziert und Beiträge passend zum Wetter schreibt. Benötigt (kostenlosen) API-Key von <a href="https://api.openweathermap.org" target="_blank" rel="noopener noreferrer">api.openweathermap.org</a>.
									</ul>
								</div>
					</div>
					<div class="pythonTab">
								<div class="media-body">
								<p> Hier kann ein <a href="/assets/v4.2.0/material/python/socialbot-example.zip">Beispielbot mit Python</a> heruntergeladen werden. Der Bot kann Beiträge posten und liken und mit der Pokemon API von <a href="https://pokeapi.co/" target="_blank" rel="noopener noreferrer">pokeapi.co</a> kommunizieren, um Fakten über Pokemons zu posten.</p>
								
								<p> Die Python Materialien wurden dankenswerterweise von Christopher Frank (E-Mail: <@mailObfuscator.generate coded="eBxr@KU01U0VF.BUF" key="cbUFwYfSyBh8JsVPaW09TK3xvgEDNqil7QzXCjOL4HAodG5etZpRImn2Mk6ur1" />) erstellt, der auch gerne bei Fragen kontaktiert werden kann.
								</div>
					</div>
					<div class="jsTab">
						<div class="media-body">
									<p> Der Einsatz von JavaScript ist ebenfalls möglich, wie der <a href="https://glitch.com/~automatic-glimmer-albacore"  target="_blank" rel="noopener noreferrer">exemplarische JavaScript-Client</a> von <a href="https://wi-wissen.de" target="_blank" rel="noopener noreferrer">Julian Dorn</a> zeigt.</p>
						</div>
					</div>
				</figure>
			</div>
    </div>
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Ausarbeitungen</h2>
        </div>
        <div class="media-body">
        	<p>
        	Dieses Projekt ist im Rahmen einer Zulassungsarbeit am Lehrstuhl für Didaktik der Informatik der Ludwig-Maximilians-Universität München entstanden. 
        	<a href="/assets/v4.2.0/material/zulassungsarbeit.pdf" title="Zulassungsarbeit Social Bots herunterladen" download>Download der Zulassungsarbeit</a>
        	</p>
        	<p>
        	Die Unterrichtseinheit wurde außerdem bei der INFOS 2019 mit dem <a href="https://gi.de/aktuelles/wettbewerbe/unterrichtspreis/" target="_blank" rel="noopener noreferrer">Unterrichtspreis</a> der Gesellschaft für Informatik ausgezeichnet.
        	<a href="/assets/v4.2.0/material/gi_unterrichtspreis.pdf" title="Bewerbung für den GI Unterrichtspreis 2019 herunterladen" download>Download der Ausarbeitung</a></p>
        </div>
    </div>
</@layout.masterTemplate>
