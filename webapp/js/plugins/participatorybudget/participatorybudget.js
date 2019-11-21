var dialog, idResourceToVote, resourceTypeToVote,voteValue,baseUrl,arrondissement,title,thematique,arrondUser;
//displayLoginPagePopup
function displayLoginBp(backUrl) {
	displayCreateAccountPage(backUrl);
}

function sendValidationMail(callBackFunctionOk,callBackFunctionError,callBackFunctionUserNotSigned)
{
	$.ajax({
		url : baseUrl + 'jsp/site/plugins/participatorybudget/DoSendValidationMail.jsp',
		type: 'GET',
		dataType: "json",
		data: {},
			async: false,
	    cache:false,
      success:function(data) {
      	if ( data.status == 'OK'){
	      		if ( data.result ){
	        		callBackFunctionOk();
						}
						else
						{
							callBackFunctionError();
						}
    		}
        else if(data.errorCode =='USER_NOT_SIGNED')
    		{
        		callBackFunctionUserNotSigned();
    		}
        else if(data.errorCode ==='JSON_ERROR_CHECKED_ARRONDISSEMENT'){
					popup();
        }
		},
      error: function(jqXHR, textStatus, errorThrown) { }
	});
}

function displayModalPopup(callBackFunction)
{
	$.ajax({
		url : baseUrl + "jsp/site/plugins/participatorybudget/GetMyInfosPanelForAjax.jsp",
        type: "GET",
        async: false,
    	cache:false,
    	success:function(data) {
        	if ( data  == '' )
        	{
        		// If we received no data, then the user has not logged in and we let the regular process log him in
        		doVote( );
        	}
        	else
        	{
        		/*$("#budgetparticipatifmodelpopup").html(data);*/
        		$("#mesinfos-modal .modal-body").html(data);
        		$('#forpopup_budgetparticipatif-profile-button').on( "click", function( event ) {
        			event.preventDefault( );
					var hasEmpty = false;
					$("#forpopup_budgetparticipatif-profile-form input.check-nonempty").each( function() {
						if(!hasEmpty && ($(this).val()=="" || ($(this).attr("type")=="checkbox" && !$(this).prop("checked")) )) {
							hasEmpty = true;
							$("#forpopup_message_error_js").show();
						}
					});
					if (hasEmpty) {
						$('#mesinfos-modal').animate({ scrollTop: 0 }, 'slow');
						return false;
					}
					$('#mesinfos-modal').modal('hide');
					forpopup_openPopinSavePersonalInfo( function(result) {
						$("#savePersonalData").val(result);
						$('#mesinfos-modal').modal('show');
						doSaveUserInfos( callBackFunction );
					}, function() {
						if( ! $('#mesinfos-modal').hasClass('in') ) {
							$('#mesinfos-modal').modal('show');
						}
						$(document.body).addClass('modal-open');
					});
        		});
        		  $("#send_validation_mail").click(function () {
        		    	sendValidationMail( function(){$('#mesinfos-modal').modal('hide'); },function(){displayModalPopup(function(){checkValidAccountAfterAuthentication(); })}, function() { });
        		        return false;
        		    });
			 	/*suggestpoi*/
			    initMyInfoAddressAutoComplete();
			    $("#budget-birthdate").inputmask('dd/mm/yyyy',{ "placeholder": "jj/mm/aaaa" });
        			/*dialog.dialog( "open" );*/
				$('#mesinfos-modal').modal('show');
				var tmpInput = $("#mesinfos-modal").find("input[id='budget-address']");
				if(tmpInput.val()) {
				  $("#mesinfos-modal").animate({scrollTop: $("#mesinfos-modal").height()}, 'slow', 'swing', function() {
					  tmpInput.focus();
					  tmpInput.autocomplete("search");
				  });
				}
				/*display popin phase vote on close*/
				if( Cookies.get('vote_popin') != 'shown' ) {
					$('#mesinfos-modal').on('hidden.bs.modal', function () {
							$('#modalPhaseVote').modal('show');
							Cookies.set('vote_popin', 'shown', { expires: 1 });
					});
				}
        	}
        },
        error: function(jqXHR, textStatus, errorThrown) {
        }
	});
}

