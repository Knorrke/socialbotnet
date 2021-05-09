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
        	Woferlstraße 26 <br />
        	81737 München <br />
        	<br />
        	E-Mail: <@mailObfuscator.generate coded="XHqii.z@eVh.xC" key="jXZzDcVKupIrCatAUP7dsR9fYi6wTovE4x2JNS1lyQOqGnghMkBbWFmL850He3" />
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