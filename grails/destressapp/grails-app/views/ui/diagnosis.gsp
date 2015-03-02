<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>de-stress Exercise</title>

	</head>
	<body>

		<div id="page-body" role="main">
			<h1>Exercise ${ex.name}</h1>

			<div><!--Display Exercise info-->

                <p>${ex.description}</p>

                <p>Word: ${ex.word.text} </p>

                <br>

                <p>${ex.word.wordsBefore}
                    <g:link controller="Word" action="show" id="${ex.word.id}">
                        <g:each var="syll" in="${ex.word.syllables}">
                            <b>${syll}</b>
                        </g:each>
                    </g:link>
                    ${ex.word.wordsAfter}</p>

			</div>

			<br><br>




			<div>
				<h4>Student utterance:</h4>
				<p>${studUtt}</p>
				<!--<audio src="${studWav}" controls></audio>-->
				<audio src="<g:resource dir="audio" file="${studWav}" />" controls></audio>
				<p><g:link action="download" id="${studUtt.id}">Download</g:link></p>

				<br>
				<h4>Reference utterance:</h4>
				<p>${refUtt}</p>
				<audio src="<g:resource dir="audio" file="${refWav}" />" controls></audio>
				<p><g:link action="download" id="${refUtt.id}">Download</g:link></p>

			</div>
		</div>
	</body>
</html>
