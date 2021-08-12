<#ftl output_format="XML">
<#macro generate coded key>
<span id="${coded}"></span>
<script>
<!--
{
	var el = document.getElementById('${coded}')
	el.innerHTML = 'Klicken, um E-Mail anzuzeigen'
	el.classList.add('obfuscated')
	el.onclick = () => {
	// Email obfuscator script 2.1 by Tim Williams, University of Arizona
	// Random encryption key feature coded by Andrew Moulden
	// This code is freeware provided these four comment lines remain intact
	// A wizard to generate this code is at http://www.jottings.com/obfuscator/
		{ coded = "${coded}"
		  key = "${key}"
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
		  el.innerHTML = "<a href='mailto:"+link+"'>"+link+"</a>"
		  el.classList.remove('obfuscated')
		}
	}
}
//-->
</script><noscript>Sorry, you need Javascript on to email me.</noscript>
</#macro>