function doSaveUserInfos(callBackFunction)
{
	$("#budgetparticipatif-image-loading").toggle();
		var userInfos = {
		lastname: $("#budget-lastname").val(),
		firstname: $("#budget-firstname").val(),
    address:$("#budget-address").val(),
	  arrondissement:$("#arrondissement").val(),
	 civility:$("#budget-civility-mme").is(":checked")?$("#budget-civility-mme").val():($("#budget-civility-mr").is(":checked")?$("#budget-civility-mr").val():($("#budget-civility-npr").is(":checked")?$("#budget-civility-npr").val():'')),
     postal_code:$("#postal_code").val(),
    nickname:$("#budget-nickname").val(),
    birthdate:$("#budget-birthdate").val(),
    iliveinparis:$("#budget-iliveinparis").is(":checked")?$("#budget-iliveinparis").val():'',
    stayconnected:$("#budget-stayconnected").is(":checked")?$("#budget-stayconnected").val():'',
    geojson:$("#geojson").val(),
    sendaccountvalidation:$("#budget-sendaccountvalidation").is(":checked")?$("#budget-sendaccountvalidation").val():'',
    j_captcha_response:$("#j_captcha_response").length > 0?$("#j_captcha_response").val():'',
    token: $("#token").val(),
	savePersonalData: $("#savePersonalData").val(),
  };
	var urlDoSave = baseUrl + "jsp/site/plugins/participatorybudget/DoSaveMyInfosForAjax.jsp";
	$.ajax({
			url : urlDoSave,
    	type: "POST",
    	//dataType: "json",
    	async: false,
    	cache:false,
    	data: userInfos,
    	success:function(data) {
    	callBackFunction();
    },
      error: function(jqXHR, textStatus, errorThrown) {
    }
	});
}

