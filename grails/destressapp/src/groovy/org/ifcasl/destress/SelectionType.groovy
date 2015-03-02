package org.ifcasl.destress

enum SelectionType {
    MANUAL('MANUAL'),   //Student chooses
    AUTO('AUTO'),       //System chooses
    FIXED('FIXED')      //Teacher or researcher chooses (in advance)


    String value

    SelectionType(value) {
        this.value = value
    }
}
