package testvalues

class StringValueGenerator {

    static String text(Map args) {
        def length = args.length
        def text = ""
        (0..length).each {
            text << "Ä"
        }
        text
    }

}
