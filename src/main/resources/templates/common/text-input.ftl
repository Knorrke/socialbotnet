<#macro show placeholder="Schreibe eine Nachricht...">
<span class="text-element">
    <input type="text" name="message" class="form-control" required size="80" placeholder="${placeholder}" />
    <button class="img-button" type="submit">
    	<img src="/assets/images/senden.svg" width="auto" height="20px" />
	</button>
</span>
</#macro>
