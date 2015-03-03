package org.ifcasl.destress

enum SelectionType {
    MANUAL('MANUAL'),
    AUTO('AUTO'),
    FIXED('FIXED')

    // MANUAL > Student chooses
    // AUTO   > System chooses
    // FIXED  > Teacher or researcher chooses (in advance)

    String value

    SelectionType(value) {
        this.value = value
    }
}
