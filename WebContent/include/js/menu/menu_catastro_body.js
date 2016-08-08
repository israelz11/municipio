// JavaScript Document

_menuCloseDelay=0           // The time delay for menus to remain visible on mouse out
_menuOpenDelay=0            // The time delay before menus open on mouse over
_subOffsetTop=0              // Sub menu top offset
_subOffsetLeft=0            // Sub menu left offset

/// Style Definitions ///

/*
function encontrarUrl(){
	var url = document.location.toString();
	var num = 0;
		num =  url.lastIndexOf("/"); 
	if ( num > 0) {
		result = url.substring(0,(num)); 	
		direccion = result; 
	}//Fin del if
}//fin de la función
*/


with(subStyle=new mm_style()){

styleid=1;
bordercolor="#999999";
borderstyle="solid";
borderwidth=1;
fontfamily="Verdana, Arial, Helvetica, sans-serif; ";
fontsize="12px";
fontstyle="normal";
headerbgcolor="#AFD1B5";
headerborder=1;
headercolor="#000099";
imagepadding=3;
offbgcolor="transparent";
offcolor="#333333";
//E7E8E3";
onbgcolor="#E4E5E0";

onborder="1px solid #999999";
oncolor="#9E2F35";
menubgimage="../../imagenes/backoff_red.png";  
image="../../imagenes/18_blank.gif";
pageimage="../../imagenes/db_red.gif";
onsubimage="../../imagenes/submenu-on.gif";
subimage="../../imagenes/submenu-off.gif";
outfilter="randomdissolve(duration=0.2)";
overfilter="Fade(duration=0.1);Alpha(opacity=95);Shadow(color=#777777', Direction=135, Strength=3)";
padding=3;
pagebgcolor="#CFE2D1";
pagecolor="#000066";

separatoralign="right";
separatorcolor="#BB0000";
separatorpadding=1;
separatorwidth="80%";
menubgcolor="#F5F5F5";
}

/// Submenu Definitions ///

with(milonic=new menuname("catPredios")){
style=subStyle;
top=-84;

aI("text=&nbsp;Buscar Predio;status=Buscar Predio;url=../../catastro/buscarPredio/buscarPredio.action;");
aI("text=&nbsp;Registrar Predio;status=Registrar Predio;url=../../catastro/predio/detallePredio.html;separatorsize=2");
aI("text=&nbsp;Buscar Persona;status=Buscar Persona;url=../../catastro/buscarPersona/buscarPersona.action;");
aI("text=&nbsp;Registrar Persona;status=Registrar Persona;url=../../catastro/registrarPersona/registrarPersona.action;separatorsize=2");

aI("text=&nbsp;EJEMPLO TAB;status=Registrar Predio;url=../../catastro/carpeta/ejemploTab.html;");

aI("text=Sub1.1;showmenu=sub1.1;target=mainFrame;");
aI("text=Sub1.2;showmenu=sub1.2;");
aI("text=Sub1.3;showmenu=sub1.3;");
aI("text=Sub1.4;showmenu=sub1.4;");
}


with(milonic=new menuname("catTramites")){
style=subStyle;
top=-84;

aI("text=&nbsp;Buscar Trámite;status=Buscar Trámite;url=../../catastro/consultas/consultaDeTramite.action;");
aI("text=&nbsp;Registrar Predio;status=Registrar Predio;url=../../catastro/predio/detallePredio.html;separatorsize=2");
aI("text=&nbsp;Buscar Persona;status=Buscar Persona;url=../../catastro/buscarPersona/buscarPersona.action;");
aI("text=&nbsp;Registrar Persona;status=Registrar Persona;url=../../catastro/registrarPersona/registrarPersona.action;separatorsize=2");

aI("text=&nbsp;EJEMPLO TAB;status=Registrar Predio;url=../../catastro/carpeta/ejemploTab.html;");

aI("text=Sub1.1;showmenu=sub1.1;target=mainFrame;");
aI("text=Sub1.2;showmenu=sub1.2;");
aI("text=Sub1.3;showmenu=sub1.3;");
aI("text=Sub1.4;showmenu=sub1.4;");
}


with(milonic=new menuname("catValuaciones")){
style=subStyle;
top=-84;

aI("text=&nbsp;Buscar Valuación;status=Registrar Valuación;url=../../catastro/valuacion/consultaValuacion.html;");
aI("text=&nbsp;Registrar Valuacion;status=Registrar Valuación;url=../../catastro/valuacion/valuacion.html;");
aI("text=Sub1.1;showmenu=sub1.1;target=mainFrame;");
}

