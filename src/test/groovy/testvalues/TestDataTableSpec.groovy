package testvalues

import spock.lang.Ignore
import spock.lang.Specification


class TestDataTableSpec extends Specification {

    def testData = new TestDataBuilder()

    def getValidValues() {
        testData.validValues
    }

    def getInvalidValues() {
        testData.invalidValues
    }

    @Ignore
    def "valid values are some values are defined as data table"() {
        when:
            testData {

                params {

                    firstname(type: String) {
                        maxLength = 100
                    }

                    lastname(type: String) {
                        maxLength = 100
                    }

                    age(type: String) {
                        maxLength = 199
                        validValues = ["42"]
                    }

                }

                validValues {

                    firstname  |  lastname
                    "Michael"  |  "Jackson"
                    "Georg"    |  "Michael"

                }

            }
        then:
            validValues == [[firstname: "Michael", lastname: "Jackson", age: 42],
                            [firstname: "Georg", lastname: "Michael", age: 42]]
    }

    @Ignore
    def "invalid values are some values are defined as data table"() {
        when:
        testData {

            params {

                firstname(type: String) {
                    maxLength = 100
                }

                lastname(type: String) {
                    maxLength = 100
                }

                age(type: String) {
                    validValues = ["42"]
                }

            }

            invalidValues {

                firstname  |  lastname
                ""         |  "Jackson"
                "Georg"    |  ""

            }

        }
        then:
            invalidValues == [[firstname: "",       lastname: "Jackson",    age: "42"],
                              [firstname: "Georg",  lastname: "",           age: "42"]]
    }


}