function doVote( idResource )
{
	$.ajax({
		url : baseUrl + "jsp/site/plugins/participatorybudget/DoVote.jsp?idExtendableResource=" + encodeURIComponent(idResourceToVote) + "&extendableResourceType=" + encodeURIComponent(resourceTypeToVote) + "&thematique=" + encodeURIComponent(thematique) + "&localisation=" +encodeURIComponent(arrondissement) + "&title=" + encodeURIComponent(title),
		type: 'GET',
		dataType: "json",
		data: {},
		async: false,
	  cache:false,
    success:function(data) {
		$('.modal-title').html("");
		$('.modal-footer').html("");
		if ( data.status == 	'OK' )
			{
    		var divVote = $( "#divVote" + resourceTypeToVote + idResourceToVote );
      		
      		divVote.html(data.result);
			
			loadVotes ( );
			
			if(arrondissement == "Tout Paris"){
				setVotesParis( true );
			}else{
				setVotesArrdt( true );
			}
			$('#mesinfos-modal').modal('hide');
        	$(".budget-cancel-vote-button").each( function() {
        		this.addEventListener("click",doNotExitPage);
        	});
    		}
        	else if(data.errorCode == 'USER_CAN_NOT_VOTE')
        	{
					$('#processing-modal').modal();
					
        	/*alert("vous ne pouvez pas voter ")*/
        }
	    	else if(data.errorCode =='JSON_ERROR_ALREADY_VOTED_ARRONDISSEMENT')
        {
					$('.modal-title').append('Vous avez atteint le nombre maximal de votes possibles pour des projets d\'arrondissement. Vous avez encore la possibilité de voter pour des projets "Tout Paris"');
			 		$('.modal-footer').append('<p class="text-center"><a class="btn btn-idee" href="jsp/site/Portal.jsp?page=search-solr&conf=projects_mdp&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:Tout Paris" class="btn btn-std" onClick="voteArrond();">Voter pour des projets "Tout Paris"</a></p>');
			 		$('#myModal').modal('toggle');
        }
		    else if(data.errorCode =='JSON_ERROR_ALREADY_VOTED_TOUT_PARIS')
        {
					$('.modal-title').append('Vous avez atteint le nombre maximal de votes possibles pour des projets "Tout Paris". Vous avez encore la possibilité de voter pour des projets d\'arrondissement.');
			 		$('.modal-footer').append('<p class="text-center"><a class="btn btn-idee" href="jsp/site/Portal.jsp?page=search-solr&conf=projects_mdp&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:' +arrondUser+ '">Voter pour des projets d\'arrondissement</a></p>');
			 		$('#myModal').modal('toggle');
        }
		   	else if(data.errorCode == "JSON_ERROR_VOTE_USER_ARROND")
				{
				  $('.modal-title').append("Il ne vous est pas possible de voter pour ce projet, car celui-ci n'est pas situé dans votre arrondissement de vote. Si vous n'avez pas validé définitivement vos votes, vous pouvez modifier votre arrondissement de vote au sein de votre profil, accessible dans le menu 'Mon compte'").css("text-align","center");
			 		$('.modal-footer').append('<a class="btn btn-idee" href="javascript:location.reload();">Ok</a>').css("text-align","center");
			 		$('#myModal').modal('toggle');
				}
				else if(data.errorCode == "ERROR_CODE_USER_VOTED_MAX")
				{
				   $('.modal-title').append('Vous avez atteint le nombre maximal de votes possibles pour des projets "Tout Paris" et d\'arrondissement.<br><br>Pour valider vos votes, cliquez sur la barre verte "Mes votes", puis sur le bouton "Valider mes votes".');
				   $('.modal-footer').append('<p class="text-center"><button type="button" class="btn btn-idee" data-dismiss="modal">Fermer</button></p>');
			 	   $('#myModal').modal('toggle');
		   	}
				else if(data.errorCode == "USER_ALREADY_VOTED")
				{
			  	$('.modal-title').append("Vous avez déja voté sur ce projet");
			   	$('.modal-footer').append('<a class="btn btn-idee" href="jsp/site/Portal.jsp?page=mesVotes&view=myVotes">Voir mes votes</a>').css("text-align","center");
		 	   	$('#myModal').modal('toggle');
		   	}
        else
				{
					window.location = data.errorCode;
				}
			},
    	error: function(jqXHR, textStatus, errorThrown) {
    	}
	});
}

function doCancelVote( idResource, resourceType )
{
	idResourceToVote = idResource;
	resourceTypeToVote = resourceType;
	arrondissement= $('#localisation_'+idResource).val();
	$.ajax({
		url : baseUrl + "jsp/site/plugins/participatorybudget/DoCancelVote.jsp?idExtendableResource=" + idResourceToVote + "&extendableResourceType=" + resourceTypeToVote,

		type: 'GET',
		dataType: "json",
		data: {},
		async: false,
	    cache:false,
        success:function(data) {
        	if ( data.status == 'OK' )
    		{
        var divVote = $( "#divVote" + resourceTypeToVote + idResourceToVote );
				if(arrondissement == "Tout Paris"){
					setVotesParis( false );
				}else{
					setVotesArrdt( false );
				}
					      		
	      		divVote.html(data.result);
	      		
	      		loadVotes ( );
	      		
        $(".budget-btn-do-vote").each( function() {
        	this.addEventListener("click",doNotExitPage);
        });
        }
      	else
				{
					window.location = data.errorCode;
				}
			},
        error: function(jqXHR, textStatus, errorThrown) {
      }
	});
}

