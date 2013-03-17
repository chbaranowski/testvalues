package testvalues

abstract class Parameter {

    String name

    List validValues = []

    List invalidValues = []

    Boolean optional

    boolean definedValidValues = false

    boolean definedInvalidValues = false

    void setValidValues(List validValues) {
        definedValidValues = true
        this.validValues = validValues
    }

    List getValidValues() {
        List values = []
        if (definedValidValues) {
            values += validValues
        }
        if (optional) {
            values << null
        }
        values
    }

    void setInvalidValues(List invalidValues) {
        definedInvalidValues = true
        this.invalidValues = invalidValues
    }

    List getInvalidValues() {
        List values = []
        if (definedInvalidValues) {
            values += invalidValues
        }
        if (optional != null && !optional) {
            values << null
        }
        values
    }

}

class StringParameter extends Parameter {

    Integer maxLength

    Integer minLength

    Boolean empty

    List getValidValues() {
        List values = super.validValues
        if (empty) {
            values << ""
            values << "   "
        }
        if (!definedValidValues) {
            if (minLength) {
                values << text(length: minLength)
            }
            if (maxLength) {
                values << text(length: maxLength)
            }
        }
        values
    }

    List getInvalidValues() {
        List values = super.invalidValues
        if (empty != null && !empty) {
            values << ""
            values << "   "
        }
        if (minLength) {
            values << text(length: minLength - 1)
        }
        if (maxLength) {
            values << text(length: maxLength + 1)
        }
        values
    }

    static String text(Map args) {
        def length = args.length
        def text = new StringBuilder()
        for (int i = 0; i < length; i++) {
            text << "Ä"
        }
        text.toString()
    }
}

@Singleton
class ParameterFactory {

    static types = [:]

    static {
        types[String] = StringParameter.class
    }

    def getType(name) {
        types[name]
    }

}

