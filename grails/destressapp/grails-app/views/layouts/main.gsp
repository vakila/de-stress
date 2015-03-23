<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
		<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
  		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<g:layoutHead/>
		<!--<g:javascript library="jquery" />-->
	</head>
	<body>
		<!-- <div id="grailsLogo" role="banner">
			<a href="http://grails.org"><asset:image src="grails_logo.png" alt="Grails"/></a>  -->
		<div id="destressLogo" role="banner">
			<div style="display:block;float:right;padding-top:0.8em;margin-right:10px">
				<span class="fa-stack fa-2x">
				  <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
				  <i style="color:#1E90FF;" class="fa fa-user fa-stack-1x"></i>
				</span>
				<!--i style="color:#FFFFFF;" class="fa fa-caret-down fa-lg"></i-->
			</div>
			<a href="/"><h1>de-stress</h1></a>

		</div>
		<g:layoutBody/>
		<div class="footer" role="contentinfo"></div>
		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
	</body>
</html>
