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

			<br>


			<div>
				<p>Choose utterances to compare</p>
				<g:form controller="ui"
						action="diagnosis"
						id="${ex.id}" >
		            <label>Student word utterance: </label>
					<g:select 	id="fgUtts.id" name="fgUtts.id"
		  						from="${fgUtts}"
								optionKey="id"
								/>

					<label>Reference word utterance: </label>
					<g:select 	id="ggUtts.id" name="ggUtts.id"
		  						from="${ggUtts}"
								optionKey="id"
								/>


		            <g:submitButton name="Submit" value="Submit"/>
		        </g:form>
			</div>
		</div>
	</body>
</html>
