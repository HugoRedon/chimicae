/**
 * 
 */

function hello(){
	document.writeln("hola");
	
	 var xmlhttp;
    // compatible with IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200){
        	//document.writeln("xmlhttp.status" + xmlhttp.status);
        	document.writeln(xmlhttp.responseText);
            callback(xmlhttp.responseText);
        }
    }
    xmlhttp.open("GET", "http://localhost:8080/chimicae/CallFromJavascriptTest?test=holatuu", true);
    xmlhttp.send();
}