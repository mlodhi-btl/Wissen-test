'use strict';
	/*
	 * upload file 
	 */
    var input = document.getElementById("myFile");
    var url = document.getElementById("frm").action;
    
    /*
     * progress bar
     */
    var progress=  document.getElementById("progress");
    var pbar=  document.getElementById("pbar");
    var percentage = document.getElementById("p_percentage");
    var chunkUplaoded=document.getElementById("chunkUplaoded");
 /*
  * download file using the URL
  */
    var multipleFileUploadError = document.getElementById('singleFileUploadError');
    var multipleFileUploadSuccess = document.getElementById('singleFileUploadSuccess');
    
    
    
    var chunk_size = 512*1000;
    var offset = 0;
    let index =0;
    let requestArray=new Array();
    
    function UploadFileToserver(){
    	var files =input.files;
   	 alert(files);
    }
    
   var UploadFileToserver  = function () {
    	var files =input.files;
      while(files && files[index]) {
    	  
    	 let flag= true;
    	 offset=0;
        var myFile = files[index];
        var fsize = myFile.size; // getting the file size so that we can use
									// it for loop statement
        let number_of_parts = Math.ceil(fsize/chunk_size);
        var part =0;
        var i=0;
        let partCouter=0;
        
    
         
        /*
		 * enable the use of await return promise resolve is called with the
		 * whatever is sent in body
		 */
         async function process (url,  blob){
        	 var  request = new XMLHttpRequest();
        	 let formData = new FormData();
             formData.append("f1",blob);
                
            // request.setRequestHeader("X-Chunk-Id", part);
            // requestArray.push(url+query);
             request.onreadystatechange = function(){
            	 
            	 if(request.readState==4)
            		 if(request.status!=200){
            		 process(url, blob);
            	 }else{
            		   var p = JSON.parse(request.responseText);
            		   uplaodStatus(p);
            		
            	 }
             }
             request.onerror = function () {
            	  console.log("** An error occurred during the transaction");
            	  
            	  var p = JSON.parse(request.responseText);
            	  
            	  percentage.innerHTML= p.message;
            	};
            	
             request.open('POST',url, false);  
             request.send(formData);
             };
        
		    function uplaodStatus(p){
		    	setTimeout(function(){
		    		pbar.value= Math.round((p.chunk_number/number_of_parts)*100);
    	            percentage.innerHTML= Math.round((p.chunk_number/number_of_parts)*100) + "%";
		    	},5)
		    }
             async function processRequest(){
            	var req=[];
            	
            	 try{
            		 
            		 while( /* i<size */ part<number_of_parts){
            			 var blob = myFile.slice(offset, offset + chunk_size); /* slice the file by specifying the chunk and offset*/
                         partCouter= partCouter+1;
                         var eof= partCouter==number_of_parts?true:false
                         var query="?part_number="+partCouter+"&file="+myFile.name+"&EOF="+eof;
                         req.push(process(url+query, blob));
                         offset=offset + chunk_size;
                         
            			 if((number_of_parts-part)<200){
            				 await Promise.all(req).then( function(result	){
            					 for(i=0;i<result.length;i++){}
            					 console.log(result[i]);
            					 req=[];
            				 });
            			 	}else{
            					 if(part!=0&&part%200==0){
            						 let resutl = await Promise.all(req).then(function(){
            							 
            							 
            							 req=[]; 
            						 });
            					   
            					 }
            					
            				 }

         			  /* 	pbar.value= Math.round((part/number_of_parts)*100);
        	            percentage.innerHTML= Math.round((part/number_of_parts)*100) + "%";*/
        	            part=part+1;  
            			 }
            			 
            		    pbar.value= Math.round((part/number_of_parts)*100);
     	                percentage.innerHTML= Math.round((part/number_of_parts)*100) + "%"; 
     	               part=0;
            		 }
            		 

            	 catch(error){
            		 
            		 console.log("error in loadding file");
            		 
            	 }
            	 number_of_parts=0;
             }
             
             processRequest().then(function(result){
            	 console.log("success");
            	 
            	 recreatData();
            	
             });
             
             
             function recreatData(){
            	 
            		var xhr= new  XMLHttpRequest();
            		xhr.open('GET',url+"/download/"+myFile.name);
            		
            	 
            	 xhr.onload = function() {
            	        console.log(xhr.responseText);
            	        var response = JSON.parse(xhr.responseText);
            	        if(xhr.status == 200) {
            	            

            	            singleFileUploadError.style.display = "none";
            	            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            	            singleFileUploadSuccess.style.display = "block";
            	            
            	        } else {
            	        	singleFileUploadSuccess.style.display = "none";
            	        	singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
            	        }
            	    }
            	 xhr.send(); 
             }
             
          
        index++;
       
      }
   
    };
