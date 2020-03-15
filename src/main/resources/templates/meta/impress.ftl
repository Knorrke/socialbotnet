<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />

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
        	Schopenhauerstraße 66 <br />
        	80807 München <br />
        	<br />
        	E-Mail: <script type="text/javascript" language="javascript">
			<!--
			// Email obfuscator script 2.1 by Tim Williams, University of Arizona
			// Random encryption key feature coded by Andrew Moulden
			// This code is freeware provided these four comment lines remain intact
			// A wizard to generate this code is at http://www.jottings.com/obfuscator/
			{ coded = "XHqii.z@eVh.xC"
			  key = "jXZzDcVKupIrCatAUP7dsR9fYi6wTovE4x2JNS1lyQOqGnghMkBbWFmL850He3"
			  shift=coded.length
			  link=""
			  for (i=0; i<coded.length; i++) {
			    if (key.indexOf(coded.charAt(i))==-1) {
			      ltr = coded.charAt(i)
			      link += (ltr)
			    }
			    else {     
			      ltr = (key.indexOf(coded.charAt(i))-shift+key.length) % key.length
			      link += (key.charAt(ltr))
			    }
			  }
			document.write("<a href='mailto:"+link+"'>"+link+"</a>")
			}
			//-->
			</script><noscript>Bitte aktiviere Javascript um die E-Mail-Adresse zu sehen.</noscript>
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