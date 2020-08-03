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
        	<h2 class="media-title">Ausarbeitungen</h2>
        </div>
        <div class="media-body">
        	<p>
        	Dieses Projekt ist im Rahmen einer Zulassungsarbeit am Lehrstuhl f&uuml;r Didaktik der Informatik der Ludwig-Maximilians-Universit&auml;t M&uuml;nchen entstanden. 
        	<a href="/assets/material/zulassungsarbeit.pdf" title="Zulassungsarbeit Social Bots herunterladen" download>Download der Zulassugnsarbeit</a>
        	</p>
        	<p>
        	Die Unterrichtseinheit wurde außerdem bei der INFOS 2019 mit dem <a href="https://gi.de/aktuelles/wettbewerbe/unterrichtspreis/">Unterrichtspreis</a> der Gesellschaft für Informatik ausgezeichnet.
        	<a href="/assets/material/gi_unterrichtspreis.pdf" title="Bewerbung für den GI Unterrichtspreis 2019 herunterladen" download>Download der Ausarbeitung</a></p>
        </div>
    </div>
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Beispielprojekte</h2>
        </div>
        <div class="media-body">
        	<h3>Java</h3>
        	<p> Hier kann ein <a href="/assets/material/java/socialbot-examples.zip">Javaprojekt mit Beispielbots</a> heruntergeladen werden. Enthalten sind:
        	 <ul>
        	 	<li> ein einfacher und ein fortgeschrittener Social Bot im Projekt mit Objekten, </li>
        	 	<li> der einfache Bot nur mit JSON zum Vergleich , </li>
        	 	<li> ein Bot, der mit der Wetter-API von <a href="https://api.openweathermap.org">openweathermap.org</a> kommuniziert und Beiträge passend zum Wetter schreibt. Benötigt (kostenlosen) API-Key von <a href="https://api.openweathermap.org">api.openweathermap.org</a>.
        	 </ul>
    	 	</p>
    	 	<h3>Python</h3>
    	 	<p> Hier kann ein <a href="/assets/material/python/socialbot-example.zip">Beispielbot mit Python</a> heruntergeladen werden. Der Bot kann Beiträge posten und liken und mit der Pokemon API von <a href="https://pokeapi.co/">pokeapi.co</a> kommunizieren, um Fakten über Pokemons zu posten.</p>
    	 	
    	 	<p> Die Python Materialien wurden dankenswerterweise von Christopher Frank (E-Mail: <@mailObfuscator.generate coded="eBxr@KU01U0VF.BUF" key="cbUFwYfSyBh8JsVPaW09TK3xvgEDNqil7QzXCjOL4HAodG5etZpRImn2Mk6ur1" />) erstellt, der auch gerne bei Fragen kontaktiert werden kann.
    	 	<h3>JavaScript</h3>
        	<p> Der Einsatz von JavaScript ist ebenfalls möglich, wie der <a href="https://glitch.com/~automatic-glimmer-albacore">exemplarische JavaScript-Client</a> von <a href="https://wi-wissen.de">Julian Dorn</a> zeigt.
        </div>
    </div>    
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Überblick der Didaktik</h2>
        </div>
        <div class="media-body">
        	<i>Coming soon</i>
        </div>
    </div>
</@layout.masterTemplate>