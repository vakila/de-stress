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

								<td style="width:50%;vertical-align:middle">
									<h3>Your utterance:</h3>
								</td>
								<td style="width:60%;vertical-align:middle">
									<p>
										<g:link action="download" id="${studUtt.id}" style="text-decoration:none;color:#333333;">
											<i class="fa fa-download"></i>
										</g:link>
										${studUtt}
									</p>
									<!--<audio src="${studWav}" controls></audio>-->
									<audio src="<g:resource dir="audio" file="${studWav}" />" controls></audio>
									<!--<p><g:link action="download" id="${studUtt.id}">Download</g:link></p>-->
								</td>
								</tr>
							</table>
						</div>


						<g:each var="refUtt" in="${refUtts}">
							<div style="padding:10px;">
								<table style="border-bottom:1px solid #DFDFDF;">
								<tr>

									<td style="width:50%;vertical-align:middle">
										<h3>Reference utterance ${refUtts.indexOf(refUtt)+1}:</h3>
									</td>
									<td style="width:60%;vertical-align:middle">
										<p>
											<g:link action="download" id="${refUtt.id}" style="text-decoration:none;color:#333333;">
												<i class="fa fa-download"></i>
											</g:link>
											${refUtt}
										</p>
										<audio src="<g:resource dir="audio" file="${refUtt.sentenceUtterance.sampleName + ".wav"}" />" controls></audio>
										<!--<p><g:link action="download" id="${refUtt.id}">Download</g:link></p>-->
									</td>
									</tr>
								</table>
							</div>
						</g:each>


					</div>

				</div>

				<div style="padding:20px; margin:auto; width:60%;">
					<h2>Self-assessment</h2>

					<p>Listen to your utterance and the reference utterance(s).</p>
					<p>Then answer these questions:</p>
					<!--
					<p>Before I give you feedback on this word,
					listen to your utterance and the reference utterance.</p>

					<p>Think about how you stressed the word "<g:each var="syll" in="${ex.word.syllables}">${syll}</g:each>",
						and how the reference speaker stressed that word.</p>

					<p>Then answer these questions:</p>-->

					<br>


					<g:form controller="ui" action="feedback" id="${ex.id}">


						<!--<label>Which syllable should be stressed in this word?</label>-->



			            <label><h4>Which syllable did you stress?</h4></label>
						<table>
						<g:radioGroup name="stressLabel"
						              labels="['The first syllable (correct)',
											   'The second syllable (incorrect)',
											   'Neither syllable (incorrect)']"
						              values="['correct','incorrect','none']">
						<tr>
							<td style="width:10%;text-align:right;">${it.radio}</td>
							<td style="width:90%;text-align:left;"> ${it.label}</td>
						</tr>
						</g:radioGroup>
						</table>

						<label><h4>Is the stress as clear in your utterance as it is in the reference utterance?</h4></label>
						<table>
						<g:radioGroup name="clearEnough"
						              labels="['Just as clear as in reference','Not as clear as in reference','I don\'t know']"
						              values="['clear','unclear','unsure']">
						<tr>
							<td style="width:10%;text-align:right;">${it.radio}</td>
							<td style="width:90%;text-align:left;">${it.label}</td>
						</tr>
						</g:radioGroup>
						</table>


			            <label><h4>What could you work on for next time?</h4></label>
						<table>
						<tr>
							<td style="width:10%;text-align:right;"></td>
							<td style="width:90%;text-align:left;"><g:textArea name="advice"/></td>
						</tr>
						</table>

						<br><br>

						<g:hiddenField name="fgUtts.id" value="${studUtt.id}" />
						<g:if test="${refUtts.size() > 0}">
							<g:each var="i" in="${1..refUtts.size()}">
								<g:hiddenField name="ggUtts.${i}" value="${refUtts[i-1].id}" />
							</g:each>
						</g:if>


			            <div style="text-align:center;margin:10px;">
							<g:submitButton name="Submit" value="Continue"/>
						</div>
			        </g:form>
				</div>

			</div>
		</div>
	</body>
</html>
