<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>de-stress Exercises</title>

	</head>
	<body>

		<div id="page-body" role="main">

			<h2 style="width:80%;padding:10px;margin:10px auto;">Welcome to de-stress! Please choose an exercise to complete.</h2>

			<!--<h3 style="padding-left:10px;">Exercises</h3>-->



			<g:each var="ex" in="${exercises}">
				<div style="width:200px; min-height:200px; padding:9px; margin:10px; border:1px solid gray; float:left;">
			    	<g:link controller="ui" action="prompt" id="${ex.id}">
						<h1>${ex.name}</h1>
					</g:link>

					<p style="font-weight:bold;">${ex.description}</p>

	                <p>Word:
						<g:each var="syll" in="${ex.word.syllables}">${syll}</g:each>
					</p>

					<p>DM: ${ex.diagnosisMethod}</p>
					<p>FB: ${ex.feedbackMethod}</p>
				</div>
			</g:each>

        </div>

	</body>
</html>
