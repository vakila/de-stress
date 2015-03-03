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
				<!--
                <p>${ex.description}</p>

                <p>Word: ${ex.word.text} </p>

                <br>
				-->
				<div style="width:90%; text-align:center; font-size:2em; margin-left:auto; margin-right:auto">
					${ex.word.wordsBefore}
                    <!--<g:link controller="Word" action="show" id="${ex.word.id}">-->
                        <g:each var="syll" in="${ex.word.syllables}">
                            <a><span style="font-size:2em">${syll}</b></a>
                        </g:each>
                    <!--</g:link>-->
                    ${ex.word.wordsAfter}
				</div>

			</div>

			<br>


			<div style="padding:20px; margin:auto; width:60%;">
				<h3>Choose utterance to evaluate</h3>

				<g:form controller="ui"
						action="feedback"
						id="${ex.id}">
					<table style="margin:auto">
					<tr>
			            <td><label><h4>Student utterance:</h4> </label></td>
						<td><g:select 	id="fgUtts.id" name="fgUtts.id"
			  						from="${fgUtts}"
									optionKey="id"
									/></td>
					</tr>
					<g:if test="${ggUtts}">
						<g:each var="i" in="${nRefs}">
							<tr>
							<td><label><h4>Reference utterance ${i}:</h4> </label></td>
							<td><g:select 	id="ggUtts.${i}" name="ggUtts.${i}"
				  						from="${ggUtts}"
										optionKey="id"
										/></td>
							</tr>
						</g:each>

					</g:if>
					</table>

		            <g:submitButton name="Submit" value="Submit"/>
		        </g:form>

			</div>
		</div>
	</body>
</html>
