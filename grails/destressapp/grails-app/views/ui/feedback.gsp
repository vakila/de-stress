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
					<a><b>${ex.word.text}</b></a>
                    ${ex.word.wordsAfter}</div>

			</div>


			<div style="margin-top:40px; margin-left:auto; margin-right:auto; width:90%;">
				<!--<div style="text-align:center;">-->
				<div>
					<div style="padding:10px;">
						<table style="border-bottom:1px solid #DFDFDF;">
						<tr>

							<td style="width:20%;vertical-align:middle">
								<h3>Your utterance:</h3>
							</td>

							<td style="width:50%;vertical-align:middle">
								<div style="width:90%; text-align:left; font-size:2em; margin-left:auto; margin-right:auto;">
			                    <!--<g:link controller="Word" action="show" id="${ex.word.id}">-->
									<g:each var="s" in="${ (0..1) }">
										<div style="display:table-cell;padding:2px;text-align:left">
											<div style="height:40px;width:${studSyllDurs[s]*300}px;background:blue;border:1px solid blue;border-radius:10px;margin-bottom:3px;"
												 title="Duration: ${(studSyllDurs[s]*100).round(2)}% of word">
											</div>
										<a>
											<g:if test="${ex.feedbackMethod.styleText == true}">
												<span style="font-size:${studSyllDurs[s]*3}em" title="Syllable duration: ${studSyllDurs[s]}% of word">
											</g:if>
												${ex.word.syllables[s]}
											</span>

										</a>
										</div>
									</g:each>
			                    <!--</g:link>-->
								</div>
							</td>

							<td style="width:30%;vertical-align:middle">
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

								<td style="width:20%;vertical-align:middle">
									<h3>Reference utterance ${refUtts.indexOf(refUtt)+1}:</h3>
								</td>
								<td style="width:50%;vertical-align:middle">
									<div style="width:90%; text-align:left; font-size:2em; margin-left:auto; margin-right:auto;">
					                        <g:each var="s" in="${ (0..1) }">
												<div style="display:table-cell;padding:2px;text-align:left">
													<div style="height:40px;width:${refSyllDurs[refUtts.indexOf(refUtt)][s]*300}px;background:blue;border:1px solid blue;border-radius:10px;margin-bottom:3px;"
														title="Duration: ${(refSyllDurs[refUtts.indexOf(refUtt)][s]*100).round(2)}% of word">
													</div>
						                            <a>
														<g:if test="${ex.feedbackMethod.styleText == true}">
															<span style="font-size:${refSyllSizes[refUtts.indexOf(refUtt)][s]}em">
														</g:if>
														<!--<span style="letter-spacing:${refSyllSizes[refUtts.indexOf(refUtt)][s]}px">-->
														${ex.word.syllables[s]}
													</a>
												</div>
					                        </g:each>
									</div>
								</td>

								<td style="width:30%;vertical-align:middle">
									<p>${refUtt}</p>
									<audio src="<g:resource dir="audio" file="${refUtt.sentenceUtterance.sampleName + ".wav"}" />" controls></audio>
									<p><g:link action="download" id="${refUtt.id}">Download</g:link></p>
								</td>
								</tr>
							</table>
						</div>
					</g:each>
					<g:if test="${ex.feedbackMethod.playFeedbackSignal == true}">
						<g:if test="${fbWav != null}">
							<div style="padding:10px">
								<table style="border-bottom:1px solid #DFDFDF;">
									<tr>

									<td style="width:70%;vertical-align:middle">
										<h3>Feedback utterance:</h3>
									</td>
									<td style="width:30%;vertical-align:middle">
										<p>${diag}</p>
										<audio src="<g:resource dir="audio/feedback" file="${fbWav}" />" controls></audio>
										<p><g:link action="downloadFeedback" id="${diag.id}">Download</g:link></p>
									</td>
									</tr>
								</table>
							</div>
						</g:if>
					</g:if>
				</div>

				<g:if test="${ex.feedbackMethod.showSkillBars == true}">
					<div style="margin-top:40px;text-align:center;">
						<h2>Your scores</h2>
						<table>
							<tr><td style="width:10%">Duration</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:40px; background:${durCol}; width:${durPct}%"></div></div></td><td style="width:10%">${durPct}%</td>
							<tr><td style="width:10%">Pitch</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:40px; background:${f0Col}; width:${f0Pct}%"></div></div></td><td style="width:10%">${f0Pct}%</td>
							<tr><td style="width:10%">Intensity</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:40px; background:${intCol}; width:${intPct}%"></div></div></td><td style="width:10%">${intPct}%</td>
							<tr><td style="width:10%">Overall</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:50px; background:${allCol}; width:${allPct}%"></div></div></td><td style="width:10%">${allPct}%</td>
						</table>
					</div>
				</g:if>
			</div>
		</div>
	</body>
</html>
