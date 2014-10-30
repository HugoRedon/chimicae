function Plant (scene) {
	this.objects=[];
	this.scene;	
	this.tankGeometry;
	this.heatExchangerGeometry;
	this.towerGeometry;
	this.material = new THREE.MeshPhongMaterial( { 
											        color: 0x444444, 
											        ambient: 0x444444, // should generally match color
											        specular: 0x050505,
											        side: THREE.DoubleSide,
											        shininess: 100
											    } );  
								

       var parent;
       

       function addGeometry( geometry, color, x, y, z, rx, ry, rz, s ) {
         var mesh = new THREE.Mesh( geometry, plant.material );
         mesh.castShadow = true;
      

         mesh.position.set( x, y, z - 75 );
         // mesh.rotation.set( rx, ry, rz );
         mesh.scale.set( s, s, s );

         if (geometry.debug) mesh.add(geometry.debug);

         parent.add( mesh );




       }
	

	
      
	
	
	this.addTank = function(report){
		var tank = createMeshAndAdd( this.tankGeometry,"Tanque esférico");
		tank.javaid = report.id;
		tank.report=report;
		
		tank.callback = function(){
			 var propertiesDiv = document.getElementById( 'properties' );
			 propertiesDiv.style.display='block';
			 propertiesDiv.innerHTML='';
	
			 addProperty('idTF','id: ',report.id,propertiesDiv);
			 addProperty('propertyTempTF','Temperatura [K]: ',report.pr.temperature,propertiesDiv);
			 addProperty('propertyPressTF','Presión [Pa]: ',report.pr.pressure,propertiesDiv);
			 addProperty('cnameTF','Compuesto: ',report.pr.compoundName,propertiesDiv);
			 addProperty('capacityTF','Capacidad [m^3]: ',report.capacity,propertiesDiv);
			 addProperty('enthalpyTF','Entalpía [J/kmol]: ',report.pr.enthalpy,propertiesDiv);
			 addProperty('entropyTF','Entropía [J/kmol K]: ',report.pr.entropy,propertiesDiv);
			 addProperty('gibbsTF','E. Gibbs [J/kmol]: ',report.pr.gibbs,propertiesDiv);
			 addProperty('vFTF','Relación v/F',report.pr.vF,propertiesDiv);
		};
	   
		
		
	}
	this.addHeatExchanger = function(report){
		var he = createMeshAndAdd(this.heatExchangerGeometry,"Intercambiador de calor");
		
		he.callback=function(){
			
		}
		
		return he; 
		
	};
	
	this.addTower= function(){
		return createMeshAndAdd(this.towerGeometry,"Torre de destilación");
	}
	
	function createMeshAndAdd(geometry,info){
		var mesh = new THREE.Mesh( geometry,plant.material.clone());
		mesh.scale.set(20,20,20);
		mesh.position.y = Math.random()*500;
		mesh.position.x = Math.random()*500;
		mesh.rotation.x = Math.PI/2;
		mesh.castShadow=true;
		mesh.receiveShadow=true;
		mesh.userData={name:info};
		scene.add(mesh);
		plant.objects.push(mesh);
		return mesh;
	}
	
	this.drawTubes=function(){
		scene.remove(parent);
		parent = new THREE.Object3D();
	   scene.add( parent );
		
		
		for(var i = 0; i < plant.objects.length ;i++){
			var obj = plant.objects[i];
			if(obj.nextEquipment){
				var next = obj.nextEquipment;
				this.drawTubeBetween(obj,next);
			}
//				var ob1 = plant.objects[i];
//				var ob2 = plant.objects[i+1];
//				this.drawTubeBetween(ob1,ob2);
			
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
	
	
	this.loadGeometry = function(modelPath){
		
		
	};
	
	this.loadGeometries= function(){
		var loader = new THREE.JSONLoader();
		loader.load( "experimental/models/sphericalTank.txt", function( geometry ){
			plant.tankGeometry = geometry;				
		});

		loader.load( "experimental/models/simple-heat-exchanger.txt", function( geometry ){
			plant.heatExchangerGeometry = geometry;				
		});
		loader.load( "experimental/models/towerModel.txt", function( geometry ){
			plant.towerGeometry = geometry;				
		});
		
	
	};
	this.loadGeometries();
	
	
	this.remove = function(toRemove){
		var index = plant.objects.indexOf(toRemove);
		if(index > -1){
			plant.objects.splice(index,1);	
		}
		
		
		if(toRemove.previousEquipment){
			toRemove.previousEquipment.nextEquipment = null;
		}
		if(toRemove.nextEquipment){
			plant.remove(toRemove.nextEquipment);
		}
		
		scene.remove(toRemove);
		plant.drawTubes();
	};
	
	
	
}



