// JavaScript Document
function salirDelSistema(){
if ( confirm("¿Esta seguro que desea salir del sistema?") ){	
		top.location.replace("../../login.html");
	}

}


var numeroMenu = 0;   //Variable para controlar el número de menus existentes
var cacheMenuMostrado = -1;

_menuCloseDelay=0           // The time delay for menus to remain visible on mouse out
_menuOpenDelay=0            // The time delay before menus open on mouse over
_subOffsetTop=0              // Sub menu top offset
_subOffsetLeft=0            // Sub menu left offset

with(mainStyleHoriz=new mm_style()){
styleid=1;
bordercolor="#999999";
borderstyle="solid";
borderwidth=0;
fontfamily="Verdana, Arial, Helvetica, sans-serif; ";
fontsize="12px";
fontstyle="normal";
headerbgcolor="#AFD1B5";
headerborder=1;
headercolor="#000099";

offbgcolor="transparent";
offcolor="#333333";
onbgcolor="#FEFAD2";
onborder="1px solid #999999";
oncolor="#9E2F35";
//overbgimage="../../imagenes/backon_beige.gif";
overbgimage="../../imagenes/menu/menu_bg2.jpg";
onsubimage="../../imagenes/on_downboxed.gif";
subimage="../../imagenes/downboxed.gif";
outfilter="randomdissolve(duration=0.2)";
//overfilter="Fade(duration=0.1);Alpha(opacity=95);Shadow(color=#777777', Direction=135, Strength=3)";
padding=3;
pagebgcolor="#CFE2D1";
pagecolor="#000066";
pageimage="../../imagenes/db_red.gif";

separatorcolor="#B2B3AE";
separatorheight=20;
separatorsize=1;
separatoralign='center';
separatorpadding=5;
swap3d=1;
}

/*  Este método ya no será usado...
function pintarMenus(menuPorDefecto){  //Pinta todos los menús

		if(menuPorDefecto == "CATASTRO")
			menuCatastro();
	else if(menuPorDefecto == "PARTICIPACIONES")
			menuParticipacion();	


	drawMenus();	// Método que pinta los menús
	//Si para algún valor de menú esconderá los demás y mostrará sólo uno...
//	if (menuPorDefecto != null && menuPorDefecto != "")
//		mostrarMenu(menuPorDefecto);

}

function mostrarMenu(nombreMenu){
	var idMenu = getMenuByName(nombreMenu);
	var menuAMostrar = gmobj("menu" + idMenu);

	if (menuAMostrar != null)
		menuAMostrar.style.display  = "block";
	
	//Esto evita entrar al ciclo para buscar a todos los menus...
	 // Escondemos el menu que se muestra actualmente utilizando la variable cache, que no sea el actual
	if ( cacheMenuMostrado >= 0 && cacheMenuMostrado!= idMenu)
		$("menu"+cacheMenuMostrado).style.display  = "none";
	else { //Si no hay ninguno hacer un ciclo para esconder todos los demás...
		
			for (var a =0; a < numeroMenu; a++){
				if ( a != idMenu)
						$("menu"+a).style.display  = "none";
			}//fin del for				
	}

	cacheMenuMostrado = idMenu; // Cache del Menú que se muestra actualmente...
}
*/

/****************************************************** NOTA IMPORTANTE: *********************************************************/
/* Cada vez que se agregue un menú nuevo hay que hacer una funcion similar a las siguientes y agregarla en la funcion pintarMenus*/

function menuCatastro(){

	with(catastro=new menuname("catastro")){
		style=mainStyleHoriz;
		top=82;
		//left=129;
		orientation="horizontal";
		alwaysvisible=1;
		itemheight=26;



		aI("text=Inicio;status=inicio;url=../../modulos/catastro/main.html;target=mainFrame;image=../../imagenes/menu/inicio.png;");
		aI("text=Predios;status=Predios;showmenu=catPredios;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();image=../../imagenes/menu/predios.png;");
	        aI("text=Trámites;status=Trámites;showmenu=catTramites;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();image=../../imagenes/menu/reportes.png;")
		aI("text=Valuación;status=Valuación;showmenu=catValuaciones;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();image=../../imagenes/menu/valuaciones.png;");		
	   
aI("text=Catalogos;status=Catalogos;showmenu=catConsultas;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();image=../../imagenes/menu/consultas.png;");

		aI("text=Reportes;status=Reportes;showmenu=catReportes;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();image=../../imagenes/menu/reportes.png;");

		aI("text=Cerrar Sesión;status=Cerrar Sesión;title=Cerrar Cesión;clickfunction=salirDelSistema();target=mainFrame;image=../../imagenes/menu/logout.png;");
};	

	numeroMenu++;
	drawMenus();
}//Fin del menú catastro

function menuParticipacion(){
	with(participaciones=new menuname("participaciones")){
		style=mainStyleHoriz;
		top=82;
		//left=129;
		orientation="horizontal";
		alwaysvisible=1;
		itemheight=26;
		aI("text=Inicio;url=../jsp/marcos/principal.html;target=mainFrame;");
		aI("text=Participacion;showmenu=sub1;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();");
		aI("text=Participacion;showmenu=sub2;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();");
		aI("text=Participacion;showmenu=sub3;target=mainFrame;onfunction=openSubmenu();offfunction=closeSubmenu();");
		aI("text=Participacion mainFrame;url=http://www.google.com/;target=mainFrame;")
		aI("text=Participacion new;url=http://www.google.com/;target=_new;")
	}		
	numeroMenu++;
	drawMenus();
}


