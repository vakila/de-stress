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
											<g:if test="${ex.feedbackMethod.displayShapes == true}">
												<div style="height:100px;display:table-cell;vertical-align:bottom;">
												<div style="height:${studSyllF0s[s]*50}px;
															width:${studSyllDurs[s]*300}px;
															opacity:${studSyllInts[s]};
															background:blue;border:1px solid blue;border-radius:10px;margin-bottom:3px;"
													 title="Duration (width): ${(studSyllDurs[s]*100).round(2)}% of word  &#013Pitch (height): ${(studSyllF0s[s]*100).round(2)}% of mean &#013Intensity (darkness): ${studSyllInts[s]}% of mean">
												</div>
												</div>
											</g:if>
										<a>
											<g:if test="${ex.feedbackMethod.styleText == true}">
												<div style="height:80px;display:table-cell;vertical-align:bottom;">
												<span style="font-size:${studSyllDurs[s]*3}em" title="Syllable duration: ${studSyllDurs[s]}% of word">
											</g:if>
													${ex.word.syllables[s]}
											<g:if test="${ex.feedbackMethod.styleText == true}">
												</span>
												</div>
											</g:if>
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
													<g:if test="${ex.feedbackMethod.displayShapes == true}">
														<div style="height:100px;display:table-cell;vertical-align:bottom;">
														<div style="height:${refSyllF0s[refUtts.indexOf(refUtt)][s]*50}px;
																	width:${refSyllDurs[refUtts.indexOf(refUtt)][s]*300}px;
																	opacity:${refSyllInts[refUtts.indexOf(refUtt)][s]};
																	background:green;border:1px solid green;border-radius:10px;margin-bottom:3px;"
															title="Duration: ${(refSyllDurs[refUtts.indexOf(refUtt)][s]*100).round(2)}% of word &#013Pitch: ${refSyllF0s[refUtts.indexOf(refUtt)][s].round(2)*100}% of mean &#013Intensity (darkness): ${refSyllInts[refUtts.indexOf(refUtt)][s]}% of mean">
														</div>
														</div>
													</g:if>
						                            <a>
														<g:if test="${ex.feedbackMethod.styleText == true}">
															<div style="height:80px;display:table-cell;vertical-align:bottom;">
															<span style="font-size:${refSyllSizes[refUtts.indexOf(refUtt)][s]}em">
														</g:if>
																${ex.word.syllables[s]}
														<g:if test="${ex.feedbackMethod.styleText == true}">
															</span>
															</div>
														</g:if>
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


				<!--<g:if test="${diag.label}">
					<div style="margin-top:40px;text-align:center;">
						<h2>Diagnosis: </h2>
						<h2 style="color:${labelCol}">${diag.label}</h2>
					</div>
				</g:if>-->

				<g:if test="${ex.feedbackMethod.displayMessage == true && diag.label}">
					<div style="margin-top:40px;text-align:center;">
						<h2 style="color:${labelCol}">${labelMsg}</h2>
					</div>
				</g:if>

				<g:if test="${ex.feedbackMethod.showSkillBars == true}">
					<div style="margin-top:40px;text-align:center;">
						<h2>Your scores</h2>
						<table>

							<!-- EACH SKILL -->
							<g:each var="skillStuff" in="${[durStuff, f0Stuff, intStuff]}">
								<g:set var="skillName" value="${skillStuff[0]}" />
								<g:set var="skillPct" value="${skillStuff[1]}" />
								<g:set var="skillCol" value="${skillStuff[2]}" />
								<g:set var="skillMsg" value="${skillStuff[3]}" />
								<tr>
									<td style="width:10%;vertical-align:middle;"><h3>${skillName}</h3></td>
									<td style="width:80%">
										<div style="width:100%; margin:5px; border:1px solid black">
											<div style="height:40px; background:${skillCol}; width:${skillPct}%"></div>
										</div>
									</td>
									<td style="width:10%;vertical-align:middle;text-align:right">${(skillPct/10).round()}/10</td>
								</tr>
								<g:if test="${ex.feedbackMethod.displayMessage == true}">
									<tr><td></td><td>${skillMsg}</td><td></td></tr>
									<tr><td><br></td></tr>
								</g:if>
							</g:each>

							<!--<tr><td><br></td></tr>-->

							<!-- OVERALL -->
							<tr>
								<td style="width:10%;vertical-align:middle;"><h3>Overall</h3></td>
								<td style="width:80%">
									<div style="width:100%; margin:5px; border:1px solid black">
										<div style="height:60px; background:${allCol}; width:${allPct}%"></div>
									</div>
								</td>
								<td style="width:10%;vertical-align:middle;text-align:right">${(allPct/10).round()}/10</td>
								<tr><td></td><td>Explanation of overall score composition</td><td></td></tr>
							</tr>
						</table>
					</div>
				</g:if>
			</div>
		</div>
	</body>
</html>
