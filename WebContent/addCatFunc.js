/**
 * Javascript for enabling 'Add a Category' feature
 */
function myFunction(id){
	if(id=="addCatButton"){ 
	   var newSelectTag = document.createElement("select");
	   // newTag.id = "newSelect";
	   newSelectTag.setAttribute("name","category");
	    var x = document.getElementById("addCat");
	    // var y = document.getElementById("newSelect");
	    for(var i=0;i<x.length;i++){  
	       var c = document.createElement("option");
	       c.text = x.options[i].text; 
	       //y.options.add(c,i);
	       newSelectTag.appendChild(c);
	   } 
	    document.getElementById("catParagraph").appendChild(newSelectTag);
	}
}