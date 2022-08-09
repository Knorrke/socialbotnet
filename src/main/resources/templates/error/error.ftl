<#ftl output_format="XML">
<#import "../layout/layout.ftl" as layout />
<#assign pageTitle>Fehler &mdash; ${error.status}: ${defaultMessage}</#assign>

<@layout.masterTemplate title="${error.status} &mdash; ${defaultMessage}" colored=false pageTitle=pageTitle>
    	<p>Bei der Bearbeitung der Anfrage ist leider ein Fehler aufgetreten:</p>
		<div class="warning">
			<p style="padding: 15px 0;">${error.message}</p>
    	</div>
</@layout.masterTemplate>