<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>SoloSyncUI</title>

	  <script
	  src="https://code.jquery.com/jquery-3.1.1.min.js"
	  integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
	  crossorigin="anonymous"></script>

	  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<style type="text/css">
	.full-with{
		width: 100%;
	}	
	.container-padding{
		padding:10px;
		margin-top: 50px;
	}


	.pbar-container {
	  width: 600px;
	  margin: 100px auto; 
	}
	.step-progressbar {
	  margin: 0;
	  padding: 0;
	  counter-reset: step;
	}
	.step-progressbar li {
	  list-style-type: none;
	  width: 16%;
	  float: left;
	  font-size: 12px;
	  position: relative;
	  text-align: center;
	  text-transform: uppercase;
	  color: #7d7d7d;
	}
	.step-progressbar li:before {
	  width: 30px;
	  height: 30px;
	  content: counter(step);
	  counter-increment: step;
	  line-height: 30px;
	  border: 2px solid #7d7d7d;
	  display: block;
	  text-align: center;
	  margin: 0 auto 10px auto;
	  border-radius: 50%;
	  background-color: white;
	}
	.step-progressbar li:after {
	  width: 100%;
	  height: 2px;
	  content: '';
	  position: absolute;
	  background-color: #7d7d7d;
	  top: 15px;
	  left: -50%;
	  z-index: -1;
	}
	.step-progressbar li:first-child:after {
	  content: none;
	}
	.step-progressbar li.active {
	  color: green;
	}
	.step-progressbar li.active:before {
	  border-color: #55b776;
	  background-color: #cae8d4;
	}
	.step-progressbar li.active + li:after {
	  background-color: #55b776;
	}

	.step-progressbar li.failed {
	  color: red;
	}
	.step-progressbar li.failed:before {
	  border-color: #b32400;  
	  background-color: #ffcccc;
	}
	.step-progressbar li.failed + li:after {
	  background-color: #b32400;
	}


</style>

</head>

<body>
	
	<div class="container container-padding">
	<form class="col-md-6 col-md-offset-3 centered">

	<div class="row">
	<div class="col-md-4">
	<label for="" >Message Source:</label>
	</div>
	<div class="col-md-8 ">
	<input type="radio" name="messagetype" value="CSG"> CSG<br>
  	<input type="radio" name="messagetype" value="Icoms"> Icoms
  	</div>
  	</div>

  	<br>

  	<div class="row" id="csgblock">
	<div class="col-md-4">
	<label for="" >Update Type: </label>
	</div>
	<div class="col-md-8">
	<select id="csgblock-select" class="full-with form-control input-md">
	  <option value="account">Update Account Details</option>
	  <option value="location">Update Location Details</option>
	</select>
	</div>
  	</div>


  	<div class="row" id="icomsblock">
	<div class="col-md-4">
	<label for="" >Update Type: </label>
	</div>
	<div class="col-md-8">
	<select id="icomsblock-select" class="full-with form-control input-md">
	  <option value="house">House Update</option>
	</select>
	</div>
  	</div>

  	<br>

  	<div class="row"  id="accountdetailsblock">
	<div class="col-md-4">
	<label for="" >Account Number: </label>
	</div>
	<div class="col-md-8">
	<input id="account_number" type="text" class="full-with form-control input-md"> 
	</div>
  	</div>

  	
  	<div class="row" id="locationdetailsblock">
	<div class="col-md-4">
	<label for="" >Location ID: </label>
	</div>
	<div class="col-md-8">
	<input id="location_id" type="text" class="full-with form-control input-md">
	</div>
  	</div>

  	
  	<div class="row" id="locationidentifierblock">
	<div class="col-md-4">
	<label for="" >Location Identifier: </label>
	</div>
	<div class="col-md-8" >
	<input id="location_identifier" type="text" class="full-with form-control input-md">
	</div>
	</div>

	<br>

	<div class="row">
	<div class="col-md-4 col-md-offset-4 ">
		<input type="button" id="execute" class="btn btn-block btn-info" data-toggle="collapse" data-target="#progressbar" value="Execute">
	</div>
	</div>

	<br>
	<br>
	<br>

	</form>

	</div>

	<div class="container mycontainer">
	<strong>
        <ul class="step-progressbar">
            <li id="p-producer">Producer</li>
            <li id="p-kafka">Kafka</li>
            <li id="p-consumer">Consumer</li>
            <li id="p-solosyncservice">SoloSyncService</li>
            <li id="p-jesi">JESI</li>
            <li id="p-sf">S/F</li>
        </ul>
	</strong>
	</div>

<script type="text/javascript">

