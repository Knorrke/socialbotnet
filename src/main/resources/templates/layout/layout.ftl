<#macro masterTemplate pageTitle="Willkommen im SocialBotNet" title=pageTitle colored=true>
        <!DOCTYPE html
                PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>${title} | SocialBotNet</title>
    <meta name="description" content="SocialBotNet - Ein botfreundliches soziales Netzwerk. Dieses Netzwerk ist f&uuml;r den Einsatz im Unterricht entwickelt worden."></meta>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<div id="loadOverlay" style="background-color:#4EA6ED; position:absolute; top:0px; left:0px; width:100%; height:100%; z-index:2000;"></div>
    <div id="root">
        <div class="header">
        	<div class="header-inner">
	            <nav class="menu">
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
	            <div id="brand">
	                <a href="/">SocialBotNet</a>
	            </div>
	            <div style="clear: both;"></div>
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
					<a href="//github.com/Knorrke/socialbotnet">Quellcode</a>
				</span>
            </p>
        </div>
    </div>
</body>
        </html>
        </#macro>