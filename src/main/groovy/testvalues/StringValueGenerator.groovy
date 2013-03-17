package testvalues

class StringValueGenerator {

    static String text(Map args) {
        def length = args.length
        def text = new StringBuilder()
        for (int i = 0; i < length; i++) {
            text << "Ä"
        }
        text.toString()
    }

}
