package testvalues

class ParameterBuilder {

    TestDataModel model = new TestDataModel()

    def call(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = this
        closure()
    }

    def params(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = new ParamsBuilder(model: model.params)
        closure()
    }

    def getValidValues() {
        def validValues = []
        def maxValidValues = ((model.params*.validValues)*.size()).max() -1
        (0..maxValidValues).each { i ->
            def testSet = [:]
            model.params.each { param ->
                if (param.validValues.size() > i) {
                    testSet[param.name] = param.validValues[i]
                } else {
                    testSet[param.name] = param.validValues[0]
                }
            }
            validValues.add(testSet)
        }
        validValues
    }

    def getInvalidValues() {
        def invalidValues = []
        model.params.each {  invalidParam ->
            invalidParam.invalidValues.each { invalidValue ->
                def invalidDataSet = [:]
                model.params.findAll {it != invalidParam}.each { validParam ->
                   invalidDataSet[validParam.name] = validParam.validValues[0]
                }
                invalidDataSet[invalidParam.name] = invalidValue
                invalidValues.add(invalidDataSet)
            }
        }
        invalidValues
    }
}

class ParamsBuilder {

    List<Parameter> model

    public Object invokeMethod(String name, Object args) {
        param(name, (Map) args[0], (Closure) args[1])
    }

    def param(Object name, Map attributes, Closure closure) {
        def typeName = attributes.type
        def type = ParameterFactory.getInstance().types[typeName]
        Parameter parameterModel = type.newInstance()
        parameterModel.name = name
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = parameterModel
        closure()
        model.add(parameterModel)
    }
}

