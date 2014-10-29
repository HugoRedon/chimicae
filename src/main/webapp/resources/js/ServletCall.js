/**
 * 
 */

var host = "http://localhost:8080/chimicae/CallFromJavascriptTest";

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
	xmlhttp.open("GET",host+"?test=heterogeneousList",true);
	xmlhttp.send();
}

function createTank(){
	var el = document.getElementById('hlist');
	 var selected = el.options[el.selectedIndex].value;
	 var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange=function(){
			if(xmlhttp.readyState==4 && xmlhttp.status ==200){
				var report = xmlhttp.responseText;
				
				plant.addTank(report.id);
			}
		}
		var temperature = document.getElementById('tempTF').value;
		var pressure =document.getElementById('pressTF').value;
		xmlhttp.open("GET",host+"?test=createTank&temperature="+temperature+"&pressure="+ pressure+ "&selected="+selected,true);
		xmlhttp.send();
	removeD('controls');
}

function removeD(idDiv){
	var el = document.getElementById(idDiv);
	//el.parentNode.removeChild(el);
	el.style.display='none';		
}