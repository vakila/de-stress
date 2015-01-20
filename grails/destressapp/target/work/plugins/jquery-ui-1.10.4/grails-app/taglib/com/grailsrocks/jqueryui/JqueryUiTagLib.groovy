package com.grailsrocks.jqueryui

class JqueryUiTagLib {

    static namespace = "jqui"

    static writeAttrs( attrs, output) {
        // Output any remaining user-specified attributes
        attrs.each { k, v ->
            output << k
            output << '="'
            output << v.encodeAsHTML()
            output << '" '
        }
    }

    static LINK_WRITERS = [
        js: { url, constants, attrs ->
            def o = new StringBuilder()
            o << "<script src=\"${url.encodeAsHTML()}\" "

            // Output info from the mappings
            writeAttrs(constants, o)
            writeAttrs(attrs, o)

            o << '></script>'
            return o
        },

        link: { url, constants, attrs ->
            def o = new StringBuilder()
            o << "<link href=\"${url.encodeAsHTML()}\" "

            // Output info from the mappings
            writeAttrs(constants, o)
            writeAttrs(attrs, o)

            o << '/>'
            return o
        }
    ]

    static RESOURCE_MAPPINGS = [
        css:[type:"text/css", rel:'stylesheet'],
        rss:[type:'application/rss+xml', rel:'alternate'],
        atom:[type:'application/atom+xml', rel:'alternate'],
        favicon:[type:'image/x-icon', rel:'shortcut icon'],
        appleicon:[type:'image/x-icon', rel:'apple-touch-icon'],
        js:[writer:'js', type:'text/javascript']
    ]

    static CDN_URLS = [
        googlecode: [
            css:{jqver, theme, min -> "http://ajax.googleapis.com/ajax/libs/jqueryui/$jqver/themes/$theme/jquery-ui.css"},
            js:{jqver, theme, min -> "http://ajax.googleapis.com/ajax/libs/jqueryui/$jqver/jquery-ui.${min ? 'min.js' : 'js'}"}
        ]
    ]

    def pluginManager

    def resources = { attrs ->
        def theme = attrs.theme ?: 'ui-lightness'
        def plugin = pluginManager.getGrailsPlugin('jquery-ui')
        def jqver = plugin.instance.JQUERYUI_VERSION
        // If a plugin is using THIS plugin, if they change theme they need to be able
        // to specify their plugin name so we can load the theme!
        def plug = attrs.plugin ? attrs.plugin : 'jqueryUi'
        def themedir = attrs.themeDir ? attrs.themeDir + '/' + theme : 'jquery-ui/themes/'+theme
        def themeCSS = attrs.themeCss

        // if nothing is in config, serve it minified to stay compatible with older plugin versions
        def min = grailsApplication.config.jqueryUi.get('minified', true);
        // must point to a key in CDN_URLS (currently 'googlecode' is the only key there)
        def cdn = grailsApplication.config.jqueryUi.cdn
        if (cdn && !CDN_URLS[cdn]) {
            throwTagError "Unknown CDN name: ${cdn}"
        }

        // use the theme css from cdn only if it's not provided by the app or a plugin.
        if (cdn && !attrs.plugin && !attrs.themeDir) {
            out << cdnLink (cdn:cdn, type:'css', jqver:jqver, theme:theme, minified:min, id:'jquery-ui-theme')
        }
        else {
            if (themeCSS) {
                out << resourceLink(url: themeCSS, type:'css')
            } else {
                out << resourceLink(plugin: plug, type:'css', dir: themedir,
                                    file:'jquery-ui-'+jqver+'.custom.css', media:'screen, projection',
                                    id:'jquery-ui-theme')
            }
        }

        // use the .js always from cdn if requested
        if (cdn) {
            out << cdnLink (cdn:cdn, type:'js', jqver:jqver, theme:theme, minified:min)
        }
        else {
            out << resourceLink(plugin: 'jqueryUi', type:'js', dir:'jquery-ui/js',
                                file:"jquery-ui-${jqver}.custom.${min ? 'min.js' : 'js'}")
        }
    }

    def resourceLink = { attrs ->
        def t = attrs.remove('type')
        def typeInfo = [:] + RESOURCE_MAPPINGS[t]
        if (!typeInfo) {
            throwTagError "Unknown resourceLink type: ${t}"
        }
        def url = attrs.remove('url')
        if (!url) {
            url = g.resource(plugin:attrs.remove('plugin'), dir:attrs.remove('dir'), file:attrs.remove('file'))
        }

        def writerName = typeInfo.remove('writer')
        def writer = LINK_WRITERS[writerName ?: 'link']
        out << writer(url, typeInfo, attrs)
    }

    def cdnLink = { attrs ->
        def t = attrs.type
        def cdnName = attrs.remove('cdn')
        def jqver = attrs.remove('jqver')
        def theme = attrs.remove('theme')
        def min = attrs.remove('minified')

        def cdnUrlHandler = CDN_URLS[cdnName]
        if (!cdnUrlHandler) {
            throwTagError "Unknown CDN name: ${cdnName}"
        }

        def url = cdnUrlHandler."$t"(jqver, theme, min)
        out << resourceLink (attrs + [url:url])
    }
}
