package testvalues

class TestData {

   Params params = new Params()

   List constraints = []

}

class Params {

    @Delegate
    List<Parameter> parameters = []

    Map helpers = [:]

}