with(milonic=new menuname("catConsultas")){
style=subStyle;
top=-84;
aI("text=&nbsp;Consultas... Valuaciones;status=Registrar Valuaciones;url=../../catastro/valuacion/consultaValuacion.html;");
aI("text=&nbsp;Áreas Generales;status=Areas Generales;url=../../catastro/catalogos/areasGenerales.action;");
aI("text=&nbsp;Valores Construccion;showmenu=catVloresContruccion;target=mainFrame;");
aI("text=&nbsp;Tipos de Escritura;status=Tipos de Escritura;url=../../catastro/catalogos/tipoDeEscritura.action;");
}


with(milonic=new menuname("catVloresContruccion")){
style=subStyle;
aI("text=&nbsp;Tipos de Construccion;status=Tipos de Construccion;url=../../catastro/catalogos/tipos_construccion/buscar.action;");
aI("text=&nbsp;Clases de Construccion;status=Clases de Construccion;url=../../catastro/catalogos/claseConstruccion.action;");
aI("text=&nbsp;Estados de Conservacion;status=Estados de Conservacion;url=../../catastro/catalogos/estadosConservacion.action;");
}

with(milonic=new menuname("catReportes")){
style=subStyle;
top=-84;
aI("text=&nbsp;Buscar Predio;status=Buscar Predio;url=../../catastro/predio/buscarPredio.html;");
aI("text=&nbsp;Registrar Predio;status=Registrar Predio;url=../../catastro/predio/detallePredio.html;separatorsize=2");
aI("text=&nbsp;Buscar Persona;status=Buscar Persona;url=../../catastro/buscarPersona/buscarPersona.action;");
aI("text=&nbsp;Registrar Persona;status=Registrar Persona;url=../../catastro/registrarPersona/registrarPersona.action;separatorsize=2");
}



with(milonic=new menuname("sub1")){
style=subStyle;
top=-84;
aI("text=&nbsp;Consulta 1;status=Consulta 1;url=../index.htm;separatorsize=2;image=../../imagenes/grey_13x13_wb.gif");
aI("text=Sub1.1;showmenu=sub1.1;target=mainFrame;");
aI("text=Sub1.2;showmenu=sub1.2;");
aI("text=Sub1.3;showmenu=sub1.3;");
aI("text=Sub1.4;showmenu=sub1.4;");
}


with(milonic=new menuname("sub3")){
style=subStyle;
top=-84;

aI("text=Sub3.1;showmenu=sub1.1;");
aI("text=Sub3.2;showmenu=sub1.2;");
aI("text=Sub3.3;showmenu=sub1.3;");
aI("text=Sub3.4;showmenu=sub1.4;");
}

with(milonic=new menuname("sub4")){
style=subStyle;
top=-84;
aI("text=Sub4.1;showmenu=sub1.1;");
aI("text=Sub4.2;showmenu=sub1.2;");
aI("text=Sub4.3;showmenu=sub1.3;");
aI("text=Sub4.4;showmenu=sub1.4;");
}

with(milonic=new menuname("sub1.1")){
style=subStyle;
aI("text=Open page1;url=page1.htm;");
aI("text=castastro;url=../catastro/principal.html;target=mainFrame;");
aI("text=carpeta;url=../catastro/carpeta/principal.html;target=mainFrame;");

aI("text=Open page2;url=page2.htm;");
}

with(milonic=new menuname("sub1.2")){
style=subStyle;
aI("text=Open page1;url=page1.htm;");
aI("text=Open page2;url=page2.htm;");
aI("text=Open page3;url=page3.htm;");
}

with(milonic=new menuname("sub1.3")){
style=subStyle;
aI("text=Open page1;url=page1.htm;");
aI("text=Open page2;url=page2.htm;");
aI("text=Open page3;url=page3.htm;");
aI("text=Open page4;url=page4.htm;");
}

with(milonic=new menuname("sub1.4")){
style=subStyle;
aI("text=Open page1;url=page1.htm;");
aI("text=Open page2;url=page2.htm;");
aI("text=Open page3;url=page3.htm;");
aI("text=Open page4;url=page4.htm;");
aI("text=Open page5;url=page5.htm;target=_new;");
}


drawMenus();
