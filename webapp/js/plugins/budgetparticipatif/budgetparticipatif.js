var dialog, idResourceToVote, resourceTypeToVote,voteValue,baseUrl,arrondissement,title,thematique;


//displayLoginPagePopup
function displayLoginBp() {
	//$(window).scrollTop(0);

	 $('html, body').animate({scrollTop:0}, 'slow');
	$("#form-connect").show('bounce');
	//reload if the user is disconnected 
//	if($("#form-connect-noaccount").length==0) 
//	{
//		window.location.reload();
//		
//	}
	
}


function sendValidationMail(callBackFunctionOk,callBackFunctionError,callBackFunctionUserNotSigned)

{
	$.ajax({

		url : baseUrl + 'jsp/site/plugins/budgetparticipatif/DoSendValidationMail.jsp',

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
        	else if(data.errorCode ==='JSON_ERROR_CHECKED_ARRONDISSEMENT'){      		
			popup();
        	}

		}

		,

        error: function(jqXHR, textStatus, errorThrown) {

        }

	});
}



function displayModalPopup(callBackFunction)

{

	$.ajax({

		url : baseUrl + "jsp/site/plugins/campagnebp/GetMyInfosPanelForAjax.jsp",

        type: "GET",

        async: false,

    	cache:false,

    	success:function(data) {

        	
    		if ( data  != '' )
    		{

        		/*$("#budgetparticipatifmodelpopup").html(data);*/
        		$("#mesinfos-modal .modal-body").html(data);

        		$('#budgetparticipatif-profile-button').on( "click", function( event ) {
        			event.preventDefault( );
        			doSaveUserInfos( callBackFunction);
					
        		});
        		  $("#send_validation_mail").click(function () {
        		    	sendValidationMail( function(){$('#mesinfos-modal').modal('hide'); },function(){displayModalPopup(function(){checkValidAccountAfterAuthentication(); })}, function() { });

        		        return false;
        		    });
        		  
        		  /*suggestpoi*/
        		  initMyInfoAddressAutoComplete();
        		  $("#budget-birthdate").mask("00/00/0000",{placeholder: "Date de naissance : __/__/____ *"});

        		/*dialog.dialog( "open" );*/
				$('#mesinfos-modal').modal('show');

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
            
            civility:$("#budget-civility-mme").is(":checked")?$("#budget-civility-mme").val():($("#budget-civility-mr").is(":checked")?$("#budget-civility-mr").val():($("#budget-civility-npr").is(":checked")?$("#budget-civility-npr").val():''))
            
            postal_code:$("#postal_code").val(),
            
            arrondissement:$("#arrondissement").val(),
            
            nickname:$("#budget-nickname").val(),
           
             birthdate:$("#budget-birthdate").val(),

            iliveinparis:$("#budget-iliveinparis").is(":checked")?$("#budget-iliveinparis").val():'',

            stayconnected:$("#budget-stayconnected").is(":checked")?$("#budget-stayconnected").val():'',

            geojson:$("#geojson").val(),

            sendaccountvalidation:$("#budget-sendaccountvalidation").is(":checked")?$("#budget-sendaccountvalidation").val():'',

            j_captcha_response:$("#j_captcha_response").length > 0?$("#j_captcha_response").val():'',

            token: $("#token").val(),

        };

	

	var urlDoSave = baseUrl + "jsp/site/plugins/campagnebp/DoSaveMyInfosForAjax.jsp";

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







function isUserIsValid(callBackFunctionOk,callBackFunctionError,callBackFunctionUserNotSigned)
{
	$.ajax({

		url : baseUrl + 'jsp/site/plugins/campagnebp/IsUserValid.jsp',

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

		/* height: 770,

		 width: 600,
		 resizable:true,
 */
		 modal: true,
		
		

		 });

	dialog.find( "form" ).on( "submit", function( event ) {

    	event.preventDefault();

    	doSaveUserInfos();

	});




});
	

function popup()
{
	 $('#modal_title').replaceWith("<div class='btn-in-popup'><span id='modal_title' >Vous souhaitez voter pour des projets qui ne sont pas dans votre arrondissement de lieu de vie ou de travail enregistré dans votre profil. </span></div>");
	 $('#modal_button_valide').replaceWith("<div class='btn-in-popup' ><span id='modal_button' ><a class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' onClick='getInfoUser();'><span> J'ANNULE MES VOTES ET JE MODIFIE MON ARRONDISSEMENT</span></a></span></div>");
	 $('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' data-dismiss='modal'><span> JE CONSERVE MON ARRONDISSEMNT</span></a></span></div>");

	 $('#myModal').modal('toggle');
 
};
function getInfoUser(){
	
	$.ajax({

		url : baseUrl + 'jsp/site/plugins/budgetparticipatif/DOCancelAllVoteUser.jsp?extendableResourceType=' + resourceTypeToVote,

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



//Partie vote

function checkIfUserIsValid(idResource, resourceType, vote)

{

	idResourceToVote = idResource;

	resourceTypeToVote = resourceType;

	voteValue = vote;
	
	arrondissement= $('#localisation_'+idResource).val();
	
	title= $('#title_'+idResource).val(),
	
	thematique= $('#thematique_'+idResource).val(),

	displayLogin=false;

	isUserIsValid( function(){doVote(  );},function(){displayModalPopup( function(){checkIfUserIsValid(idResource, resourceType, vote);})}, function() { displayLogin=true; })

	if(displayLogin == true)

	{

		displayLoginBp();

	}

	return false;

}


function doVote( idResource )

{
	

	$.ajax({

		url : baseUrl + "jsp/site/plugins/budgetparticipatif/DoVote.jsp?idExtendableResource=" + encodeURIComponent(idResourceToVote) + "&extendableResourceType=" + encodeURIComponent(resourceTypeToVote) + "&thematique=" + encodeURIComponent(thematique) + "&localisation=" +encodeURIComponent(arrondissement) + "&title=" + encodeURIComponent(title),
		
		type: 'GET',

		dataType: "json",

		data: {},

		async: false,

	    cache:false,

      success:function(data) {

      	if ( data.status == 'OK' )

  		{

      		var divVote = $( "#divVote" + resourceTypeToVote + idResourceToVote );

      		var html = data.result.split("|_|");
      		
      		divVote.html(html[0]);
      		$("#recap_vote").html(html[1]);
      		
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
				
			$('#modal_title').replaceWith( data.message );
	 		$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button' ><a  href='jsp/site/Portal.jsp?page=search-solr&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:Tout Paris&fq=type:Projet 2015' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' onClick='voteArrond();' ><span> Je vote pour tout paris</span></a></span></div>");
	 		$('#modal_button_valide').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a  href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle'><span> Voir mes votes</span></a></span></div>");
	 		$('#myModal').modal('toggle');
      	}
		    else if(data.errorCode =='JSON_ERROR_ALREADY_VOTED_TOUT_PARIS')

      	{	
			$('#modal_title').replaceWith( data.message );
	 		$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button' ><a class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' onClick='voteArrond();' ><span> Je vote pour mon arrondissement</span></a></span></div>");
	 		$('#modal_button_valide').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a  href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle'> <span> Voir mes votes</span></a></span></div>");
		 	
	 		$('#myModal').modal('toggle');   		

      	}
		   else if(data.errorCode == "JSON_ERROR_VOTE_USER_ARROND"){
			   $('#modal_title').replaceWith( data.message );
		 		$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button' ><a  class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' onClick='getInfoUser();' ><span>  J'annule mes votes et je valide mon arrondissement </span></a></span></div>");
		 		$('#modal_button_valide').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a  href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' ><span> Voir mes votes</span></a></span></div>");
			 	
		 		$('#myModal').modal('toggle');   		
			}
		else if(data.errorCode == "USER_ALREADY_VOTED"){
			   
			   $('#modal_title').replaceWith( data.message );
			   $('#modal_button_valide').replaceWith("");
			 	
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


function valideVotes(user_arrondissement, maxVotesArrdt){
	
	$.ajax({

		url : baseUrl + 'jsp/site/plugins/budgetparticipatif/DoValideVote.jsp',

		type: 'POST',

		dataType: "json",

		data: {},

		async: false,

	    	cache:false,

           	success:function(data) {
			if(data.status == "OK"){
				if(data.result.nbTotVotes == 0){
					
					$('#modal_title').replaceWith("<div class='btn-in-popup'><span id='modal_title'> Vous n’avez voté pour aucun projet. </span></div>");
		 			$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button' ><a href='jsp/site/Portal.jsp?page=search-solr&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:Tout Paris&fq=type:Projet 2015' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' onClick='voteArrond();' ><span>  J'ajoute des votes à ma sélection </span> </a></span></div>");
		 	
					 $('#myModal').modal('toggle');
	 

				}else if(data.result.nbTotVotes == 8 + maxVotesArrdt){

					$('#modal_title').replaceWith("<div class='btn-in-popup'><span id='modal_title'>  Vous avez voté pour 8/8 pour Paris, "+maxVotesArrdt+" projets pour votre arrondissement.</span></div>");
	 				$('#modal_button_valide').replaceWith("<div class='btn-in-popup'> <span id='modal_button_valide'><a href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes&notify_valide=true' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle'><span> Je valide mes votes</span></a></span></div>");
	 				$('#myModal').modal('toggle');



				}else if(data.result.totVotesArrondissement == maxVotesArrdt && data.result.totVotesToutParis < 8){

					$('#modal_title').replaceWith("<div class='btn-in-popup'><span id='modal_title'>  Vous avez voté pour des projets concernant votre arrondissement, souhaitez-vous voter pour des projets concernant tout Paris</span></div>");
	 				$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button'> <a href='jsp/site/Portal.jsp?page=search-solr&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:Tout Paris&fq=type:Projet 2015' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' ><span> Je vote pour tout paris</span></a></span></div>");
	 				$('#modal_button_valide').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes&notify_valide=true' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle'><span> Je valide mes votes</span></a></span></div>");
	 				$('#myModal').modal('toggle');

				}else if(data.result.totVotesToutParis == 8 && data.result.totVotesArrondissement < maxVotesArrdt){

					$('#modal_title').replaceWith("<div class='btn-in-popup'><span id='modal_title'>  Vous avez voté pour des projets concernant tout Paris, souhaitez-vous voter pour des projets concernant votre arrondissement ?</span></div>");
	 				$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button' > <a class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' href='jsp/site/Portal.jsp?page=search-solr&sort_name=1440769236633_random&sort_order=random&fq=localisation_text:"+user_arrondissement+"&fq=type:Projet 2015' ><span> Je vote pour mon arrondissement</span></a></span></div>");
	 				$('#modal_button_valide').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes&notify_valide=true' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle'><span> Je valide mes votes</span></a></span></div>");
		 	
	 				$('#myModal').modal('toggle');   		


				}else {

					$('#modal_title').replaceWith("<div class='btn-in-popup'><span id='modal_title' >  Vous avez voté pour "+data.result.totVotesToutParis+" projets pour Paris, "+data.result.totVotesArrondissement+ " projet pour votre arrondissement.</span></div>");
	 				$('#modal_button').replaceWith("<div class='btn-in-popup'><span id='modal_button' ><a href='jsp/site/Portal.jsp?page=search-solr&sort_name=code_projet_long&sort_order=asc&fq=localisation_text:Tout Paris&fq=type:Projet 2015' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' ><span> j’ajoute des votes à ma sélection</span></a></span></div>");
	 				$('#modal_button_valide').replaceWith("<div class='btn-in-popup'><span id='modal_button_valide'><a href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes&notify_valide=true' class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle'><span> Je valide mes votes</span></a></span></div>");
	 				$('#myModal').modal('toggle');
				}
			}
      		

		},
	
		error: function(jqXHR, textStatus, errorThrown) {
			/*dialog.dialog( "open" );*/
			alert("echec")

		}

	
	});
};	






function doCancelMyvote( idResource, resourceType )

{

	idResourceToVote = idResource;

	resourceTypeToVote = resourceType;
	arrondissement= $('#localisation_'+idResource).val();

	$.ajax({

		url : baseUrl + "jsp/site/plugins/budgetparticipatif/DoCancelVote.jsp?idExtendableResource=" + idResourceToVote + "&extendableResourceType=" + resourceTypeToVote,

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


function doCancelVote( idResource, resourceType )

{

	idResourceToVote = idResource;

	resourceTypeToVote = resourceType;
	arrondissement= $('#localisation_'+idResource).val();

	$.ajax({

		url : baseUrl + "jsp/site/plugins/budgetparticipatif/DoCancelVote.jsp?idExtendableResource=" + idResourceToVote + "&extendableResourceType=" + resourceTypeToVote,

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

				var html = data.result.split("|_|");
	      		
	      		divVote.html(html[0]);
	      		$("#recap_vote").html(html[1]);

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

	$(".nb-user-votes_paris").each(function( index ) {

		var spanNbVote = $(".nb-user-votes_paris");

		var nBuserVote=parseInt($(this).text());

		( inc == true ) ? nBuserVote++ : nBuserVote--;

		nBuserVote=(  nBuserVote > 0 ) ? nBuserVote : 0;

		$(this).text( nBuserVote);

	});

	/* Set Project vote value */

	var spanProjetNbVote="#vote-amount-" + idResourceToVote;

	var nBprojectVote=parseInt($(spanProjetNbVote).text());

	( inc == true ) ? nBprojectVote++ : nBprojectVote--;

	nBprojectVote=(  nBprojectVote > 0 ) ? nBprojectVote : 0;

	$( spanProjetNbVote ).text( nBprojectVote);

}


function setVotesArrdt( inc ){

	$(".nb-user-votes_arrdt").each(function( index ) {

		var spanNbVote = $(".nb-user-votes_arrdt");

		var nBuserVote=parseInt($(this).text());

		( inc == true ) ? nBuserVote++ : nBuserVote--;

		nBuserVote=(  nBuserVote > 0 ) ? nBuserVote : 0;

		$(this).text( nBuserVote);

	});

	/* Set Project vote value */

	var spanProjetNbVote="#vote-amount-" + idResourceToVote;

	var nBprojectVote=parseInt($(spanProjetNbVote).text());

	( inc == true ) ? nBprojectVote++ : nBprojectVote--;

	nBprojectVote=(  nBprojectVote > 0 ) ? nBprojectVote : 0;

	$( spanProjetNbVote ).text( nBprojectVote);

}






	

	