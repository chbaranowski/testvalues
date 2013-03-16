package testvalues

import spock.lang.Specification;

import static testvalues.StringValueGenerator.*

class StringParameterSpec extends Specification {

    def testData = new ParameterBuilder()

    def "valid values form a meta model with two string parameters"() {
        when:
            testData {
                params {
                    name(type: String) {
                        validValues = ["A", "B"]
                    }

                    lastname(type: String) {
                        validValues = ["C"]
                    }
                }
            }
        then:
            testData.validValues == [[name: "A", lastname: "C"],
                                     [name: "B", lastname: "C"]]
    }

    def "invalid values form a meta model with two string parameters"() {
        when:
            testData {
                params {
                    name(type: String) {
                        validValues     = ["A"]
                        invalidValues   = ["B", "C"]
                    }

                    lastname(type: String) {
                        validValues     = ["D"]
                        invalidValues   = ["E"]
                    }
                }
            }
        then:
            testData.invalidValues == [[name: "B", lastname: "D"],
                                       [name: "C", lastname: "D"],
                                       [name: "A", lastname: "E"]]
    }

    def "valid values with a optional test parameter"() {
        when:
            testData {
                params {
                    name(type: String) {
                        validValues = ["A"]
                        optional    = true
                    }

                    lastname(type: String) {
                        validValues = ["B"]
                    }
                }
            }
        then:
            testData.validValues == [[name: "A",  lastname: "B"],
                                     [name: null, lastname: "B"]]
    }

    def "invalid values with an explicit non optional parameter"() {
        when:
            testData {
                params {
                    name(type: String) {
                        validValues = ["A"]
                        optional    = false
                    }

                    lastname(type: String) {
                        validValues = ["B"]
                    }
                }
            }
        then:
            testData.invalidValues == [[name: null, lastname: "B"]]
    }

    def "valid values with an explicit empty String parameter"() {
        when:
            testData {
                params {
                    name(type: String) {
                        validValues = ["A"]
                        empty()
                    }
                }
            }
        then:
            testData.validValues == [[name: "A"],
                                     [name: ""],
                                     [name: "   "]]
    }

    def "invalid values with an explicit not empty String parameter"() {
        when:
            testData {
                params {
                    name(type: String) {
                        invalidValues = ["A"]
                        notEmpty()
                    }
                }
            }
        then:
            testData.invalidValues == [[name: "A"],
                                       [name: ""],
                                       [name: "   "]]
    }

    def "valid and invalid values meta model with an String parameter with defined max length"() {
        when:
            testData {
                params {
                    name(type: String) {
                        maxLength = 100
                    }
                }
            }
        then:
            testData.validValues    == [[name: text(length: 100)]]
            testData.invalidValues  == [[name: text(length: 101)]]
    }

    def "valid and invalid values meta model with an String parameter with defined min length"() {
        when:
            testData {
                params {
                    name(type: String) {
                        minLength = 99
                    }
                }
            }
        then:
            testData.validValues    == [[name: text(length: 99)]]
            testData.invalidValues  == [[name: text(length: 98)]]
    }

}
