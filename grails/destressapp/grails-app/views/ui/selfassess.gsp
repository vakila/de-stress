<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>de-stress Exercise</title>

	</head>
	<body>

		<div id="page-body" role="main">
			<!--<h1>Exercise ${ex.name}</h1>-->

			<div style="margin-top:40px"><!--Display Exercise info-->
				<!--
                <p>${ex.description}</p>

                <p>Word: ${ex.word.text} </p>

                <br>-->

				<div style="width:90%; text-align:center; font-size:2em; margin-left:auto; margin-right:auto">
					${ex.word.wordsBefore}
                    <!--<g:link controller="Word" action="show" id="${ex.word.id}">-->

						<!--
						<g:each var="syll" in="${ex.word.syllables}">
                            <a><span style="font-size:2em;">${syll}</span></a>
                        </g:each>
						-->

                    <!--</g:link>-->
					<a><b><g:each var="syll" in="${ex.word.syllables}">${syll}</g:each></b></a>
                    ${ex.word.wordsAfter}
				</div>

				<div style="margin-top:40px; margin-left:auto; margin-right:auto; width:90%;">
					<!--<div style="text-align:center;">-->
					<div>
						<div style="padding:10px;">
							<table style="border-bottom:1px solid #DFDFDF;">
							<tr>

								<td style="width:40%;vertical-align:middle">
									<h3>Your utterance:</h3>
								</td>
								<td style="width:60%;vertical-align:middle">
									<p>${studUtt}</p>
									<!--<audio src="${studWav}" controls></audio>-->
									<audio src="<g:resource dir="audio" file="${studWav}" />" controls></audio>
									<p><g:link action="download" id="${studUtt.id}">Download</g:link></p>
								</td>
								</tr>
							</table>
						</div>


						<g:each var="refUtt" in="${refUtts}">
							<div style="padding:10px;">
								<table style="border-bottom:1px solid #DFDFDF;">
								<tr>

									<td style="width:40%;vertical-align:middle">
										<h3>Reference utterance ${refUtts.indexOf(refUtt)+1}:</h3>
									</td>
									<td style="width:60%;vertical-align:middle">
										<p>${refUtt}</p>
										<audio src="<g:resource dir="audio" file="${refUtt.sentenceUtterance.sampleName + ".wav"}" />" controls></audio>
										<p><g:link action="download" id="${refUtt.id}">Download</g:link></p>
									</td>
									</tr>
								</table>
							</div>
						</g:each>


					</div>

				</div>

				<div>
					<g:form controller="ui" action="selfassess" id="${ex.id}">
						<p>Before I give you feedback on this word,
						listen your utterance and the reference utterance.</p>

						<p>Think about how you stressed the word "<g:each var="syll" in="${ex.word.syllables}">${syll}</g>",
							and how the reference speaker stressed that word.</p>

						<p>Then answer these questions:</p>

						<!--<label>Which syllable should be stressed in this word?</label>-->


			            <label>Which syllable did you stress?</label>
						<g:radioGroup name="stressedSyll"
						              labels="['The first syllable (correct)',
											   'The second syllable (incorrect)',
											   'Neither syllable (incorrect)']"
						              values="['correct','incorrect','none']">
						<p>${it.label} ${it.radio}</p>
						</g:radioGroup>

						<label>Is the stress in this word as clear in your utterance as in the reference utterance?</label>
						<g:radioGroup name="clearEnough"
						              labels="['Yes','No','I'm not sure']"
						              values="['yes','no','unsure']">
						<p>${it.label} ${it.radio}</p>
						</g:radioGroup>


			            <label>What could you work on for next time?</label>
			            <g:textField name="advice"/><br/>

			            <g:actionSubmit value="Save & get feedback"/>
			        </g:form>
				</div>

			</div>
		</div>
	</body>
</html>
