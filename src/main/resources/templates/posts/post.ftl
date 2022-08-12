<#ftl output_format="XML">
<#import "../common/fa-icons.ftl" as fa/>
<#macro show post>
    <div id="post-${post.id?c}" class="media colored">
        <div class="media-body">
            <h4 class="media-heading">
                <a href="/user/profile/${post.user.username?url}">
                <img alt="Profilbild von ${post.user.username}" src="${post.user.image}" width="50" style="vertical-align:middle">
                ${post.user.username}
                </a>
                <#if post.wall.username != post.user.username>
                    an <a href="/user/profile/${post.wall.username?url}">
                	${post.wall.username}
                	</a> 
                </#if>
            </h4>
        	${post.message} <br/>
			<small>&mdash; ${post.publishingDate}</small>
			<br/>
			<br/>
			<div class="likes-box">
				<#assign likedByAuthenticatedUser=false>
				<#assign likesShown=(post.likesCount < 5)?then(post.likesCount, 5)>
				<#if authenticatedUser??>
					<#if postsLikedByUser?map(p->p.getId())?seq_contains(post.getId())>
						<form method="POST" action="/unlike" class="likes-count">
							<input type="hidden" name="post" value="${post.id?c}">
							<button class="button action-button colored" type="submit" title="Gefällt mir nicht mehr">
								<@fa.icon fa="heart" class="icon-current" />
								<@fa.icon fa="heart-broken" class="icon-action" />
								<span>${post.likesCount}${(post.likesCount >= 1000)?then("+","")}</span>
							</button>
						</form>
					<#else>
						<form method="POST" action="/like" class="likes-count">
							<input type="hidden" name="post" value="${post.id?c}">
							<button class="button action-button colored" type="submit" title="Gefällt mir">
								<@fa.icon class="icon-current" solid=false fa="heart" />
								<@fa.icon class="icon-action" fa="heart" />
								<span>${post.likesCount}${(post.likesCount >= 1000)?then("+","")}</span>
							</button>
						</form>
					</#if>
				<#else>
					<span class="likes-count"><@fa.icon solid=false fa="heart" /><span>${post.likesCount}${(post.likesCount >= 1000)?then("+","")}</span></span>
				</#if>
				<span class="likes">
					<#if post.likesCount gt likesShown><span class="overlapping"><span class="profile-pic small" data-tooltip="${post.likesCount - likesShown}${(post.likesCount >= 1000)?then("+","")} weitere">...</span></span></#if>
					<#if post.likesCount gt 0>
						<#list post.getRecentLikes()[(likesShown-1)..0] as likingUser>
							 <a class="overlapping" data-tooltip="${likingUser.username}" href="/user/profile/${likingUser.username?url}"><img class="profile-pic small" alt="${likingUser.username} Like" src="${likingUser.image}" style="vertical-align:middle"></a>
						</#list>
					</#if>
				</span>
			</div>
        </div>
    </div>
</#macro>
