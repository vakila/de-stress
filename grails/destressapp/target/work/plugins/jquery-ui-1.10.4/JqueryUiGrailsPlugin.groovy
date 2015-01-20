class JqueryUiGrailsPlugin {
    // @todo This MUST be updated when you update the version of jquery ui
    static JQUERYUI_VERSION = "1.10.4"

    // Put in here the minor revision of this plugin
    static PLUGIN_MINOR_REVISION = ""

    // the version attribute must be constant, so manually update this to be
    // JQUERYUI_VERSION and PLUGIN_MINOR_REVISION concatenated, with a '.'
    // if PLUGIN_MINOR_REVISION isn't blank
    def version = '1.10.4'


    def grailsVersion = "2.0 > *"
    def title = "jQuery UI Plugin"
    def description = 'Simply supplies jQuery UI resources, depends on jQuery plugin. Use this plugin to avoid resource duplication and conflicts.'

    def documentation = "http://grails.org/plugin/jquery-ui"
    def license = "APACHE"
    def organization = [name: "Grails Plugin Collective", url: "http://github.com/gpc"]
    def developers = [
        [name: 'Marc Palmer', email: 'marc@grailsrocks.com']
    ]
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPJQUERYUI"]
    def scm = [url: 'https://github.com/gpc/grails-jquery-ui']
}
