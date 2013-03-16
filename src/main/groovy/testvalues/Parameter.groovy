package testvalues

import static testvalues.StringValueGenerator.text

abstract class Parameter {

    String name

    List validValues = []

    List invalidValues = []

    Boolean optional

    void setOptional(Boolean optional) {
        this.optional = optional
        if (optional) {
            validValues << null
        } else {
            invalidValues << null
        }
    }

}

class StringParameter extends Parameter {

    Integer maxLength

    Integer minLength

    Boolean empty

    def notEmpty() {
        this.empty = false
        invalidValues << ""
        invalidValues << "   "
    }

    def empty() {
        this.empty = true
        validValues << ""
        validValues << "   "
    }

    void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength
        validValues << text(length: maxLength)
        invalidValues << text(length: maxLength + 1)
    }

    void setMinLength(Integer minLength) {
        this.minLength = minLength
        validValues << text(length: minLength)
        invalidValues << text(length: minLength - 1)
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

