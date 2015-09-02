<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>de-stress Admin</title>

	</head>
	<body>

		<div id="page-body" role="main">
			<h2 style="width:40%;margin:auto;">Welcome to de-stress Admin</h2>
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
				<h2 style="padding:10px; margin:5px;">
					<g:link controller="${m}" title="View ${m} list">${m}s (${grailsApplication.getDomainClass("org.ifcasl.destress.${m}").clazz.count()})</g:link>
					<g:link controller="${m}" action="create" title="Create new ${m}">
						<!-- + -->
						<i class="fa fa-plus"></i>
					</g:link>
				</h2>
				</g:each>

			</div>
		</div>
	</body>
</html>