var biller = "";
var type = "";
var usedTextbox_id = "";
var URL = "";

	
$( document ).ready(function() {
	hideAll();    
});

$("input:radio[name=messagetype]").click(function() {
    var value = $(this).val();
    biller = value;

    if(value=='CSG'){
    	hideAll();
    	$('#csgblock').show();
    	$('#accountdetailsblock').show();
    	$('#execute').show();

    	usedTextbox_id = $('#account_number');
    	type = 'account';
    }
    else{
    	hideAll();
    	$('#icomsblock').show();
    	$('#locationidentifierblock').show();
    	$('#execute').show();

    	usedTextbox_id = $('#location_identifier');
    	type = 'location';
    }
});

$('#csgblock-select').change(function(){ 
    var value = $(this).val();

    if(value=='account'){
    	hideTextBoxes();
    	$('#accountdetailsblock').show();
    	usedTextbox_id = $('#account_number');
    	type = 'account';
    }
    else if(value=='location'){
    	hideTextBoxes();
    	$('#locationdetailsblock').show();
    	usedTextbox_id = $('#location_id');
    	type = 'location';
    }
    
});

$('#icomsblock-select').change(function(){ 
    var value = $(this).val();
    
    if(value=='house'){
    	hideTextBoxes();
    	$('#locationidentifierblock').show();

    	usedTextbox_id = $('#location_identifier')
    	type = 'location';
    }
    //TODO: else.. 
});


$("#execute").click(function(){
//	URL = "http://localhost:8080/triggerProducer/" + biller + "/" + type + "/" + usedTextbox_id.val();
    URL = "http://localhost:8080/triggerProducer/CSG/1232131233433";
   	resetProgressBar();
    
    
//    $.ajax({
//        url: URL
//    }).then(function(data) {
//       alert('success');
//    });
    
    $.ajax({
    	url: URL , 
        dataType:'json',
    	success: function(result){
    
            var status = result.messageStatus;

    		switch(status) {
			    case 'P':
			        		$('#p-producer').addClass('active');
			        		break;
			    case 'PK':
			        		$('#p-producer').addClass('active');
			        		$('#p-kafka').addClass('active');
			        		break;
			    case 'PKC':
			        		$('#p-producer').addClass('active');
			        		$('#p-kafka').addClass('active');
			        		$('#p-consumer').addClass('active');
			        		break;
			    case 'PKCS':
			        		$('#p-producer').addClass('active');
			        		$('#p-kafka').addClass('active');
			        		$('#p-consumer').addClass('active');
			        		$('#p-solosyncservice').addClass('active');
			        		break;
			    case 'PKCSJ':
			        		$('#p-producer').addClass('active');
			        		$('#p-kafka').addClass('active');
			        		$('#p-consumer').addClass('active');
			        		$('#p-solosyncservice').addClass('active');
			        		$('#p-jesi').addClass('active');
			        		break;
			    case 'PKCSJS':
			        		$('#p-producer').addClass('active');
			        		$('#p-kafka').addClass('active');
			        		$('#p-consumer').addClass('active');
			        		$('#p-solosyncservice').addClass('active');
			        		$('#p-jesi').addClass('active');
			        		$('#p-sf').addClass('active');
			        		$('#p-sf').text('Success');
			        		break;
			    case 'PKCSJF':
			        		$('#p-producer').addClass('active');
			        		$('#p-kafka').addClass('active');
			        		$('#p-consumer').addClass('active');
			        		$('#p-solosyncservice').addClass('active');
			        		$('#p-jesi').addClass('active');
			        		$('#p-sf').addClass('failed');
			        		$('#p-sf').text('failed');
			        break;
			    		        
			}

        //refresh progressbar
    	},
    	error: function(data){
            console.log(data);
            alert(data);
        }
		});

});

function hideAll(){

	$('#csgblock').hide();
	$('#icomsblock').hide();
	$('#accountdetailsblock').hide();
	$('#locationdetailsblock').hide();
	$('#locationidentifierblock').hide();
	$('#execute').hide();
}

function hideTextBoxes(){
	$('#accountdetailsblock').hide();
	$('#locationdetailsblock').hide();
	$('#locationidentifierblock').hide();
}

function resetProgressBar(){
	$('#p-producer').removeClass('active');
	$('#p-kafka').removeClass('active');
	$('#p-consumer').removeClass('active');
	$('#p-solosyncservice').removeClass('active');
	$('#p-jesi').removeClass('active');
	$('#p-sf').removeClass('failed');
	$('#p-sf').removeClass('active');
	$('#p-sf').text('S/F');
}


</script>
</body>
</html>