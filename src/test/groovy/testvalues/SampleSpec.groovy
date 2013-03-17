package testvalues
import spock.lang.Specification

class SampleSpec extends Specification {

    static user = {

        params {

            name(type: String) {
                validValues = ["tux"]
            }

            password(type: String) {
                minLength = 2
                maxLength = 20
            }

        }

    }

    def "valid user with name: #user.name and password with length #user.password.length()"() {
        expect:
            user.name != "root"
            user.password.length() > 1
            user.password.length() < 21
        where:
            user << create(user).validValues
    }

    def "invalid user with name: #user.name and password with length #user.password.length()"() {
        expect:
            user.name == "tux"
            user.password.length() != 20
        where:
            user << create(user).invalidValues
    }

    def create(Closure testDataSpec) {
        ParameterBuilder builder = new ParameterBuilder()
        builder.call(testDataSpec)
        builder
    }

}