function setVotesParis( inc ){
	var voteParisUser = parseInt($("#vote-user-paris").text());
	( inc == true ) ? voteParisUser++ : voteParisUser--;
	$("#vote-user-paris").text(voteParisUser);
	$(".nb-user-votes").each(function( index ) {
		var spanNbVote = $(".nb-user-votes");
		var nBuserVote=parseInt($(this).text());
		( inc == true ) ? nBuserVote++ : nBuserVote--;
		nBuserVote=(  nBuserVote > 0 ) ? nBuserVote : 0;
		$(this).text( nBuserVote);
		var m=$("#vote-pop-content").html()
		$("#vote-user").attr( "data-content", m );
	});

}

function setVotesArrdt( inc ){
	var voteArrUser = parseInt($("#vote-user-arrdt").text());
	( inc == true ) ? voteArrUser++ : voteArrUser--;
	$("#vote-user-arrdt").text( voteArrUser );
	$(".nb-user-votes").each(function( index ) {
		var spanNbVote = $(".nb-user-votes");
		var nBuserVote=parseInt($(this).text());
		( inc == true ) ? nBuserVote++ : nBuserVote--;
		nBuserVote=(  nBuserVote > 0 ) ? nBuserVote : 0;
		$(this).text( nBuserVote);
		var m=$("#vote-pop-content").html()
		$("#vote-user").attr( "data-content", m );
		
	});
}

function checkIfUserIsValid(idResource, resourceType, vote)
{
	idResourceToVote = idResource;
	resourceTypeToVote = resourceType;
	voteValue = vote;
	arrondissement= $('#localisation_'+idResource).val();
	title= $('#title_'+idResource).val(),
	thematique= $('#thematique_'+idResource).val(),
	arrondUser= $("#arrondUser").val(),
	displayLogin=false;
	isUserIsValid( function(){doVote(  );},function(){displayModalPopup( function(){checkIfUserIsValid(idResource, resourceType, vote);})}, function() { displayLogin=true; })
	if(displayLogin == true)
	{
		displayLoginBp();
	}
	return false;
}

function isUserIsValid(callBackFunctionOk,callBackFunctionError,callBackFunctionUserNotSigned)
{$.ajax({
		url : baseUrl + 'jsp/site/plugins/participatorybudget/IsUserValid.jsp',

		type: 'GET',
		dataType: "json",
		data: {},
		async: false,
	  cache:false,
    success:function(data) {
    	if ( data.status == 'OK' )
    	{
	    	if ( data.result )
				{
	    		callBackFunctionOk();
				}
	    	else
				{
					callBackFunctionError();
				}
    	}
      	else if(data.errorCode =='USER_NOT_SIGNED')
    	{
		callBackFunctionUserNotSigned();
    	}
	else if(data.errorCode == 'ACCOUNT_NOT_VERIFIED'){
		popupAccountNotVerified();
	}
      else if(data.errorCode ==='JSON_ERROR_CHECKED_ARRONDISSEMENT'){
				popup();
      }
		}
		,
    error: function(jqXHR, textStatus, errorThrown) {
    }
	});
}

function doNotExitPage( event )
{
	event.preventDefault();
}

$(function() {
	baseUrl = document.getElementsByTagName('base')[0].href;
	dialog = $( "#budgetparticipatifmodelpopup" ).dialog({
		 autoOpen: false,
		/* height: 770, width: 600, resizable:true,	*/
		 modal: true,
		 });
	dialog.find( "form" ).on( "submit", function( event ) {
  	event.preventDefault();
  	doSaveUserInfos();
	});
	$(".budget-btn-do-vote").each( function() {
		this.addEventListener("click",doNotExitPage);
	});
	$(".budget-cancel-vote-button").each( function() {
		this.addEventListener("click",doNotExitPage);
	});
});

function popup()
{
 $('.modal-title').html("");
 $('.modal-footer').html("");
 $('.modal-title').append("Vous souhaitez voter pour des projets qui ne sont pas dans votre arrondissement de lieu de vie ou de travail enregistré dans votre profil.");
 $('.modal-footer').append("<a class='btn btn-idee' onClick='getInfoUser();'>J'ANNULE MES VOTES ET JE MODIFIE MON ARRONDISSEMENT</a> <a class='btn btn-std' data-dismiss='modal'>JE CONSERVE MON ARRONDISSEMENT</a>");
 $('#myModal').modal('toggle');
};

