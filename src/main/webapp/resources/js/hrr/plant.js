function Plant (scene) {
	this.objects=[];
	this.scene;	
	
   
       var parent;
       

       function addGeometry( geometry, color, x, y, z, rx, ry, rz, s ) {
         var mesh = new THREE.Mesh( geometry, new THREE.MeshPhongMaterial( { 
	            color: 0x444444, 
	            ambient: 0x444444, // should generally match color
	            specular: 0x050505,
	            side: THREE.DoubleSide,
	            shininess: 100
	        } )  );
         mesh.castShadow = true;
      

         mesh.position.set( x, y, z - 75 );
         // mesh.rotation.set( rx, ry, rz );
         mesh.scale.set( s, s, s );

         if (geometry.debug) mesh.add(geometry.debug);

         parent.add( mesh );




       }
	
	this.smiley=function(){
		
		   var randomPoints = [];

	        for (var i=0; i<10;i++) {
	          randomPoints.push(
	            new THREE.Vector3(Math.random() * 200,Math.random() * 200,Math.random() * 200 )
	            );
	        }
		var extrudeSettings = { amount: 200,  bevelEnabled: true, bevelSegments: 2, steps: 150 };
		var randomSpline =  new THREE.SplineCurve3(randomPoints);

        extrudeSettings.extrudePath = randomSpline; 
		 
	
	      
	        var tube = new THREE.TubeGeometry(extrudeSettings.extrudePath, 150, 4, 5, false, true);
	        // new THREE.TubeGeometry(extrudePath, segments, 2, radiusSegments, closed2, debug);	      	
	        addGeometry( tube, 0x00ff11,  0,  0, 0,     0, 0, 0, 1 );  
	        console.log(tube);
	};
	
	

	this.load3dModel = function(modelPath,nameInfo,index){
		
		console.log("start loading");
		var loader = new THREE.JSONLoader();
        loader.load( modelPath, function( geometry ) {
        	variable = new THREE.Mesh( geometry, new THREE.MeshPhongMaterial( { 
	            color: 0x444444, 
	            ambient: 0x444444, // should generally match color
	            specular: 0x050505,
	            side: THREE.DoubleSide,
	            shininess: 100
	        } )  );
        	variable.scale.set( 20, 20, 20 );
        	//variable.position.z=25;
        	variable.position.y = Math.random()*500;
        	variable.position.x = Math.random()*500;
        	variable.rotation.x = Math.PI/2;
        	variable.castShadow = true;
        	variable.receiveShadow = true;
	    	variable.userData={name:nameInfo};
	        scene.add(variable);
	        plant.objects[index]=variable;
	        //plant.drawTubeBetween(variable,{position:{x:Math.random()*500,y:Math.random()*500,z:125}});
	       plant.drawTubes();
        });
        
        return heatExchanger;
	};
	
	this.drawTubes=function(){
		scene.remove(parent);
		parent = new THREE.Object3D();
	   scene.add( parent );
		
		if(plant.objects.length == 3){
			for(var i = 0; i < plant.objects.length-1;i++){
				var ob1 = plant.objects[i];
				var ob2 = plant.objects[i+1];
				this.drawTubeBetween(ob1,ob2);
			}
		}
	}
	this.drawTubeBetween= function(o1,o2){
		
		var points = [];
		
		var n =60;
		
		var xStep = (o2.position.x -o1.position.x)/n;
		var yStep = (o2.position.y-o1.position.y)/n;
		
		for(var i = 0; i <n ;i++){
			var x = o1.position.x  + i * xStep;
			var y = o1.position.y;
			var z = 100;
			var vector = new THREE.Vector3(x,y,z);
			points.push(vector);
		}
		
		for(var i = 0; i <n ; i++){
			var x = o2.position.x;
			var y = o1.position.y + i *yStep;
			var z = 100;
			var vector = new THREE.Vector3(x,y,z);
			points.push(vector);
		}
		
		
		
		
		var extrudeSettings = { amount: 200,  bevelEnabled: true, bevelSegments: 2, steps: 150 };
		var randomSpline =  new THREE.SplineCurve3(points);

        extrudeSettings.extrudePath = randomSpline; 
		 	
        var tube = new THREE.TubeGeometry(extrudeSettings.extrudePath, 150, 4, 15, false, true);	        	     
        addGeometry( tube, 0x00ff11,  0,  0, 0,     0, 0, 0, 1 );  
        console.log(tube);
		
	};
	var loading = true;
	
	
	this.loadModels= function(){
		
		this.load3dModel("models/sphericalTank.txt","Tanque esférico",0);
		this.load3dModel("models/simple-heat-exchanger.txt","Intercambiador de calor",1);
		this.load3dModel("models/towerModel.txt","Torre de destilación",2);
		

		//this.smiley();
	};
	this.loadModels();
	
	
	
	
}



