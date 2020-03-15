<#macro masterTemplate pageTitle="Willkommen im SocialBotNet" title=pageTitle colored=true>
        <!DOCTYPE html
                PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 	<meta charset="UTF-8" />
    <title>${title} | SocialBotNet</title>
    <meta name="description" content="SocialBotNet - Ein botfreundliches soziales Netzwerk. Dieses Netzwerk ist f&uuml;r den Einsatz im Unterricht entwickelt worden." />
    <link rel="stylesheet" type="text/css" href="/assets/css/style.css">
    <link rel="icon" type="image/png" href="/assets/images/favicon-32x32.png" sizes="32x32" />
	<link rel="icon" type="image/png" href="/assets/images/favicon-16x16.png" sizes="16x16" />
</head>
<body>
	<div id="loadOverlay" style="background-color:#4EA6ED; position:absolute; top:0px; left:0px; width:100%; height:100%; z-index:2000;"></div>
    <div id="root">
        <div class="header">
        	<div class="header-inner">
        		<div>
	            	<div id="gi-header">
		            	<a href="https://fg-bil.gi.de/">
		            		<div id="gi-fg-bil">
		            			<img id="gi-image" src="https://gi.de/fileadmin/GI/Allgemein/Logos/GI_Logo_links.png">
		            			<span id="gi-fg-bil-title">
		            				Fachgruppe<br>BIL
	            				</span>
	            			</div>
	        			</a>
	    			</div>
		            <nav id="menu-meta">
		            	<ul>
		                	<li>
		                		<a href="/"><img id="header-image" src="/assets/images/favicon-16x16.png">SocialBotNet</a>
		                	</li>
		                	<li>
			                	<a href="/material">Materialien</a>
		                	</li>
		                	<li>
			                	<a href="/didaktik">Informationen für Lehrkräfte</a>
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
		                        <a href="/user/profile/${authenticatedUser.username}">Eigenes Profil</a>
		                    </li>
		                    <li>
		                        <a href="/logout">Abmelden</a>
		                    </li>
		                <#else>
		                    <li>
		                        <a href="/registrieren">Registrieren</a>
		                    </li>
		                    <li>
		                        <a href="/login">Anmelden</a>
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
					<a href="//github.com/Knorrke/socialbotnet">Quellcode</a>
				</span>
            </p>
        </div>
    </div>
</body>
        </html>
        </#macro>