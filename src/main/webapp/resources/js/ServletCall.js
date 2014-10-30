/**
 * 
 */

var host = "http://localhost:8080/chimicae/";

function hello(callback){
	//document.writeln("hola");
	
	 var xmlhttp;
    // compatible with IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200){
        	//document.writeln("xmlhttp.status" + xmlhttp.status);
        	//document.writeln(xmlhttp.responseText);
            callback(xmlhttp.responseText);        	
        }
    }
    xmlhttp.open("GET", host+"?test=holatuu", true);
    xmlhttp.send();
}

function heterogeneousList(callback){
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange=function(){
		if(xmlhttp.readyState==4 && xmlhttp.status ==200){
			callback(xmlhttp.responseText);
		}
	}
	xmlhttp.open("GET",host+ 'CallFromJavascriptTest'+"?test=heterogeneousList",true);
	xmlhttp.send();
}

function createTank(){
	var el = document.getElementById('hlist');
	 var selected = el.options[el.selectedIndex].value;
	 var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange=function(){
			if(xmlhttp.readyState==4 && xmlhttp.status ==200){
				console.log(xmlhttp.responseText);
				var report = JSON.parse( xmlhttp.responseText);
				
				plant.addTank(report);
			}
		};
		var temperature = document.getElementById('tempTF').value;
		var pressure =document.getElementById('pressTF').value;
		xmlhttp.open("GET",host+'CallFromJavascriptTest'+"?test=createTank&temperature="+temperature+"&pressure="+ pressure+ "&selected="+selected,true);
		xmlhttp.send();
	removeD('controls');
}

function createHeatExchanger(javaid, newTemperature){
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange=function(){
		if(xmlhttp.readyState==4 && xmlhttp.status==200){
			console.log('intercambiador: '+xmlhttp.responseText);
		}
	};
	
	
	xmlhttp.open("GET",host+'CreateHeatExchangerServlet' +'?fromequipmentid='+javaid + '&newTemperature='+newTemperature );
	xmlhttp.send();
}

function removeD(idDiv){
	var el = document.getElementById(idDiv);
	//el.parentNode.removeChild(el);
	el.style.display='none';		
}
function addProperty(idText,label,text,propertiesDiv ){
	   var idTF  = document.createElement('input');
		idTF.type = 'text';
		idTF.readOnly=true;
		idTF.value=text;
		idTF.className='propertiesTF';
		//idTF.style.width='50px';
		idTF.id=idText;
		
		var idL = document.createElement('label');
		idL.htmlfor=idText;
		idL.innerHTML=label;
		
		
		propertiesDiv.appendChild(idL);
		propertiesDiv.appendChild(idTF);
		propertiesDiv.appendChild(document.createElement('br'));
} 