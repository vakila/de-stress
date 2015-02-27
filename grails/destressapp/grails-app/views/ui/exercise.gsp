<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>de-stress Exercise</title>

	</head>
	<body>

		<div id="page-body" role="main">
			<h1>Exercise ${ex.name}</h1>

			<div>

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
		</div>
	</body>
</html>
