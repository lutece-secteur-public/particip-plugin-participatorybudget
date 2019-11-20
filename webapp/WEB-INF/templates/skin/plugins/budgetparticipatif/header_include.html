<#if 1 = 0>
<div id="banner" class="${header_color} hidden-xs">
  <div class="row">
    <#if infos_user??>
      <input id="defaultArrond" type="hidden"  value="${infos_user.arrondissement!}">
    <#else>
      <input id="defaultArrond" type="hidden"  value="">
    </#if>
    <#if ( user_votes_paris?has_content || user_votes_arrdt?has_content )>
	    <#assign voteTotal= user_votes_paris + user_votes_arrdt>
        <div id="user-feedback" class="bg-color2">
		    <div id="favorite-user" class="text-center">
          <a href="jsp/site/Portal.jsp?page=myFavourites">
            <p>${numberFavouritesProject!}</p>
            <small>favoris</small>
          </a>
        </div>
        <!-- FIXME : Avoir le nombre max de vote par user / Paris et /Arrdt -->
		<!-- Bug Turn Around by JPO, 2016/06/20 -->
		<#if infos_user.arrondissement?? && infos_user.arrondissement?is_number >
			<#assign nArrdt=infos_user.arrondissement?number>
		<#else>
			<#assign nArrdt=74999>
		</#if>
		
		<#if nArrdt==75001>
		  <#assign arrdt="1">
		<#else>
		  <#assign arrdt=(nArrdt - 75000)?string >
		</#if>

        <#if 1=0>
		<!-- JPO : Avant VOTE -->
			<div id="vote-user" class="text-center" data-container="body" data-html="true" data-toggle="popover" data-template='<div class="popover bg-color2" role="tooltip"><div class="arrow"></div><div class="popover-content"></div></div>' data-trigger="hover" data-placement="left" data-content="<span class='nb-user-votes'>Rendez-vous du 16 septembre<br>au 02 octobre pour voter !">
			  <a>
				<p class="nb-user-votes">${voteTotal!}</p>
				<small>votes</small>
				<span class="hidden" id="vote-pop-content">
				  Rendez-vous du 16 septembre<br>au 02 octobre pour voter !
				</span>
			  </a>
			</div>
		<#else>
			<!-- JPO : Pour le VOTE -->
			<div id="vote-user" class="text-center" data-container="body" data-html="true" data-toggle="popover" data-template='<div class="popover bg-color2" role="tooltip"><div class="arrow"></div><div class="popover-content"></div></div>' data-trigger="hover" data-placement="left" data-content="<span class='nb-user-votes'><#if infos_user?? ><strong>Votes effectués :</strong><br>- <strong>${user_votes_paris!}</strong></span> sur ${maxVotesToutParis!} pour Tout Paris <br>- <strong>${user_votes_arrdt!}</strong> sur ${maxVotesArrond!} pour le ${arrdt!}<sup><#if arrdt="1">er<#else>e</#if></sup> arrdt <#else>#dskey{ideation.site_property.template.userInfoPopVotes.textblock}</#if>">
			  <a href="jsp/site/Portal.jsp?page=mesVotes&view=myVotes">
				<p class="nb-user-votes">${voteTotal!}</p>
				<small>votes</small>
				<span class="hidden" id="vote-pop-content">
				  <#if infos_user?? ><strong>Votes effectués :</strong><br>- <span id="vote-user-paris"><strong>${user_votes_paris!}</strong></span> sur ${maxVotesToutParis!} pour Tout Paris<br>- <span id="vote-user-arrdt"><strong>${user_votes_arrdt!}</strong></span> sur ${maxVotesArrond!} pour le ${arrdt!}<sup><#if arrdt="1">er<#else>e</#if></sup> arrdt <#else>#dskey{ideation.site_property.template.userInfoPopVotes.textblock}</#if>
				</span>
			  </a>
			</div>
        </#if>

		</div>
		</#if>
  </div>
</div>
<!-- Responsive include -->
<div class="row visible-xs hidden-print ${header_color}" id="banner-xs">
	<div id="banner-xs-content" class="col-xs-12 col-main">
	</div>
  <div id="banner-main-arrow-xs" class="col-xs-12">
	</div>
	<div id="banner-mesvotes-xs" class="col-xs-12">
	  <#if user_votes_paris?has_content || user_votes_arrdt?has_content>
      <div class="row">
        <div class="col-xs-offset-1 col-xs-5">
		      <p class="text-center bg-color2">
            <a href="jsp/site/Portal.jsp?page=myFavourites">
            <i class="fa fa-star"></i> FAVORIS : <span class="nb-favouris_project">${numberFavouritesProject!}</span>
          </a>
          </p>
        </div>
		
        <#if 1=0>
			<!-- JPO : Avant VOTE -->
			<div class="col-xs-5" data-container="body" data-html="true" data-toggle="popover" data-template='<div class="popover bg-color2" role="tooltip"><div class="arrow"></div><div class="popover-content"></div></div>' data-trigger="hover" data-placement="bottom" data-content="Rendez-vous du 16 septembre<br>au 02 octobre pour voter !">
			  <p class="text-center bg-color2">
				<a>
				  <i class="fa fa-check-square" id="vote-check"></i> VOTES : <span class="nb-user-votes">${voteTotal!}</span>
				</a>
			  </p>
			</div>
       <#else>
			<!-- JPO : Pour le VOTE -->
			<div class="col-xs-5" data-container="body" data-html="true" data-toggle="popover" data-template='<div class="popover bg-color2" role="tooltip"><div class="arrow"></div><div class="popover-content"></div></div>' data-trigger="hover" data-placement="bottom" data-content="<strong>Votes effectués :</strong><br><#if infos_user?? >- <strong>${user_votes_paris!}</strong> sur ${maxVotesToutParis!} pour Tout Paris<br>- <strong>${user_votes_arrdt!}</strong> sur ${maxVotesArrond!} pour le ${arrdt!}<sup><#if arrdt="1">er<#else>e</#if></sup> arrdt <#else>#dskey{ideation.site_property.template.userInfoPopVotes.textblock}</#if>">
			  <p class="text-center bg-color2">
				<a href="jsp/site/Portal.jsp?page=mesVotes&view=myVotes">
				  <i class="fa fa-check-square" id="vote-check"></i> VOTES : <span class="nb-user-votes">${voteTotal!}</span>
				</a>
			  </p>
			</div>
        </#if>

		</div>
	  <#else>
	  </#if>
	</div>
</div>
</#if>