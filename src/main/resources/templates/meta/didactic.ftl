<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />

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
        	<h2 class="media-title">Überblick der Didaktik</h2>
        </div>
        <div class="media-body">
        	<i>Coming soon</i>
        </div>
    </div>
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Beispielprojekte</h2>
        </div>
        <div class="media-body">
        	<p> Hier kann ein <a href="/assets/material/socialbot-examples">Projekt mit Beispielbots</a> heruntergeladen werden. Enthalten sind:
        	 <ul>
        	 	<li> ein einfacher und ein fortgeschrittener Social Bot im Projekt mit Objekten, </li>
        	 	<li> der einfache Bot nur mit JSON zum Vergleich , </li>
        	 	<li> ein Bot, der mit der Wetter-API von <a href="https://api.openweathermap.org">openweathermap.org</a> kommuniziert und Beiträge passend zum Wetter schreibt. Benötigt (kostenlosen) API-Key von <a href="https://api.openweathermap.org">api.openweathermap.org</a>.
        	 </ul>
    	 	</p>
        	<p> Der Einsatz von JavaScript ist ebenfalls möglich, wie der <a href="https://glitch.com/~automatic-glimmer-albacore">exemplarische JavaScript-Client</a> von <a href="https://wi-wissen.de">Julian Dorn</a> zeigt.
        </div>
    </div>
</@layout.masterTemplate>