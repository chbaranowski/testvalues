package testvalues

import spock.lang.Ignore
import spock.lang.Specification


class ConstraintSpec extends Specification {

    def testData = new ParameterBuilder()

    def getValidValues() {
        testData.validValues
    }

    def getInvalidValues() {
        testData.invalidValues
    }

    @Ignore
    def "two optional params but both could be null or both must be set"() {
        when:
            testData {
                params {
                    street(type: String) {
                        validValues = ["", "Bakerstreet"]
                    }
                    streetNumber(type: String) {
                        validValues = ["42", ""]
                    }
                }
                constraint {
                    (street && streetNumber ) || (!street  && !streetNumber)
                }
            }
        then:
            validValues == [[street: "", streetNumber: ""],
                            [street: "Bakerstreet", streetNumber: "42"]]

    }

}
