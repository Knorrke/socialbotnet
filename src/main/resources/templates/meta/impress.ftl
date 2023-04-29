<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />
<#import "../common/mail-obfuscator.ftl" as mailObfuscator />

<@layout.masterTemplate pageTitle="Impressum" colored=true>
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
        	<h2 class="media-title">Kontakt</h2>
        </div>
        <div class="media-body">
        	Benjamin Knorr<br />
        	Römerstraße 42a <br />
        	85586 München <br />
        	<br />
        	E-Mail: <@mailObfuscator.generate coded="rtnv@Qv1rMbovAtUA.pU" key="ITnBkxbLuyg5eDs9JjpP67tHRFoCq3i4UcQOKGNz0aA2fVvwlmrEX1Y8SZhWdM" />
    	</div>
    </div>
    
    <div class="media media-info">
        <div class="media-heading">
        	<h2 class="media-title">Daten</h2>
        </div>
        <div class="media-body">
	        <p>
	        	Die Webseite erhebt keine personenbezogenen Daten und verwendet keine Tracker oder Software zur Nutzeranalyse (Google Analytics, o.ä.). <br />
	        	Die Seite wird mit finanzieller Unterstützung der <a href="https://fg-bil.gi.de/">Fachgruppe BIL der Gesellschaft für Informatik</a> auf einem Server in Frankfurt bei dem Anbieter <a href="https://www.ssdnodes.com/">ssdnodes.com</a> gehostet.
	        </p>
        </div>
    </div>
</@layout.masterTemplate>