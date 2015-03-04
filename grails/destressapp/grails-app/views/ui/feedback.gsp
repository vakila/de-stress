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

				<div style="width:90%; text-align:center; font-size:2em; margin-left:auto; margin-right:auto">
					${ex.word.wordsBefore}
                    <!--<g:link controller="Word" action="show" id="${ex.word.id}">-->
                        <g:each var="syll" in="${ex.word.syllables}">
                            <a><span style="font-size:2em">${syll}</b></a>
                        </g:each>
                    <!--</g:link>-->
                    ${ex.word.wordsAfter}</div>

			</div>


			<div style="margin-top:40px; margin-left:auto; margin-right:auto; width:60%; text-align:center;">
				<div style="text-align:center;">
					<div style="padding:10px;">
						<h3>Your utterance:</h3>
						<p>${studUtt}</p>
						<!--<audio src="${studWav}" controls></audio>-->
						<audio src="<g:resource dir="audio" file="${studWav}" />" controls></audio>
						<p><g:link action="download" id="${studUtt.id}">Download</g:link></p>
					</div>
					<g:each var="refUtt" in="${refUtts}">
						<div style="padding:10px;">
							<h3>Reference utterance ${refUtts.indexOf(refUtt)+1}:</h3>
							<p>${refUtt}</p>
							<audio src="<g:resource dir="audio" file="${refUtt.sentenceUtterance.sampleName + ".wav"}" />" controls></audio>
							<p><g:link action="download" id="${refUtt.id}">Download</g:link></p>
						</div>
					</g:each>
					<g:if test="${fbWav != null}">
						<div style="padding:10px;">
							<h3>Feedback utterance:</h3>
							<p>${diag}</p>
							<audio src="<g:resource dir="audio/feedback" file="${fbWav}" />" controls></audio>
							<p><g:link action="downloadFeedback" id="${diag.id}">Download</g:link></p>
						</div>
					</g:if>
				</div>


				<div style="margin-top:40px;">
					<h2>Your scores</h2>
					<table>
						<tr><td style="width:10%">Duration</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:40px; background:${durCol}; width:${durPct}%"></div></div></td><td style="width:10%">${durPct}%</td>
						<tr><td style="width:10%">Pitch</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:40px; background:${f0Col}; width:${f0Pct}%"></div></div></td><td style="width:10%">${f0Pct}%</td>
						<tr><td style="width:10%">Intensity</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:40px; background:${intCol}; width:${intPct}%"></div></div></td><td style="width:10%">${intPct}%</td>
						<tr><td style="width:10%">Overall</td><td style="width:80%"><div style="width:100%; margin:5px; border:1px solid black"><div style="height:50px; background:${allCol}; width:${allPct}%"></div></div></td><td style="width:10%">${allPct}%</td>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>