function popupAccountNotVerified()
{
 $('.modal-title').html("");
 $('.modal-footer').html("");
 $('.modal-title').append("Pour utiliser le site du Budget Participatif, merci de valider votre compte en cliquant sur le lien qui vous a &eacute;t&eacute; envoy&eacute; par mail.");
 $('.modal-footer').append("<a class='btn btn-idee' onClick='reSendValidationMail();'>Me renvoyer le mail de validation de compte</a>").css("text-align","center");
 $('#myModal').modal('toggle');
};

function reSendValidationMail(){
	sendValidationMail( function(){$('#mesinfos-modal').modal('hide'); },function(){displayModalPopup(function(){checkValidAccountAfterAuthentication(); })}, function() { });
	$('#myModal').modal('hide');
};

function getInfoUser(){
$.ajax({
	url : baseUrl + 'jsp/site/plugins/participatorybudget/DOCancelAllVoteUser.jsp?extendableResourceType=' + resourceTypeToVote,
	type: 'GET',
	//dataType: "json",
	data: {},
	async: false,
  cache:false,
  success:function(data) {
		var nbVotes= 0;
		$(".nb-user-votes_arrdt").text(nbVotes);
		$('#myModal').modal('hide');
  	$("#mesinfos-modal .modal-body").html(data);
  	$('#budgetparticipatif-profile-button').on( "click", function( event ) {
  		event.preventDefault( );
  		doSaveUserInfos($('#mesinfos-modal').modal('hide') );
  	});
  	/*dialog.dialog( "open" );*/
		$('#mesinfos-modal').modal('show');
	},
		error: function(jqXHR, textStatus, errorThrown) {
			/*dialog.dialog( "open" );*/
		}
	});
};

