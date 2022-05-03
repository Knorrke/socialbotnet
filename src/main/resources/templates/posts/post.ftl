<#ftl output_format="XML">
<#macro show post>
    <div id="post-${post.id?c}" class="media colored">
        <div class="media-body">
            <h4 class="media-heading">
                <a href="/user/profile/${post.user.username}">
                <img alt="Profilbild von ${post.user.username}" src="${post.user.image}" width="50" style="vertical-align:middle">
                ${post.user.username}
                </a>
                <#if post.wall.username != post.user.username>
                    an <a href="/user/profile/${post.wall.username}">
                	${post.wall.username}
                	</a> 
                </#if>
            </h4>
        	${post.message} <br/>
			<small>&mdash; ${post.publishingDate}</small>
			<br/>
			<br/>
			<#assign likedByAuthenticatedUser=false>
			<#assign likesShown=(post.likesCount < 3)?then(post.likesCount, 3)>
			<#if authenticatedUser??>
				<#if postsLikedByUser?map(p->p.getId())?seq_contains(post.getId())>
					<a href="/unlike/${post.id?c}" style="margin-right:10px">Gef&auml;llt mir nicht mehr</a>
				<#else>
					<a href="/like/${post.id?c}" style="margin-right:10px">Gef&auml;llt mir</a>
				</#if>
			</#if>
			<#if post.likesCount gt 0>
				<#list post.recentLikes[0..(likesShown-1)] as likingUser>
					<#if likingUser?index ==0>Gef&auml;llt: <#else>,</#if>
					 <a href="/user/profile/${likingUser.username}">${likingUser.username}</a>
				</#list>
			</#if>
			<#if post.likesCount gt likesShown> und ${post.likesCount - likesShown} weiteren.</#if>
        </div>
    </div>
</#macro>
