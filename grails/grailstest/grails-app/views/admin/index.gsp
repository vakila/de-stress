<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to de-stress admin</title>
		
	</head>
	<body>

		<div id="page-body" role="main">
			<h1>Welcome to de-stress admin</h1>
		<!-- 
			<p>Congratulations, you have successfully started your first Grails application! At the moment
			   this is the default page, feel free to modify it to either redirect to a controller or display whatever
			   content you may choose. Below is a list of controllers that are currently deployed in this application,
			   click on each to execute its default action:</p>

			<div id="controller-list" role="navigation">

				<h2>Available Controllers:</h2>
				<ul>	
					<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
						<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
					</g:each>
				</ul>
			</div>
		-->
			<div>
				<g:each var="m" in="${modelList}">
					<p><g:link controller="${m}">${m}s</g:link> <g:link controller="${m}" action="create">+</g:link></p>
				</g:each>
				
			</div>
		</div>
	</body>
</html>