function valideVotes(user_arrondissement, maxVotesArrdt, maxVotesParis, validate){
	$.ajax({
		url : baseUrl + 'jsp/site/plugins/participatorybudget/DoValideVote.jsp',
		type: 'POST',
		dataType: "json",
		data: {},
		async: false,
		cache:false,
		success:function(data) {
		if(data.status == "OK"){
			// Reset modal content
			 $('#modalConfirmVoteContent').html("");
			 $('#modalConfirmVoteButtonAdd').html("");
			 $('#modalConfirmVoteButtonValid').html("");

			 if( data.result.nbTotVotes == 0 ){
				 	$('#vote_follow_form').hide();
					$('#modalConfirmVoteContent').append('<p class="main">Vous devez avoir effectué au moins un vote, sur un projet tout&nbsp;Paris ou de votre arrondissement de vote, pour valider vos votes</p>');
					$('#modalConfirmVoteButtonAdd').append("<a class='btn btn-12rem btn-white-on-green' href='jsp/site/Portal.jsp?page=solrProjectSearch&conf=projects_mdp&sort_name=code_projet_long&sort_order=asc' onClick='voteArrond();'>J'ajoute des votes à ma sélection</a>").css("text-align","center");
					$('#modalConfirmVote').modal('toggle');
			
			 } else if( data.result.totVotesArrondissement == maxVotesArrdt && data.result.totVotesToutParis == maxVotesParis ){
					$('#modalConfirmVoteContent').append("<p class='main'>Vous avez vot&eacute; pour " + data.result.totVotesToutParis + " projet(s) tout Paris,<br>et "+ data.result.totVotesArrondissement + " projet(s) pour votre arrondissement. Lorsque vous aurez validé vos votes, il ne vous sera plus possible de les modifier ou d'en ajouter.</p>");
	 				$('#modalConfirmVoteButtonValid').append("<span class='btn btn-18rem btn-black-on-white' onclick='confirmValidateVote();'>Je valide mes votes</span>").css("text-align","center");
	 				$('#modalConfirmVote').modal('toggle');
			
			 } else if( data.result.totVotesArrondissement == maxVotesArrdt && data.result.totVotesToutParis < maxVotesParis ){
					$('#modalConfirmVoteContent').append("<p class='main'>Vous avez vot&eacute; pour " + data.result.totVotesToutParis + " projet(s) tout Paris,<br>et "+ data.result.totVotesArrondissement + " projet(s) pour votre arrondissement.</p><p>Il vous est encore possible de voter pour des projets Tout Paris supplémentaires. Lorsque vous aurez validé vos votes, il ne vous sera plus possible de les modifier ou d'en ajouter.</p>");
					$('#modalConfirmVoteButtonAdd').append("<a class='btn btn-12rem btn-white-on-green' href='jsp/site/Portal.jsp?page=solrProjectSearch&conf=projects_mdp&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:Tout Paris'>Je vote pour tout paris</a>").css("text-align","center");
	 				$('#modalConfirmVoteButtonValid').append("<span class='btn btn-18rem btn-black-on-white' onclick='confirmValidateVote();'>Je valide mes votes</span>").css("text-align","center");
	 				$('#modalConfirmVote').modal('toggle');
			
			 } else if( data.result.totVotesToutParis == maxVotesParis && data.result.totVotesArrondissement < maxVotesArrdt ){
					$('#modalConfirmVoteContent').append("<p class='main'>Vous avez vot&eacute; pour " + data.result.totVotesToutParis + " projet(s) tout Paris,<br>et "+ data.result.totVotesArrondissement + " projet(s) pour votre arrondissement.</p><p>Il vous est encore possible de voter pour des projets supplémentaires dans votre arrondissement de vote. Lorsque vous aurez validé vos votes, il ne vous sera plus possible de les modifier ou d'en ajouter.</p>");
					$('#modalConfirmVoteButtonAdd').append("<a class='btn btn-12rem btn-white-on-green' href='jsp/site/Portal.jsp?page=solrProjectSearch&conf=projects_mdp&sort_name=1440769236633_random&sort_order=random&fq=localisation_text:"+user_arrondissement+"'>Je vote pour mon arrondissement</a>").css("text-align","center");
	 				$('#modalConfirmVoteButtonValid').append("<span class='btn btn-18rem btn-black-on-white' onclick='confirmValidateVote();'>Je valide mes votes</span>").css("text-align","center");
	 				$('#modalConfirmVote').modal('toggle');
			
			 } else {
					$('#modalConfirmVoteContent').append("<p class='main'>Vous avez vot&eacute; pour " + data.result.totVotesToutParis + " projet(s) tout Paris,<br>et "+ data.result.totVotesArrondissement + " projet(s) pour votre arrondissement.</p><p>Il vous est encore possible d'ajouter des votes, n'hésitez pas à utiliser le maximum de votes autorisés ! Lorsque vous aurez validé vos votes, il ne vous sera plus possible de les modifier ou d'en ajouter.</p></span></div>");
					$('#modalConfirmVoteButtonAdd').append("<a class='btn btn-12rem btn-white-on-green' href='jsp/site/Portal.jsp?page=solrProjectSearch&conf=projects_mdp&sort_name=code_projet_long&sort_order=asc'>J’ajoute des votes à ma sélection</a>").css("text-align","center");
	 				$('#modalConfirmVoteButtonValid').append("<span class='btn btn-18rem btn-black-on-white' onclick='confirmValidateVote();'>Je valide mes votes</span>").css("text-align","center");
	 				$('#modalConfirmVote').modal('toggle');
			}
		}
	},
		error: function(jqXHR, textStatus, errorThrown) {
		/*dialog.dialog( "open" );*/
		/*alert("Echec");*/
	}
	});
};

