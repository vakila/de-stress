package com.burtbeckwith.grails.plugin.famfamfam

class FamfamfamTagLib {

	static namespace = 'fam'

	def icon = { attrs ->
		out << resource(dir: '/images/icons/', file: attrs.name + '.png', plugin:'famfamfam')
	}
}
