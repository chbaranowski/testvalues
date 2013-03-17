package testvalues

class TestDataModel {

   Parameters params = new Parameters()

   List constraints = []

}

class Parameters {

    @Delegate
    List<Parameter> parameters = []

    Map helpers = [:]

}