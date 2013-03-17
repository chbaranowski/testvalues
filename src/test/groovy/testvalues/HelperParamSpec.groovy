package testvalues

import spock.lang.Specification


class HelperParamSpec extends Specification {

    def testData = new ParameterBuilder()

    def getInvalidValues() {
        testData.invalidValues
    }

    def "Error messages for invalid String parameters"() {
        when:
            testData {

                params {

                    firstname(type: String) {
                        validValues   = ["A"]
                        invalidValues = ["B"]
                    }

                    lastname(type: String) {
                        validValues   = ["A"]
                        invalidValues = ["C"]
                    }

                    error {
                        "$name with the value '$value' is not allowed."
                    }

                }

            }
        then:
            invalidValues[0].error  ==  "firstname with the value 'B' is not allowed."
            invalidValues[1].error  ==  "lastname with the value 'C' is not allowed."
    }

    def "get invalid parameter name and value"() {
        when:
            testData {
                params {

                    firstname(type: String) {
                        validValues   = ["A"]
                        invalidValues = ["B"]
                    }

                    lastname(type: String) {
                        validValues   = ["A"]
                        invalidValues = ["C"]
                    }

                    invalidParam {
                        name
                    }

                    invalidValue {
                        value
                    }

                }
            }
       then:
            invalidValues[0].invalidParam == "firstname"
            invalidValues[0].invalidValue == "B"
            invalidValues[1].invalidParam == "lastname"
            invalidValues[1].invalidValue == "C"
    }
}
