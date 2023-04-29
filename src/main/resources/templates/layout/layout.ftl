<#import "../common/fa-icons.ftl" as fa/>
<#macro masterTemplate pageTitle="Willkommen im SocialBotNet" title=pageTitle colored=true>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
 	<meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title} | SocialBotNet</title>
    <meta name="description" content="SocialBotNet - Ein botfreundliches soziales Netzwerk. Dieses Netzwerk ist für den Einsatz im Unterricht entwickelt worden." />
    <link rel="stylesheet" type="text/css" href="/assets/v4.2.0/css/layout.css">
    <link rel="stylesheet" type="text/css" href="/assets/v4.2.0/css/content.css">
    <link rel="stylesheet" type="text/css" href="/assets/v4.2.0/css/tabs.css">
    <link rel="icon" type="image/png" href="/assets/v4.2.0/images/favicon-32x32.png" sizes="32x32" />
	<link rel="icon" type="image/png" href="/assets/v4.2.0/images/favicon-16x16.png" sizes="16x16" />
	<link rel="preload" href="/assets/v4.2.0/images/favicon-16x16.png" as="image" />
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400&display=swap" rel="stylesheet">
</head>
<body>
    <div id="root">
        <div class="header">
        	<div class="header-inner">
        		<div>
	            	<div id="gi-header">
		            	<a class="prevent-external" href="https://fg-bil.gi.de/">
		            		<div id="gi-fg-bil">
		            			<img width="150" height="50" alt="Logo der Gesellschaft für Informatik" id="gi-image" src="/assets/v4.2.0/images/GI_Logo_links.png">
		            			<span id="gi-fg-bil-title">
		            				Fachgruppe<br>BIL
	            				</span>
	            			</div>
	        			</a>
	    			</div>
		            <nav id="menu-meta">
		            	<ul>
		                	<li>
		                		<a href="/"><img id="header-image" src="/assets/v4.2.0/images/favicon-16x16.png" alt="Logo SocialBotNet.de">SocialBotNet</a>
		                	</li>
		                	<li>
			                	<a href="/material">Materialien</a>
		                	</li>
		                	<li>
			                	<a href="/didaktik">Informationen für Lehrkräfte</a>
		                	</li>
		                	<li>
		                		<a href="/api">API Explorer</a>
		                	</li>
	                	</ul>
		            </nav>
		        </div>
	            <nav id="menu">
	                <ul>
	                    <li>
	                        <a href="/">Startseite</a>
	                    </li>
	                    <#if authenticatedUser??>
		                    <li>
		                        <a href="/user/profile/${authenticatedUser.username?url}">Eigenes Profil</a>
		                    </li>
		                    <li>
		                        <a href="/logout">Abmelden</a>
		                    </li>
		                <#else>
		                    <li>
		                        <a href="/registrieren">Registrieren</a>
		                    </li>
		                    <li>
		                        <a href="/login"><@fa.icon fa="user" />Anmelden</a>
		                    </li>
		                </#if>
		            </ul>
		        </nav>
            </div>
		</div>
		<div class="pageTitle">
			<h1>${pageTitle}</h1>
		</div>
        <div class="main <#if colored==true>colored</#if>">
            <#nested />
        </div>
        <div class="footer">
            <p>
                SocialBotNet &mdash; Ein botfreundliches soziales Netzwerk
    			<span style="float: right;">
    				<a href="/impressum">Impressum</a>
					<a href="//github.com/Knorrke/socialbotnet" target="_blank" rel="noopener noreferrer" >Quellcode</a>
				</span>
            </p>
        </div>
    </div>
</body>
        </html>
        </#macro>