function confirmValidateVote()
{
	// Is vote_follow filled ?
	if (typeof $('input[name=vote_follow]:checked').val() == 'undefined')
	{
		$('#vote_follow_msg').show();
	}
	else
	{
		$('#vote_follow_msg').hide();
		$('#modalConfirmVote').modal('toggle');
		window.location.href = baseUrl + 'jsp/site/Portal.jsp?page=mesVotes&action=validateVote&validate=true&notify_valide=true&vote_follow=' + $('input[name=vote_follow]:checked').val();
	}
	
}

function doCancelMyvote( idResource, resourceType )
{
	idResourceToVote = idResource;
	resourceTypeToVote = resourceType;
	arrondissement= $('#localisation_'+idResource).val();
	$.ajax({
		url : baseUrl + "jsp/site/plugins/participatorybudget/DoCancelVote.jsp?idExtendableResource=" + idResourceToVote + "&extendableResourceType=" + resourceTypeToVote,
		type: 'GET',
		dataType: "json",
		data: {},
		async: false,
	  cache:false,
    success:function(data) {
    if ( data.status == 'OK' )
    	{
    		var divVote = $( "#divVote" + resourceTypeToVote + idResourceToVote );
				if(arrondissement == "Tout Paris"){
					setVotesParis( false );
				}else{
					setVotesArrdt( false );
				}
        divVote.html(data.result);
        $(".budget-btn-do-vote").each( function() {
        	this.addEventListener("click",doNotExitPage);
        });
				window.location.reload();
      }
      else
			{
				window.location = data.errorCode;
			}
		},
    error: function(jqXHR, textStatus, errorThrown) {
    }
	});
}

function initMyInfoAddressAutoComplete()
{
	var sourceSRID = new Proj4js.Proj('EPSG:27561');
	var destSRID = new Proj4js.Proj('EPSG:4326');
	var jAdresse = $('#budget-address');
	jAdresse.suggestPOI();
	jAdresse.bind($.suggestPOI.EVT_SELECT, function(event) {
	var poi = event.poi;
	if (poi) {
	   var address = poi.libelleTypo;
	   var lambert_x = poi.x;
	   var lambert_y = poi.y;
	  p = new Proj4js.Point(lambert_x, lambert_y);
	  Proj4js.transform(sourceSRID, destSRID, p);
	  var obj= {
	  	"type": "Feature",
	    "properties": {
	    	"address": address
	    },
	    "geometry": {
	    	"type": "Point",
	      "coordinates": [p.x, p.y]
	    }
	  };
	  $('#geojson').val(JSON.stringify(obj));
	  $('#geojson_state').val(JSON.stringify(obj));
	  }
	});
	//fix open in modal
	jAdresse.on( "autocompleteopen", function( event, ui ) {
		var maxZ = parseInt( jAdresse.autocomplete('widget').css('z-index') );
		$(".modal").each( function() {
			var currZ = parseInt( $(this).css('z-index') );
			if( currZ > maxZ ) { maxZ = currZ+1 }
		});
		jAdresse.autocomplete('widget').css('z-index', maxZ);
	} );
	
	$("#button_delete_adress").on('click', function () {
	$('#budget-address').val("");

	//Don't put the empty string to disambiguate with the user
	//clearing the field, and then pressing the browser back button
	$('#geojson_state').val("false");
	$('#geojson').val("");
	});

	//Try to restore from back/forward browser history buttons
	var prev_data = $('#geojson_state').val();
	var prev_value;
	var user_cleared = false;
	if (prev_data) {
		prev_value=JSON.parse(prev_data);
	  if (prev_value) {
	  	$('#geojson').val(JSON.stringify(prev_value));
	  } else {
	  	user_cleared = true;
	  }
	} else {
		var geojson_val = $('#geojson').val();
		if (geojson_val) {
	   	prev_value=JSON.parse(geojson_val);
		} else {
	   	user_cleared = true;
		}
	}
	if (!user_cleared) {
	 $('#budget-address').val(prev_value.properties.address);
	}
}